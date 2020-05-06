package fr.bescouvois.department.service

import fr.bescouvois.department.repository.DepartmentRepository
import org.springframework.stereotype.Service

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
}