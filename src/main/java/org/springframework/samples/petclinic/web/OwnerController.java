package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final ClinicService clinicService;

    public OwnerController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/find")
    public String initFindForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/findOwners";
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        Collection<Owner> results = clinicService.findOwnerByLastName(owner.getLastName());
        if (results.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if (results.size() == 1) {
            owner = results.iterator().next();
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute("owners", results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/{ownerId}")
    public String showOwner(@PathVariable int ownerId, Model model) {
        Owner owner = clinicService.findOwnerById(ownerId);
        model.addAttribute("owner", owner);
        return "owners/ownerDetails";
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        }
        clinicService.saveOwner(owner);
        redirectAttributes.addFlashAttribute("message", "New Owner Created");
        return "redirect:/owners/" + owner.getId();
    }

    @GetMapping("/{ownerId}/edit")
    public String initUpdateForm(@PathVariable int ownerId, Model model) {
        Owner owner = clinicService.findOwnerById(ownerId);
        model.addAttribute("owner", owner);
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateForm(@Valid Owner owner, BindingResult result, @PathVariable int ownerId,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        }
        owner.setId(ownerId);
        clinicService.saveOwner(owner);
        redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
        return "redirect:/owners/" + ownerId;
    }

    // --- Pet sub-resource ---

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return clinicService.findPetTypes();
    }

    @GetMapping("/{ownerId}/pets/new")
    public String initNewPetForm(@PathVariable int ownerId, Model model) {
        Owner owner = clinicService.findOwnerById(ownerId);
        var pet = new Pet();
        owner.addPet(pet);
        model.addAttribute("owner", owner);
        model.addAttribute("pet", pet);
        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/{ownerId}/pets/new")
    public String processNewPetForm(@PathVariable int ownerId, @Valid Pet pet, BindingResult result,
                                    Model model, RedirectAttributes redirectAttributes) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (result.hasErrors()) {
            model.addAttribute("owner", owner);
            return "pets/createOrUpdatePetForm";
        }
        owner.addPet(pet);
        clinicService.savePet(pet);
        redirectAttributes.addFlashAttribute("message", "New Pet Added");
        return "redirect:/owners/" + ownerId;
    }

    @GetMapping("/{ownerId}/pets/{petId}/edit")
    public String initEditPetForm(@PathVariable int ownerId, @PathVariable int petId, Model model) {
        Owner owner = clinicService.findOwnerById(ownerId);
        Pet pet = clinicService.findPetById(petId);
        model.addAttribute("owner", owner);
        model.addAttribute("pet", pet);
        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/{ownerId}/pets/{petId}/edit")
    public String processEditPetForm(@PathVariable int ownerId, @PathVariable int petId,
                                     @Valid Pet pet, BindingResult result, Model model,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Owner owner = clinicService.findOwnerById(ownerId);
            model.addAttribute("owner", owner);
            return "pets/createOrUpdatePetForm";
        }
        pet.setId(petId);
        Owner owner = clinicService.findOwnerById(ownerId);
        owner.addPet(pet);
        clinicService.savePet(pet);
        redirectAttributes.addFlashAttribute("message", "Pet Updated");
        return "redirect:/owners/" + ownerId;
    }

    // --- Visit sub-resource ---

    @GetMapping("/{ownerId}/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable int ownerId, @PathVariable int petId, Model model) {
        Owner owner = clinicService.findOwnerById(ownerId);
        Pet pet = clinicService.findPetById(petId);
        var visit = new Visit();
        pet.addVisit(visit);
        model.addAttribute("owner", owner);
        model.addAttribute("pet", pet);
        model.addAttribute("visit", visit);
        return "visits/createOrUpdateVisitForm";
    }

    @PostMapping("/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@PathVariable int ownerId, @PathVariable int petId,
                                      @Valid Visit visit, BindingResult result, Model model,
                                      RedirectAttributes redirectAttributes) {
        Pet pet = clinicService.findPetById(petId);
        if (result.hasErrors()) {
            Owner owner = clinicService.findOwnerById(ownerId);
            model.addAttribute("owner", owner);
            model.addAttribute("pet", pet);
            return "visits/createOrUpdateVisitForm";
        }
        pet.addVisit(visit);
        clinicService.saveVisit(visit);
        redirectAttributes.addFlashAttribute("message", "Visit Added");
        return "redirect:/owners/" + ownerId;
    }
}
