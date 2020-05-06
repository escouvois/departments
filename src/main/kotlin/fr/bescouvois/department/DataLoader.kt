package fr.bescouvois.department

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.DepartmentJSON
import fr.bescouvois.department.repository.DepartmentRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import java.io.FileReader
import javax.annotation.PostConstruct

@Component
class DataLoader(private val rs: ResourceLoader,
                 private val repo: DepartmentRepository) {

    @PostConstruct
    fun load() {
        val listDepartmentType = object : TypeToken<List<DepartmentJSON>>() {}.type
        val file = rs.getResource("classpath:departements-region.json").file
        var departments: List<DepartmentJSON> = Gson().fromJson(FileReader(file), listDepartmentType)
        println("> From JSON File:\n$departments")
        repo.deleteAll()
                .thenMany(departments.toFlux().map { Department.mapToDepartment(it) }.flatMap { repo.save(it) })
                .thenMany(repo.count())
                .subscribe { println("Numbers of departments inserted: $it") }
    }
}