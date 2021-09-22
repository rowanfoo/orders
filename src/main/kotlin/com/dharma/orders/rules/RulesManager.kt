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



    fun run() {

        var z = portfolioService.getAllBuy()

        var t = priceRules.pricefall(z)
        println(t)
        var s = priceRules.pricestoploss(z)


       var u = portfolioRules.lastbuy()
        var v = portfolioRules.total(z)

        println(s)
        var a = arrayListOf<RulesDTO>()
        a.addAll(t)
        a.addAll(s)
        a.addAll(t)
        a.addAll(u)
        a.addAll(v)


        for (rulesDTO in a) {
            emailService.sendSimpleMessage(rulesDTO.warn , rulesDTO.msg)
        }

    }


}
