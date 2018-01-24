package Com.controllers;


import Com.models.Article;
import Com.models.Interests;
import Com.services.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SessionAttributes(names = {"article", "users"})
@Controller
public class ArticlesFormController {


    private ArticleService articleService;

    public ArticlesFormController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @ModelAttribute("interestsList")
    public List<Interests> loadAccessories() {
        List<Interests> interests = articleService.getInterestsList();
        return interests;
    }

    @RequestMapping(value = "/articleForm.html", method = RequestMethod.GET)
    public String showForm(Model model) {

        model.addAttribute("article", new Article());

        return "articleForm.html";
    }

    @RequestMapping(value = "/articleForm.html", method = RequestMethod.POST)
    public String procressForm(@ModelAttribute("article") Article a, BindingResult errors) {

        if (errors.hasErrors()) {
            return "articleForm";
        }


        articleService.saveMainImage(a);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        a.setCreationDate(new Date());

        a.setUser(articleService.getUser(auth.getName()));

        a.setEnable(false);

        articleService.saveArticle(a);


        return "redirect:/articleList.html?waitingRoom";
    }

    @GetMapping(path = "enableArticle", params = {"eid"})
    public String enableArticle(long eid) {
        Article article = articleService.getArticle(eid);
        article.setEnable(true);
        articleService.saveArticle(article);

        return "redirect:/";
    }

}
