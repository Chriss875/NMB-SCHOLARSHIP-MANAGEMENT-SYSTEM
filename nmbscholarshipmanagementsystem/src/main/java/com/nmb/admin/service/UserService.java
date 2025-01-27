package com.nmb.scholarshipmanagementsystem.service;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.User;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.UserRole;
import com.nmb.scholarshipmanagementsystem.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService is responsible for handling user-related operations, including:
 * - Creating new admin users.
 * - Creating the factory admin user.
 * Deactivating the factory admin user.
 * - Resetting user passwords.
 * 
 * The service interacts with the UserRepository to store and retrieve user data
 * and the EmailService to send password reset emails.
 * <p>This class manages the core functionalities needed to administrate users in the system,
 * including ensuring that the factory admin user is present, creating new admins, and managing 
 * password reset processes.</p>
 */


@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
/**
 * Initializes the factory admin user if not already present in the system.
 * The factory admin is a special user that is required for the system to function.
 * This method checks if a factory admin exists and creates one if it doesn't,
 * assigning a random password and sending it via email.
 * 
 * @PostConstruct annotation ensures that this method is invoked after the
 * bean's properties are initialized.
 */
    private static final String FACTORY_ADMIN_EMAIL = "christophermtoi@gmail.com";

    @PostConstruct
    public void initializeFactoryAdmin(){
        if (userRepository.findByEmail(FACTORY_ADMIN_EMAIL).isEmpty()){
            User factoryAdmin= new User();
            factoryAdmin.setEmail(FACTORY_ADMIN_EMAIL);
            String generatedPassword = generateRandomPassword();
            factoryAdmin.setPassword(passwordEncoder.encode(generatedPassword));
            factoryAdmin.setRole(UserRole.ADMIN);
            factoryAdmin.setFactory(true);
            factoryAdmin.setActive(true);
            userRepository.save(factoryAdmin);
            log.info("Factory admin created successfully");
        }
    }
/**
 * Creates a new admin user with the given name and email.
 * The method generates a random password, encrypts it, and sends an email to the user
 * with a password reset link. This is useful for onboarding new administrators.
 * 
 * @param name the name of the new admin.
 * @param email the email address of the new admin.
 * @return the saved User object for the new admin.
 * 
 * @throws IllegalArgumentException if the email is already in use.
 */
    public User createNewAdmin(String name, String email){
        String generatedPassword= generateRandomPassword();
        User newAdmin= new User();
        newAdmin.setName(name);
        newAdmin.setEmail(email);
        newAdmin.setPassword(passwordEncoder.encode(generatedPassword));
        newAdmin.setRole(UserRole.ADMIN);
        newAdmin.setFactory(false);
        newAdmin.setActive(false);
        String resetToken= UUID.randomUUID().toString();
        newAdmin.setPasswordResetToken(resetToken);
        newAdmin.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));

        User savedAdmin= userRepository.save(newAdmin);

        String resetLink="http://nmbsms.com/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(email, resetLink);

        return savedAdmin;
    }
/**
 * Resets the password for a user based on the provided reset token.
 * The method checks if the token is valid and has not expired. If valid,
 * it updates the user's password and clears the token-related information.
 * 
 * @param token the reset token sent to the user.
 * @param newPassword the new password to be set for the user.
 * @throws IllegalArgumentException if the token is invalid or expired.
 */
    public void resetPassword(String token, String newPassword){
        User user= userRepository.findByPasswordResetToken(token).orElseThrow(()-> new IllegalArgumentException("Invalid token"));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Token has expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        user.setActive(true);

        userRepository.save(user);

        deactivateFactoryUser();
        userRepository.findByEmail(FACTORY_ADMIN_EMAIL).ifPresent(factoryUser -> deactivateFactoryUser());
    }
    /**
 * Deactivates the factory admin user. This ensures that the factory admin is only active
 * when needed, and can be deactivated when the password reset process is complete.
 * This method is called during the password reset process for security purposes.
 */
    private void deactivateFactoryUser(){
        userRepository.findByEmail(FACTORY_ADMIN_EMAIL).ifPresent(factoryUser ->{
            factoryUser.setActive(false);
            userRepository.save(factoryUser);
        });
     }
     /**
 * Generates a random password of 12 characters length. This password is used for
 * creating new users and resetting their passwords in the system.
 * 
 * @return a random 12-character string.
 */
     private String generateRandomPassword(){
        return RandomStringUtils.random(12, true, true);
     }



    

    
}
