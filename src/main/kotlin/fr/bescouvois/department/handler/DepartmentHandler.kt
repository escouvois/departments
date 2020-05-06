package fr.bescouvois.department.handler

import fr.bescouvois.department.model.Department
import fr.bescouvois.department.model.Region
import fr.bescouvois.department.service.DepartmentService
import org.springframework.http.HttpStatus
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

    fun all(request: ServerRequest) =
            ok().body(service.getAll().handleInternalError(), Department::class.java)

    fun byId(request: ServerRequest) =
            service.getByNum(request.pathVariable("num"))
                    .flatMap(ok()::bodyValue)
                    .switchIfEmpty(notFound().build())
                    .handleInternalError()

    fun allByRegion(request: ServerRequest) =
            ok().body(service.getAllByRegion().handleInternalError(), Region::class.java)

    private fun Flux<*>.handleInternalError() =
            this.onErrorMap { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }

    private fun Mono<ServerResponse>.handleInternalError() =
            this.onErrorMap { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }
}