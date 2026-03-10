(function () {
  const exportBtn = document.getElementById("exportBtn");
  const fileInput = document.getElementById("fileInput");
  const importBtn = document.getElementById("importBtn");
  const importApplyBtn = document.getElementById("importApplyBtn");
  const reportEl = document.getElementById("report");
  const resultsSection = document.getElementById("resultsSection");
  const resultsTbody = document.querySelector("#resultsTable tbody");
  const errorSection = document.getElementById("errorSection");
  const errorTbody = document.querySelector("#errorTable tbody");
  const summaryEl = document.getElementById("summary");
  const summaryText = document.getElementById("summaryText");
  let lastFile = null;

  function showReport(text, isError) {
    reportEl.textContent = text;
    reportEl.classList.remove("hidden", "alert-success");
    if (isError) {
      reportEl.classList.add("alert");
    } else {
      reportEl.classList.add("alert", "alert-success");
    }
  }

  function hideAll() {
    reportEl.classList.add("hidden");
    resultsSection.classList.add("hidden");
    errorSection.classList.add("hidden");
    summaryEl.classList.add("hidden");
    importApplyBtn.disabled = true;
    resultsTbody.innerHTML = "";
    errorTbody.innerHTML = "";
  }

  function renderReport(report) {
    hideAll();
    var rows = report.rows || [];
    var errors = report.errors || [];
    var duplicates = report.duplicates || [];

    // Build a lookup: sourceRow -> duplicate info
    var dupMap = {};
    for (var i = 0; i < duplicates.length; i++) {
      dupMap[duplicates[i].sourceRow] = duplicates[i];
    }

    // Errors
    if (errors.length > 0) {
      errorSection.classList.remove("hidden");
      for (var e = 0; e < errors.length; e++) {
        var err = errors[e];
        var tr = document.createElement("tr");
        tr.innerHTML =
          "<td>" +
          err.row +
          "</td><td>" +
          esc(err.field) +
          "</td><td>" +
          esc(err.message) +
          "</td>";
        errorTbody.appendChild(tr);
      }
    }

    // Results table — one row per imported row
    if (rows.length > 0) {
      resultsSection.classList.remove("hidden");
      var fields = ["firstName", "lastName", "address", "city", "telephone"];
      for (var r = 0; r < rows.length; r++) {
        var row = rows[r];
        var dup = dupMap[row.sourceRow];
        var tr = document.createElement("tr");
        var statusLabel;

        if (!dup) {
          // New row — highlight entire row green
          statusLabel = "New";
          tr.className = "row-new";
        } else {
          var changed = fields.some(function (f) {
            return (dup.imported[f] || "") !== (dup.existing[f] || "");
          });
          if (changed) {
            statusLabel = "Changed";
            tr.className = "row-changed";
          } else {
            statusLabel = "Unchanged";
            tr.className = "row-unchanged";
          }
        }

        var html = "<td>" + row.sourceRow + "</td>";
        html +=
          '<td><span class="badge badge-' +
          statusLabel.toLowerCase() +
          '">' +
          statusLabel +
          "</span></td>";

        for (var fi = 0; fi < fields.length; fi++) {
          var f = fields[fi];
          var val = row[f] || "";
          var isChanged =
            dup && (dup.imported[f] || "") !== (dup.existing[f] || "");
          if (isChanged) {
            html +=
              '<td class="cell-changed" title="Was: ' +
              esc(dup.existing[f] || "") +
              '">' +
              esc(val) +
              "</td>";
          } else {
            html += "<td>" + esc(val) + "</td>";
          }
        }
        tr.innerHTML = html;
        resultsTbody.appendChild(tr);
      }
    }

    // Summary
    var newCount = rows.length - duplicates.length;
    var changedCount = 0;
    var unchangedCount = 0;
    for (var d = 0; d < duplicates.length; d++) {
      var dup2 = duplicates[d];
      var hasChange = fields.some(function (f) {
        return (dup2.imported[f] || "") !== (dup2.existing[f] || "");
      });
      if (hasChange) changedCount++;
      else unchangedCount++;
    }

    summaryEl.classList.remove("hidden");
    summaryText.textContent =
      rows.length +
      " row(s): " +
      newCount +
      " new, " +
      changedCount +
      " changed, " +
      unchangedCount +
      " unchanged.";

    if (errors.length === 0 && rows.length > 0) {
      showReport("Validation passed — ready to import.", false);
      importApplyBtn.disabled = false;
    } else if (errors.length > 0) {
      showReport(
        "Validation failed — fix " +
          errors.length +
          " error(s) before importing.",
        true,
      );
    }
  }

  function esc(s) {
    var d = document.createElement("div");
    d.textContent = s;
    return d.innerHTML;
  }

  exportBtn?.addEventListener("click", function () {
    fetch("/api/owners/export", { method: "GET" })
      .then(function (resp) {
        if (!resp.ok) throw new Error("Export failed: " + resp.status);
        return resp.blob();
      })
      .then(function (blob) {
        var url = URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = url;
        a.download = "owners.xlsx";
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(url);
      })
      .catch(function (err) {
        showReport(err.toString(), true);
      });
  });

  importBtn?.addEventListener("click", function () {
    var f = fileInput.files && fileInput.files[0];
    if (!f) {
      hideAll();
      showReport("Please choose a file first.", true);
      return;
    }
    lastFile = f;
    hideAll();
    showReport("Uploading and validating...", false);
    var fd = new FormData();
    fd.append("file", f);

    fetch("/api/owners/import/validate", { method: "POST", body: fd })
      .then(function (resp) {
        return resp.json();
      })
      .then(function (report) {
        renderReport(report);
      })
      .catch(function (err) {
        showReport("Validation request failed: " + err.toString(), true);
      });
  });

  importApplyBtn?.addEventListener("click", function () {
    if (!lastFile || !confirm("Are you sure you want to apply the import?"))
      return;
    hideAll();
    showReport("Applying import...", false);
    var fd = new FormData();
    fd.append("file", lastFile);

    fetch("/api/owners/import", { method: "POST", body: fd })
      .then(function (resp) {
        if (resp.status === 204) {
          showReport("Import applied successfully!", false);
          summaryEl.classList.remove("hidden");
          summaryText.textContent = "Import complete.";
          importApplyBtn.disabled = true;
        } else {
          showReport("Import failed with status " + resp.status, true);
        }
      })
      .catch(function (err) {
        showReport("Import failed: " + err.toString(), true);
      });
  });
})();
