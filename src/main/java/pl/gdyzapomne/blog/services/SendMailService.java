package pl.gdyzapomne.blog.services;

import pl.gdyzapomne.blog.entities.Post;
import pl.gdyzapomne.blog.entities.Subscriber;
import pl.gdyzapomne.blog.pojos.MailPOJO;
import pl.gdyzapomne.blog.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa, która wykorzystuje bibliotekę JavaMail oraz repozytorium subskrybentów do implementacji funkcjonalności subskbrybcji oraz powiadomień mailowych.
 */
@Service
public class SendMailService {

    private final JavaMailSender javaMailSender;
    private final SubscriberRepository subscriberRepository;

    @Autowired
    public SendMailService(JavaMailSender javaMailSender, SubscriberRepository subscriberRepository) {
        this.javaMailSender = javaMailSender;
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Pobiera z repozytorium adresy e-mail wszystkich subskrybentów.
     */
    public String[] getAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        List<String> addresses = new ArrayList<String>();
        if (!(subscribers == null)) {
            for (Subscriber subscriber : subscribers) {
                addresses.add(subscriber.getEmail());
            }
        }
        String[] addressesList = addresses.toArray(new String[0]);
        return addressesList;
    }

    /**
     * Tworzy nową wiadomość e-mail z danych, które zostały podane do funkcji.
     * Następnie wysyła wiadomość e-mail do skrzynki mailowej administratora bloga za pomocą biblioteki JavaMail.
     */
    public boolean sendEmail(String to, String body, String topic) {
        System.out.println("Próbuję wysłać e-mail...");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("gdyzapomne.blog@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(topic);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
        return true;
    }

    /**
     * Tworzy nową wiadomość e-mail z danych, które zostały podane do funkcji.
     * Następnie wysyła wiadomość e-mail do wszystkich subskrybentów bloga za pomocą biblioteki JavaMail.
     */
    public boolean sendEmailToSubscribers (String[] to, String body, String topic) {
        System.out.println("Próbuję wysłać e-mail do subskrybentów...");
        if (to.length == 0) {
            System.out.println("Nie ma żadnych subskrybentów.");
            return false;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("gdyzapomne.blog@gmail.com");
        simpleMailMessage.setBcc(to);
        simpleMailMessage.setSubject(topic);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
        return true;
    }

    /**
     * Tworzy nowego subskrybenta na podstawie podanego adresu e-mail, a następnie zapisuje go w repozytorium.
     */
    public boolean subscribe(String emailAddress) {
        Subscriber subscriber = (Subscriber) subscriberRepository.findByEmail(emailAddress);
        if (subscriber != null) {
            return false;
        }
        else {
            subscriberRepository.save(new Subscriber(emailAddress));
            return true;
        }
    }

    /**
     * Usuwa z repozytorium subskrybenta o podanym adresie e-mail.
     */
    public boolean unsubscribe(String emailAddress) {
        Subscriber subscriber = (Subscriber) subscriberRepository.findByEmail(emailAddress);
        if (subscriber == null) {
            return false;
        }
        else {
            subscriberRepository.delete(subscriber);
            return true;
        }
    }

    /**
     * Pobiera z repozytorium wszystkich subskrybentów, a następnie wysyła dane do funkcji „sendEmailToSubscribers()”, aby wysłać wiadomość e-mail do wszystkich subskrybentów.
     */
    public boolean sendMessageToSubscribers(MailPOJO mail) {
        String[] addressesList = getAllSubscribers();
        if (addressesList.length == 0) {
            System.out.println("Nie ma żadnych subskrybentów.");
            return false;
        }
        sendEmailToSubscribers(addressesList, mail.getBody(), mail.getTitle());
        return true;
    }

    /**
     * Asynchroniczna funkcja, która wysyła wiadomość e-mail do wszystkich subskrybentów bloga, aby powiadomić ich o nowym poście, który został opublikowany na blogu.
     * Funkcja jest asynchroniczna ze względu na to, żeby maile wysyłały się w tle po opublikowaniu nowego posta.
     */
    @Async
    public Boolean notifySubscribers(Post post) {
        System.out.println("Wykonywanie metody asynchronicznie... ----" + Thread.currentThread().getName());
        String[] addressesList = getAllSubscribers();
        if (addressesList.length == 0) {
            System.out.println("Nie ma żadnych subskrybentów.");
            return false;
        }
        String message = "Witaj! Na naszym blogu pojawił się nowy post od redaktora: " + post.getCreator().getName() + " "
                + post.getCreator().getSurname() + ". Tytuł posta: " + post.getTitle() + ".";
        sendEmailToSubscribers(addressesList, message, "Powiadomienie o nowym poście");
        System.out.println("Wysłano e-mail. ");
        return true;
    }
}