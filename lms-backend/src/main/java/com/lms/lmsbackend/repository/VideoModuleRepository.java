package com.lms.lmsbackend.repository;

import com.lms.lmsbackend.entity.VideoModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoModuleRepository extends JpaRepository<VideoModule, Long> {
    /**
     * Find all video modules for a given course whose orderIndex is
     * greater than or equal to the specified index, ordered ascending.
     */
    List<VideoModule> findByCourse_IdAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(
        Long courseId,
        Integer orderIndex
    );
}
