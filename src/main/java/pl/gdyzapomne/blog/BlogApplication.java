package pl.gdyzapomne.blog;

import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.pojos.CustomUserDetails;
import pl.gdyzapomne.blog.repositories.UserRepository;
import pl.gdyzapomne.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Główna klasa aplikacji. Służy do wystartowania aplikacji plus dodatkowej logiki z tym związanej.
 */
@SpringBootApplication
public class BlogApplication {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BlogApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Jeśli aplikacja nie znajdzie w bazie konta administratora oraz moderatorów to są one automatycznie tworzone i zapisywane w bazie.
     */
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository, UserService userService) throws Exception {
        if (isEmpty(userService.getUser("admin"))) {
            userService.save(new User("admin", "admin1234", "Administrator", "portalu","gdyzapomne.blog@gmail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"),new Role("ADMIN"))));
        }
        if (isEmpty(userService.getUser("jmajewska"))) {
            userService.save(new User("jmajewska", "test1234", "Julia", "Majewska","jmajewska@mail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"))));
        }
        if (isEmpty(userService.getUser("kkowalski"))) {
            userService.save(new User("kkowalski", "test1234", "Kamil", "Kowalski","kkowalski@mail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"))));
        }
        if (isEmpty(userService.getUser("knowak"))) {
            userService.save(new User("knowak", "test1234", "Karolina", "Nowak","knowak@mail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"))));
        }
        if (isEmpty(userService.getUser("jazajac"))) {
            userService.save(new User("jzajac", "test1234", "Justyna", "Zając","jzajac@mail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"))));
        }
        builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
    }

    /**
     * Start aplikacji.
     */
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * Ładuje dane specyficzne dla użytkownika.
     */
    private UserDetailsService userDetailsService(final UserRepository repository) {
        return username -> new CustomUserDetails(repository.findByUsername(username));
    }
}