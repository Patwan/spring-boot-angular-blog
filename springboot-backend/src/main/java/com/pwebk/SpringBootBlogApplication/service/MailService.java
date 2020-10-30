package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.exceptions.SpringRedditException;
import com.pwebk.SpringBootBlogApplication.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    //The method sendMail is void meaning it returns nothing. Passing NotificationEmail entity as a
    //parameter. Inside the method we construct an instance of MimeMessageHelper inside the lambda.
    //This creates an instance of MimeMessagePreparator
    //To the MimeMessageHelper we are passing the following: setFrom (Email origin), setTo (Email Destination),
    //setSubject (subject of the email), setText (email body).
    //For the setText method we are calling the build method of mailContentBuilder, this method will
    //return the message in HTML format.

    //Lastly we use send method of Java MailSender class in the try bock, if sent we log a message as activation
    //email sent.

    //For log, we define Sl4J from lombok annotation, which creates an instance of SL4J logger object and inject
    //it into our class.

    //Inside the catch bock we throw the errors in SpringRedditException (class found in
    // exceptions pckage) withe the message.

    @Async
    void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringRedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }
}
