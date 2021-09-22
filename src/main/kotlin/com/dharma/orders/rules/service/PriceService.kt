package com.dharma.orders.rules.service

import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.Portfolio
import com.dhamma.pesistence.entity.repo.DataRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PriceService {
    @Autowired
    lateinit var dataRepo: DataRepo

    fun price(port: List<Portfolio>): Map<String, CoreData> {
        var map = mutableMapOf<String, CoreData>()
        port.distinctBy { it.code }.forEach {
            var z = dataRepo.findbyCodelatest(it.code)
            map.put(it.code, z)
        }
        return map
    }

    fun today() = dataRepo.findbyCodelatest("BHP.AX").date
}
