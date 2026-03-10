package org.springframework.samples.petclinic.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owners")
public class OwnersImportExportUiController {

    @GetMapping("/import-export")
    public String importExport() {
        return "owners/importExport";
    }
}
