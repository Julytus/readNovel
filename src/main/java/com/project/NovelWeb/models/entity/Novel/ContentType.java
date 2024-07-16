package com.project.NovelWeb.models.entity.Novel;

import com.project.NovelWeb.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "content_types")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @ManyToMany(mappedBy = "contentTypes")
    private Set<Novel> novels = new HashSet<>();
}
