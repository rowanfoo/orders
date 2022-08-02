package com.dharma.orders

import org.jsoup.Jsoup
import java.util.*

//fun labelid(gmail: Gmail, labelName: String): Label {
//    val labelList = gmail.users().labels().list("me").execute()
////    labelList.labels.forEach {
////        println(it)
////    }
//    val label = labelList.labels
//        .find { it.name == labelName } ?: error("Label `$labelName` is unknown.")
//    return label
//}

//fun messages(gmail: Gmail, lableid: String, query: String = ""): ListMessagesResponse {
//
//    var z = gmail.Users().messages().list("me").apply {
//        pageToken = null
//        includeSpamTrash = true
//        q = query
//    }.execute()
//    println("-->>>>SIZE---${z.resultSizeEstimate}")
//    println("-->>>>x--")
//    //return if( z.resultSizeEstimate > 0)return z else null
//    return z
//}


//fun mail(gmail: Gmail, m: Message) = gmail.Users().messages().get("me", m.id).execute()
//fun mail(gmail: Gmail, m: Message): Message {
//    //  println("---------mail---${m}---")
//    return gmail.Users().messages().get("me", m.id).execute()
//}
//
//
//fun attachment(gmail: Gmail, messageid: String, attachid: String) =
//    gmail.Users().messages().Attachments().get("me", messageid, attachid).execute()

fun processtxtplain(message: String): String {
    var z = String(Base64.getUrlDecoder().decode(message))
//    println("--processtxtplain---$z")
//    println("--processtxtplain---")
    return z

}


fun parseANZ(data: String): List<String> {
    // println(data)
    val doc = Jsoup.parse(data)
    val rows = doc.select("td[bgcolor=\"#ffffff\"]")
    rows.forEach {
        //  println(it.text())
//        if ((it.text().indexOf("partially") < 0)) {
        if (it.text().isNotEmpty()) {
            //            println("ANX:${it.text()}")
            //getoutput(it.text())
            var z = getoutputanz(it.text())
            println(z)
            return z
        }
    }

    return emptyList<String>()
}


fun parseNAB(data: String): List<String> {
    //   println(data)
    val doc = Jsoup.parse(data)
    val rows = doc.select("p[style*=padding-top:0]")
    rows.forEach {
        //      println("NAB:${it.text()}")
        if (testnab(it.text()).isNotEmpty()) {
            //println(it.text())
            return getoutput(it.text())
        }
    }
    return emptyList<String>()
}

private fun testnab(tmp: String): String {
    var i = tmp.indexOf("confirmation")
    return if (i <= 0) return "" else tmp
}


private fun getoutput(tmp: String): List<String> {
    var z = tmp.split(" ")
    //  println(z)
    var status = z[2]
    var ordid = z[4]
    var sum = z[6].replace(",", "")
    var code = z[7]
    var price = z[9]
//      println("-NAB--$status------$ordid-------$code-----$sum----$price")
    //println("-----NAB-----$z")
    return listOf(z[2].trim().toLowerCase(), z[4].trim(), sum, z[7].replace("ASX", "AX"), z[9].replace("$", ""), "fill")
}

private fun getoutputanz(tmp: String): List<String> {
    // println("-----getoutputanz-----$tmp")
    var z = tmp.split(" ")
    //  z.forEachIndexed { index: Int, s: String -> println("-------$index =====$s") }

//    var status = z[5]
//    var ordid = z[40]
//    var sum = z[6].replace(",", "")
//    var code = "${z[7]}.AX"
//    var price = z[9]
//    // println("-ANZ--$status------$ordid-------$code-----$sum----$price")
//    return listOf(z[5].trim().toLowerCase(), z[40].trim(), sum, code, z[9])
    //println("-----ANZ-----$z")
    //println("-----ANZ>>>>>>-----${z[16]}")
    var status = z[70]
    var ordid = z[67]
    // println("-ANZ--ordid -- $ordid")
    if (ordid.toIntOrNull() == null) {
        println("-----ERROR-----$z")
        return getoutputanzfill(tmp)
    }

    var sum = z[87].replace(",", "")
    var code = "${z[74]}.AX"
    var price = z[90]
    // println("-ANZ--$status------$ordid-------$code-----$sum----$price")
    return listOf(status.trim().toLowerCase(), ordid.trim(), sum, code, price, "partial")
}


private fun getoutputanzfill(tmp: String): List<String> {
    //  println("-----getoutputanz-----$tmp")
    var z = tmp.split(" ")
    //   z.forEachIndexed { index: Int, s: String -> println("-------$index =====$s") }

//    var status = z[5]
//    var ordid = z[40]
//    var sum = z[6].replace(",", "")
//    var code = "${z[7]}.AX"
//    var price = z[9]
//    // println("-ANZ--$status------$ordid-------$code-----$sum----$price")
//    return listOf(z[5].trim().toLowerCase(), z[40].trim(), sum, code, z[9])

    var status = z[5]
    var ordid = z[40]
    // println("-ANZ--ordid -- $ordid")
    var sum = z[6].replace(",", "")
    //println("-ANZ--sum -- $sum")
    var code = "${z[7]}.AX"
    //println("-ANZ--code -- $code")

    var price = z[9]
    println("-ANZ---FILL----$status------$ordid-------$code-----$sum----$price")
    if (price.toDoubleOrNull() == null) return listOf()
    return listOf(status.trim().toLowerCase(), ordid.trim(), sum, code, price, "fill")
}
