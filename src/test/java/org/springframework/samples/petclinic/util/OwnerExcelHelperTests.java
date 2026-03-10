package org.springframework.samples.petclinic.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerExcelHelperTests {

    @Test
    void shouldParseAndNormalizeRow() throws Exception {
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
            r.createCell(0).setCellValue(42);
            r.createCell(1).setCellValue("  alice  ");
            r.createCell(2).setCellValue("SMITH");
            r.createCell(3).setCellValue("1 Main St");
            r.createCell(4).setCellValue("springfield");
            r.createCell(5).setCellValue("(123) 456-7890");

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                wb.write(out);
                var in = new ByteArrayInputStream(out.toByteArray());
                var rows = OwnerExcelHelper.parseOwnersXlsx(in);
                assertThat(rows).hasSize(1);
                var row = rows.get(0);
                assertThat(row.id()).isEqualTo(42);
                assertThat(row.firstName()).isEqualTo("Alice");
                assertThat(row.lastName()).isEqualTo("Smith");
                assertThat(row.city()).isEqualTo("springfield");
                assertThat(row.telephone()).isEqualTo("1234567890");
            }
        }
    }

    @Test
    void shouldSkipEmptyRowsAndHandleNumericIdAndPhoneVariants() throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            var sheet = wb.createSheet("Owners");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("firstName");
            header.createCell(2).setCellValue("lastName");
            header.createCell(3).setCellValue("address");
            header.createCell(4).setCellValue("city");
            header.createCell(5).setCellValue("telephone");

            // empty row (should be skipped)
            sheet.createRow(1);

            // numeric id cell
            var r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue(7); // numeric cell
            r2.createCell(1).setCellValue("bOB");
            r2.createCell(2).setCellValue("o'neill");
            r2.createCell(3).setCellValue("");
            r2.createCell(4).setCellValue("Metropolis");
            r2.createCell(5).setCellValue("+1 800 555 1212 ext.34");

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                wb.write(out);
                var in = new ByteArrayInputStream(out.toByteArray());
                var rows = OwnerExcelHelper.parseOwnersXlsx(in);
                assertThat(rows).hasSize(1);
                var row = rows.get(0);
                assertThat(row.id()).isEqualTo(7);
                assertThat(row.firstName()).isEqualTo("Bob");
                assertThat(row.lastName()).isEqualTo("O'neill");
                assertThat(row.city()).isEqualTo("Metropolis");
                // phone digits only
                assertThat(row.telephone()).containsOnlyDigits();
            }
        }
    }

    @Test
    void titleCaseHandledViaParsing() throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            var sheet = wb.createSheet("Owners");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("firstName");
            header.createCell(2).setCellValue("lastName");
            header.createCell(3).setCellValue("address");
            header.createCell(4).setCellValue("city");
            header.createCell(5).setCellValue("telephone");

            var r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue(1);
            r1.createCell(1).setCellValue(""); // empty -> should become empty string
            r1.createCell(2).setCellValue("doe");
            r1.createCell(3).setCellValue("");
            r1.createCell(4).setCellValue("");
            r1.createCell(5).setCellValue("");

            var r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue(2);
            r2.createCell(1).setCellValue("b"); // single char -> uppercase B
            r2.createCell(2).setCellValue("smith");
            r2.createCell(3).setCellValue("");
            r2.createCell(4).setCellValue("");
            r2.createCell(5).setCellValue("");

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                wb.write(out);
                var in = new ByteArrayInputStream(out.toByteArray());
                var rows = OwnerExcelHelper.parseOwnersXlsx(in);
                assertThat(rows).hasSize(2);
                assertThat(rows.get(0).firstName()).isEqualTo("");
                assertThat(rows.get(1).firstName()).isEqualTo("B");
            }
        }
    }
}
