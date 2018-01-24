package Com.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
@Setter
@AllArgsConstructor
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "text")
    private String contents;

    @Column(name = "mainImage", nullable = false)
    private String mainImage;

    @Column(name = "negative_opinion", nullable = false)
    private int negativeOpinion;

    @Column(name = "positive_opinion", nullable = false)
    private int positiveOpinion;

    @ManyToMany(fetch = FetchType.LAZY)//LAZY powoduje dociągnięcie tych elementów dopiero wtedy, gdy są używane
    private List<Interests> interests;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="rated_articles",
            joinColumns=@JoinColumn(name="article_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private  List<User> ratedArticles;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    public Article() {
        interests = new ArrayList<Interests>();
        this.user = new User();
    }
}
