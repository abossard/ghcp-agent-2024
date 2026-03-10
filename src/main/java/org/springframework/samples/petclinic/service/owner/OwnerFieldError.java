package org.springframework.samples.petclinic.service.owner;

public record OwnerFieldError(int row, String field, String message) {
}
