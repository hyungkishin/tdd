package sample.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

    public boolean sendEmail(final String fromEmail, final String toEmail, final String subject, final String contents) {
        // 메일 전송
        log.info("메일 전송");
        throw new IllegalStateException("메일 전송");
    }


    public void a() {
        log.info("a");
    }

    public void b() {
        log.info("b");
    }

    public void c() {
        log.info("c");
    }

}
