package pl.gdyzapomne.blog.pojos;

/**
 * Zwykła prosta klasa java (POJO) pojedynczego ciągu znaków - używana podczas wysyłania zwykłego ciągu danych z frontendu do endpointa z naszego REST API.
 */
public class StringPOJO
{
    private String responseString;

    public StringPOJO() {
    }

    public StringPOJO(String responseString) {
        this.responseString = responseString;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
