package pl.gdyzapomne.blog.repositories;

import pl.gdyzapomne.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozytorium komentarzy.
 * Działa bezpośrednio na bazie danych.
 * Repozytorium może pobierać dane z tabeli „comments”, zapisywać w niej nowe dane lub wykonywać na nich operacje aktualizacji i usuwania.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    /**
     * Pobiera z bazy wszystkie komentarze, które są przypięte do posta o podanym ID.
     */
    List<Comment> findByPostId(Long postId);

    /**
     * Usuwa z bazy komentarz o podanym ID.
     */
    void deleteById(Long id);

    /**
     * Pobiera z bazy wszystkie komentarze, które zostały utworzone przez użytkownika o podanym ID.
     */
    List<Comment> findByCreatorId(Long id);

    /**
     * Usuwa podane komentarze z bazy danych.
     */
    void deleteAll(Iterable<? extends Comment> comments);
}
