package it.danilo.blog.buisnesslayer.services.impl.email;

import it.danilo.blog.buisnesslayer.services.interfaces.email.MailService;

import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.EmailSendingException;
import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.UnsupportedEmailEncodingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {


    private final JavaMailSender javaMailSender;

    @Value("${value.endpoint.reset.password}")
    private String endpointResetPassword;

    @Override
    public void sendResetPasswordMail(String email, String token) throws EmailSendingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("reset della password");
            String emailContent =
                    "<head>" +
                            "<style>" +
                            "body { font-family: 'Arial', sans-serif; font-weight: 400; }" +
                            ".email-container { padding: 20px; }" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='email-container'>" +
                            "<div>" +
                            "<p style='font-size: 18px;'>Gentile Utente,</p>" +
                            "<p style='font-size: 14px;'>Hai richiesto di reimpostare la tua password. Per procedere con il reset, ti invitiamo a cliccare sul seguente " +
                            "<a href='"+ endpointResetPassword + token + "'>link</a>.</p>" +
                            "<p style='font-size: 14px;'>Se non hai effettuato questa richiesta, ti preghiamo di ignorare questa email. Il link per il reset della password scadrà automaticamente dopo un determinato periodo di tempo per ragioni di sicurezza.</p>" +
                            "<p style='font-size: 14px;'>Per assistenza o ulteriori informazioni, puoi contattarci rispondendo a questa email.</p>" +
                            "<p style='font-size: 14px;'>Cordiali saluti,</p>" +
                            "<p style='font-size: 14px;'>Il Team di Supporto</p>" +
                            "</div>" +
                            "</div>" +
                            "</body>";;

            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException(email);
        }
    }
}
