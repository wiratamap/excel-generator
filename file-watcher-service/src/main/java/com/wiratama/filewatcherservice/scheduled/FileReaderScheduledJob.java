package com.wiratama.filewatcherservice.scheduled;

import com.wiratama.filewatcherservice.client.ExcelGeneratorServiceClient;
import com.wiratama.filewatcherservice.client.dto.GenerateExcelResponse;
import com.wiratama.filewatcherservice.properties.PathProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileReaderScheduledJob {

  private final PathProperties pathProperties;

  private final ExcelGeneratorServiceClient excelGeneratorServiceClient;

  @Scheduled(fixedDelay = 10_000)
  public void onReadIncomingXmlFile() {
    log.info("Running scheduled job, reading xml file from folder {}", pathProperties.getIncomingPath());
    Mono.fromSupplier(this::toIncomingFiles)
      .filter(Objects::nonNull)
      .map(Arrays::asList)
      .flatMapMany(Flux::fromIterable)
      .flatMap(this::generateExcel)
      .flatMap(this::deleteIncomingXmlFile)
      .subscribeOn(Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor()))
      .subscribe(response -> log.info("Generate Excel result: {}, with detail: {}, xml name: {}, generated file path: {}",
        response.getMessage(), response.getDetail(), response.getXmlFileName(), response.getGeneratedFilePath()));
  }

  private Mono<GenerateExcelResponse> deleteIncomingXmlFile(GenerateExcelResponse response) {
    return Mono.fromSupplier(() -> {
      try {
        String pathFile = pathProperties.getIncomingPath() + response.getXmlFileName();
        Files.delete(Paths.get(pathFile));
        return response;
      } catch (IOException e) {
        log.error("Failed to delete incoming xml file with file name {}", response.getXmlFileName(), e);
        return response;
      }
    })
    .subscribeOn(Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor()));
  }

  private Mono<GenerateExcelResponse> generateExcel(File file) {
    return Mono.fromSupplier(() -> file)
      .flatMap(f -> excelGeneratorServiceClient.generateExcel(new FileSystemResource(f)))
      .mapNotNull(ResponseEntity::getBody);
  }

  private File[] toIncomingFiles() {
    File folder = new File(pathProperties.getIncomingPath());
    return folder.listFiles((dir, name) -> name.endsWith(".xml"));
  }

}
