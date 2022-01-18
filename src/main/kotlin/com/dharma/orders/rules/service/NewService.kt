package com.dharma.orders.rules.service

import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.News
import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.data.QNews
import com.dhamma.pesistence.entity.repo.DataRepo
import com.dhamma.pesistence.entity.repo.NewsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NewService {
    @Autowired
    lateinit var dataRepo: DataRepo

    @Autowired
    lateinit var newsRepo: NewsRepo

    fun news(port: List<Portfolio>): Map<String, List<News>> {
        var map = mutableMapOf<String, CoreData>()


        var stocklist = port
            .map { it.code }
            .toList()

        var tables = newsRepo.findAll(
            QNews.news.code.`in`(stocklist)
                .and(QNews.news.date.eq(today()))
        )
            //.toList().toM
            .groupBy { it.code }
        //  println(tables)
        return tables
    }

    fun today() = dataRepo.findbyCodelatest("BHP.AX").date
}
