package org.springframework.samples.petclinic.rest.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.samples.petclinic.service.owner.OwnerImportReport;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@SpringJUnitConfig(classes = ApplicationTestConfig.class)
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class OwnerImportRestControllerTests {

    @Mock
    private ClinicService clinicService;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private VisitMapper visitMapper;

    private OwnerRestController ownerRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.ownerRestController = new OwnerRestController(clinicService, ownerMapper, petMapper, visitMapper);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
            .setControllerAdvice(new ExceptionControllerAdvice()).build();
    }

    private byte[] makeSimpleOwnersXlsx() throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            var sheet = wb.createSheet("Owners");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("firstName");
            header.createCell(2).setCellValue("lastName");
            header.createCell(3).setCellValue("address");
            header.createCell(4).setCellValue("city");
            header.createCell(5).setCellValue("telephone");

            var r = sheet.createRow(1);
            r.createCell(0).setCellValue(1);
            r.createCell(1).setCellValue("Jane");
            r.createCell(2).setCellValue("Doe");
            r.createCell(3).setCellValue("1 Main St");
            r.createCell(4).setCellValue("Townsville");
            r.createCell(5).setCellValue("1234567890");

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                wb.write(out);
                return out.toByteArray();
            }
        }
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testImportSuccessReturnsNoContent() throws Exception {
        byte[] bytes = makeSimpleOwnersXlsx();
        OwnerImportReport report = new OwnerImportReport();
        doReturn(report).when(clinicService).importOwnersFromXlsx(any(InputStream.class));

        MockMultipartFile file = new MockMultipartFile("file", "owners.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", bytes);

        this.mockMvc.perform(multipart("/api/owners/import").file(file).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testImportBadRequestWhenReportHasErrors() throws Exception {
        byte[] bytes = makeSimpleOwnersXlsx();
        OwnerImportReport report = new OwnerImportReport();
        report.addError(new org.springframework.samples.petclinic.service.owner.OwnerFieldError(1, "firstName", "missing"));
        doReturn(report).when(clinicService).importOwnersFromXlsx(any(InputStream.class));

        MockMultipartFile file = new MockMultipartFile("file", "owners.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", bytes);

        this.mockMvc.perform(multipart("/api/owners/import").file(file).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testImportInternalServerErrorOnException() throws Exception {
        byte[] bytes = makeSimpleOwnersXlsx();
        doThrow(new RuntimeException("boom")).when(clinicService).importOwnersFromXlsx(any(InputStream.class));

        MockMultipartFile file = new MockMultipartFile("file", "owners.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", bytes);

        this.mockMvc.perform(multipart("/api/owners/import").file(file).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }
}
