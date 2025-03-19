package com.appointment.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appointment.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

     // Custom query method to find appointments by doctorId
     List<Appointment> findByDoctorId(Long doctorId);

     // Custom query method to find appointments by patientId
     List<Appointment> findByPatientId(Long patientId);

}
