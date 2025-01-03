package com.nmb.scholarshipmanagementsystem.repository;
import com.nmb.scholarshipmanagementsystem.nmbscholarshipmanagementsystem.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional <User> findByEmail(String email);
    Optional <User> findByPasswordResetToken(String token);

}
