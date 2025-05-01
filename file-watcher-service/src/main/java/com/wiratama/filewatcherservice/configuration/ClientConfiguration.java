package com.wiratama.filewatcherservice.configuration;

import com.wiratama.filewatcherservice.client.ExcelGeneratorServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

  private final ConfigurableBeanFactory configurableBeanFactory;

  @Bean
  public WebClient restTemplate(LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction) {
    return WebClient.builder()
      .filter(loadBalancedExchangeFilterFunction)
      .build();
  }

  @Bean
  public ExcelGeneratorServiceClient excelGeneratorServiceClient(WebClient webClient) {
    HttpServiceProxyFactory httpServiceProxyFactory =
      HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .embeddedValueResolver(new EmbeddedValueResolver(configurableBeanFactory))
        .build();
    return httpServiceProxyFactory.createClient(ExcelGeneratorServiceClient.class);
  }
}
