package com.appointment.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "doctor ID is required")
    @Positive(message = "doctor ID must be positive")
    private Long doctorId;

    @NotNull(message = "patient ID is required")
    @Positive(message = "patient ID must be positive")
    private Long patientId;

    private LocalDateTime appointmentDate;

    @Pattern(regexp = "SCHEDULED|CANCELLED|COMPLETED", message = "Status must be SCHEDULED, CANCELLED, or COMPLETED")
    @NotBlank(message = "Status is required")
    private String status; // SCHEDULED, CANCELLED, COMPLETED

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Appointment [id=" + id + ", doctorId=" + doctorId + ", patientId=" + patientId + ", appointmentDate="
                + appointmentDate + ", status=" + status + ", createdAt=" + createdAt + "]";
    }

}
