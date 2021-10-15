package com.dharma.orders

import com.dhamma.pesistence.entity.data.Orders
import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.data.type.Source
import com.dhamma.pesistence.entity.repo.OrdersRepo
import com.dhamma.pesistence.entity.repo.PortfolioRepo
import java.time.LocalDate


fun toOrder(data: List<String>, source: Source) =
    Orders.builder().code(data[3]).date(LocalDate.now()).orderid(data[1]).quantity(data[2].toInt())
        .price(data[4].replace("$", "").toDouble()).source(source).status(data[0]).orderstatus(data[5]).build()


//fun processSellOrder(portfolioService: PortfolioService, sellService: SellService, ord: Orders) {
//    var avgprice = portfolioService.getAverageBuyPrice(ord.code, ord.source)
//    println("SAVE MARGIN")
//    sellService.save(ord, avgprice)
//    //println(ord)
//    println("SAVE PORT")
//}

fun processOrder(ordersRepo: OrdersRepo, ord: Orders) {
    println("-------SAVE   ORDER--------${ord}")
    ordersRepo.save(ord)
}

fun saveportfolio(portfolioRepo: PortfolioRepo, data: Orders) {
    portfolioRepo.save(Portfolio.toOrders(data))

}


fun getAverageBuyPricePortFolio(z: List<Portfolio>): Double {
    var sum = z.sumByDouble { it.price * it.quantity }
    var total = z.sumBy { it.quantity }
    return Math.round((sum / total) * 1000.0) / 1000.0
}


fun getAverageBuyPriceOrders(z: List<Orders>): Double {
    var sum = z.sumByDouble { it.price * it.quantity }
    var total = z.sumBy { it.quantity }
    return Math.round((sum / total) * 1000.0) / 1000.0
}
