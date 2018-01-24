package Com.repositories.criteria;

import Com.models.Article;
import Com.models.Comments;
import Com.models.Comments_;
import org.springframework.data.jpa.domain.Specification;

public class CommentsSpecifications {
    public static Specification<Comments> findArticleComments(Article article){
        return (root, query, cb) -> {
            return cb.equal(root.get(Comments_.article), article);
        };
    }

}
