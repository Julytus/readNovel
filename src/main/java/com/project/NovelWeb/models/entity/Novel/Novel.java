package com.project.NovelWeb.models.entity.Novel;

import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "novels")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Novel extends BaseEntity {
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
    private Status status;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
