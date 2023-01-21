package pl.gdyzapomne.blog.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Obiekt pojedynczej roli w aplikacji.
 * Mapujemy tabelę z bazy danych na obiekt Java.
 * Pola klasy = kolumny w tabeli w bazie.
 */

@Entity
public class Role {
    
    /**
     * Atrybuty obiektu, czyli kolumny w tabeli.
     */

    @Id
    @GeneratedValue
    private Long id;
    String name;

    /**
     * Kontruktory obiektu.
     * Definujemy w jaki sposób można utworzyć nowego posta.
     */

     /**
     * Pusty kontruktor jest wymagany przez Springa, żeby mapować obiekty do bazy danych.
     */

    public Role() {
    }

    /**
     * Tak zwane gettery i settery.
     * Robimy hermetyzację/enkapsulację w klasie.
     * Ukrywamy widoczność pól obiektu, żeby był do nich dostęp tylko przez metody GET/SET.
     */

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
