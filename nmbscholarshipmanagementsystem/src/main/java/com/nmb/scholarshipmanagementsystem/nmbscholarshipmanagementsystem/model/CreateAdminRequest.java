package com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminRequest {
    @NotBlank(message="Name cannot be blank")
    private String name;

    @NotBlank(message="Name cannot be blank")
    @Email(message="Invalid email format")
    private String email;

    
}
