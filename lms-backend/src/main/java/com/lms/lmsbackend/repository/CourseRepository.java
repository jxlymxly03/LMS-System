package com.lms.lmsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.lmsbackend.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {}
