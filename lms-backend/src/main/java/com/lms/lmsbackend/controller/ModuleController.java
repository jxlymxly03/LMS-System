// src/main/java/com/lms/lmsbackend/controller/ModuleController.java
package com.lms.lmsbackend.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.lmsbackend.entity.CourseModule;
import com.lms.lmsbackend.entity.VideoModule;
import com.lms.lmsbackend.repository.CourseRepository;
import com.lms.lmsbackend.repository.VideoModuleRepository;
import com.lms.lmsbackend.service.VideoStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final VideoModuleRepository moduleRepo;
    private final CourseRepository courseRepo;
    private final VideoStorageService storageService;

    /**  
     * Upload a video file + metadata, shift existing modules, then save.
     */
    @PostMapping(
      path = "/upload-video/course/{courseId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<VideoModule> uploadAndCreate(
        @PathVariable Long courseId,
        @RequestParam("file") MultipartFile file,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("orderIndex") Integer orderIndex
    ) {
        return courseRepo.findById(courseId).map(course -> {
            // shift any existing modules at or after this index
            List<VideoModule> toShift = moduleRepo
                .findByCourse_IdAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(courseId, orderIndex);
            toShift.forEach(m -> m.setOrderIndex(m.getOrderIndex() + 1));
            moduleRepo.saveAll(toShift);

            // store file
            String videoUrl = storageService.store(file);

            // build + save new module
            VideoModule mod = VideoModule.builder()
                .title(title)
                .description(description)
                .orderIndex(orderIndex)
                .videoUrl(videoUrl)
                .course(course)
                .build();

            return ResponseEntity.ok(moduleRepo.save(mod));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** List all modules in a course */
    @GetMapping("/course/{courseId}")
    public List<CourseModule> listModules(@PathVariable Long courseId) {
        return courseRepo.findById(courseId)
            .map(c -> c.getModules())
            .orElse(List.of())
            .stream()
            .sorted(Comparator.comparing(CourseModule::getOrderIndex))
            .toList();
    }

    /** Get one module by its ID */
    @GetMapping("/{moduleId}")
    public ResponseEntity<VideoModule> getModule(@PathVariable Long moduleId) {
        return moduleRepo.findById(moduleId)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    /** Delete a single module by ID */
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        if (!moduleRepo.existsById(moduleId)) {
            return ResponseEntity.notFound().build();
        }
        moduleRepo.deleteById(moduleId);
        return ResponseEntity.noContent().build();
    }
}
