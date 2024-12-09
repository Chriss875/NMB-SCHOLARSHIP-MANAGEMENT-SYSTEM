package com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank
    private String Token;

    @NotBlank
    private String newPassword;
}
