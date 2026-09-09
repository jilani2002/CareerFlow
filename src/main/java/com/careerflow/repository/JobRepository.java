package com.careerflow.repository;

import com.careerflow.entity.JobEntity;
import com.careerflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<JobEntity, Long> {

    List<JobEntity> findByRecruiter(UserEntity recruiter);

    Optional<JobEntity> findByJobIdAndRecruiter(Long jobId, UserEntity recruiter);
}