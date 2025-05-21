package com.lms.lmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;    // ← add
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    /**
     * Ordered list of modules.
     * @Builder.Default preserves the default value when using the builder.
     */
    @Builder.Default
    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("orderIndex ASC")
    @JsonManagedReference       // ← add this
    private List<CourseModule> modules = new ArrayList<>();
}
