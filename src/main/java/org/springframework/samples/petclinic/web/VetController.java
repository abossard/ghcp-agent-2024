package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VetController {

    private final ClinicService clinicService;

    public VetController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/vets")
    public String showVetList(Model model) {
        model.addAttribute("vets", clinicService.findAllVets());
        return "vets/vetList";
    }
}
