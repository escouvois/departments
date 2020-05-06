package fr.bescouvois.department

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.DepartmentJSON
import fr.bescouvois.department.repository.DepartmentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import java.io.FileReader
import java.lang.invoke.MethodHandles
import javax.annotation.PostConstruct

@Component
class DataLoader(private val rs: ResourceLoader,
                 private val repo: DepartmentRepository) {

    private val logger: Logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    @PostConstruct
    fun load() {
        val listDepartmentType = object : TypeToken<List<DepartmentJSON>>() {}.type
        val file = rs.getResource("classpath:departements-region.json").file
        val departments: List<DepartmentJSON> = Gson().fromJson<List<DepartmentJSON>>(FileReader(file), listDepartmentType)
                .also { logger.info("> From JSON File:\n$it") }
        repo.deleteAll()
                .thenMany(departments.toFlux().map { Department.mapToDepartment(it) }.flatMap { repo.save(it) })
                .thenMany(repo.count())
                .subscribe { logger.info("Numbers of departments inserted: $it") }
    }
}