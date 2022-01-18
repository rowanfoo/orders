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
            z[it]!!.forEach {
                list.add(
                    RulesDTO(
                        "News ",
                        "CODE: , ${it.code}  , ${it.title}"
                    )
                )
            }


        }
        println("---------NewsRules----------$list")
        return list
    }

}

