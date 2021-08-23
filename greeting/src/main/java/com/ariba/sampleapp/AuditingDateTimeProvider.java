/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp;

import com.ariba.sampleapp.service.DateTimeService;
import org.springframework.data.auditing.DateTimeProvider;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class AuditingDateTimeProvider implements DateTimeProvider {
  private final DateTimeService currentDateTimeProvider;

  public AuditingDateTimeProvider(DateTimeService currentDateTimeProvider) {
    this.currentDateTimeProvider = currentDateTimeProvider;
  }

  public Optional<TemporalAccessor> getNow() {
    return Optional.of(this.currentDateTimeProvider.getCurrentDateAndTime());
  }
}

