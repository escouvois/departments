package fr.bescouvois.department

import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.Region

class TestUtils {
    companion object {
        val d1 = Department(num = "87", name = "Haute-Vienne", region = "Nouvelle-Aquitaine")
        val d2 = Department(num = "19", name = "Corrèze", region = "Nouvelle-Aquitaine")
        val d3 = Department(num = "973", name = "Guyane", region = "Guyane")

        val r1 = Region("Nouvelle-Aquitaine", listOf("Haute-Vienne", "Corrèze"))
        val r2 = Region("Guyane", listOf("Guyane"))
        
        val p1 = Department(id = "111-111", num = "87", name = "Haute-Vienne", region = "Nouvelle-Aquitaine", population = 10)
        val p2 = Department(id = "111-111", num = "87", name = "Haute-Vienne", region = "Nouvelle-Aquitaine", population = 20)
        val p3 = Department(id = "111-111", num = "87", name = "Haute-Vienne", region = "Nouvelle-Aquitaine", population = 30)
    }
}