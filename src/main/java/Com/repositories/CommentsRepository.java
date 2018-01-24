package Com.repositories;

import Com.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentsRepository extends JpaRepository<Comments, Long> , JpaSpecificationExecutor<Comments> {

}
