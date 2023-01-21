package pl.gdyzapomne.blog.services;

import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa działająca między kontrolerem użytkowników, a repozytorium użytkowników.
 * Implementuje wszystkie funkcjonalności użytkowników.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Szyfruje hasło użytkownika i zapisuje użytkownika w repozytorium.
     */
    public boolean save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * Usuwa podanego użytkownika.
     */
    public boolean delete(User user) {
        User thisUser = (User) userRepository.findByUsername(user.getUsername());
        if (thisUser == null) {
            return false;
        }
        userRepository.delete(thisUser);
        return true;
    }

    /**
     * Aktualizuje hasło danego użytkownika, szyfruje je i aktualizuje użytkownika w repozytorium.
     */
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    /**
     * Zwraca użytkownika o podanym ID.
     */
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Pobiera wszystkich użytkowników z repozytorium.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Aktualizuje role użytkownika o podanej nazwie użytkownika.
     * Ustawia role na: USER (standardowy użytkownik) oraz REDACTOR (redaktor)
     */
    public boolean setRedactor(String username) {
        User user = (User) userRepository.findByUsername(username);
        List<Role> roles = new ArrayList<Role>(Arrays.asList(new Role("USER"), new Role("REDACTOR")));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    /**
     * Aktualizuje role użytkownika o podanej nazwie użytkownika.
     * Ustawia role na: USER (standardowy użytkownik)
     */
    public boolean setUser(String username) {
        User user = (User) userRepository.findByUsername(username);
        List<Role> roles = new ArrayList<Role>(Arrays.asList(new Role("USER")));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    /**
     * Aktualizuje role użytkownika o podanej nazwie użytkownika.
     * Ustawia role na: USER (standardowy użytkownik), REDACTOR (redaktor) oraz ADMINISTRATOR
     */
    public boolean setAdmin(String username) {
        User user = (User) userRepository.findByUsername(username);
        List<Role> roles = new ArrayList<Role>(Arrays.asList(new Role("USER"), new Role("REDACTOR"), new Role("ADMIN")));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    /**
     * Sprawdza czy istnieje użytkownik o podanej nazwie użytkownika.
     */
    public boolean checkIfExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
