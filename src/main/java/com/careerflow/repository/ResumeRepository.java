package com.careerflow.repository;

import com.careerflow.entity.ResumeEntity;
import com.careerflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {

    List<ResumeEntity> findByUser(UserEntity user);

    Optional<ResumeEntity> findByResumeIdAndUser(Long resumeId, UserEntity user);

    Optional<ResumeEntity> findByUserAndPrimaryResumeTrue(UserEntity user);
}