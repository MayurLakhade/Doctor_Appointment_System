package com.appointment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime appointmentDate;
    private String status; // SCHEDULED, CANCELLED, COMPLETED
    private LocalDateTime createdAt;

    private DoctorDto doctor;
    private PatientDto patient;
}
