package com.nmb.admin.service;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nmb.admin.model.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import com.nmb.admin.repository.UserRepository;
import com.nmb.admin.model.User;
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
  
    private void deactivateFactoryUser(){
        userRepository.findByEmail(FACTORY_ADMIN_EMAIL).ifPresent(factoryUser ->{
            factoryUser.setActive(false);
            userRepository.save(factoryUser);
        });
     }
 
     private String generateRandomPassword(){
        return RandomStringUtils.random(12, true, true);
     }



    

    
}
