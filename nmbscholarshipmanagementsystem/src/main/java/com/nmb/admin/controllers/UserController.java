package com.nmb.admin.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nmb.admin.controllers.dto.CreateAdminRequest;
import com.nmb.admin.controllers.dto.ResetPasswordRequest;
import com.nmb.scholarshipmanagementsystem.model.User;
import com.nmb.admin.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody CreateAdminRequest request) {
        User newAdmin= userService.createNewAdmin(request.getName(),request.getEmail());
        return ResponseEntity.ok(newAdmin);
    }

    @PostMapping("/reset-password")
        public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request){
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok().build();
      }  
}

