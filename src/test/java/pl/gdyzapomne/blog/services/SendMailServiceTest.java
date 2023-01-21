package pl.gdyzapomne.blog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import pl.gdyzapomne.blog.entities.Role;
import pl.gdyzapomne.blog.entities.User;
import pl.gdyzapomne.blog.repositories.PostRepository;
import pl.gdyzapomne.blog.repositories.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gdyzapomne.blog.repositories.UserRepository;


import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;


@SpringBootTest
class SendMailServiceTest {


    @Mock
    private PostRepository postRepository;


    /**
     * Sprawdza, czy funkcja wysyłająca wiadomość e-mail zwraca wartość false, jeśli nie znaleziono subskrybentów.
     */
    @Test
    @DisplayName("Powinien zwrócić wartość false, jeśli nie znaleziono subskrybentów")
    public void sendEmailToSubscribersWhenNoSubscribersFoundReturnsFalse() {
        SubscriberRepository mockSubscriberRepository = mock(SubscriberRepository.class);
        when(mockSubscriberRepository.findAll()).thenReturn(null);
        JavaMailSender javaMailSender = new JavaMailSender() {
            @Override
            public MimeMessage createMimeMessage() {
                return null;
            }

            @Override
            public MimeMessage createMimeMessage(InputStream inputStream) throws MailException {
                return null;
            }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException {

            }

            @Override
            public void send(MimeMessage... mimeMessages) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

            }

            @Override
            public void send(SimpleMailMessage simpleMailMessage) throws MailException {

            }

            @Override
            public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

            }
        };
        SendMailService testSendMailService = new SendMailService(javaMailSender, mockSubscriberRepository);
        Assertions.assertEquals(testSendMailService.notifySubscribers(postRepository.getOne((long) 1)), false);
    }

}