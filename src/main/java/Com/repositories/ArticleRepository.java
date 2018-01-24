package Com.repositories;

import Com.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Page<Article> findAllByOrderByCreationDateDesc(Pageable pageable, Specification<Article> specification);


    @Query("SELECT a FROM Article a WHERE ((a.enable = :status) " +
            "AND " +
            "(:phrase is null OR :phrase = '' OR " +
            "upper(a.title) LIKE upper(:phrase) OR " +
            "upper(a.user.username) LIKE upper(:phrase))) " +
            "ORDER BY a.creationDate DESC")
    Page<Article> findAllArticles(@Param("status") boolean s, @Param("phrase") String p, Pageable pageable);


}
