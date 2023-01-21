package pl.gdyzapomne.blog.controllers;

import pl.gdyzapomne.blog.entities.Comment;
import pl.gdyzapomne.blog.entities.Post;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.pojos.CommentPOJO;
import pl.gdyzapomne.blog.pojos.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.gdyzapomne.blog.services.CommentService;
import pl.gdyzapomne.blog.services.PostService;
import pl.gdyzapomne.blog.services.SendMailService;
import pl.gdyzapomne.blog.services.UserService;
import javax.validation.Valid;
import java.util.*;

/**
 * Kontroler obsługujący metody HTTP wysłane przez front-end.
 * Udostępnia wszystkie funkcjonalności związane z postami i komentarzami, które są obsługiwane przez Post Service i Comment Service.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final SendMailService sendMailService;

    @Autowired
    public PostController(PostService postService, UserService userService, CommentService commentService, SendMailService sendMailService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.sendMailService = sendMailService;
    }

    /**
     * Zwraca wszystkie posty z bazy danych.
     */
    @GetMapping
    public List<Post> posts() {
        List<Post> posts = postService.getAllPosts();
        posts.sort(Comparator.comparing(Post::getDateCreated).reversed());
        return posts;
    }

    /**
     * Zwraca post z bazy danych o podanym ID.
     */
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.find(id);
    }

    /**
     * Tworzy nowy post i zapisuje go w bazie danych.
     */
    @PostMapping("/add")
    public ResponseEntity<?> publishPost(@Valid @RequestBody Post post) {
        if(post == null) {
            return new ResponseEntity<>("Brakuje danych posta.", HttpStatus.BAD_REQUEST);
        }
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (post.getTitle().length() > 200) {
            return new ResponseEntity<>("Tytuł posta jest za długi. Dozwolone maksymalnie 200 znaków.", HttpStatus.BAD_REQUEST);
        }
        if (post.getBody().length() > 100000) {
            return new ResponseEntity<>("Przekroczono limit znaków treści posta.", HttpStatus.BAD_REQUEST);
        }
        if (post.getDateCreated() == null) {
            post.setDateCreated(new Date());
        }
        if (post.getBody().length()>990) {
            post.setPreview(post.getBody().substring(0, 990) + "...");
        } else {
            post.setPreview(post.getBody());
        }
        post.setCreator(userService.getUser(userDetails.getUsername()));
        if (postService.insert(post)) {
            sendMailService.notifySubscribers(post);
            return new ResponseEntity<>("Post został opublikowany.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Post nie został opublikowany. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Zwraca wszystkie posty po szukanym kluczu.
     */
    @GetMapping("/search/{keyword}")
    public List<Post> getFilteredPosts(@PathVariable String keyword) {
        return postService.findByKeyword(keyword);
    }

    /**
     * Zwraca wszystkie posty utworzone przez podanego użytkownika.
     */
    @GetMapping("/user/{username}")
    public List<Post> postsByUser(@PathVariable String username) {
        return postService.findByUser(userService.getUser(username));
    }

    /**
     * Zwraca wszystkie posty o wybranej kategorii.
     */
    @GetMapping("/category/{category}")
    public List<Post> postsByCategory(@PathVariable String category) {
        return postService.findByCategory(category);
    }

    /**
     * Usuwa post o podanym ID.
     */
    @DeleteMapping( "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        if(!postService.checkIfExists(id)) {
            return new ResponseEntity<>("Post nie istnieje.", HttpStatus.BAD_REQUEST);
        } else {
            if (postService.deletePost(id)) {
                return new ResponseEntity<>("Post został usunięty.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Post o podanym ID nie istnieje.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Usuwa komentarz o podanym ID.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        if(!commentService.checkIfExists(id)) {
            return new ResponseEntity<>("Komentarz nie istnieje.", HttpStatus.BAD_REQUEST);
        } else {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails.getUsername().equals(commentService.find(id).getCreator().getUsername()) ||
                    Arrays.toString(userDetails.getAuthorities().toArray()).contains("REDACTOR")) {
                if (commentService.deleteComment(id)) {
                    return new ResponseEntity<>("Komentarz został usunięty.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Nie udało się usunąć komentarza. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Użytkownik nie ma uprawnień do usunięcia komentarza.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Zwraca wszystkie komentarze przypisane do posta o podanym ID.
     */
    @GetMapping("/comments/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    /**
     * Tworzy nowy komentarz i zapisuje go w bazie danych.
     */
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody CommentPOJO comment) {
        if(!postService.checkIfExists(comment.getPostId())) {
            return new ResponseEntity<>("Post nie istnieje.", HttpStatus.BAD_REQUEST);
        } else {
            Post post = postService.find(comment.getPostId());
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User creator = userService.getUser(userDetails.getUsername());
            if (comment.getText().length() > 100000) {
                return new ResponseEntity<>("Przekroczono limit znaków w treści komentarza.", HttpStatus.BAD_REQUEST);
            }
            if (post == null || creator == null) {
                return new ResponseEntity<>("Brak posta lub twórcy posta. Komentarz nie został dodany.", HttpStatus.BAD_REQUEST);
            } else {
                if (commentService.comment(new Comment(comment.getText(), post, creator, new Date()))) {
                    return new ResponseEntity<>("Dodano komentarz.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Nie udało się dodać komentarza. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    /**
     * Aktualizuje podany post.
     */
    @PutMapping
    public ResponseEntity<?> updatePost(@Valid @RequestBody Post post) {
        if(!postService.checkIfExists(post.getId())) {
            return new ResponseEntity<>("Post nie istnieje.", HttpStatus.BAD_REQUEST);
        } else {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (post.getDateCreated() == null) {
                post.setDateCreated(new Date());
            }
            post.setCreator(userService.getUser(userDetails.getUsername()));
            if (post.getBody().length() > 990) {
                post.setPreview(post.getBody().substring(0, 990) + "...");
            } else {
                post.setPreview(post.getBody());
            }
            if (postService.update(post)) {
                return new ResponseEntity<>("Post został zaktualizowany.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nie udało się zaktualizować posta. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}