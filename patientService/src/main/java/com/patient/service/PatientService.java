package com.patient.service;

import java.util.List;
import com.patient.entities.Patient;

public interface PatientService {

    public Patient savePatient(Patient patient);

    public List<Patient> getAllPatients();

    public Patient getPatientById(Long id);

    public Patient updatePatient(Long id, Patient updatedPatient);

    public void deletePatient(Long id);
}
