package com.dharma.orders

import org.apache.commons.mail.Email
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender


@SpringBootTest
class TestEmail {


//    @Autowired
//    lateinit var email: Email


    @Test
    fun mytest() {
        sendSimpleMessage("hello world")
    }

    @Autowired
    lateinit var emailSender: JavaMailSender

    fun sendSimpleMessage(text: String) {
        var message = SimpleMailMessage()
//        message.setTo("rowanfoo@gmail.com");
        message.setTo("rowanfoo@commoditystartegyfund.com");
        message.setSubject("!!!Error from APP !!!");
        message.setText(text);
        println("-------send message--")
        emailSender.send(message);
        println("-------send message----done-")

    }

}
