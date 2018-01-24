package Com.services;

import Com.controllers.commands.ArticleFilter;
import Com.exceptions.ArticleNotFoundException;
import Com.models.Article;
import Com.models.Interests;
import Com.models.User;
import Com.repositories.ArticleRepository;
import Com.repositories.InterestsRepository;
import Com.repositories.UserRepository;
import Com.repositories.criteria.ArticleSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestsRepository interestsRepository;

    public void saveArticle(Article article) {
        articleRepository.save(article);

    }



    @Override
    public void deleteArticle(long id) {
        articleRepository.deleteById(id);
    }



    @Override
    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }



    @Override
    public Article getArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        Article article = optionalArticle.orElseThrow(() -> new ArticleNotFoundException(id));
        return article;

    }

    @Override
    public boolean isRatedArticle(Article article, User user) {
        for (int i = 0 ; i < article.getRatedArticles().size() ; i++){
            if (user.getId().equals(article.getRatedArticles().get(i).getId())) return false;
        }
        return true;
    }


    @Override
    public List<Interests> getInterestsList() {
        return interestsRepository.findAll();
    }

    @Override
    public Page<Article> getAllArticles(boolean status, Pageable pageable, String phrase) {
        Page page;
        page = articleRepository.findAllArticles(status, phrase, pageable);

        return page;
    }

    @Override
    public Page<Article> getAllSearchArticles(boolean status, Pageable pageable, ArticleFilter phrase) {
        Page page;
        page = articleRepository.findAll(
                Specification.where(
                        ArticleSpecifications.findByPhrase(phrase.getPhrase())
                ), pageable);
        return page;
    }


    @Override
    public Page<Article> getDedicatedArticles(Pageable pageable, List<Interests> userIterests, String phrase, boolean status) {
        List<Article> articles = articleRepository.findAllArticles(status, phrase, pageable).getContent();
        List<Article> dedicated = new ArrayList<Article>();
        boolean itContains = false;

        Page<Article> page;
        for (int i = 0; i < articles.size(); i++) {
            for (int j = 0; j < userIterests.size(); j++) {
                for (int k = 0; k < articles.get(i).getInterests().size(); k++) {
                    if (!itContains)
                        if (userIterests.get(j).getId() == articles.get(i).getInterests().get(k).getId()) {
                            itContains = true;
                            dedicated.add(articles.get(i));
                        }

                }
            }
            itContains = false;
        }
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dedicated.size() ? dedicated.size() : (start + pageable.getPageSize());
        page = new PageImpl<Article>(dedicated.subList(start, end), pageable, dedicated.size());
        return page;
    }

    @Override
    public void saveMainImage(Article article) {
        try {
            URL url = new URL(article.getMainImage());
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            Random random  = new Random();
            int number = 0;
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i < 20 ; i++){
                sb.append(number = random.nextInt(10));
            }
            String link = "C://Users/Marek/IdeaProjects/articlesproject/src/main/resources/static/images/"+sb.toString()+".jpg";
            article.setMainImage(sb.toString()+".jpg");
            FileOutputStream fos = new FileOutputStream(link);
            fos.write(response);
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }


    }


}
