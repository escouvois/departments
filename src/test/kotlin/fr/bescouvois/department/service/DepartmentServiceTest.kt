package fr.bescouvois.department.service

import fr.bescouvois.department.TestUtils.Companion.d1
import fr.bescouvois.department.TestUtils.Companion.d2
import fr.bescouvois.department.TestUtils.Companion.d3
import fr.bescouvois.department.TestUtils.Companion.r1
import fr.bescouvois.department.TestUtils.Companion.r2
import fr.bescouvois.department.repository.DepartmentRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@WebFluxTest(DepartmentService::class)
internal class DepartmentServiceTest {

    @Autowired
    lateinit var departmentService: DepartmentService

    @MockBean
    lateinit var departmentRepository: DepartmentRepository

    @Nested
    @DisplayName("get all departments")
    inner class GetAllDepartments {

        @Test
        fun `should return all the departments`() {
            Mockito.`when`(departmentRepository.findAll()).thenReturn(Flux.just(d1, d2, d3))

            StepVerifier.create(departmentService.getAll())
                    .expectNext(d1, d2, d3)
                    .verifyComplete()
        }

        @Test
        fun `should return an empty list`() {
            Mockito.`when`(departmentRepository.findAll()).thenReturn(Flux.empty())

            StepVerifier.create(departmentService.getAll())
                    .expectNextCount(0)
                    .verifyComplete()
        }
    }

    @Nested
    @DisplayName("get by num")
    inner class GetByNum {

        @Test
        fun `should return the department`() {
            Mockito.`when`(departmentRepository.findByNum(d1.num)).thenReturn(Mono.just(d1))

            StepVerifier.create(departmentService.getByNum(d1.num))
                    .expectNext(d1)
                    .verifyComplete()
        }

        @Test
        fun `should return an empty mono`() {
            Mockito.`when`(departmentRepository.findByNum("999")).thenReturn(Mono.empty())

            StepVerifier.create(departmentService.getByNum("999"))
                    .expectNextCount(0)
                    .verifyComplete()
        }
    }

    @Test
    fun count() {
        Mockito.`when`(departmentRepository.count()).thenReturn(Mono.just(listOf(d1, d2, d3).size.toLong()))

        StepVerifier.create(departmentService.count())
                .expectNext(3)
                .verifyComplete()
    }

    @Nested
    @DisplayName("get departments by region")
    inner class GetByRegion {

        @Test
        fun `should return 2 regions`() {
            Mockito.`when`(departmentRepository.findAllByRegion()).thenReturn(Flux.just(r1, r2))

            StepVerifier.create(departmentService.getAllByRegion())
                    .expectNext(r1, r2)
                    .verifyComplete()
        }

        @Test
        fun `should return an empty flux`() {
            Mockito.`when`(departmentRepository.findAllByRegion()).thenReturn(Flux.empty())

            StepVerifier.create(departmentRepository.findAllByRegion())
                    .expectNextCount(0)
                    .verifyComplete()
        }
    }
}