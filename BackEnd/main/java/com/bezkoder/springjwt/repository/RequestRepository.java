package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Request;
        import org.springframework.stereotype.Repository;
        import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
        List<Request> findByToUser(String toUser);
}