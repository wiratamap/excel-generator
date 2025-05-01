package com.wiratama.filewatcherservice.client;

import com.wiratama.filewatcherservice.client.dto.GenerateExcelResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("${client.excel-generator-base-url}")
public interface ExcelGeneratorServiceClient {

  @PostExchange("/excels")
  ResponseEntity<GenerateExcelResponse> generateExcel(@RequestPart("file") Resource file);
}

