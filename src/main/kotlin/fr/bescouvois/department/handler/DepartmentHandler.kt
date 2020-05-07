package fr.bescouvois.department.handler

import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.Region
import fr.bescouvois.department.service.DepartmentService
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class DepartmentHandler(private val service: DepartmentService) {

    suspend fun all(request: ServerRequest): ServerResponse =
            ok().body(service.getAll().handleInternalError(), Department::class.java).awaitFirst()

    suspend fun byId(request: ServerRequest): ServerResponse =
            service.getByNum(request.pathVariable("num"))
                    .flatMap(ok()::bodyValue)
                    .switchIfEmpty(notFound().build())
                    .handleInternalError()
                    .awaitSingle()

    suspend fun allByRegion(request: ServerRequest): ServerResponse =
            ok().body(service.getAllByRegion().handleInternalError(), Region::class.java).awaitFirst()

    suspend fun getPopulationByDepartment(request: ServerRequest): ServerResponse =
            ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(service.getPopulationByNum(request.pathVariable("num")).handleInternalError(), Department::class.java)
                    .awaitFirst()

    private fun Flux<*>.handleInternalError() =
            this.onErrorMap { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }

    private fun Mono<ServerResponse>.handleInternalError() =
            this.onErrorMap { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }
}