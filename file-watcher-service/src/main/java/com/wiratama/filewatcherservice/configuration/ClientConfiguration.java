package com.wiratama.filewatcherservice.configuration;

import com.wiratama.filewatcherservice.client.ExcelGeneratorServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

  private final ConfigurableBeanFactory configurableBeanFactory;

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ExcelGeneratorServiceClient excelGeneratorServiceClient(RestTemplate restTemplate) {
    HttpServiceProxyFactory factory = HttpServiceProxyFactory
      .builder()
      .exchangeAdapter(RestTemplateAdapter.create(restTemplate))
      .embeddedValueResolver(new EmbeddedValueResolver(configurableBeanFactory))
      .build();

    return factory.createClient(ExcelGeneratorServiceClient.class);
  }
}
