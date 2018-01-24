package Com.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such article")
public class ArticleNotFoundException extends RuntimeException{
    public ArticleNotFoundException(){
        super(String.format("Artykuł nie istnieje"));
    }

    public ArticleNotFoundException(Long id){
        super(String.format("Artykuł o id #d nie istnieje", id));
    }
}
