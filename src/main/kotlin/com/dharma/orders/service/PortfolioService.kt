package com.dharma.orders.service

import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.data.QPortfolio
import com.dhamma.pesistence.entity.data.type.Source
import com.dhamma.pesistence.entity.repo.PortfolioArchiveRepo
import com.dhamma.pesistence.entity.repo.PortfolioRepo
import com.dharma.orders.getAverageBuyPricePortFolio
import com.dharma.orders.rules.service.PriceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping


@Component
class PortfolioService {
    @Autowired
    lateinit var portfolioRepo: PortfolioRepo

    @Autowired
    lateinit var portfolioArchiveRepo: PortfolioArchiveRepo

    @Autowired
    lateinit var priceService: PriceService

    fun getAverageBuyPrice(code: String, source: Source): Double {
        var z = portfolioRepo.findAll(
            QPortfolio.portfolio.code.eq(code).and(QPortfolio.portfolio.source.eq(source))
                .and(QPortfolio.portfolio.status.eq("buy"))
        ).toList()
        return getAverageBuyPricePortFolio(z)


    }


    fun getAverageBuyPriceAll(code: String): Double {
        var z = portfolioRepo.findAll(
            QPortfolio.portfolio.code.eq(code)
                .and(QPortfolio.portfolio.status.eq("buy"))
        ).toList()
        return getAverageBuyPricePortFolio(z)
    }




    fun getAllAverage(): List<Portfolio> {
        var s = portfolioRepo.findAll().toList()
        var t = s.groupBy { it.code }
        var d = mutableListOf<Portfolio>()

        t.keys.forEach {

            var quantity = t[it]!!.sumBy { it.quantity }
            var price = getAverageBuyPricePortFolio(t[it]!!)
            var port = Portfolio.builder()
                .code(it)
                .price(price)
                .quantity(quantity)
                .build()
            d.add(port)
        }
        println(d)
        return d
    }

//    fun cleanup() {
//        cleanupall(Source.NAB)
//        cleanupall(Source.ANZ)
//    }


//    fun getYesterdayBuysPortfolio(code: String): Portfolio {
//        var z = portfolioRepo.findAll(
//            QPortfolio.portfolio.code.eq(code).and(QPortfolio.portfolio.status.eq("buy"))
//        ).toList()
//        var first = z.sortedByDescending { it.date }.toList()[0]
//        return first
//    }
//
//
//    fun getLastetBuy(): Map<String, Double> {
//        var z = portfolioRepo.findAll(
//            QPortfolio.portfolio.date.eq(LocalDate.now()).and(QPortfolio.portfolio.status.eq("buy"))
//        ).groupBy { it.code }
//
//        var map = mutableMapOf<String, Double>()
//        z.keys.forEach {
//            var ls = z[it]!!
//            var sum = ls.sumByDouble { it.price * it.quantity }
//            var total = ls.sumBy { it.quantity }
//            map[it] = Math.round((sum / total) * 1000.0) / 1000.0
//        }
//        return map
//    }


    fun getAllBuy(): List<Portfolio> =
        portfolioRepo.findAll(
            ((QPortfolio.portfolio.status.eq("buy")))
        ).toList()


    fun yesterdayBuysPortfolio(): List<Portfolio> {
        var date = priceService.today()
        return portfolioRepo.findAll(
            QPortfolio.portfolio.date.lt(date).and(QPortfolio.portfolio.status.eq("buy"))
        ).toList()
    }


