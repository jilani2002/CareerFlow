package com.careerflow.repository;

import com.careerflow.entity.JobEntity;
import com.careerflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<JobEntity, Long>, JpaSpecificationExecutor<JobEntity> {

    List<JobEntity> findByRecruiter(UserEntity recruiter);

    Optional<JobEntity> findByJobIdAndRecruiter(Long jobId, UserEntity recruiter);
}