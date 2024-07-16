package com.project.NovelWeb.models.entity.Novel;

import com.project.NovelWeb.models.entity.BaseEntity;
import com.project.NovelWeb.models.entity.User;
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
    private String status;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "content_type_id")
    private ContentType contentType;

    @ManyToOne
    @JoinColumn(name = "poster_id")
    private User poster;
}
