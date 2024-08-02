package com.project.NovelWeb.models.entities.Novel;

import com.project.NovelWeb.models.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private List<Novel> novels;
}
