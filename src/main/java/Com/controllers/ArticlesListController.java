package Com.controllers;


import Com.controllers.commands.ArticleFilter;
import Com.models.Article;
import Com.models.Comments;
import Com.models.User;
import Com.services.ArticleService;
import Com.services.CommentService;
import Com.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes("searchCommand")
public class ArticlesListController {


    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String showHomeSite(Model model, Pageable pageable, @ModelAttribute("searchCommand") ArticleFilter search) {
        model.addAttribute("articleListPage", articleService.getAllArticles(true, pageable, search.getPhrase()));
        model.addAttribute("concat", "?");
        search.clear();
        return "home";
    }


    @RequestMapping(value = "/articleList.html", params = {"waitingRoom"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String showWaitingRoom(Model model, Pageable pageable, @ModelAttribute("searchCommand") ArticleFilter search) {
        model.addAttribute("articleListPage", articleService.getAllArticles(false, pageable, search.getPhrase()));
        model.addAttribute("waitingRoom", true);
        model.addAttribute("page", "waitingRoom");
        model.addAttribute("concat", "&");
        search.clear();
        return "home";
    }

    @RequestMapping(value = "/articleList.html", params = {"dedicated"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String showUserDedicated(Model model, Pageable pageable, @ModelAttribute("searchCommand") ArticleFilter search) {
        model.addAttribute("articleListPage", articleService.getDedicatedArticles(pageable, userService.getCurrentUserInterests(), search.getPhrase(), true));
        model.addAttribute("page", "dedicated");
        model.addAttribute("concat", "&");
        search.clear();
        return "home";
    }

    @RequestMapping(value = "/articleContent.html", params = "id", method = RequestMethod.GET)
    public String showArticle(Model model, Long id) {
        Article article = articleService.getArticle(id);
        User user = userService.getCurrentUser();
        model.addAttribute("article", article);
        model.addAttribute("comments", new Comments());
        model.addAttribute("articleComment", commentService.getArticleComments(article));
        if (user != null)
            model.addAttribute("isRated", articleService.isRatedArticle(article, user));
        else
            model.addAttribute("isRated", false);
        return "articleContent";
    }

    @GetMapping(path = "deleteArticle", params = {"did", "link"})
    public String deleteArticle(long did, String link, HttpServletRequest request) {
        articleService.deleteArticle(did);
        return "redirect:articleList.html?waitingRoom";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(path = "rate", params = {"aid", "opinion"})
    public String rateArticle(long aid, boolean opinion, HttpServletRequest request) {
        Article article = articleService.getArticle(aid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        article.getRatedArticles().add(userService.getCurrentUser());

        if (opinion)
            article.setPositiveOpinion(article.getPositiveOpinion() + 1);
        else
            article.setNegativeOpinion(article.getNegativeOpinion() + 1);
        articleService.saveArticle(article);
        return "redirect:articleContent.html?id=" + aid;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(path = "addComment", params = {"aid"})
    public String addComment(@ModelAttribute("comments") Comments comments, long aid) {
        comments.setCreationDate(new Date());
        comments.setUser(userService.getCurrentUser());
        comments.setArticle(articleService.getArticle(aid));
        commentService.saveComment(comments);
        return "redirect:articleContent.html?id=" + aid;
    }

    @ModelAttribute("searchCommand")
    public ArticleFilter getSimpleSearch() {
        return new ArticleFilter();
    }

}
