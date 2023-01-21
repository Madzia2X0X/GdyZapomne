package pl.gdyzapomne.blog.pojos;

/**
 * Zwykła prosta klasa java (POJO) pojedynczego e-maila - używana podczas wysyłania e-maila z frontendu do endpointa z naszego REST API.
 */
public class MailPOJO
{
    private String recipient;
    private String title;
    private String body;

    public MailPOJO() {
    }

    public MailPOJO(String recipient, String title, String body) {
        this.recipient = recipient;
        this.title = title;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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
}
