package com.app.Ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Service
public class WhatsAppService {

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    public void sendWhatsapp(String toNumber, String messageText) {
        Message.creator(
            new PhoneNumber("whatsapp:" + toNumber),
            new PhoneNumber(fromNumber),
            messageText
        ).create();
    }
}
