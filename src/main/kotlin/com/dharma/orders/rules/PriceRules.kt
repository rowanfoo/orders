package com.dharma.orders.rules

import com.dhamma.pesistence.entity.data.Portfolio
import com.dharma.orders.rules.service.PriceService
import com.dharma.orders.service.PortfolioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PriceRules {

    @Autowired
    lateinit var priceService: PriceService

    @Autowired
    lateinit var portfolioService: PortfolioService


    fun pricefall(list: List<Portfolio>): List<RulesDTO> {
        var z = priceService.price(list)
        var list = mutableListOf<RulesDTO>()
        z.keys.forEach {
            if (z[it]!!.changepercent != 0.0 && z[it]!!.changepercent < -5) {

                list.add(
                    RulesDTO(
                        "Warn -PRICE- Price fall",
                        "Price fall ,for $it  , ${z[it]?.changepercent}% , ${z[it]?.close}"
                    )
                )
            }
        }
        return list
    }

    fun pricestoploss(list: List<Portfolio>): List<RulesDTO> {

        var data = mutableListOf<RulesDTO>()
        var todaypricemap = priceService.price(list)

        list.distinctBy { it.code }.forEach {
            var portprice = portfolioService.getAverageBuyPriceAll(it.code)
            //      println("--------AVG PRICE -------${it.code}--------${portprice}-----")
            var todayprice = todaypricemap[it.code]!!.close
            var stoploss = (todayprice - portprice) / portprice
            //    println("--------today price PRICE -------${it.code}--------${todayprice}-------stolosss --->$stoploss ")
            if (stoploss < -0.08) {
                data.add(
                    RulesDTO(
                        "Warn -PRICE- Stop loss",
                        "Stop loss  > 8% ,for ${it.code}  drop, ${Math.round(stoploss * 1000.0) / 1000.0}% ,--- buy price ${portprice}  vs  today price : $todayprice}"
                    )
                )
            }
        }
        return data
    }
}
/*
price fall today price fall > 7%
stop loss > 8%


 */

