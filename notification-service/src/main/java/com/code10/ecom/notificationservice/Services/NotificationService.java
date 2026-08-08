package com.code10.ecom.notificationservice.Services;

import com.code10.ecom.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @Value("${app.notification.allowed-recipient:}")
    private String allowedRecipient;

    @KafkaListener(topics = "order-placed-topic" )
    public void listen(OrderPlacedEvent orderPlacedEvent){
        log.info("Got message from order-placed topic: {}", orderPlacedEvent);

        String recipientEmail = orderPlacedEvent.getEmail();
        if (allowedRecipient != null && !allowedRecipient.isBlank()) {
            log.info("Restricting recipient. Redirecting email from {} to allowed recipient: {}", recipientEmail, allowedRecipient);
            recipientEmail = allowedRecipient;
        }

        final String finalRecipient = recipientEmail;

        try {
            javaMailSender.send(mimeMessage -> {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                mimeMessageHelper.setFrom("EComMicroServices@email.com");
                mimeMessageHelper.setTo(finalRecipient);
                mimeMessageHelper.setSubject(String.format("Your order with order number %s is placed Successfully", String.valueOf(orderPlacedEvent.getOrderID())));
                mimeMessageHelper.setText("Thank you for shopping with us. We are processing your order and will update you once it's shipped.");
            });
        } catch (Exception e){
            log.error("Failed to send email notification for order ID: {}", orderPlacedEvent.getOrderID(), e);
        }

    }
}
