package com.glimpse.gpm.service;

import com.glimpse.gpm.event.OrcamentoEnviadoEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Usa o executor configurado no passo 3
    @Async("emailTaskExecutor")
    public void enviarEmailRecuperacao(String emailDestino, String linkRecuperacao) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Configura o Context do Thymeleaf para injetar variáveis no HTML
            Context context = new Context();
            context.setVariable("link", linkRecuperacao);

            // Processa o arquivo 'email-recuperacao.html' que criamos na pasta templates
            String htmlBody = templateEngine.process("email-recuperacao", context);

            helper.setTo(emailDestino);
            helper.setSubject("Redefinição de Senha - GPM");
            helper.setText(htmlBody, true); // true indica que o conteúdo é HTML
            helper.setFrom("glimpse.lab26@gmail.com");

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail SMTP: " + e.getMessage());
        }
    }

    @Async("emailTaskExecutor")
    public void enviarEmailNovoOrcamento(String emailDestino, OrcamentoEnviadoEvent event) {
        System.out.println("[EmailService] Preparando e-mail para: " + emailDestino + " (orcamentoId=" + event.id() + ")");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("orcamentoId", event.id());
            context.setVariable("clienteNome", event.clienteNome());
            context.setVariable("clienteEmail", event.clienteEmail());
            context.setVariable("descricaoProjeto", event.descricaoProjeto());
            context.setVariable("endereco", event.endereco());
            context.setVariable("status", event.status());
            context.setVariable("dataSolicitacao", event.dataSolicitacao());

            String htmlBody = templateEngine.process("email-novo-orcamento", context);

            helper.setTo(emailDestino);
            helper.setSubject("Novo orçamento recebido - GPM");
            helper.setText(htmlBody, true);
            helper.setFrom("glimpse.lab26@gmail.com");

            mailSender.send(message);
            System.out.println("[EmailService] E-mail enviado para: " + emailDestino + " (orcamentoId=" + event.id() + ")");
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail de novo orçamento para " + emailDestino + ": " + e.getMessage());
        }
    }
}