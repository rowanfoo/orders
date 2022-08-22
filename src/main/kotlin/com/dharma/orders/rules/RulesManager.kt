package com.dharma.orders.rules

import com.dharma.orders.service.EmailService
import com.dharma.orders.service.PortfolioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RulesManager {
    @Autowired
    lateinit var priceRules: PriceRules

    @Autowired
    lateinit var portfolioRules: PortfolioRules


    @Autowired
    lateinit var portfolioService: PortfolioService

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var newRules: NewRules


    fun run() {

        var z = portfolioService.getAllBuy()

        var t = priceRules.pricefall(z)

        var s = priceRules.pricestoploss(z)


        var u = portfolioRules.lastbuy()
        var v = portfolioRules.total(z)
        sendMail(v)
        println(s)


        var x6 = newRules.newsrules(z)
        sendMail(x6)

//        var a = arrayListOf<RulesDTO>()
//        a.addAll(t)
//        a.addAll(s)
//        a.addAll(t)
//        a.addAll(u)
//        a.addAll(v)
//        a.addAll(x6)
//
//        var msg = ""
//        for (rulesDTO in a) {
//            msg.plus("${rulesDTO.msg} . \n" )
//
//        }
//        emailService.sendSimpleMessage(rulesDTO.warn, rulesDTO.msg)

    }

    private fun sendMail(list:List<RulesDTO>){
        var msg = ""
        for (rulesDTO in list) {
            msg = msg.plus("${rulesDTO.msg} . \n" )

        }
        if(msg.isNotEmpty()){
            emailService.sendSimpleMessage(list[0].warn, msg)
        }
    }
}
