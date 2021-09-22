package com.dharma.orders.manager

import com.dhamma.pesistence.entity.data.Orders
import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.data.QPortfolio
import com.dhamma.pesistence.entity.repo.PortfolioArchiveRepo
import com.dhamma.pesistence.entity.repo.PortfolioRepo
import com.dhamma.pesistence.entity.repo.SellRepo
import com.dharma.orders.service.SellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SellManager {
    @Autowired
    lateinit var portfolioRepo: PortfolioRepo

    @Autowired
    lateinit var sellService: SellService


    @Autowired
    lateinit var portfolioArchiveRepo: PortfolioArchiveRepo

    @Autowired
    lateinit var sellRepo: SellRepo


    fun processSell(order: Orders) {
        var buy = portfolioRepo.findAll(
            ((QPortfolio.portfolio.status.eq("buy")
                .and(QPortfolio.portfolio.source.eq(order.source))
                .and(QPortfolio.portfolio.code.eq(order.code))))
        ).toList().sortedBy { it.date }

        var sellprice = order.price
        var sellqty = order.quantity
        var tempqty = order.quantity


        for (buyprice in buy) {
            if (buyprice.quantity - sellqty < 0) {
                sellqty = sellqty - buyprice.quantity
                var temp = buyprice.quantity
                buyprice.quantity = 0
                savesell(buyprice, sellprice, temp)
            } else if (buyprice.quantity - sellqty > 0) {
                buyprice.quantity = buyprice.quantity - sellqty
                savesell(buyprice, sellprice, sellqty)
                return
            } else if (buyprice.quantity - sellqty == 0) {
                buyprice.quantity = 0
                savesell(buyprice, sellprice, tempqty)
                return
            }
        }
    }

    fun savesell(port: Portfolio, sellprice: Double, qty: Int) {
        //port qty =0
        //save to margin
        println("------$port-------$qty---------")

        //update portfolio table
        //archive portolio table
        //SELL table
        if (port.quantity == 0) {
            var portfolioArchive = portfolioRepo.findById(port.id).get().toPortfolioArchive()

            portfolioArchiveRepo.save(portfolioArchive)
            portfolioRepo.delete(port)
        } else {
            var b = port.toPortfolioArchive()
            b.price = sellprice
            b.quantity = qty
            portfolioArchiveRepo.save(b)
            portfolioRepo.save(port)
//            println("=======UPDATE  ARCHIVE PORT====$b")
//            println("=======UPDATE  PORT====$port")
        }
        var sell = sellService.toSell(port)
        sell.price = sellprice
        sell.quantity = qty
        sellRepo.save(sell)
        //        println("======= SELL ====$b")


    }
}
