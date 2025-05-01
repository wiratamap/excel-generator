package com.wiratama.excelgeneratorservice.service;

import com.wiratama.excelgeneratorservice.constant.ResponseConstant;
import com.wiratama.excelgeneratorservice.dto.GenerateExcelResponse;
import com.wiratama.excelgeneratorservice.dto.XmlDataDto;
import com.wiratama.excelgeneratorservice.dto.XmlRowDto;
import com.wiratama.excelgeneratorservice.properties.PathProperties;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelGeneratorService {

  private final PathProperties pathProperties;

  public ResponseEntity<GenerateExcelResponse> generateExcel(MultipartFile file) {
    try {
      XmlDataDto xmlDataDto = xmlToDto(file);

      List<Map<String, String>> parsedXml = xmlDataDto.getRows().stream()
        .map(XmlRowDto::toMap)
        .toList();

      XSSFWorkbook workbook = new XSSFWorkbook();
      XSSFSheet sheet = workbook.createSheet("Data");

      Set<String> headersContent = parsedXml
        .stream()
        .map(Map::keySet)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

      createHeader(sheet, headersContent);
      createRowContent(parsedXml, sheet, headersContent);

      String generatedFilePath = pathProperties.getOutcomingPath() + FilenameUtils.getBaseName(file.getOriginalFilename()) + ".xlsx";
      FileOutputStream fileOutputStream = new FileOutputStream(generatedFilePath);
      workbook.write(fileOutputStream);
      workbook.close();

      return ResponseEntity.status(HttpStatus.CREATED)
        .body(GenerateExcelResponse.builder()
          .xmlFileName(file.getOriginalFilename())
          .generatedFilePath(generatedFilePath)
          .message(ResponseConstant.SUCCESS_UPLOAD)
        .build());
    } catch (IOException e) {
      log.error("Error while reading file {}", e.getMessage(), e);
      return toFailedResponse(e, file);
    } catch (JAXBException e) {
      log.error("Error while parsing xml file {}", e.getMessage(), e);
      return toFailedResponse(e, file);
    }
  }

  private static XmlDataDto xmlToDto(MultipartFile file) throws IOException, JAXBException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
    String xmlContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));

    JAXBContext context = JAXBContext.newInstance(XmlDataDto.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    return (XmlDataDto) unmarshaller.unmarshal(new StringReader(xmlContent));
  }

  private static void createRowContent(List<Map<String, String>> parsedXml, XSSFSheet sheet, Set<String> headersContent) {
    int rowNumber = 1;
    for (Map<String, String> item : parsedXml) {
      Row row = sheet.createRow(rowNumber);
      int cellNum = 0;
      for (String header : headersContent) {
        String rowContent = item.getOrDefault(header, StringUtils.EMPTY);
        row.createCell(cellNum, CellType.STRING).setCellValue(rowContent);
        cellNum++;
      }
      rowNumber++;
    }
  }

  private static void createHeader(XSSFSheet sheet, Set<String> headersContent) {
    Row headerRow = sheet.createRow(0);
    int cellNumber = 0;
    for (String header : headersContent) {
      Cell cell = headerRow.createCell(cellNumber, CellType.STRING);
      cell.setCellValue(header);
      cellNumber++;
    }
  }

  private static ResponseEntity<GenerateExcelResponse> toFailedResponse(Exception e, MultipartFile file) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(GenerateExcelResponse.builder()
        .xmlFileName(file.getOriginalFilename())
        .message(ResponseConstant.FAILED_UPLOAD)
        .detail(e.getMessage())
        .build());
  }
}
