package fr.bescouvois.department.router

import fr.bescouvois.department.handler.DepartmentHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class DepartmentRouter {
    @Bean
    fun route(handler: DepartmentHandler) = router {
        listOf(
                GET("/departments", handler::all),
                GET("/departments/region", handler::allByRegion),
                GET("/departments/{num}", handler::byId)
        )
    }
}