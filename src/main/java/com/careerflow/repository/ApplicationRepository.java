package com.careerflow.repository;

import com.careerflow.entity.ApplicationEntity;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, Long> {

    boolean existsByCandidateAndJob(UserEntity candidate, JobEntity job);

    List<ApplicationEntity> findByCandidateOrderByAppliedAtDesc(UserEntity candidate);

    Optional<ApplicationEntity> findByApplicationIdAndCandidate(Long applicationId, UserEntity candidate);
}