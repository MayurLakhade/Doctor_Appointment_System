package com.doctor.service;

import java.util.List;
import com.doctor.entities.Doctor;

public interface DoctorService {

    public Doctor saveDoctor(Doctor doctor);

    public List<Doctor> getAllDoctors();

    public Doctor getDoctorById(Long id);

    public Doctor updateDoctor(Long id, Doctor updatedDoctor);

    public void deleteDoctor(Long id);

}
