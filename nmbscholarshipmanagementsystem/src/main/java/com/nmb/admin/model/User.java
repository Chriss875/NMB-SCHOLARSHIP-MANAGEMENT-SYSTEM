package com.nmb.admin.model;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
