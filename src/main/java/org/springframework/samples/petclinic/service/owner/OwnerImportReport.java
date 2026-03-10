package org.springframework.samples.petclinic.service.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerImportReport {
    private final List<OwnerImportRow> rows = new ArrayList<>();
    private final List<OwnerFieldError> errors = new ArrayList<>();
    private final List<OwnerDuplicate> duplicates = new ArrayList<>();
    private int created;
    private int updated;
    private int skipped;

    public void addRow(OwnerImportRow row) {
        this.rows.add(row);
    }

    public void addError(OwnerFieldError error) {
        this.errors.add(error);
    }

    public void addDuplicate(OwnerDuplicate duplicate) {
        this.duplicates.add(duplicate);
    }

    public List<OwnerImportRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public List<OwnerFieldError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<OwnerDuplicate> getDuplicates() {
        return Collections.unmodifiableList(duplicates);
    }

    public int getCreated() { return created; }
    public void setCreated(int created) { this.created = created; }

    public int getUpdated() { return updated; }
    public void setUpdated(int updated) { this.updated = updated; }

    public int getSkipped() { return skipped; }
    public void setSkipped(int skipped) { this.skipped = skipped; }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public boolean hasDuplicates() {
        return !this.duplicates.isEmpty();
    }
}
