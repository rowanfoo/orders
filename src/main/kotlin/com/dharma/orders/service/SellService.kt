package com.dharma.orders.service

import com.dhamma.pesistence.entity.data.Orders
import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.data.Sell
import com.dhamma.pesistence.entity.repo.SellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SellService {


    @Autowired
    lateinit var sellRepo: SellRepo


    fun save(order: Orders, avgPrice: Double) {
        var m = Sell.builder().code(order.code)
            .date(order.date)
            .orderid(order.orderid)
            .quantity(order.quantity)
            .price(order.price)
            .source(order.getSource())
            .avgprice(avgPrice)
            .build()

        sellRepo.save(m)
    }


    fun toSell(order: Portfolio) =
        Sell.builder().code(order.code)
            .date(order.date)
            .orderid(order.orderid)
            .quantity(order.quantity)

            .source(order.getSource())
            .avgprice(order.price)
            .build()

}
