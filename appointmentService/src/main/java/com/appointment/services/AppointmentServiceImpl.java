package com.appointment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.appointment.dto.AppointmentDto;
import com.appointment.dto.DoctorDto;
import com.appointment.dto.PatientDto;
import com.appointment.entities.Appointment;
import com.appointment.repositories.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    private final String DOCTOR_SERVICE_URL = "http://localhost:8082/dapi/doctors/";
    private final String PATIENT_SERVICE_URL = "http://localhost:8083/papi/patients/";

    @Override
    public Appointment createAppointment(Long doctorId, Long patientId, LocalDateTime appointmentDate) {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus("SCHEDULED");
        return appointmentRepository.save(appointment);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) {
            throw new RuntimeException("Appointment not found with ID: " + id);
        }

        Appointment appointment = appointmentOptional.get();

        // Fetch Doctor Details
        // String doctorUrl = DOCTOR_SERVICE_URL + appointment.getDoctorId();
        DoctorDto doctor = getDoctorById(appointment.getDoctorId());
        
        // Fetch Patient Details
        // String patientUrl = PATIENT_SERVICE_URL + appointment.getPatientId();
        PatientDto patient = getPatientById(appointment.getPatientId());

        // Convert Appointment to AppointmentDTO
        return new AppointmentDto(
            appointment.getId(),
            appointment.getDoctorId(),
            appointment.getPatientId(),
            appointment.getAppointmentDate(),
            appointment.getStatus(),
            appointment.getCreatedAt(),
            doctor,
            patient
        );
    }

    public DoctorDto getDoctorById(Long doctorId) {
        String url = DOCTOR_SERVICE_URL + doctorId;
        try {
            return restTemplate.getForObject(url, DoctorDto.class);
        } catch (RestClientException e) {
            return new DoctorDto(doctorId, "Unknown Doctor", "N/A", "N/A", "N/A", false, "N/A", "N/A", 0L);
        }
    }

    public PatientDto getPatientById(Long patientId) {
        String url = PATIENT_SERVICE_URL + patientId;
        try {
            return restTemplate.getForObject(url, PatientDto.class);
        } catch (RestClientException e) {
            return new PatientDto(patientId, "Unknown Patient", "N/A", 0, "N/A", "N/A", "N/A", 0L);
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public Appointment updateAppointmentStatus(Long id, String status) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Appointment not found");
    }

    // @Override
    // public void cancelAppointment(Long id) {
    //     Optional<Appoinment> appointmentOpt = appointmentRepository.findById(id);
    //     if (appointmentOpt.isPresent()) {
    //         Appoinment appointment = appointmentOpt.get();
    //         appointment.setStatus("CANCELLED");
    //         appointmentRepository.save(appointment);
    //     } else {
    //         throw new RuntimeException("Appointment not found");
    //     }
    // }

    @Override
    public void deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

    @Override
    public Appointment rescheduleAppointment(Long id, LocalDateTime newDate) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setAppointmentDate(newDate);
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment not found with ID: " + id);
        }
    }
}
