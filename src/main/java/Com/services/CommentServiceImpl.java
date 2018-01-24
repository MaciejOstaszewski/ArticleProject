package Com.services;

import Com.models.Article;
import Com.models.Comments;
import Com.repositories.CommentsRepository;
import Com.repositories.criteria.CommentsSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentsRepository commentsRepository;

    @Override
    public void saveComment(Comments comment) {
        commentsRepository.save(comment);
    }

    @Override
    public void deleteComment(long id) {
        commentsRepository.deleteById(id);

    }

    @Override
    public List<Comments> getArticleComments(Article article) {
        return commentsRepository.findAll(Specification.where(CommentsSpecifications.findArticleComments(article)));
    }
}
