package org.springframework.samples.petclinic.service.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerImportReport {
    private final List<OwnerImportRow> rows = new ArrayList<>();
    private final List<OwnerFieldError> errors = new ArrayList<>();

    public void addRow(OwnerImportRow row) {
        this.rows.add(row);
    }

    public void addError(OwnerFieldError error) {
        this.errors.add(error);
    }

    public List<OwnerImportRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public List<OwnerFieldError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }
}
