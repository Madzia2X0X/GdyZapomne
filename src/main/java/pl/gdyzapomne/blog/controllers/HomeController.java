package pl.gdyzapomne.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Kontroler, który udostępnia endpointy do poruszania się po aplikacji frontendowej.
 * Obsługuje requesty HTTP GET i zwraca strony HTML.
 */

@Controller
public class HomeController {
    @GetMapping(value="/")
    public String index() {
        return "index";
    }

    @GetMapping(value="/register")
    public String register() {
        return "register";
    }

    @GetMapping(value="/login")
    public String login() {
        return "login";
    }

    @GetMapping(value="/post/{id}")
    public String singlePost() {
        return "post";
    }

    @GetMapping(value="/post/edit/{id}")
    public String editPost() {
        return "edit-post";
    }

    @GetMapping(value="/posts/filtered/category/{category}")
    public String postList() {
        return "post-list";
    }

    @GetMapping(value="/posts/filtered/user/{username}")
    public String showUser() {
        return "show-user";
    }

    @GetMapping(value="/redactors")
    public String redactors() {
        return "redactors";
    }

    @GetMapping(value="/about")
    public String about() {
        return "about";
    }

    @GetMapping(value="/new-post")
    public String newPost() {
        return "new-post";
    }

    @GetMapping(value="/user-profile")
    public String showProfile() {
        return "user-profile";
    }

    @GetMapping(value="/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping(value="/search")
    public String search() {
        return "search";
    }
}
