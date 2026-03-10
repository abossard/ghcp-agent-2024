package org.springframework.samples.petclinic.service.owner;

public record OwnerImportRow(Integer id,
                             String firstName,
                             String lastName,
                             String address,
                             String city,
                             String telephone,
                             int sourceRow) {
}
