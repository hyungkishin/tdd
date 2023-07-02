package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSendClient mailSendClient;

    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(final String fromEmail, final String toEmail, final String subject, final String contents) {
        final boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, contents);
        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(contents)
                    .build()
            );
            return true;
        }

        return false;
    }
}
