package com.nmb.scholarshipmanagementsystem.contollers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nmb.scholarshipmanagementsystem.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.User;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.CreateAdminRequest;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.ResetPasswordRequest;



@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody CreateAdminRequest request) {
        User newAdmin= userService.createNewAdmin(request.getName(),request.getEmail());
        return ResponseEntity.ok(newAdmin);
    }

    @PostMapping("/reset-password")
        public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request){
            userService.resetPassword(request.getToken(), request.getnewPassword());
            return ResponseEntity.ok().build();
      }  
}

