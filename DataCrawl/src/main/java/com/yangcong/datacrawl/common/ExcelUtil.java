package com.yangcong.datacrawl.common;

import com.yangcong.datacrawl.entity.ProductInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExcelUtil {

    /*
     * sheetName：表名
     * heaaList:表头
     * contentList:表内容
     * filePath：写入文件地址
     * */
    public static void exportToExcel(String sheetName, List<String> heaList, List<ProductInfo> allProductInfo, String filePath) {
        //创建sheet页
        String hasBrand = "hasBrand";
        String noBrand = "noBrand";
        Workbook workbook = new XSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.GENERAL);
        List<ProductInfo> noBrandProducts = allProductInfo.stream().filter(e -> (e.getBrand() == null || "".equals(e.getBrand()))).collect(Collectors.toList());
        List<ProductInfo> brandProducts = allProductInfo.stream().filter(e -> (e.getBrand() != null && !"".equals(e.getBrand()))).collect(Collectors.toList());
        Map<String, List<ProductInfo>> productsMap = new HashMap<>();
        productsMap.put(hasBrand, brandProducts);
        productsMap.put(noBrand, noBrandProducts);
        for (String key : productsMap.keySet()) {
            Sheet sheet = workbook.createSheet(key);
            List<ProductInfo> productInfos = productsMap.get(key);
            //创建表头(第一行)
            Row row = sheet.createRow(0);
            productInfos = productInfos.stream().sorted(Comparator.comparing(e -> e.getProductParts() + "-" + e.getProductSubParts())).collect(Collectors.toList());
            int index = 0;
            if (key.equals(noBrand)){
                index = 1;
            }
            for (int i = 0; i < heaList.size()-index; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(heaList.get(i+index));
            }
            //创建表内容
            for (int i = 0; i < productInfos.size(); i++) {
                index = 0;
                ProductInfo info = productInfos.get(i);
                Row row1 = sheet.createRow(i + 1);
                if (!key.equals(noBrand)){
                    row1.createCell(index++, CellType.STRING).setCellValue(info.getBrand());
                }
                row1.createCell(index++, CellType.STRING).setCellValue(info.getModel());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductParts());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductSubParts());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductName());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductCode());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductPrice());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getWeight());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getProductDescription());
                row1.createCell(index++, CellType.STRING).setCellValue(info.getPhotoUrl());
            }
        }
        workbook.setActiveSheet(0);
        //写入文件
        File file = new File(filePath + sheetName + ".xls");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //将文件保存到指定位置
            workbook.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("写入文件成功");
    }
}