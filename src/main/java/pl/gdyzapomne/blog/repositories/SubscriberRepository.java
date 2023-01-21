package pl.gdyzapomne.blog.repositories;

import pl.gdyzapomne.blog.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium subskrybentów.
 * Działa bezpośrednio na bazie danych.
 * Repozytorium może pobierać dane z tabeli „subscribers”, zapisywać w niej nowe dane lub wykonywać na nich operacje aktualizacji i usuwania.
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {

    /**
     * Pobiera z bazy danych subskrybenta o podanym adresie e-mail.
     */
    Subscriber findByEmail(String email);
}
