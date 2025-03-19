package com.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patient.entities.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{
    
}
