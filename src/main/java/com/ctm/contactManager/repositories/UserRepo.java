package com.ctm.contactManager.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ctm.contactManager.entities.user;

@Repository
public interface UserRepo extends JpaRepository<user, String> {

    Optional<user> findByEmail(String email);

    Optional<user> findByEmailToken(String token);
}
