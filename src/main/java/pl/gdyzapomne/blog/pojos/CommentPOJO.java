package pl.gdyzapomne.blog.pojos;

import java.util.Date;

/**
 * Zwykła prosta klasa java (POJO) pojedynczego komentarza - używana podczas wysyłania danych komentarza z frontendu do endpointa z naszego REST API.
 */

public class CommentPOJO
{

    private String text;
    private Long postId;
    private Date dateCreated;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
