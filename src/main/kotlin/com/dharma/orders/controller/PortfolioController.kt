package com.dharma.orders.controller

import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.repo.PortfolioRepo
import com.dharma.orders.rules.RulesManager
import com.dharma.orders.service.PortfolioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.streams.toList

@CrossOrigin
@RestController
class PortfolioController {
    @Autowired
    lateinit var portfolioRepo: PortfolioRepo
    @Autowired
    lateinit var portfolioService: PortfolioService
    @Autowired
    lateinit var rulesManager: RulesManager


    @GetMapping("/portfolio/items")
    fun getStocks(): List<Portfolio> {
        var s = portfolioRepo.findAll()
        println("-----cat-----$s--")
        return s
    }


    @GetMapping("/portfolio/average")
    fun testsorta(): List<Portfolio> {
        return portfolioService.getAllAverage()
    }

    @GetMapping("/portfolio/codes")
    fun getCodes(): List<String> {
        return portfolioRepo.findAll().stream().map {
            it.code
        }.distinct().toList()

//        var s = portfolioRepo.findAll()
//        println("-----cat-----$s--")
//        return s
    }


    @GetMapping("/portfolio/rulesmanager")
    fun rulesmanager(){
        rulesManager.run()
    }


}
