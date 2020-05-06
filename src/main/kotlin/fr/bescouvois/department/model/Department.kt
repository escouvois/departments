package fr.bescouvois.department.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "department")
data class Department(
        val id: String? = null,
        val num: String,
        val name: String,
        val region: String
) {
    companion object {
        fun mapToDepartment(dep: DepartmentJSON) =
                Department(num = dep.num, name = dep.name, region = dep.region)
    }

    override fun toString(): String {
        return "Departement[id: ${this.id}, num: ${this.num}, name: ${this.name}, region: ${this.region}]"
    }
}