    fun todayBuyPortofolio(): List<Portfolio> {
        var date = priceService.today()
        return portfolioRepo.findAll(
            QPortfolio.portfolio.date.eq(date).and(QPortfolio.portfolio.status.eq("buy"))
        ).toList()
    }


//    private fun cleanupall(source: Source) {
//        var buy = portfolioRepo.findAll(
//            (QPortfolio.portfolio.source.eq(source).and(QPortfolio.portfolio.status.eq("buy")))
//        ).toList().groupBy { it.code }
//
//        var sell = portfolioRepo.findAll(
//            (QPortfolio.portfolio.source.eq(source).and(QPortfolio.portfolio.status.eq("sell")))
//        ).toList().groupBy { it.code }
//
//        buy.keys.forEach {
//
//            if (sell[it] != null) {
//                var bsum = buy[it]!!.sumBy { it.quantity }
//                var ssum = sell[it]!!.sumBy { it.quantity }
//
//                if ((bsum - ssum) == 0) {
//                    var t = buy[it]!!.plus(sell[it]) as List<Portfolio>
//
//                    println("------COPY --- PORT")
//                    var zz = combine(buy[it]!!, sell[it]!!)
//                    archive(zz)
//
//                    println("------DELETE PORT")
//                    portfolioRepo.deleteAll(zz)
//                    println("------DONE")
//                }
//            }
//        }
//    }

//    fun combine(buy: List<Portfolio>, sell: List<Portfolio>): List<Portfolio> {
//        val result: MutableList<Portfolio> = ArrayList()
//        result.addAll(buy as Collection<Portfolio>)
//        result.addAll(sell as Collection<Portfolio>)
//        return result.toList()
//    }
//
//    fun archive(archive: List<Portfolio>) {
//        var ls = mutableListOf<PortfolioArchive>()
//
//        archive.forEach {
//            println("------SAVE ${it.toPortfolioArchive()}")
//            var tt = it.toPortfolioArchive()
////            ls.add(it.toPortfolioArchive())
//            ls.add(tt)
//        }
//        println("------SAVE 1--- PORT")
//        portfolioArchiveRepo.saveAll(ls)
//        println("------SAVe2 --- PORT")
//    }


//    fun todaybuy(): List<Portfolio> {
//        return portfolioRepo.findAll(
//            QPortfolio.portfolio.date.eq(LocalDate.now())
//        ).toList()
//
//
//    }

//    fun getAllPortolio(): Map<String, List<Portfolio>> {
//        var buy = portfolioRepo.findAll(
//            ((QPortfolio.portfolio.status.eq("buy")))
//        ).toList().groupBy { it.code }
//
//        var sell = portfolioRepo.findAll(
//            ((QPortfolio.portfolio.status.eq("sell")))
//        ).toList().groupBy { it.code }
//
//        var map = mutableMapOf<String, List<Portfolio>>()
//
//        buy.keys.forEach {
//            if (sell[it] == null) {
//                println("1")
//                map.put(it, buy[it]!!)
//            } else {
//                println("2")
//                println("---$it-------------")
//                map.put(it, make(buy[it]!!, sell[it]!!))
//            }
//        }
//        return map
//    }
//
//
//    private fun make(buy: List<Portfolio>, sell: List<Portfolio>): List<Portfolio> {
//        var count = 0
//        println("--buy----$buy-----")
//
//        sell.forEach {
//            var quantity = it.quantity
//
//            if (buy[count].quantity - it.quantity < 0) {
//                //     println("--sell----${buy[count].quantity}-----------${it.quantity}")
//                makezero(buy, it.quantity)
//                println("--buy----$buy-----")
//            } else if (buy[count].quantity - it.quantity > 0) {
//                buy[count].quantity = buy[count].quantity - it.quantity
//            } else if (buy[count].quantity - it.quantity == 0) {
//                buy[count].quantity = 0
//                count++
//            }
//        }
//        println("--make----$buy-----")
//        return buy
//    }
//
//
//    private fun makezero(buy: List<Portfolio>, qty: Int) {
//        var count = 0
//        var totaltqy = qty
//        println("--buy--${buy}--")
//        while (true) {
//            if (totaltqy == 0) return
//
//            var quantity = buy[count].quantity - totaltqy
//
//            if (quantity < 0) {
////                var d = abs(buy[count].quantity - totaltqy)
//
//                println("===totaltqy1==$totaltqy   =======${buy[count].quantity}")
//                totaltqy = totaltqy - buy[count].quantity
//                println("===totaltqy2==$totaltqy")
//                buy[count].quantity = 0
//                count++
//            } else {
//                buy[count].quantity = quantity
//                totaltqy = 0
//            }
//        }
//    }


}
