package pl.gdyzapomne.blog.repositories;

import pl.gdyzapomne.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium użytkowników.
 * Działa bezpośrednio na bazie danych.
 * Repozytorium może pobierać dane z tabeli „users”, zapisywać w niej nowe dane lub wykonywać na nich operacje aktualizacji i usuwania.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Pobiera z bazy użytkownika o podanej nazwie użytkownika.
     */
    User findByUsername(String username);

    /**
     * Usuwa podanego użytkownika z bazy danych.
     */
    void delete(User user);

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie użytkownika.
     */
    boolean existsByUsername(String username);
}