package pl.gdyzapomne.blog.pojos;

/**
 * Zwykła prosta klasa java (POJO) formularza rejestracji nowego użytkownika - używana podczas wysyłania danych dotyczących rejestracji z frontendu do endpointa z naszego REST API.
 */
public class UserRegistrationPOJO
{
    private String username;
    private String password;
    private String passwordConfirmation;
    private String name;
    private String surname;
    private String email;

    public UserRegistrationPOJO(String username, String password, String passwordConfirmation, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public UserRegistrationPOJO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
