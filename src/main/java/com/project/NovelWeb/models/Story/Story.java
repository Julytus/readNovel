package com.project.NovelWeb.models.Story;

import com.project.NovelWeb.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "alias", length = 100)
    private String alias;

    @Column(name = "content")
    private String content;

    @Column(name = "view")
    private Long view;

    @Column(name = "status")
    private String status;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
