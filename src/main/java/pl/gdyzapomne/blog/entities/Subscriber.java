package pl.gdyzapomne.blog.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Obiekt pojedynczego subskrybenta.
 * Mapujemy tabelę z bazy danych na obiekt Java.
 * Pola klasy = kolumny w tabeli w bazie.
 */

@Entity
public class Subscriber {

    /**
     * Atrybuty obiektu, czyli kolumny w tabeli.
     */
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email")
    private String email;

    /**
     * Kontruktory obiektu.
     * Definujemy w jaki sposób można utworzyć nowego posta.
     */

     /**
     * Pusty kontruktor jest wymagany przez Springa, żeby mapować obiekty do bazy danych.
     */

    public Subscriber() {
    }

    public Subscriber(String email) {
        this.email = email;
    }

    /**
     * Tak zwane gettery i settery.
     * Robimy hermetyzację/enkapsulację w klasie.
     * Ukrywamy widoczność pól obiektu, żeby był do nich dostęp tylko przez metody GET/SET.
     */

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
