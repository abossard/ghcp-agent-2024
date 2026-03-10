package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Owner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class OwnerExcelHelper {

    private OwnerExcelHelper() {
    }

    public static byte[] ownersToXlsx(Collection<Owner> owners) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Owners");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("firstName");
            header.createCell(2).setCellValue("lastName");
            header.createCell(3).setCellValue("address");
            header.createCell(4).setCellValue("city");
            header.createCell(5).setCellValue("telephone");

            int rowIdx = 1;
            for (Owner owner : owners) {
                Row r = sheet.createRow(rowIdx++);
                Cell c0 = r.createCell(0);
                c0.setCellValue(owner.getId() == null ? "" : owner.getId().toString());
                r.createCell(1).setCellValue(nullToEmpty(owner.getFirstName()));
                r.createCell(2).setCellValue(nullToEmpty(owner.getLastName()));
                r.createCell(3).setCellValue(nullToEmpty(owner.getAddress()));
                r.createCell(4).setCellValue(nullToEmpty(owner.getCity()));
                r.createCell(5).setCellValue(nullToEmpty(owner.getTelephone()));
            }

            wb.write(out);
            return out.toByteArray();
        }
    }

    public static List<OwnerRow> parseOwnersXlsx(InputStream in) throws IOException {
        List<OwnerRow> rows = new ArrayList<>();
        try (XSSFWorkbook wb = new XSSFWorkbook(in)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                return rows;
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                if (r == null) continue;
                String idStr = getCellString(r.getCell(0));
                String firstName = getCellString(r.getCell(1));
                String lastName = getCellString(r.getCell(2));
                String address = getCellString(r.getCell(3));
                String city = getCellString(r.getCell(4));
                String telephone = getCellString(r.getCell(5));

                if (isAllEmpty(idStr, firstName, lastName, address, city, telephone)) {
                    continue; // skip empty row
                }

                Integer id = null;
                try {
                    if (!idStr.isEmpty()) {
                        id = Integer.valueOf(idStr);
                    }
                } catch (NumberFormatException ignored) {
                }

                firstName = titleCase(firstName);
                lastName = titleCase(lastName);
                telephone = normalizePhone(telephone);

                rows.add(new OwnerRow(id, firstName, lastName, address, city, telephone));
            }
        }
        return rows;
    }

    private static String getCellString(Cell c) {
        if (c == null) return "";
        switch (c.getCellType()) {
            case STRING:
                return c.getStringCellValue().trim();
            case NUMERIC:
                double d = c.getNumericCellValue();
                long l = (long) d;
                return Long.toString(l);
            case BOOLEAN:
                return Boolean.toString(c.getBooleanCellValue());
            case BLANK:
            default:
                return "";
        }
    }

    private static boolean isAllEmpty(String... vals) {
        for (String v : vals) if (v != null && !v.trim().isEmpty()) return false;
        return true;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String normalizePhone(String s) {
        if (s == null) return "";
        String digits = s.replaceAll("\\D+", "");
        if (digits.length() == 10) return digits;
        return digits;
    }

    private static String titleCase(String s) {
        if (s == null || s.isEmpty()) return s == null ? "" : s;
        String lower = s.trim().toLowerCase(Locale.ROOT);
        if (lower.length() == 1) return lower.toUpperCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    public static record OwnerRow(Integer id, String firstName, String lastName, String address, String city, String telephone) {
    }
}
