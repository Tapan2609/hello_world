/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp.service;

import com.ariba.sampleapp.entities.CarEntity;
import java.util.List;

public interface CarService {

  List<CarEntity> getCars();

  CarEntity create(CarEntity carEntity);
}
