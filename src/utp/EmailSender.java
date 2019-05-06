package utp;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private final String senderEmail = "jankowal-lab4@wp.pl";
    private final String senderPassword = "Jankowal123!@#";

    public void sendAsHtml(String to, String title, String html) throws MessagingException, UnsupportedEncodingException {
        Session session = createSession();

        MimeMessage message = new MimeMessage(session);
        prepareEmailMessage(message, to, title, html);

        Transport.send(message);
    }

    private void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException, UnsupportedEncodingException {
        message.setContent(html, "text/html");
        message.setFrom(new InternetAddress(senderEmail, "LAB5 - Nowe haslo"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.wp.pl");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        return session;
    }
}