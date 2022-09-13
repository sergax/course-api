package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.ConfirmationCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCodeDto {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Confirmation code is required")
    private String code;

    public ConfirmationCode toConfirmationCode() {
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setEmail(this.email);
        confirmationCode.setCode(this.code);
        return confirmationCode;
    }
}
