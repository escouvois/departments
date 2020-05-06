package fr.bescouvois.department.router

import com.ninjasquad.springmockk.MockkBean
import fr.bescouvois.department.TestUtils.Companion.d1
import fr.bescouvois.department.TestUtils.Companion.d2
import fr.bescouvois.department.TestUtils.Companion.d3
import fr.bescouvois.department.TestUtils.Companion.r1
import fr.bescouvois.department.TestUtils.Companion.r2
import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.Region
import fr.bescouvois.department.service.DepartmentService
import io.mockk.coEvery
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
internal class ExternalAPITest {

    companion object {
        private const val BASE_URL = "/departments"
    }

    @Autowired
    private lateinit var client: WebTestClient

    @MockkBean
    private lateinit var departmentService: DepartmentService

    @Nested
    @DisplayName("GET $BASE_URL")
    inner class GetAllDepartments {

        @Test
        fun `should return all the departments`() {
            coEvery { departmentService.getAll() } returns Flux.just(d1, d2, d3)

            val result = client.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .returnResult<Department>()

            StepVerifier.create(result.responseBody)
                    .expectNext(d1, d2, d3)
                    .verifyComplete()
        }

        @Test
        fun `should return an empty list`() {
            coEvery { departmentService.getAll() } returns Flux.empty()

            val result = client.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk
                    .returnResult<Region>()

            StepVerifier.create(result.responseBody)
                    .expectNextCount(0)
                    .verifyComplete()
        }

        @Test
        fun `should return an error`() {
            coEvery { departmentService.getAll() } returns
                    Flux.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))

            client.get()
                    .uri("$BASE_URL")
                    .exchange()
                    .expectStatus().is5xxServerError
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.timestamp").isNotEmpty
                    .jsonPath("$.path").isEqualTo("$BASE_URL")
                    .jsonPath("$.status").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .jsonPath("$.error").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
        }
    }

    @Nested
    @DisplayName("GET $BASE_URL/region")
    inner class GetByRegion {

        @Test
        fun `should return all the regions`() {
            coEvery { departmentService.getAllByRegion() } returns Flux.just(r1, r2)

            val result = client.get()
                    .uri("$BASE_URL/region")
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .returnResult<Region>()

            StepVerifier.create(result.responseBody)
                    .expectNext(r1, r2)
                    .verifyComplete()
        }

        @Test
        fun `should return an empty list`() {
            coEvery { departmentService.getAllByRegion() } returns Flux.empty()

            val result = client.get()
                    .uri("$BASE_URL/region")
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .returnResult<Region>()

            StepVerifier.create(result.responseBody)
                    .expectNextCount(0)
                    .verifyComplete()
        }

        @Test
        fun `should return an error`() {
            coEvery { departmentService.getAllByRegion() } returns
                    Flux.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))

            client.get()
                    .uri("$BASE_URL/region")
                    .exchange()
                    .expectStatus().is5xxServerError
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.timestamp").isNotEmpty
                    .jsonPath("$.path").isEqualTo("$BASE_URL/region")
                    .jsonPath("$.status").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .jsonPath("$.error").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
        }
    }

    @Nested
    @DisplayName("GET $BASE_URL/{num}")
    inner class GetByNum {

        @Test
        fun `should return the department`() {
            coEvery { departmentService.getByNum(d2.num) } returns Mono.just(d2)

            val result = client.get()
                    .uri("$BASE_URL/${d2.num}")
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .returnResult<Department>()

            StepVerifier.create(result.responseBody)
                    .expectNext(d2)
                    .verifyComplete()
        }

        @Test
        fun `should return not found`() {
            coEvery { departmentService.getByNum("999") } returns Mono.empty()

            client.get()
                    .uri("$BASE_URL/999")
                    .exchange()
                    .expectStatus().isNotFound
                    .expectBody().isEmpty
        }

        @Test
        fun `should return an error`() {
            coEvery { departmentService.getByNum(d1.num) } returns
                    Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))

            client.get()
                    .uri("$BASE_URL/${d1.num}")
                    .exchange()
                    .expectStatus().is5xxServerError
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.timestamp").isNotEmpty
                    .jsonPath("$.path").isEqualTo("$BASE_URL/${d1.num}")
                    .jsonPath("$.status").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .jsonPath("$.error").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
        }
    }

}