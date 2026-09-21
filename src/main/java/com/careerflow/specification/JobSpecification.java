package com.careerflow.specification;

import com.careerflow.entity.EmploymentType;
import com.careerflow.entity.JobEntity;
import com.careerflow.entity.JobStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    private JobSpecification() {
    }

    public static Specification<JobEntity> filterJobs(
            String keyword,
            String location,
            EmploymentType employmentType,
            Integer experienceMin,
            Integer experienceMax,
            Long salaryMin,
            Long salaryMax) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Only OPEN jobs should be visible
            predicates.add(criteriaBuilder.equal(root.get("status"), JobStatus.OPEN));

            // Keyword search
            if (keyword != null && !keyword.isBlank()) {

                String searchKeyword =
                        "%" + keyword.trim().toLowerCase() + "%";

                Predicate titlePredicate =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                searchKeyword
                        );

                Predicate descriptionPredicate =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                searchKeyword
                        );

                Predicate skillsPredicate =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("skills")),
                                searchKeyword
                        );

                predicates.add(
                        criteriaBuilder.or(
                                titlePredicate,
                                descriptionPredicate,
                                skillsPredicate
                        )
                );
            }

            // Location filter
            if (location != null && !location.isBlank()) {

                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")),
                                "%" + location.trim().toLowerCase() + "%"
                        )
                );
            }

            // Employment type
            if (employmentType != null) {

                predicates.add(criteriaBuilder.equal(root.get("employmentType"), employmentType));
            }

            // Candidate experience range
            if (experienceMin != null) {

                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("experienceMin"), experienceMin));
            }

            if (experienceMax != null) {

                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("experienceMax"), experienceMax));
            }

            // Salary range
            if (salaryMin != null) {

                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salaryMax"), salaryMin));
            }

            if (salaryMax != null) {

                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salaryMin"), salaryMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0])
            );
        };
    }
}