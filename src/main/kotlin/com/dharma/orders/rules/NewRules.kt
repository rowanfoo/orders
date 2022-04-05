package com.dharma.orders.rules

import com.dhamma.pesistence.entity.data.Portfolio
import com.dharma.orders.rules.service.NewService
import com.dharma.orders.service.PortfolioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NewRules {

    @Autowired
    lateinit var newService: NewService

    @Autowired
    lateinit var portfolioService: PortfolioService


    fun newsrules(list: List<Portfolio>): List<RulesDTO> {
        var z = newService.news(list)

        var list = mutableListOf<RulesDTO>()

        z.keys.forEach {
            var code = it
            var message = ""
            z[it]!!.forEach {
                message += "${it.title} \n"
            }
            list.add(
                RulesDTO(
                    "News - ${code} ",
                    "CODE: , ${code}  , \n ${message}"
                )
            )

        }
        println("---------NewsRules----------$list")
        return list
    }

}

