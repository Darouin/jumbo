package com.jumbo.config;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.jumbo.service.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoOpMailConfiguration {

  private final MailService mockMailService;

  public NoOpMailConfiguration() {
    mockMailService = mock(MailService.class);
    doNothing().when(mockMailService).sendActivationEmail(any());
  }

  @Bean
  public MailService mailService() {
    return mockMailService;
  }
}
