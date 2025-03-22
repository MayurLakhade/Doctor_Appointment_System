package com.patient.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.patient.entities.Patient;

public interface PatientService {

    public Patient savePatient(Patient patient);

    public List<Patient> getAllPatients();

    public Patient getPatientById(Long id);

    public Patient updatePatient(Long id, Patient updatedPatient);

    public void deletePatient(Long id);

    public ResponseEntity<String> registerPatient(Patient patient, String password, String role);
}
