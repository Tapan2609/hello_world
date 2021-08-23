/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp.service;

import com.ariba.sampleapp.entities.CarEntity;
import com.ariba.sampleapp.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

  private final CarRepository carRepository;

  public CarServiceImpl(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  @Override
  public List<CarEntity> getCars() {
    return carRepository.findAll();
  }

  @Override
  @Transactional
  public CarEntity create(CarEntity carEntity) {
    if (carEntity.getId() != null) {
      throw new IllegalStateException("Id should be null");
    }

    return carRepository.save(carEntity);
  }
}
