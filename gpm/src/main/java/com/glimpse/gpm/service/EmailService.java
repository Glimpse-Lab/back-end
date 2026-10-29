package com.glimpse.gpm.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async("emailTaskExecutor")
    public void enviarEmailRecuperacao(String emailDestino, String linkRecuperacao) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("link", linkRecuperacao);

            String htmlBody = templateEngine.process("email-recuperacao", context);

            helper.setTo(emailDestino);
            helper.setSubject("Redefinição de Senha - GPM");
            helper.setText(htmlBody, true);
            helper.setFrom(fromEmail);

            mailSender.send(message);
            log.info("E-mail de recuperação enviado para: {}", emailDestino);
        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail de recuperação para {}: {}", emailDestino, e.getMessage());
        }
    }
}