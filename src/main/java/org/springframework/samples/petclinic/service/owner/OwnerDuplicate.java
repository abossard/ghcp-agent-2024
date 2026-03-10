package org.springframework.samples.petclinic.service.owner;

public record OwnerDuplicate(int sourceRow,
                             OwnerImportRow imported,
                             OwnerImportRow existing) {
}
