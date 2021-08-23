/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp.service;

import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;

@Service
public class CurrentDateTimeService implements DateTimeService {
  public CurrentDateTimeService() {
  }

  @Override
  public ZonedDateTime getCurrentDateAndTime() {
    return ZonedDateTime.now();
  }
}
