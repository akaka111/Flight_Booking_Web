/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import VNPay.ConfigLoader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class MailService {

    public static void sendEmail(String to, String subject, String content) {
        final String from = ConfigLoader.get("smtp.username");
        final String password = ConfigLoader.get("smtp.password");
        final String host = ConfigLoader.get("smtp.host");
        final String port = ConfigLoader.get("smtp.port");

        // Kiểm tra null để tránh lỗi khó hiểu
        if (from == null || password == null || host == null || port == null) {
            throw new RuntimeException("❌ Thiếu thông tin SMTP trong config.properties (from, password, host hoặc port null)");
        }

        // In cấu hình để debug
        System.out.println(">>> SMTP FROM     : " + from);
        System.out.println(">>> SMTP PASSWORD : " + (password != null ? "****" : "null"));  // Ẩn mật khẩu thực tế
        System.out.println(">>> SMTP HOST     : " + host);
        System.out.println(">>> SMTP PORT     : " + port);
        System.out.println(">>> TO            : " + to);
        System.out.println(">>> SUBJECT       : " + subject);
        System.out.println(">>> CONTENT       :\n" + content);

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            session.setDebug(true); // In SMTP log

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "Airline Booking"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");

            Transport.send(message);
            System.out.println("✅ GỬI EMAIL THÀNH CÔNG đến: " + to);

        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println("❌ LỖI GỬI EMAIL:");
            e.printStackTrace();  // Hiện stack trace đầy đủ
        } catch (Exception e) {
            System.out.println("❌ LỖI KHÁC:");
            e.printStackTrace();
        }
    }
}
