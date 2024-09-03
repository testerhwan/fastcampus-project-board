package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @Test
    public void givenTestData_whenSelecting_thenWorksFine() throws Exception {
        //given

        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles).isNotNull().hasSize(0);
    }

    @Test
    public void insert() throws Exception {
        //given
        long previousCount = articleRepository.count();

        //when
        Article save = articleRepository.save(Article.of("new article", "new content", "#spring"));

        //then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    public void update() throws Exception {
        //given
        Article save = articleRepository.save(Article.of("new article", "new content", "#spring"));
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        //when
        Article saveData = articleRepository.saveAndFlush(article);

        //then
        assertThat(saveData).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @Test
    public void delete() throws Exception {
        //given
        Article a = articleRepository.save(Article.of("new article", "new content", "#spring"));
        articleCommentRepository.save(ArticleComment.of(a, "content"));
        Article article = articleRepository.findById(1L).orElseThrow();

        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        //when
        articleRepository.delete(article);

        //then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }

}