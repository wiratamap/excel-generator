package com.wiratama.filewatcherservice.scheduled;

import com.wiratama.filewatcherservice.client.ExcelGeneratorServiceClient;
import com.wiratama.filewatcherservice.properties.PathProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileReaderScheduledJob {

  private final PathProperties pathProperties;

  private final ExcelGeneratorServiceClient excelGeneratorServiceClient;

  @Scheduled(fixedDelay = 10_000)
  public void run() {
    log.info("Running scheduled job, reading xml file from folder {}", pathProperties.getIncomingPath());
    File folder = new File(pathProperties.getIncomingPath());
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".xml"));

    Optional.ofNullable(files)
      .map(Arrays::stream)
      .stream()
      .flatMap(s -> s)
      .map(FileSystemResource::new)
      .map(excelGeneratorServiceClient::generateExcel)
      .map(ResponseEntity::getBody)
      .filter(Objects::nonNull)
      .forEach(response -> {
        log.info("Generate Excel result: {}, with detail: {}, xml name: {}, generated file path: {}", response.getMessage(), response.getDetail(), response.getXmlFileName(), response.getGeneratedFilePath());
        try {
          Files.delete(Paths.get(pathProperties.getIncomingPath() + response.getXmlFileName()));
        } catch (IOException e) {
          log.error("Failed to delete incoming xml file with file name {}", response.getXmlFileName(), e);
        }
      });
  }

}
