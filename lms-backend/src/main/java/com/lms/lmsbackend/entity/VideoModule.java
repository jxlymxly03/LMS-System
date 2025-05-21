package com.lms.lmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("VIDEO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VideoModule extends CourseModule {
    /**
     * URL or S3 key where the video is stored.
     */
    private String videoUrl;
}
