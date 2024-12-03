package com.nmb.scholarshipmanagementsystem.service;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.User;
import com.nmb.scholarshipmanagementsystem.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.UUID;



@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void initializeFactoryAdmin(){
        if (userRepository.findByEmail("christophermtoi@gmail.com").isEmpty()){
            User factoryAdmin= new User();
            factoryAdmin.setEmail("christophermtoi@gmail.com");
            factoryAdmin.setPassword(passwordEncoder.encode("NMBFOUNDATION2024"));
            factoryAdmin.setRole(UserRole.ADMIN);
            factoryAdmin.setFactory(true);
            factoryAdmin.setActive(true);
            userRepository.save(factoryAdmin);
            log.info("Factory admin created successfully");
        }
    }

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
        emailService.sendPasswordResetEmail(email,resetLink);

        return savedAdmin;
    }

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
    }
     private void deactivateFactoryUser(){
        userRepository.findByEmail("christophermtoi@gmail.com").ifPresent(factoryUser ->{
            factoryUser.setActive(false);
            userRepository.save(factoryUser);
        });
     }

     private String generateRandomPassword(){
        return RandomStringUtils.random(12);
     }



    

    
}
