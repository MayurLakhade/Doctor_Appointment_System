package com.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doctor.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>  {
    
}
