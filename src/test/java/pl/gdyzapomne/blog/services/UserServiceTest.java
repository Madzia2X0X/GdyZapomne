package pl.gdyzapomne.blog.services;

import static org.mockito.Mockito.doReturn;

import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    /**
     * Funkcja przygotowująca testowego użytkownika do wykorzystania w testach jednostkowych.
     */
    public void setUpTestUser() {

        User user1 = new User("admin", "admin1234", "Administrator", "portalu", "gdyzapomne.blog@gmail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"),new Role("ADMIN")));
        doReturn(user1).when(userRepository).findByUsername("admin");
    }

    /**
     * Sprawdza, czy obiekt użytkownika została utworzona poprawnie.
     */
    @Test
    @DisplayName("Pomyślne utworzenie obiektu użytkownika")
    public void postEntityCreation() {
        setUpTestUser();
        User testUser = new User(userRepository.findByUsername("admin"));
        Assertions.assertEquals("admin", testUser.getUsername());
        Assertions.assertEquals("Administrator", testUser.getName());
        Assertions.assertEquals("portalu", testUser.getSurname());
        Assertions.assertEquals("gdyzapomne.blog@gmail.com", testUser.getEmail());
        List<Role> testRoles = testUser.getRoles();
        List<String> roleNames = new ArrayList<>();
        for(Role role : testRoles) {
            roleNames.add(role.getName());
        }
        Assertions.assertEquals(Arrays.asList(new Role("USER").getName(), new Role("REDACTOR").getName(), new Role("ADMIN").getName()), roleNames);
    }
}