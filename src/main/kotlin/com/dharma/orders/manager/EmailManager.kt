package com.dharma.orders.manager

import com.dhamma.pesistence.entity.data.Orders
import com.dhamma.pesistence.entity.data.QOrders
import com.dhamma.pesistence.entity.data.type.Source
import com.dhamma.pesistence.entity.repo.OrdersRepo
import com.dhamma.pesistence.entity.repo.PortfolioRepo
import com.dharma.orders.*
import com.dharma.orders.service.EmailService
import com.dharma.orders.service.PortfolioService
import com.dharma.orders.service.SellService
import org.apache.commons.mail.util.MimeMessageParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import javax.mail.Message
import javax.mail.internet.MimeMessage
import javax.mail.search.AndTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.SubjectTerm

@Component
class EmailManager {
    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var portfolioRepo: PortfolioRepo

    @Autowired
    lateinit var ordersRepo: OrdersRepo

    @Autowired
    lateinit var portfolioService: PortfolioService


    @Autowired
    lateinit var sellService: SellService

    @Autowired
    lateinit var sellManager: SellManager

    /*
    This is to filter day trades.. shouldnt go into portfolio
     */
    private fun checkdaytrade(ord: List<Orders>): List<Orders> {
        var codes = ord.groupBy { it.code }
        var stk = arrayListOf<Orders>()
        codes.keys.forEach {
            var list = codes[it]
            if (list!!.groupBy { it.status }.containsKey("buy") && list.groupBy { it.status }.containsKey("sell")) {

//                var buy = list.groupBy { it.status }["buy"]!!.sumBy { it.quantity }
//                var sell = list.groupBy { it.status }["sell"]!!.sumBy { it.quantity }

                var buy = list.groupBy { it.status }["buy"]
                var sell = list.groupBy { it.status }["sell"]

                var buyq = buy!!.sumBy { it.quantity }
                var sellq = sell!!.sumBy { it.quantity }

                if ((buyq - sellq) == 0) {
                    //dont add into portfolio , just add to orders
                    buy.forEach {
                        processOrder(ordersRepo, it)
                    }
                    sell.forEach {
                        processOrder(ordersRepo, it)
                    }
                } else {
                    stk.addAll(list)
                }
            } else {
                stk.addAll(list)
            }
        }
        return stk
    }

    fun run() {
        println("-----------EMAIL--MANAGER--------RUN")
        var folder = emailService.folder()

// maybe this email storage time diff from current time
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = cal[Calendar.DAY_OF_MONTH] - 1
        cal[Calendar.HOUR_OF_DAY] = 6
        cal[Calendar.MINUTE] = 30
        val mydate = cal.time

        println("find email date  ----- $mydate  ")
        //Nab
        var msg = emailService.messages(
            folder,
            AndTerm(
                ReceivedDateTerm(ComparisonTerm.EQ, mydate),
                SubjectTerm("nabtrade confirmation")
            )
        )


        println("----MSGS---NAB-- SIZE-${msg.size}----")
        msg.forEach {
            println("${it.subject}  ----- ${it.receivedDate}  ")
        }
        var z = read(msg, Source.NAB)

        z = checkdaytrade(z)

        z.sortedBy { it.status }.forEach {
            saveOrders(it)
        }

        var t = AndTerm(ReceivedDateTerm(ComparisonTerm.EQ, Date()), SubjectTerm("3116872"))
        t = AndTerm(t, SubjectTerm("filled"))
        msg = emailService.messages(folder, t)

        println("----MSGS---ANZ-- SIZE-${msg.size}----")
        msg.forEach {

            println("----MSG---${it.subject}")
        }
        println("----MSG--FILTERED---${msg.filter { it.subject.indexOf("filled") > 0 }.toTypedArray().size}")


        var z1 = read(msg.filter { it.subject.indexOf("filled") > 0 }.toTypedArray(), Source.ANZ)
        println("----MSG---${z1.size}")
        var group = z1.groupBy { it.orderid }
        var anz = mutableListOf<Orders>()
        group.keys.forEach {
            var ord = isorderfill(group[it]!!)
//ANZ have . fill partial then have filled(full)
            if (ord != null) {
                anz.add(ord)
            } else {
                var qty = group[it]!!.sumBy { it.quantity }
                var obj = group[it]!![0]
                obj.quantity = qty
                println("------------------GRPOUP-------------$obj----------$qty")
                anz.add(obj)
            }


///////// this is done for , anz partially transacted order , want to group all transacted with same orderid
            //eg buy 5k ...ANZ === anz will buy 2,500 then another 2500 transactions.
        }
//        anz = checkdaytrade(anz)
        var anz1 = checkdaytrade(anz)
//// process buy order first then sell
        anz1.sortedBy { it.status }.forEach {
            saveOrders(it)
        }


        if (anz1.isNotEmpty() || z.isNotEmpty()) {
            var port = portfolioService.getAllAverage()

            var tot = port.sumByDouble { it.quantity * it.price }
            var invested = percentformat((tot / 400000) * 100)
            var free = percentformat(100 - invested.toDouble())
            var z = ordersRepo.findAll(QOrders.orders.date.eq(LocalDate.now()))
            var trans = ""
            z.forEach {
                trans += "${it.code} : <${it.status}>  for  ${it.quantity}  =  ${it.price}$  = total of $ ${it.price * it.quantity}    \n"
            }

            var s = "After today transaction , Portfolio is \n Invested: $invested  %\n FREE :$free  \n \n $trans%"


            emailService.sendSimpleMessage("Portfolio changes due to transactions:", s)

        }
        folder.close(true)
    }


