package com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminRequest {
    @NotBlank(message="Name cannot be blank")
    private String Name;

    @NotBlank(message="Name cannot be blank")
    @Email(message="Invalid email format")
    private String Email;

    
}
