package com.project.NovelWeb.models.entities.novel;


import com.project.NovelWeb.models.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "novel_chapter")
public class Chapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chapter_num", nullable = false)
    private int chapterNum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Column(name = "price")
    private Float price;
}