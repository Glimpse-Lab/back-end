package com.glimpse.gpm.listener;

import com.glimpse.gpm.event.OrcamentoEnviadoEvent;
import com.glimpse.gpm.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrcamentoEnviadoListener {

    private final EmailService emailService;
    private final String adminEmail;

    public OrcamentoEnviadoListener(EmailService emailService,
                                    @Value("${app.admin.email}") String adminEmail) {
        this.emailService = emailService;
        this.adminEmail = adminEmail;
    }

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoEnviarOrcamento(OrcamentoEnviadoEvent event) {
        System.out.println("[OrcamentoEnviadoListener] Enviando novo-orçamento para admin: " + adminEmail);
        try {
            emailService.enviarEmailNovoOrcamento(adminEmail, event);
        } catch (Exception e) {
            System.err.println("[OrcamentoEnviadoListener] Erro ao enviar para admin " + adminEmail + ": " + e.getMessage());
        }
    }
}