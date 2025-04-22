package com.patient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patient.dto.UserDto;
import com.patient.entities.Patient;
import com.patient.exception.PatientNotFoundException;
import com.patient.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://USERSERVICE/uapi/users/";

    //Patient Signup
    @Override
    public ResponseEntity<String> registerPatient(Patient patient, String password, String role) {
        //Create UserDto (instead of User entity)
        UserDto userDto = new UserDto();
        userDto.setEmail(patient.getEmail());
        userDto.setPassword(password); // Handle securely
        userDto.setRole(role);

        // Call Auth Service for user registration
        String url = USER_SERVICE_URL+"signup";

        try {
            ResponseEntity<UserDto> userResponse = restTemplate.postForEntity(url, userDto, UserDto.class);

            //Check if user creation was successful
            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                Long userId = userResponse.getBody().getId(); // Get userId from Auth Service response

                //Save patient with userId
                patient.setUserId(userId);
                patientRepository.save(patient);
                return ResponseEntity.ok("Patient registered successfully!");
            }
        } catch (RestClientException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register user in Auth Service.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Patient registration failed.");
    }

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
        .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
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
            throw new PatientNotFoundException("Patient not found with ID: " + id);
        }
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
}
