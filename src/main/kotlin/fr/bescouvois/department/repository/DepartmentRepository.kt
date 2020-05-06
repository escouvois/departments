package fr.bescouvois.department.repository

import fr.bescouvois.department.model.Region
import fr.bescouvois.department.model.Department
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DepartmentRepository : ReactiveCrudRepository<Department, String> {
    fun findByNum(num: String): Mono<Department>

    @Aggregation("{ \$group: { _id: \$region, departments:{\$addToSet: \$name } } }")
    fun findAllByRegion(): Flux<Region>
}