    private fun isorderfill(ord: List<Orders>): Orders? {
        //      println("-----------------isorderfill------------$ord")
        ord.forEach {
            //   println("-----------------isorderfill-----irder-------${it.orderstatus}")
            if (it.orderstatus.equals("fill")) return it
        }
        return null
    }

    fun read(messages: Array<Message>, source: Source): List<Orders> {
//        var list = mutableListOf<List<String>>()

//        var list = mutableListOf<Orders>()
//        messages.forEach {
//            val htmlContent = MimeMessageParser(it as MimeMessage).parse().htmlContent
//            var orders = null
//
//            if (source == Source.NAB) orders = parseNAB(htmlContent)
//            if (source == Source.ANZ) list.add(toOrder(parseANZ(htmlContent), Source.ANZ))
//
////            if (source == Source.NAB) list.add(toOrder(parseNAB(htmlContent), Source.NAB))
////            if (source == Source.ANZ) list.add(toOrder(parseANZ(htmlContent), Source.ANZ))
////              list.add(s)
//        }


        var s = messages
            .map { MimeMessageParser(it as MimeMessage).parse().htmlContent }
            .map {
                if (source == Source.NAB) parseNAB(it)
                else parseANZ(it)

            }
            .filter { it.isNotEmpty() }
            .map {
                if (source == Source.NAB) toOrder(it, Source.NAB)
                else toOrder(it, Source.ANZ)

            }.toList()

        s.forEach {
            println("-----------------OERDRES------------$it")

        }

        return s
//        return list
    }

/*
if sell , then change
portfolio  - position qty
 if portfolio  - position qty  == 0 then remove from portfolio
 else update

 remove & update -- archive portfolio
 add all sell into Sell

 if sell , just add into portfolio
  */

    private fun saveOrders(ord: Orders) {

        println("-----saveOrders--$ord-")
        if (ord.status == "sell") {
            //   processSellOrder(portfolioService, sellService, ord)
            sellManager.processSell(ord)
        } else {
            saveportfolio(portfolioRepo, ord)
        }
        processOrder(ordersRepo, ord)
        println("SAVE ORDER")
    }


//        //      var messages = folder.getMessages()
//
////        val search: SearchTerm = SubjectTerm(keyword)
////        var messages = folder.search(SubjectTerm("nabtrade"))
////        var messages = folder.search( ReceivedDateTerm(ComparisonTerm.EQ,Date()))
//
//        val andTerm: SearchTerm = AndTerm(
//            ReceivedDateTerm(
//                ComparisonTerm.EQ,
//                Date()
//            ), SubjectTerm("nabtrade confirmation")
//        )
//        var messages = folder.search( andTerm)
//


//        println("messages.length--- ${messages.size}");
//        messages.forEach {
//            // println("------From:${it.from[0]}---")
//            println("------Subject:${it.subject}---------------${it.receivedDate}-")
////            println("------Subject:${it.content}---")
////            println("-----
////
////            -Subject:${it.}---")/
//            val htmlContent = MimeMessageParser(it as MimeMessage).parse().htmlContent
////            println(htmlContent)
//            MimeMessageParser(it as MimeMessage).parse().attachmentList.forEach{
//                println("------MIME:${it.contentType}---------------${it.name}----------")
//            }

}
