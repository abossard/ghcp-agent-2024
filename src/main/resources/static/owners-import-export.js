(function(){
  const exportBtn = document.getElementById('exportBtn');
  const fileInput = document.getElementById('fileInput');
  const importBtn = document.getElementById('importBtn');
  const importApplyBtn = document.getElementById('importApplyBtn');
  const reportEl = document.getElementById('report');
  let lastReport = null;

  exportBtn?.addEventListener('click', () => {
    fetch('/api/owners/export', { method: 'GET' })
      .then(resp => {
        if (!resp.ok) throw new Error('Export failed: ' + resp.status);
        return resp.blob();
      })
      .then(blob => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'owners.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(url);
      })
      .catch(err => { reportEl.textContent = err.toString(); });
  });

  importBtn?.addEventListener('click', () => {
    const f = fileInput.files && fileInput.files[0];
    if (!f) { reportEl.textContent = 'Please choose a file first.'; return; }
    reportEl.textContent = 'Uploading and validating...';
    const fd = new FormData();
    fd.append('file', f);

    fetch('/api/owners/import', { method: 'POST', body: fd })
      .then(resp => resp.json())
      .then(json => {
        lastReport = json;
        reportEl.textContent = JSON.stringify(json, null, 2);
        // Enable apply button only when server returns a report with actionable changes
        importApplyBtn.disabled = !(json && json.summary && json.summary.errors === 0 && json.summary.warnings > 0 ? false : false);
        // If server returned `canApply: true` use that
        if (json && typeof json.canApply !== 'undefined') {
          importApplyBtn.disabled = !json.canApply;
        }
      })
      .catch(err => { reportEl.textContent = err.toString(); });
  });

  importApplyBtn?.addEventListener('click', () => {
    if (!lastReport || !confirm('Are you sure you want to apply the validated import?')) return;
    reportEl.textContent = 'Applying import...';
    // server should support apply via POST /api/owners/import?apply=true or a separate endpoint
    const f = fileInput.files && fileInput.files[0];
    const fd = new FormData();
    fd.append('file', f);
    fd.append('apply', 'true');

    fetch('/api/owners/import?apply=true', { method: 'POST', body: fd })
      .then(resp => resp.json())
      .then(json => { reportEl.textContent = JSON.stringify(json, null, 2); })
      .catch(err => { reportEl.textContent = err.toString(); });
  });
})();
