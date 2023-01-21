package pl.gdyzapomne.blog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import pl.gdyzapomne.blog.entities.Post;
import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.PostRepository;
import pl.gdyzapomne.blog.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

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
     * Funkcja przygotowująca testowy post do wykorzystania w testach jednostkowych.
     */
    public void setUpTestPost() {
        setUpTestUser();
        Post post1 = new Post();
        post1.setTitle("Ten post został stworzony do celów testowych");
        post1.setBody("Ten post został stworzony do celów testowych. Ten post został stworzony do celów testowych. Ten post został stworzony do celów testowych.");
        post1.setCreator(userRepository.findByUsername("admin"));
        post1.setDateCreated(getTestDate());
        doReturn(post1).when(postRepository).getOne((long) 1);
    }

    /**
     * Funkcja przygotowująca testową datę, którą można wykorzystać w testach jednostkowych.
     */
    public Date getTestDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 07);
        calendar.set(Calendar.HOUR, 20);
        calendar.set(Calendar.MINUTE, 57);
        calendar.set(Calendar.SECOND, 12);
        return calendar.getTime();
    }

    /**
     * Sprawdza, czy obiekt posta jest tworzona poprawnie.
     */
    @Test
    @DisplayName("Pomyślne utworzenie obiektu posta")
    public void postEntityCreation() {
        setUpTestPost();
        Post testPost = new Post(postRepository.getOne((long) 1));
        Assertions.assertEquals("Ten post został stworzony do celów testowych", testPost.getTitle());
        Assertions.assertEquals("Ten post został stworzony do celów testowych. Ten post został stworzony do celów testowych. Ten post został stworzony do celów testowych.", testPost.getBody());
        Assertions.assertEquals(userRepository.findByUsername("admin").getUsername(), testPost.getCreator().getUsername());
        Assertions.assertEquals(getTestDate(), testPost.getDateCreated());
    }

    /**
     * Sprawdza, czy funkcja usuwająca post zwróci wartość false, jeśli nie ma posta o podanym ID.
     */
    @Test
    @DisplayName("Powinien zwrócić wartość false, jeśli nie ma posta o podanym ID")
    public void deletePostWhenNoPostFoundReturnsFalse() {
        PostRepository mockPostRepository = mock(PostRepository.class);
        when(mockPostRepository.getOne((long) 1)).thenReturn(null);
        PostService testPostService = new PostService(mockPostRepository);
        Assertions.assertEquals(testPostService.deletePost((long) 1), false);
    }

    /**
     * Sprawdza, czy funkcja usuwająca wszystkie posty zwraca wartość false, jeśli nie ma postów danego użytkownika.
     */
    @Test
    @DisplayName("Powinien zwrócić wartość false, jeśli nie ma żadnych postów danego użytkownika")
    public void deleteAllPostsWhenNoPostsFoundReturnsFalse() {
        setUpTestUser();
        User testUser = userRepository.findByUsername("admin");
        PostRepository mockPostRepository = mock(PostRepository.class);
        when(mockPostRepository.findByCreatorId(testUser.getId())).thenReturn(null);
        PostService testPostService = new PostService(mockPostRepository);
        Assertions.assertEquals(testPostService.deleteAll(testUser), false);
    }
    
}