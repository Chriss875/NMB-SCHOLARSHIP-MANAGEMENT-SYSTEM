package com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem;
import java.time.LocalDateTime;

import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.UserRole;

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
/*
 * The User class represents a user in the scholarship management system. It is a JPA entity that is mapped to the users table in the database. 
 * The class includes essential user details such as name, email, password, account status, role, and password reset information.
This class utilizes Lombok annotations for boilerplate code generation, such as getters, setters, constructors, toString(), equals(), and hashCode() methods.
 */

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
