package com.appointment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.appointment.dto.AppointmentDto;
import com.appointment.entities.Appointment;

public interface AppointmentService {

     // Create a new appointment
    Appointment createAppointment(Long doctorId, Long patientId, LocalDateTime appointmentDate);
    
    // Get appointment by ID
    // Optional<Appointment> getAppointmentById(Long id);
    
    // Get all appointments
    List<Appointment> getAllAppointments();
    
    // Get appointments by doctor ID
    List<Appointment> getAppointmentsByDoctorId(Long doctorId);
    
    // Get appointments by patient ID
    List<Appointment> getAppointmentsByPatientId(Long patientId);
    
    // Update appointment status (e.g., SCHEDULED, CANCELLED, COMPLETED)
    Appointment updateAppointmentStatus(Long id, String status);
    
    // Reschedule an appointment
    Appointment rescheduleAppointment(Long id, LocalDateTime newDate);
        
    // Delete an appointment
    void deleteAppointment(Long id);

    public AppointmentDto getAppointmentById(Long appointmentId);
}
