package com.goalkeepers.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "EMAIL_CODE_TB")
public class EmailCode {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_code_id")
    private Long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String code;

    public EmailCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
