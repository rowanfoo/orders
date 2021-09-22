package com.dharma.orders

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.mail.Session
import javax.mail.Store


@Configuration
class Config {
    @Value("\${myemail}")
    lateinit var myemail: String

    @Value("\${mypassword}")
    lateinit var mypassword: String

    @Value("\${mymail}")
    lateinit var mymail: String

    @Bean
    fun emailStore(): Store {
        var properties = Properties()
        properties.put("mail.imap.host", "mail.noip.com");
        properties.put("mail.imap.port", "143");
        properties.put("mail.store.protocol", "imaps");
        var emailSession = Session.getDefaultInstance(properties)
        var store = emailSession.getStore("imaps")
        store.connect(mymail, myemail, mypassword)
        return store
    }


}


//@Configuration
//class Config {}
