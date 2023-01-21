package pl.gdyzapomne.blog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.util.List;

/**
 * Obiekt pojedynczego użytkownika.
 * Mapujemy tabelę z bazy danych na obiekt Java.
 * Pola klasy = kolumny w tabeli w bazie.
 */

@Entity
public class User {

    /**
     * Atrybuty obiektu, czyli kolumny w tabeli.
     */
    
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username")
    private String username;

    @JsonIgnore
    @NotBlank(message = "Password is mandatory")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(name = "roles")
    private List<Role> roles;

    /**
     * Kontruktory obiektu.
     * Definujemy w jaki sposób można utworzyć nowego posta.
     */

     /**
     * Pusty kontruktor jest wymagany przez Springa, żeby mapować obiekty do bazy danych.
     */
    public User() {
    }

    public User(String username, String password, String name, String surname, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = roles;
    }

    public User(User another) {
        this.id = another.id;
        this.username = another.username;
        this.password = another.password;
        this.name = another.name;
        this.surname = another.surname;
        this.email = another.email;
        this.roles = another.roles;
    }

    /**
     * Tak zwane gettery i settery.
     * Robimy hermetyzację/enkapsulację w klasie.
     * Ukrywamy widoczność pól obiektu, żeby był do nich dostęp tylko przez metody GET/SET.
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}