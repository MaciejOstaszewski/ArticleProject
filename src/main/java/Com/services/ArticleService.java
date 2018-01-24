package Com.services;

import Com.controllers.commands.ArticleFilter;
import Com.models.Article;
import Com.models.Interests;
import Com.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ArticleService {


    void saveArticle(Article article);



    void deleteArticle(long id);



    User getUser(String name);

    Page<Article> getAllArticles(boolean status, Pageable pageable, String phrase);

    Page<Article> getAllSearchArticles(boolean status, Pageable pageable, ArticleFilter phrase);

    Article getArticle(Long id);

    boolean isRatedArticle(Article article, User user);

    List<Interests> getInterestsList();

    Page<Article> getDedicatedArticles(Pageable pageable, List<Interests> userIterests, String phrase, boolean status);

    void saveMainImage(Article a);


}
