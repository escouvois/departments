package fr.bescouvois.department.router

import fr.bescouvois.department.handler.DepartmentHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class DepartmentRouter {
    @Bean
    fun route(handler: DepartmentHandler) = coRouter {
        "/departments".nest {
            GET("/", handler::all)
            GET("/region", handler::allByRegion)
            GET("/{num}", handler::byId)
            GET("/{num}/population", handler::getPopulationByDepartment)
        }
    }
}