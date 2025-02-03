package org.edusync.tutor.repository;

import org.edusync.tutor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
    Optional<User> findByEmail(String email);
}
