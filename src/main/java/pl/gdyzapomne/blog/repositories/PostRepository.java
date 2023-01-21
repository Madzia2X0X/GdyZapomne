package pl.gdyzapomne.blog.repositories;

import pl.gdyzapomne.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozytorium postów.
 * Działa bezpośrednio na bazie danych.
 * Repozytorium może pobierać dane z tabeli „posts”, zapisywać w niej nowe dane lub wykonywać na nich operacje aktualizacji i usuwania.
 */
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    /**
     * Pobiera z bazy wszystkie posty, które zostały utworzone przez użytkownika o podanym ID.
     */
    List<Post> findByCreatorId(Long id);

    /**
     * Pobiera z bazy wszystkie posty, które mają przypisane podaną kategorię.
     */
    List<Post> findByCategory(String category);

    /**
     * Usuwa z bazy danych post o podanym ID.
     */
    void deleteById(Long id);

    /**
     * Usuwa z bazy danych podane posty.
     */
    void deleteAll(Iterable<? extends Post> posts);

    /**
     * Pobiera z bazy wszystkie posty, które zawierają w tytule podane słowo kluczowe.
     */
    List<Post> findByTitleContainingIgnoreCase(String keyword);

    /**
     * Pobiera z bazy wszystkie posty, które zawierają w treści podane słowo kluczowe.
     */
    List<Post> findByBodyContainingIgnoreCase(String keyword);
}
