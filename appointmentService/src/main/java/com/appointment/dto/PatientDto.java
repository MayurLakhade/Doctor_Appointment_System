package com.appointment.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Long id;

    private String name;
    private String gender;
    private Integer age;
    private String phone;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    private Long userId;

}
