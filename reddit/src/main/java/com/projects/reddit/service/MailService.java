package com.projects.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.projects.reddit.exception.SpringRedditException;
import com.projects.reddit.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
@Slf4j
class MailService {

	
    private final JavaMailSender mailSender;
	
    private final MailContentBuilder mailContentBuilder = new MailContentBuilder();
    private static final Logger log = LoggerFactory.getLogger(MailService.class);


    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Async
    void sendMail(NotificationEmail notificationEmail) throws SpringRedditException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringRedditException("Exception occurred when sending mail to ");
        }
    }

}