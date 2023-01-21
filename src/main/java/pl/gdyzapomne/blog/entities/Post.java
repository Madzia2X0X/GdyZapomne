package pl.gdyzapomne.blog.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.util.Date;

/**
 * Obiekt pojedynczego posta.
 * Mapujemy tabelę z bazy danych na obiekt Java.
 * Pola klasy = kolumny w tabeli w bazie.
 */

@Entity
public class Post {
    
    /**
     * Atrybuty obiektu, czyli kolumny w tabeli.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200)
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Column(length = 10000)
    private String preview;

    @NotBlank(message = "Body is mandatory")
    @Column(length = 100000)
    private String body;

    @NotBlank(message = "Category is mandatory")
    private String category;

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

    public Post() {
    }

    public Post(Post another) {
        this.id = another.id;
        this.title = another.title;
        this.body = another.body;
        this.preview = another.preview;
        this.creator = another.creator;
        this.category = another.category;
        this.dateCreated = another.dateCreated;
    }

    /**
     * Tak zwane gettery i settery.
     * Robimy hermetyzację/enkapsulację w klasie.
     * Ukrywamy widoczność pól obiektu, żeby był do nich dostęp tylko przez metody GET/SET.
     */

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
