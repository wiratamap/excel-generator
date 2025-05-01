package com.wiratama.excelgeneratorservice.controller;

import com.wiratama.excelgeneratorservice.dto.GenerateExcelResponse;
import com.wiratama.excelgeneratorservice.service.ExcelGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ExcelController {

  private final ExcelGeneratorService excelGeneratorService;

  @PostMapping("/excels")
  public ResponseEntity<GenerateExcelResponse> generateExcel(@RequestParam("file") MultipartFile file) {
    return excelGeneratorService.generateExcel(file);
  }
}
