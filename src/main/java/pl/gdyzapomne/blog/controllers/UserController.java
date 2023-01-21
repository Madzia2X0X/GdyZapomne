package pl.gdyzapomne.blog.controllers;

import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.pojos.CustomUserDetails;
import pl.gdyzapomne.blog.pojos.StringPOJO;
import pl.gdyzapomne.blog.pojos.UserRegistrationPOJO;
import pl.gdyzapomne.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Kontroler obsługujący metody HTTP wysłane przez front-end.
 * Udostępnia wszystkie funkcjonalności związane z użytkownikami bloga, które są obsługiwane przez User Service.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenStore tokenStore;
    private final Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    private final Pattern patternEmail = Pattern.compile("^(.+)@(.+)$");

    @Autowired
    public UserController(UserService userService, TokenStore tokenStore) {
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    /**
     * Tworzy nowego użytkownika i zapisuje go w bazie danych.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationPOJO userRegistrationPOJO) {
        if (userRegistrationPOJO.getName().length() <2 ) {
            return new ResponseEntity<>("Prosimy o podanie prawidłowego imienia.", HttpStatus.BAD_REQUEST);
        } else if (userRegistrationPOJO.getSurname().length() < 2) {
            return new ResponseEntity<>("Prosimy o podanie prawidłowego nazwiska.", HttpStatus.BAD_REQUEST);
        } else if (!patternEmail.matcher(userRegistrationPOJO.getEmail()).find()) {
            return new ResponseEntity<>("Nieprawidłowy format adresu e-mail.", HttpStatus.BAD_REQUEST);
        } else if (userRegistrationPOJO.getUsername().length() < 5) {
            return new ResponseEntity<>("Nazwa użytkownika musi zawierać co najmniej 5 znaków.", HttpStatus.BAD_REQUEST);
        } else if (userRegistrationPOJO.getPassword().length() < 8) {
            return new ResponseEntity<>("Hasło musi zawierać co najmniej 8 znaków.", HttpStatus.BAD_REQUEST);
        } else if (!userRegistrationPOJO.getPassword().equals(userRegistrationPOJO.getPasswordConfirmation())) {
            return new ResponseEntity<>("Hasła nie zgadzają się ze sobą.", HttpStatus.BAD_REQUEST);
        } else if (userService.getUser(userRegistrationPOJO.getUsername()) != null) {
            return new ResponseEntity<>("Podana nazwa użytkownika jest już zajęta.", HttpStatus.BAD_REQUEST);
        } else if (pattern.matcher(userRegistrationPOJO.getUsername()).find()) {
            return new ResponseEntity<>("Nazwa użytkownika nie może zawierać znaków specjalnych.", HttpStatus.BAD_REQUEST);
        } else {
            userService.save(new User(userRegistrationPOJO.getUsername(), userRegistrationPOJO.getPassword(), userRegistrationPOJO.getName(), userRegistrationPOJO.getSurname(), userRegistrationPOJO.getEmail(), Arrays.asList(new Role("USER"))));
            return new ResponseEntity<>("Konto zostało założone.", HttpStatus.OK);
        }
    }

    /**
     * Zwraca wszystkich użytkowników z bazy danych.
     */
    @GetMapping
    public List<User> users() {
        return userService.getAllUsers();
    }

    /**
     * Zwraca wszystkie role użytkownika, który jest obecnie zalogowany.
     */
    @GetMapping(value ="getCurrent/roles")
    public Collection<? extends GrantedAuthority> getCurrentUserRole() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAuthorities();
    }

    /**
     * Zwraca nazwę użytkownika od użytkownika, który jest obecnie zalogowany.
     */
    @GetMapping(value ="/getCurrent/username")
    public String getCurrentUsername() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Zwraca nazwisko użytkownika, który jest obecnie zalgoowany.
     */
    @GetMapping(value ="/getCurrent/surname")
    public String getCurrentSurname() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUser(userDetails.getUsername()).getSurname();
    }

    /**
     * Zwraca adres e-mail użytkownika, który jest obecnie zalogowany.
     */
    @GetMapping(value ="/getCurrent/email")
    public String getCurrentEmail() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUser(userDetails.getUsername()).getEmail();
    }

    /**
     * Zwraca imię użytkownika, który jest obecnie zalogowany.
     */
    @GetMapping(value ="/getCurrent/name")
    public String getName() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUser(userDetails.getUsername()).getName();
    }

    /**
     * Wylogowywuje użytkownika, który jest obecnie zalogowany.
     */
    @GetMapping(value = "/logout")
    public void logout(@RequestParam(value = "access_token") String accessToken) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(accessToken));
    }

    /**
     * Zwraca imię i nazwisko podanego użytkownika.
     */
    @GetMapping(value="/getName/{username}")
    public String getName(@PathVariable String username) {
        return userService.getUser(username).getName() + " " + userService.getUser(username).getSurname();
    }

    /**
     * Usuwa podanego użytkownika.
     */
    @DeleteMapping(value = "/{username}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
        if(!userService.checkIfExists(username)) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.BAD_REQUEST);
        }
        if (username == "admin") {
            return new ResponseEntity<>("Nie wolno usunąć konta głównego administratora.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if(userService.delete(userService.getUser(username))) {
                return new ResponseEntity<>("Usunięto użytkownika.", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Nie udało się usunąć użytkownika. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Usuwa użytkownika, który jest obecnie zalogowany.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUsername() == "admin") {
            return new ResponseEntity<>("Nie wolno usunąć konta głównego administratora.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (userService.delete(userService.getUser(userDetails.getUsername()))) {
                return new ResponseEntity<>("Usunięto użytkownika.", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Nie udało się usunąć użytkownika. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Aktualizuje hasło użytkownika, który jest obecnie zalogowany.
     */
    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody StringPOJO newPassword) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userService.changePassword(userService.getUser(userDetails.getUsername()), newPassword.getResponseString())) {
            return new ResponseEntity<>("Hasło zostało zmienione.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nie udało się zmienić hasła. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Aktualizuje hasło podanego użytkownika.
     */
    @PatchMapping(value ="/{username}")
    public ResponseEntity<?> resetPassword(@PathVariable String username, @RequestBody StringPOJO newPassword) {
        if(!userService.checkIfExists(username)) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.BAD_REQUEST);
        }
        if(userService.changePassword(userService.getUser(username),newPassword.getResponseString())) {
            return new ResponseEntity<>("Hasło zostało zmienione.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nie udało się zmienić hasła. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Aktualizuje role podanego użytkownika.
     * Ustawia role na: USER (użytkownik)
     */
    @PatchMapping(value = "/setUser/{username}")
    public ResponseEntity<?> setUser(@PathVariable String username) {
        if(!userService.checkIfExists(username)) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.BAD_REQUEST);
        }
        if(userService.setUser(username)) {
            return new ResponseEntity<>("Ustawiono uprawnienia użytkownika na: Standardowy użytkownik bloga.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nie udało się zmienić uprawnień. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Aktualizuje role podanego użytkownika.
     * Ustawia role na: USER (użytkownik) oraz REDACTOR (redaktor)
     */
    @PatchMapping(value = "/setRedactor/{username}")
    public ResponseEntity<?> setRedactor(@PathVariable String username) {
        if(!userService.checkIfExists(username)) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.BAD_REQUEST);
        }
        if(userService.setRedactor(username)) {
            return new ResponseEntity<>("Ustawiono uprawnienia użytkownika na: Redaktor bloga.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nie udało się zmienić uprawnień. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Aktualizuje role podanego użytkownika.
     * Ustawia role na: USER (użytkownik), REDACTOR (redaktor) oraz ADMINISTRATOR
     */
    @PatchMapping(value = "/setAdmin/{username}")
    public ResponseEntity<?> setAdmin(@PathVariable String username) {
        if(!userService.checkIfExists(username)) {
            return new ResponseEntity<>("Podany użytkownik nie istnieje.", HttpStatus.BAD_REQUEST);
        }
        if(userService.setAdmin(username)) {
            return new ResponseEntity<>("Ustawiono uprawnienia użytkownika na: Administrator bloga.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nie udało się zmienić uprawnień. Spróbuj ponownie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}