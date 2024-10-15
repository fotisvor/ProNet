package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Job;
import com.bezkoder.springjwt.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface JobRepository extends JpaRepository<Job,Long>{
    Optional<Job> findById(Integer Id);

    @Query("SELECT DISTINCT j FROM Job j JOIN j.skills js WHERE js IN :userSkills")
    List<Job> findJobsByUserSkills(@Param("userSkills") Set<Skill> userSkills);

    List<Job> findJobsByEmployer(String employer);
}
