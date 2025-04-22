package com.doctor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.doctor.dto.UserDto;
import com.doctor.entities.Doctor;
import com.doctor.exception.DoctorNotFoundException;
import com.doctor.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://USERSERVICE/uapi/users/";

    //Doctor Signup
    @Override
    public ResponseEntity<String> registerDoctor(Doctor doctor, String password, String role) {
        //Create UserDto (instead of User entity)
        UserDto userDto = new UserDto();
        userDto.setEmail(doctor.getEmail());
        userDto.setPassword(password); // Handle securely
        userDto.setRole(role);

        // Call Auth Service for user registration
        String url = USER_SERVICE_URL+"signup";

        try {
            System.out.println("Sending UserDto: " + userDto.getPassword());

            ResponseEntity<UserDto> userResponse = restTemplate.postForEntity(url, userDto, UserDto.class);

            //Check if user creation was successful
            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                Long userId = userResponse.getBody().getId(); // Get userId from Auth Service response

                //Save doctor with userId
                doctor.setUserId(userId);
                doctorRepository.save(doctor);
                return ResponseEntity.ok("Doctor registered successfully!");
            }
        } catch (RestClientException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register user in Auth Service.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doctor registration failed.");
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + id));
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);

        if (existingDoctorOpt.isPresent()) {
            Doctor existingDoctor = existingDoctorOpt.get();
            existingDoctor.setName(updatedDoctor.getName());
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
            existingDoctor.setEmail(updatedDoctor.getEmail());
            existingDoctor.setPhone(updatedDoctor.getPhone());
            existingDoctor.setAvailable(updatedDoctor.getAvailable());
            existingDoctor.setExperience(updatedDoctor.getExperience());
            existingDoctor.setQualifications(updatedDoctor.getQualifications());
            return doctorRepository.save(existingDoctor);
        } else {
            throw new DoctorNotFoundException("Doctor not found with ID: " + id);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }
    
}
