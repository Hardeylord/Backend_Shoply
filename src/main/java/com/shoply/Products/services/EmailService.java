package com.shoply.Products.services;

import com.shoply.Products.Model.CartItems;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

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

}
