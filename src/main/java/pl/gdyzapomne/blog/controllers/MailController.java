package pl.gdyzapomne.blog.controllers;

import pl.gdyzapomne.blog.pojos.MailPOJO;
import pl.gdyzapomne.blog.pojos.StringPOJO;
import pl.gdyzapomne.blog.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kontroler, który używa klasy SendMailService do wysyłania powiadomień.
 */

@RestController
@RequestMapping("/mails")
public class MailController {

    private final SendMailService sendMailService;
    private final Pattern patternEmail = Pattern.compile("^(.+)@(.+)$");
    final String recipient = "gdyzapomne.blog@gmail.com";

    @Autowired
    public MailController(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }


    /**
     * Wysyła wiadomość e-mail na główne konto e-mail bloga z wiadomością.
     * Jest używany przez funkcję „Kontakt” na blogu.
     * Użytkownicy mogą go używać do wysyłania wiadomości do administratora bloga.
     */
    @PostMapping(value = "/contact")
    public ResponseEntity<?> sendMail(@RequestBody MailPOJO mail)
    {
        if(sendMailService.sendEmail(recipient, mail.getBody(), mail.getTitle())) {
            return new ResponseEntity<String>("Wysłano wiadomość.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("Nie udało się wysłać wiadomości. Prosimy spróbować ponownie.", HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Dodaje nowego subskrybenta do bazy danych.
     */
    @PostMapping(value = "/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody StringPOJO emailAddress) {
        Matcher mat = patternEmail.matcher(emailAddress.getResponseString());
        if(mat.matches()) {
            if(sendMailService.subscribe(emailAddress.getResponseString())) {
                return new ResponseEntity<String>("Adres e-mail został dodany do listy subskrybentów.", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("Taki adres e-mail już istnieje na naszej liście subskrybentów.", HttpStatus.CONFLICT);
            }
        }
        else {
            return new ResponseEntity<String>("To nie jest prawidłowy adres e-mail.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Usuwa wybranego subskrybenta z bazy danych.
     */
    @DeleteMapping(value = "/subscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody StringPOJO emailAddress)
    {
        Matcher mat = patternEmail.matcher(emailAddress.getResponseString());
        if(mat.matches()) {
            if(sendMailService.unsubscribe(emailAddress.getResponseString())) {
                return new ResponseEntity<String>("Adres e-mail został usunięty z listy subskrybentów.", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("Taki adres e-mail nie znajduje się na naszej liście subskrybentów.", HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<String>("To nie jest prawidłowy adres e-mail.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Wysyła e-mail do wszystkich subskrybentów.
     * Jest używany przez funkcję „Powiadom subskrybentów” na blogu.
     * Administrator bloga może go użyć do wysłania wiadomości do wszystkich subskrybentów.
     */
    @PostMapping("/to-subscribers")
    public ResponseEntity<?> mailToSubscribers(@RequestBody MailPOJO mail) {
        if(sendMailService.sendMessageToSubscribers(mail)) {
        return new ResponseEntity<String>("Wysłano e-maila.", HttpStatus.OK);
        }
        else {
        return new ResponseEntity<String>("Nie udało się wysłać e-maila.", HttpStatus.BAD_REQUEST);
        }
    }
}