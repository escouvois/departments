package fr.bescouvois.department.service

import fr.bescouvois.department.repository.DepartmentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.Stream
import kotlin.random.Random

@Service
class DepartmentService(private val repo: DepartmentRepository) {
    fun getAll() =
            repo.findAll()

    fun getByNum(num: String) =
            repo.findByNum(num)

    fun count() =
            repo.count()

    fun getAllByRegion() =
            repo.findAllByRegion()

    /**
     * Simulate the population trends of the given department, and save it.
     */
    fun getPopulationByNum(num: String) =
            getByNum(num).flatMapMany { department ->
                Flux.fromStream(Stream.iterate(department.population) { Random.nextLong(0, 100) })
                        .delayElements(Duration.ofSeconds(1))
                        .doOnNext { department.population += it }
                        .flatMap { repo.save(department) }
            }

}