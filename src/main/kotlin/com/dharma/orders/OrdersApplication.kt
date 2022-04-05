package com.dharma.orders

import com.dharma.orders.manager.EmailManager
import com.dharma.orders.rules.RulesManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@SpringBootApplication
@ComponentScan(basePackages = ["com.dharma.orders", "com.dhamma.pesistence"])
@EnableScheduling

class OrdersApplication : CommandLineRunner {

    @Autowired
    lateinit var emailManager: EmailManager


    @Autowired
    lateinit var rulesManager: RulesManager


    override fun run(vararg args: String?) {
        println("---------RUN------NOW -----")
        //  emailManager.run()
//        rulesManager.run()
    }

    @Scheduled(cron = "0 15 15 ? * MON-FRI")
    fun reportCurrentTime() {
        println("---------RUN------${LocalDateTime.now()}-----")
        emailManager.run()
    }

    @Scheduled(cron = "0 45 15 ? * MON-FRI")
    fun runporttest() {
        println("---------RUN------${LocalDateTime.now()}-----")
        rulesManager.run()
    }

}

fun main(args: Array<String>) {
    runApplication<OrdersApplication>(*args)
}
