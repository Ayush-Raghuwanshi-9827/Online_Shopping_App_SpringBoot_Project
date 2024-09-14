package com.ecomerse.Online_Shopping_App.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class CommonUtil {

    @Autowired
    private  JavaMailSender mailSender;

    public Boolean sendEmail(String url, String reciepentEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ayushraghuwanshi283@gmail.com", "Shooping Cart");
        helper.setTo(reciepentEmail);


        String content = "<p>Hello,</p>"
                + "<p>You have a requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + url + "\">Change my password</a></p>";

        helper.setSubject("Password Reset");
        helper.setText(content, true);
        System.out.println();
        mailSender.send(message);

        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteurl=  request.getRequestURL().toString();
         return siteurl.replace(request.getServletPath(),"");
    }
}
