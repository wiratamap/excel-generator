package com.wiratama.excelgeneratorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateExcelResponse {
  private String xmlFileName;
  private String generatedFilePath;
  private String message;
  private String detail;
}
