package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {

    private Long id;
    private Article article; //게시글 (id)
    private String content; 

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
