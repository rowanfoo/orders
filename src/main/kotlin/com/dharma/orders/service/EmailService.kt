package com.dharma.orders.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import javax.mail.Folder
import javax.mail.Store
import javax.mail.search.SearchTerm

@Component
class EmailService {
//    @Autowired
//    lateinit var folder: Folder

    @Autowired
    lateinit var store: Store


    fun folder(): Folder {
        var sentFolder = store.getFolder("Inbox")
        // var sentFolder = store.getFolder("INBOX")
        println("------FOLDERS---1-")
        // var sentFolder = store.defaultFolder
        println("------FOLDERS---2-")
        sentFolder.open(Folder.READ_WRITE)
        println("------FOLDERS----name--${sentFolder.name}")
        println("------FOLDERS-----isopen-${sentFolder.isOpen}")
        println("------FOLDERS-----subscribe-${sentFolder.isSubscribed}")
        return sentFolder
    }


    fun messages(folder: Folder, andTerm: SearchTerm) = folder.search(andTerm)


//    fun folder(folder: String): Folder {
//        var sentFolder = emailStore.getFolder("Inbox")
//        // var sentFolder = store.getFolder("INBOX")
//        println("------FOLDERS---1-")
//        // var sentFolder = store.defaultFolder
//        println("------FOLDERS---2-")
//        sentFolder.open(Folder.READ_WRITE)
//        println("------FOLDERS----name--${sentFolder.name}")
//        println("------FOLDERS-----isopen-${sentFolder.isOpen}")
//        println("------FOLDERS-----subscribe-${sentFolder.isSubscribed}")
//        return sentFolder
//    }
//
//    fun read(folder: String ,  andTerm: SearchTerm): Folder {
//        var sentFolder = emailStore.getFolder("Inbox")
//        // var sentFolder = store.getFolder("INBOX")
//        println("------FOLDERS---1-")
//        // var sentFolder = store.defaultFolder
//        println("------FOLDERS---2-")
//        sentFolder.open(Folder.READ_WRITE)
//        println("------FOLDERS----name--${sentFolder.name}")
//        println("------FOLDERS-----isopen-${sentFolder.isOpen}")
//        println("------FOLDERS-----subscribe-${sentFolder.isSubscribed}")
//        return sentFolder
//    }


    fun close() {

    }

    @Autowired
    lateinit var emailSender: JavaMailSender

    fun sendSimpleMessage(subject: String, text: String) {
        var message = SimpleMailMessage()
        message.setTo("rowanfoo@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        println("-------send message--")
        emailSender.send(message);
        println("-------send message----done-")

    }


}

//@Component
//class EmailService {}


