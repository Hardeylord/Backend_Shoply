package com.shoply.Products.services;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.shoply.Products.Model.CartItems;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Value("${sendgrid.apiKey}")
    private String sendgrid_apiKey;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String token){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setFrom("adedipupoadeagbo@gmail.com");
            mailMessage.setSubject("Click on the link to upgrade your account");

            String messageLink= """
                   
                        Click on the link to upgrade your account
                  
                        http://localhost:8080/validateToken?token=%s
                    """.formatted(token);

            mailMessage.setText(messageLink);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void orderConfirmation2(String email, double subTotal, List<CartItems> items, String id) throws IOException {
//        System.out.println("sent to: "+email);
        StringBuilder itemHtml = new StringBuilder();
        for (CartItems pro: items) {
            itemHtml.append("""
                <tr class="product-item">
                  <td>
                    %s <br />
                  </td>
                  <td class="price">$%.2f</td>
                </tr>
                """.formatted(
                    pro.getProductName(),
                    pro.getPrice()
            ));
        }
        String orderSummary= """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8" />
                        <style>
                          body {
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            margin: 0;
                            padding: 0;
                            background-color: #333;
                          }
                          .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #ffffff;
                            padding: 20px;
                            border-radius: 8px;
                            border: 1px solid #eeeeee;
                          }
                          .header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #f4f4f4;
                          }
                          .shop-name {
                            color: #333;
                            font-size: 24px;
                            font-weight: bold;
                            text-decoration: none;
                          }
                          .section-title {
                            font-size: 14px;
                            color: #888;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-top: 20px;
                          }
                          .amount {
                            font-size: 32px;
                            color: #2ecc71;
                            font-weight: bold;
                            margin: 10px 0;
                          }
                          .product-list {
                            width: 100%%;
                            border-collapse: collapse;
                            margin-top: 15px;
                          }
                          .product-item {
                            border-bottom: 1px solid #f4f4f4;
                          }
                          .product-item td {
                            padding: 12px 0;
                            color: #555;
                          }
                          .price {
                            text-align: right;
                            font-weight: bold;
                            color: #333;
                          }
                          .footer {
                            text-align: center;
                            font-size: 12px;
                            color: #aaa;
                            margin-top: 30px;
                          }
                        </style>
                      </head>
                      <body>
                        <div class="container">
                          <div class="header">
                            <div class="shop-name">Shoply</div>
                            <p style="color: #666">Order Confirmed: #%s</p>
                          </div>
                    
                          <div class="section-title">$ Total Cost</div>
                          <div class="amount">$ %s</div>
                          <div class="section-title">🛒 Your Items</div>
                          <table class="product-list">
                            %s
                          </table>
                    
                          <div class="footer">
                            Thank you for shopping with Shoply!<br />
                            Do not reply to this email.
                          </div>
                        </div>
                      </body>
                    </html>
                    
                    """.formatted(id, subTotal, itemHtml.toString());
        Email from = new  Email("adedipupoadeagbo@gmail.com");
        Email to = new Email(email);
        Content content = new Content("text/html", orderSummary);
        Mail mail = new Mail(from, String.format("Shoply %s Order Confirmed", id), to, content);
        SendGrid sendGrid = new SendGrid(sendgrid_apiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
        Response response = sendGrid.api(request);
    }

    public void resendMailer(String email, double subTotal, List<CartItems> items, String id){
        StringBuilder itemHtml = new StringBuilder();
        for (CartItems pro: items) {
            itemHtml.append("""
                <tr class="product-item">
                  <td>
                    %s <br />
                  </td>
                  <td class="price">$%.2f</td>
                </tr>
                """.formatted(
                    pro.getProductName(),
                    pro.getPrice()
            ));
        }
        String orderSummary= """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8" />
                        <style>
                          body {
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            margin: 0;
                            padding: 0;
                            background-color: #333;
                          }
                          .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #ffffff;
                            padding: 20px;
                            border-radius: 8px;
                            border: 1px solid #eeeeee;
                          }
                          .header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #f4f4f4;
                          }
                          .shop-name {
                            color: #333;
                            font-size: 24px;
                            font-weight: bold;
                            text-decoration: none;
                          }
                          .section-title {
                            font-size: 14px;
                            color: #888;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-top: 20px;
                          }
                          .amount {
                            font-size: 32px;
                            color: #2ecc71;
                            font-weight: bold;
                            margin: 10px 0;
                          }
                          .product-list {
                            width: 100%%;
                            border-collapse: collapse;
                            margin-top: 15px;
                          }
                          .product-item {
                            border-bottom: 1px solid #f4f4f4;
                          }
                          .product-item td {
                            padding: 12px 0;
                            color: #555;
                          }
                          .price {
                            text-align: right;
                            font-weight: bold;
                            color: #333;
                          }
                          .footer {
                            text-align: center;
                            font-size: 12px;
                            color: #aaa;
                            margin-top: 30px;
                          }
                        </style>
                      </head>
                      <body>
                        <div class="container">
                          <div class="header">
                            <div class="shop-name">Shoply</div>
                            <p style="color: #666">Order Confirmed: #%s</p>
                          </div>
                    
                          <div class="section-title">$ Total Cost</div>
                          <div class="amount">$ %s</div>
                          <div class="section-title">🛒 Your Items</div>
                          <table class="product-list">
                            %s
                          </table>
                    
                          <div class="footer">
                            Thank you for shopping with Shoply!<br />
                            Do not reply to this email.
                          </div>
                        </div>
                      </body>
                    </html>
                    
                    """.formatted(id, subTotal, itemHtml.toString());
        Resend resend = new Resend("re_gN9Sdd4E_AEbjcL8qx2MLH27V6TT9UxnS");
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(email)
                .subject(String.format("Shoply %s Order Confirmed", id))
                .html(orderSummary)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    public void orderConfirmation(String email, double subTotal, List<CartItems> items, String id){
        StringBuilder itemHtml = new StringBuilder();
        for (CartItems pro: items) {
            itemHtml.append("""
                <tr class="product-item">
                  <td>
                    %s <br />
                  </td>
                  <td class="price">$%.2f</td>
                </tr>
                """.formatted(
                                pro.getProductName(),
                                pro.getPrice()
                        ));
            }
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper orderMail = new MimeMessageHelper(message, true);

            orderMail.setTo(email);
            orderMail.setFrom("adedipupoadeagbo@gmail.com");
            orderMail.setSubject(String.format("Shoply %s Order Confirmed", id));
            String orderSummary= """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8" />
                        <style>
                          body {
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            margin: 0;
                            padding: 0;
                            background-color: #333;
                          }
                          .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #ffffff;
                            padding: 20px;
                            border-radius: 8px;
                            border: 1px solid #eeeeee;
                          }
                          .header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #f4f4f4;
                          }
                          .shop-name {
                            color: #333;
                            font-size: 24px;
                            font-weight: bold;
                            text-decoration: none;
                          }
                          .section-title {
                            font-size: 14px;
                            color: #888;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-top: 20px;
                          }
                          .amount {
                            font-size: 32px;
                            color: #2ecc71;
                            font-weight: bold;
                            margin: 10px 0;
                          }
                          .product-list {
                            width: 100%%;
                            border-collapse: collapse;
                            margin-top: 15px;
                          }
                          .product-item {
                            border-bottom: 1px solid #f4f4f4;
                          }
                          .product-item td {
                            padding: 12px 0;
                            color: #555;
                          }
                          .price {
                            text-align: right;
                            font-weight: bold;
                            color: #333;
                          }
                          .footer {
                            text-align: center;
                            font-size: 12px;
                            color: #aaa;
                            margin-top: 30px;
                          }
                        </style>
                      </head>
                      <body>
                        <div class="container">
                          <div class="header">
                            <div class="shop-name">Shoply</div>
                            <p style="color: #666">Order Confirmed: #%s</p>
                          </div>
                    
                          <div class="section-title">$ Total Cost</div>
                          <div class="amount">$ %s</div>
                          <div class="section-title">🛒 Your Items</div>
                          <table class="product-list">
                            %s
                          </table>
                    
                          <div class="footer">
                            Thank you for shopping with Shoply!<br />
                            Do not reply to this email.
                          </div>
                        </div>
                      </body>
                    </html>
                    
                    """.formatted(id, subTotal, itemHtml.toString());
            orderMail.setText(orderSummary, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void orderConfirmation3(){

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper orderMail = new MimeMessageHelper(message, true);

            orderMail.setTo("olanrewajuadeagbo3@gmail.com");
            orderMail.setFrom("adedipupoadeagbo@gmail.com");
            orderMail.setSubject("Shoply %s Order Confirmed");
            orderMail.setText("orderSummary testing");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
