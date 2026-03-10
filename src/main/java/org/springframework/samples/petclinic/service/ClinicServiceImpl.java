/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.io.InputStream;
import java.io.IOException;
import org.springframework.samples.petclinic.service.owner.OwnerDuplicate;
import org.springframework.samples.petclinic.service.owner.OwnerImportReport;
import org.springframework.samples.petclinic.service.owner.OwnerImportRow;
import org.springframework.samples.petclinic.util.OwnerExcelHelper;
import java.util.ArrayList;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@Service
public class ClinicServiceImpl implements ClinicService {

    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PetTypeRepository petTypeRepository;

    public ClinicServiceImpl(
        PetRepository petRepository,
        VetRepository vetRepository,
        OwnerRepository ownerRepository,
        VisitRepository visitRepository,
        SpecialtyRepository specialtyRepository,
        PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Pet> findAllPets() throws DataAccessException {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public void deletePet(Pet pet) throws DataAccessException {
        petRepository.delete(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Visit findVisitById(int visitId) throws DataAccessException {
        return findEntityById(() -> visitRepository.findById(visitId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Visit> findAllVisits() throws DataAccessException {
        return visitRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteVisit(Visit visit) throws DataAccessException {
        visitRepository.delete(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public Vet findVetById(int id) throws DataAccessException {
        return findEntityById(() -> vetRepository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Vet> findAllVets() throws DataAccessException {
        return vetRepository.findAll();
    }

    @Override
    @Transactional
    public void saveVet(Vet vet) throws DataAccessException {
        vetRepository.save(vet);
    }

    @Override
    @Transactional
    public void deleteVet(Vet vet) throws DataAccessException {
        vetRepository.delete(vet);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Owner> findAllOwners() throws DataAccessException {
        return ownerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportOwnersToXlsx() throws DataAccessException {
        try {
            return OwnerExcelHelper.ownersToXlsx(findAllOwners());
        } catch (IOException e) {
            throw new DataAccessException("Failed to export owners to XLSX", e) {
            };
        }
    }

    @Override
    @Transactional
    public void deleteOwner(Owner owner) throws DataAccessException {
        ownerRepository.delete(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public PetType findPetTypeById(int petTypeId) {
        return findEntityById(() -> petTypeRepository.findById(petTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<PetType> findAllPetTypes() throws DataAccessException {
        return petTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void savePetType(PetType petType) throws DataAccessException {
        petTypeRepository.save(petType);
    }

    @Override
    @Transactional
    public void deletePetType(PetType petType) throws DataAccessException {
        petTypeRepository.delete(petType);
    }

    @Override
    @Transactional(readOnly = true)
    public Specialty findSpecialtyById(int specialtyId) {
        return findEntityById(() -> specialtyRepository.findById(specialtyId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Specialty> findAllSpecialties() throws DataAccessException {
        return specialtyRepository.findAll();
    }

    @Override
    @Transactional
    public void saveSpecialty(Specialty specialty) throws DataAccessException {
        specialtyRepository.save(specialty);
    }

    @Override
    @Transactional
    public void deleteSpecialty(Specialty specialty) throws DataAccessException {
        specialtyRepository.delete(specialty);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<PetType> findPetTypes() throws DataAccessException {
        return petRepository.findPetTypes();
    }

    @Override
    @Transactional(readOnly = true)
    public Owner findOwnerById(int id) throws DataAccessException {
        return findEntityById(() -> ownerRepository.findById(id));
    }

    @Override
    @Transactional
    public OwnerImportReport importOwnersFromXlsx(InputStream in, boolean dryRun) throws DataAccessException {
        try {
            List<OwnerExcelHelper.OwnerRow> parsed = OwnerExcelHelper.parseOwnersXlsx(in);
            Collection<Owner> existingOwners = ownerRepository.findAll();
            OwnerImportReport report = new OwnerImportReport();
            int created = 0;
            int updated = 0;
            int skipped = 0;
            int srcRow = 1;
            for (OwnerExcelHelper.OwnerRow r : parsed) {
                OwnerImportRow importRow = new OwnerImportRow(r.id(), r.firstName(), r.lastName(), r.address(), r.city(), r.telephone(), srcRow);
                report.addRow(importRow);

                // Duplicate detection: match by ID or by (firstName + lastName + telephone)
                Owner matchedOwner = null;
                for (Owner existing : existingOwners) {
                    boolean idMatch = r.id() != null && existing.getId() != null && r.id().equals(existing.getId());
                    boolean namePhoneMatch = r.firstName() != null && r.lastName() != null && r.telephone() != null
                        && r.firstName().equalsIgnoreCase(existing.getFirstName())
                        && r.lastName().equalsIgnoreCase(existing.getLastName())
                        && r.telephone().equals(existing.getTelephone());
                    if (idMatch || namePhoneMatch) {
                        matchedOwner = existing;
                        OwnerImportRow existingRow = new OwnerImportRow(
                            existing.getId(), existing.getFirstName(), existing.getLastName(),
                            existing.getAddress(), existing.getCity(), existing.getTelephone(), 0);
                        report.addDuplicate(new OwnerDuplicate(srcRow, importRow, existingRow));
                        break;
                    }
                }

                if (!dryRun && !report.hasErrors()) {
                    if (matchedOwner == null) {
                        // New owner
                        Owner owner = new Owner();
                        owner.setFirstName(r.firstName());
                        owner.setLastName(r.lastName());
                        owner.setAddress(r.address());
                        owner.setCity(r.city());
                        owner.setTelephone(r.telephone());
                        ownerRepository.save(owner);
                        created++;
                    } else if (hasChanges(r, matchedOwner)) {
                        // Update existing owner with changed fields
                        matchedOwner.setFirstName(r.firstName());
                        matchedOwner.setLastName(r.lastName());
                        matchedOwner.setAddress(r.address());
                        matchedOwner.setCity(r.city());
                        matchedOwner.setTelephone(r.telephone());
                        ownerRepository.save(matchedOwner);
                        updated++;
                    } else {
                        skipped++;
                    }
                }
                srcRow++;
            }
            report.setCreated(created);
            report.setUpdated(updated);
            report.setSkipped(skipped);
            return report;
        } catch (IOException e) {
            throw new DataAccessException("Failed to import owners from XLSX", e) {
            };
        }
    }

    private boolean hasChanges(OwnerExcelHelper.OwnerRow imported, Owner existing) {
        return !nullSafeEquals(imported.firstName(), existing.getFirstName())
            || !nullSafeEquals(imported.lastName(), existing.getLastName())
            || !nullSafeEquals(imported.address(), existing.getAddress())
            || !nullSafeEquals(imported.city(), existing.getCity())
            || !nullSafeEquals(imported.telephone(), existing.getTelephone());
    }

    private boolean nullSafeEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    @Override
    @Transactional(readOnly = true)
    public Pet findPetById(int id) throws DataAccessException {
        return findEntityById(() -> petRepository.findById(id));
    }

    @Override
    @Transactional
    public void savePet(Pet pet) throws DataAccessException {
        pet.setType(findPetTypeById(pet.getType().getId()));
        petRepository.save(pet);
    }

    @Override
    @Transactional
    public void saveVisit(Visit visit) throws DataAccessException {
        visitRepository.save(visit);

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Vet> findVets() throws DataAccessException {
        return vetRepository.findAll();
    }

    @Override
    @Transactional
    public void saveOwner(Owner owner) throws DataAccessException {
        ownerRepository.save(owner);

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException {
        return ownerRepository.findByLastName(lastName);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Visit> findVisitsByPetId(int petId) {
        return visitRepository.findByPetId(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Specialty> findSpecialtiesByNameIn(Set<String> names) {
        return findEntityById(() -> specialtyRepository.findSpecialtiesByNameIn(names));
    }

    private <T> T findEntityById(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            // Just ignore not found exceptions for Jdbc/Jpa realization
            return null;
        }
    }

}
