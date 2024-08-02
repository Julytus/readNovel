package com.project.NovelWeb.models.entities.Novel;

import com.project.NovelWeb.enums.Status;
import com.project.NovelWeb.models.entities.BaseEntity;
import com.project.NovelWeb.models.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "image")
    private String image;

    @ManyToMany
    @JoinTable(
            name = "novel_content_type",
            joinColumns = @JoinColumn(name = "novel_id"),
            inverseJoinColumns = @JoinColumn(name = "content_type_id")
    )
    private List<ContentType> contentTypes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "poster_id")
    private User poster;
}
