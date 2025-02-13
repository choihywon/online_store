package com.example.bookstore.blacklist.repository;

import com.example.bookstore.blacklist.domain.Blacklist;
import com.example.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByUser_Email(String email);
    List<Blacklist> findByUser(User user);
}
