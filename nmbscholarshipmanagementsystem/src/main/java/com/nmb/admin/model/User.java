package com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    private String name;

    @Column (unique=true)

    private String email;

    private String password;
    
    private boolean isActive;

    private boolean isFactory;

    @Enumerated(EnumType.STRING)

    private UserRole role;

    private String passwordResetToken;

    private LocalDateTime passwordResetTokenExpiry;


    
}
