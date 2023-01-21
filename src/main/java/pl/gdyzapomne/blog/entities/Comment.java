package pl.gdyzapomne.blog.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.util.Date;

/**
 * Obiekt pojedynczego komentarza.
 * Mapujemy tabelę z bazy danych na obiekt Java.
 * Pola klasy = kolumny w tabeli w bazie.
 */

@Entity
public class Comment {

    /**
     * Atrybuty obiektu, czyli kolumny w tabeli.
     */
    
    @Id
    @GeneratedValue
    private Long Id;

    @NotBlank(message = "Body is mandatory")
    @Column(length = 100000)
    private String text;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    private Date dateCreated;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User creator;

    /**
     * Kontruktory obiektu.
     * Definujemy w jaki sposób można utworzyć nowego posta.
     */

     /**
     * Pusty kontruktor jest wymagany przez Springa, żeby mapować obiekty do bazy danych.
     */

    public Comment() {
    }

    public Comment(Comment another) {
        this.Id = another.Id;
        this.text = another.text;
        this.creator = another.creator;
        this.post = another.post;
        this.dateCreated = another.dateCreated;
    }

    public Comment(String text, Post post, User creator, Date dateCreated) {
        this.text = text;
        this.post = post;
        this.creator = creator;
        this.dateCreated = dateCreated;
    }

    /**
     * Tak zwane gettery i settery.
     * Robimy hermetyzację/enkapsulację w klasie.
     * Ukrywamy widoczność pól obiektu, żeby był do nich dostęp tylko przez metody GET/SET.
     */

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
