/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "car")
@EntityListeners(AuditingEntityListener.class)
public class CarEntity implements Serializable {

  @Id
  @Column(length = 16)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private UUID id;

  @Version
  private Long version;

  @Column(nullable = false)
  @CreatedDate
  private ZonedDateTime timeCreated;

  @Column(nullable = false)
  @LastModifiedDate
  private ZonedDateTime timeUpdated;

  @Column(nullable = false)
  private String model;

  @Column(nullable = false)
  private String year;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public ZonedDateTime getTimeCreated() {
    return timeCreated;
  }

  public void setTimeCreated(ZonedDateTime timeCreated) {
    this.timeCreated = timeCreated;
  }

  public ZonedDateTime getTimeUpdated() {
    return timeUpdated;
  }

  public void setTimeUpdated(ZonedDateTime timeUpdated) {
    this.timeUpdated = timeUpdated;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }
}
