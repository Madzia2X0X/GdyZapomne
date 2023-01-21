package pl.gdyzapomne.blog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import pl.gdyzapomne.blog.entities.Comment;
import pl.gdyzapomne.blog.entities.Post;
import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.CommentRepository;
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
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    /**
     * Funkcja przygotowująca testowego użytkownika do wykorzystania w testach jednostkowych.
     */
    public void setUpTestUser() {
        User user1 = new User("admin", "admin1234", "Administrator", "portalu", "gdyzapomne.blog@gmail.com", Arrays.asList(new Role("USER"), new Role("REDACTOR"), new Role("ADMIN")));
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
     * Funkcja przygotowująca komentarz testowy do wykorzystania w testach jednostkowych.
     */
    public void setUpTestComment() {
        setUpTestPost();
        Comment comment1 = new Comment();
        comment1.setText("Ten komentarz został stworzony do celów testowych");
        comment1.setCreator(userRepository.findByUsername("admin"));
        comment1.setPost(postRepository.getOne((long) 1));
        comment1.setDateCreated(getTestDate());
        doReturn(comment1).when(commentRepository).getOne((long) 1);
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
     * Sprawdza, czy obiekt komentarza został utworzony poprawnie.
     */
    @Test
    @DisplayName("Pomyślne utworzenie encji komentarza")
    public void commentEntityCreation() {
        setUpTestComment();
        Comment testComment = new Comment(commentRepository.getOne((long) 1));
        Assertions.assertEquals("Ten komentarz został stworzony do celów testowych", testComment.getText());
        Assertions.assertEquals(postRepository.getOne((long) 1), testComment.getPost());
        Assertions.assertEquals(userRepository.findByUsername("admin").getUsername(), testComment.getCreator().getUsername());
        Assertions.assertEquals(getTestDate(), testComment.getDateCreated());
    }

    /**
     * Sprawdza, czy funkcja usuwająca komentarz zwraca wartość false, jeśli nie ma komentarza o podanym identyfikatorze.
     */
    @Test
    @DisplayName("Powinien zwrócić false, jeśli nie ma komentarza o podanym identyfikatorze")
    public void deleteCommentWhenNoPostFoundReturnsFalse() {
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        when(mockCommentRepository.getOne((long) 1)).thenReturn(null);
        CommentService testCommentService = new CommentService(mockCommentRepository);
        Assertions.assertEquals(testCommentService.deleteComment((long) 1), false);
    }

    /**
     * Sprawdza, czy funkcja usuwająca wszystkie komentarze zwraca false w przypadku braku komentarzy danego użytkownika.
     */
    @Test
    @DisplayName("Powinien zwrócić false, jeśli brak komentarzy danego użytkownika")
    public void deleteAllCommentsWhenNoPostsFoundReturnsFalse() {
        setUpTestUser();
        User testUser = userRepository.findByUsername("admin");
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        when(mockCommentRepository.findByCreatorId(testUser.getId())).thenReturn(null);
        CommentService testCommentService = new CommentService(mockCommentRepository);
        Assertions.assertEquals(testCommentService.deleteAll(testUser), false);
    }

}