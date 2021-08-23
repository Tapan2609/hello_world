/*
    Copyright (c) 2020 Ariba, Inc
    All rights reserved. Patents pending.
*/

package com.ariba.sampleapp.repositories;

import com.ariba.sampleapp.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
}
