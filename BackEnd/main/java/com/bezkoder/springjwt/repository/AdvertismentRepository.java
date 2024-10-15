package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Advertisment;
import com.bezkoder.springjwt.models.Job;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AdvertismentRepository extends JpaRepository<Advertisment, Long>{
    Optional<Advertisment> findById(Integer id);
}
