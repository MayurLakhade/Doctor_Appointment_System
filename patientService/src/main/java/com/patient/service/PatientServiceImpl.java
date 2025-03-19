package com.patient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patient.entities.Patient;
import com.patient.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{

     @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    }

    @Override
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Optional<Patient> existingPatientOpt = patientRepository.findById(id);

        if (existingPatientOpt.isPresent()) {
            Patient existingPatient = existingPatientOpt.get();
            existingPatient.setName(updatedPatient.getName());
            existingPatient.setGender(updatedPatient.getGender());
            existingPatient.setAge(updatedPatient.getAge());
            existingPatient.setPhone(updatedPatient.getPhone());
            existingPatient.setEmail(updatedPatient.getEmail());
            existingPatient.setMedicalHistory(updatedPatient.getMedicalHistory());
            existingPatient.setUserId(updatedPatient.getUserId());
            return patientRepository.save(existingPatient);
        } else {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
}
