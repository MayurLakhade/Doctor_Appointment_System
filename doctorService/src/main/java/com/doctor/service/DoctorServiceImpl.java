package com.doctor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doctor.entities.Doctor;
import com.doctor.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

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
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
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
            throw new RuntimeException("Doctor not found with ID: " + id);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }
    
}
