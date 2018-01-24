package Com.repositories.criteria;

import Com.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class ArticleSpecifications {
    public static Specification<Article> findEnabled() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get(Article_.enable));
        };
    }

//    public static Specification<Article> findDisabled(){
//        return (root, query, cb) -> {
//            return cb.isFalse(root.get(Article_.enable));
//        };
//    }

    public static Specification<Article> findByPhrase(final String phrase) {
        return (root, query, cb) -> {

            if (StringUtils.isEmpty(phrase) == false) {
                String phraseLike = "%" + phrase.toUpperCase() + "%";
                return cb.or(
                        cb.or(
                                cb.like(cb.upper(root.get(Article_.title)), phraseLike)

                        ),
                        cb.like(cb.upper(root.get(Article_.user).get(User_.username)), phraseLike)

                );
            }
            return null;
        };
    }
}
