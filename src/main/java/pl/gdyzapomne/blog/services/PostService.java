package pl.gdyzapomne.blog.services;

import pl.gdyzapomne.blog.entities.Post;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa działająca między kontrolerem postów, a repozytorium postów.
 * Implementuje wszystkie funkcjonalności postów.
 */
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Pobiera wszystkie posty z repozytorium.
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Zapisuje nowy post w repozytorium.
     */
    public boolean insert(Post post) {
        postRepository.save(post);
        return true;
    }

    /**
     * Pobiera z repozytorium wszystkie posty opublikowane przez podanego użytkownika.
     */
    public List<Post> findByUser(User user) {
        return postRepository.findByCreatorId(user.getId());
    }

    /**
     * Pobiera z repozytorium wszystkie posty o podanej kategorii.
     */
    public List<Post> findByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    /**
     * Usuwa z repozytorium post o podanym ID.
     */
    public boolean deletePost(Long postId) {
        Post post = (Post) postRepository.getOne(postId);
        if (post == null) {
            return false;
        }
        postRepository.deleteById(postId);
        return true;
    }

    /**
     * Aktualizuje podany post w repozytorium.
     */
    public boolean update(Post post) {
        Post basePost = find(post.getId());
        basePost.setTitle(post.getTitle());
        basePost.setBody(post.getBody());
        basePost.setPreview(post.getPreview());
        basePost.setCategory(post.getCategory());
        postRepository.save(basePost);
        return true;
    }

    /**
     * Usuwa z repozytorium wszystkie posty podanego użytkownika.
     */
    public boolean deleteAll(User user) {
        List<Post> posts = postRepository.findByCreatorId(user.getId());
        if (posts == null)
            return false;
        postRepository.deleteAll(posts);
        return true;
    }

    /**
     * Pobiera z repozytorium post o podanym ID.
     */
    public Post find(Long postId) {
        return postRepository.findById(postId).get();
    }

    /**
     * Sprawdza czy istnieje post o podanym ID.
     */
    public boolean checkIfExists(Long id) {
        return postRepository.existsById(id);
    }

    /**
     * Pobiera z repozytorium wszystkie posty zawierające podane słowo kluczowe.
     */
    public List<Post> findByKeyword(String keyword) {
        final List<Post> posts = postRepository.findByTitleContainingIgnoreCase(keyword);
        posts.addAll(postRepository.findByBodyContainingIgnoreCase(keyword));
        final Set<Post> set = new LinkedHashSet<>(posts);
        posts.clear();
        posts.addAll(set);
        return posts;
    }
}
