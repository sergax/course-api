package com.sergax.courseapi.model.user;

import com.sergax.courseapi.model.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "confirmation_codes")
@NoArgsConstructor
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "email")
    private String email;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public ConfirmationCode(String code, String email, LocalDate expirationDate, Status status) {
        this.code = code;
        this.email = email;
        this.expirationDate = expirationDate;
        this.status = status;
    }
}
