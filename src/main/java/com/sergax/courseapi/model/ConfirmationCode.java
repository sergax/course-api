package com.sergax.courseapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "confirmation_codes")
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "email")
    private String email;
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    public ConfirmationCode(String code, String email, Date expirationDate, Status status) {
        this.code = code;
        this.email = email;
        this.expirationDate = expirationDate;
        this.status = status;
    }
}
