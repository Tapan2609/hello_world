/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp;

import com.ariba.sampleapp.service.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ComponentScan
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MainAppConfig {

  @Bean
  DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
    return new AuditingDateTimeProvider(dateTimeService);
  }
}
