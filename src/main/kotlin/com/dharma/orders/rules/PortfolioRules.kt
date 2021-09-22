package com.dharma.orders.rules

import com.dhamma.pesistence.entity.data.Portfolio
import com.dharma.orders.getAverageBuyPricePortFolio
import com.dharma.orders.rules.service.PriceService
import com.dharma.orders.service.PortfolioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component

class PortfolioRules {

    @Autowired
    lateinit var portfolioService: PortfolioService

    @Autowired
    lateinit var priceService: PriceService


    fun total(list: List<Portfolio>): List<RulesDTO> {
        var sum = list.sumByDouble { it.price * it.quantity }
        var list = mutableListOf<RulesDTO>()

        if ((sum / 400000) > 0.73) {
            var z = Math.round((sum / 400000) * 1000) / 1000
            list.add(RulesDTO("Warn -Portfolio- Total Porfolio ", "Total Invested > 75% , now at $z for $sum"))
        }
        return list
    }


    fun lastbuy(): List<RulesDTO> {

        var today = portfolioService.todayBuyPortofolio().groupBy { it.code }
        var yesterday = portfolioService.yesterdayBuysPortfolio().groupBy { it.code }

        var list = mutableListOf<RulesDTO>()
        today.keys.forEach {
            if (yesterday[it] != null) {
                var todayprice = getAverageBuyPricePortFolio(today[it]!!)
                var yesterdayprice = getAverageBuyPricePortFolio(yesterday[it]!!)

                var percent = todayprice - yesterdayprice / yesterdayprice
                if (percent < 0.01) {
                    list.add(
                        RulesDTO(
                            "Warn -Portfolio- Buy Position",
                            "Buying position wrong less than 1% , ${percent} %  -- yesterday:$yesterdayprice  vs today buy:$todayprice"
                        )
                    )
                }
            }
        }
        return list
    }

}
