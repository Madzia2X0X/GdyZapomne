package pl.gdyzapomne.blog.services;

import pl.gdyzapomne.blog.entities.Comment;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Klasa działająca między kontrolerem komentarzy, a repozytorium komentarzy.
 * Implementuje wszystkie funkcjonalności komentarzy.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Pobiera wszystkie komentarze z repozytorium, które są dołączone do postu o podanym ID.
     */
    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Zapisuje nowy komentarz w repozytorium.
     */
    public boolean comment(Comment comment) {
        commentRepository.save(comment);
        return true;
    }

    /**
     * Usuwa z repozytorium komentarz o podanym ID.
     */
    public boolean deleteComment(Long id) {
        Comment comment = commentRepository.getOne(id);
        if (comment == null) {
            return false;
        }
        commentRepository.deleteById(id);
        return true;
    }

    /**
     * Usuwa z repozytorium wszystkie komentarze, które zostały opublikowane przez podanego użytkownika.
     */
    public boolean deleteAll(User user) {
        List<Comment> comments = commentRepository.findByCreatorId(user.getId());
        if (comments == null) {
            return false;
        }
        commentRepository.deleteAll(comments);
        return true;
    }

    /**
     * Pobiera wszystkie komentarze z repozytorium.
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * Pobiera z repozytorium komentarz o podanym ID.
     */
    public Comment find(Long commentId) {
        return (Comment) commentRepository.getOne(commentId);
    }

    /**
     * Sprawdza czy istnieje komentarz o podanym ID.
     */
    public boolean checkIfExists(Long id) {
        return commentRepository.existsById(id);
    }
}