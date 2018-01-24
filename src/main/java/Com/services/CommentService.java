package Com.services;

import Com.models.Article;
import Com.models.Comments;

import java.util.List;

public interface CommentService {

    void saveComment(Comments comment);

    void deleteComment(long id);

    List<Comments> getArticleComments(Article article);
}
