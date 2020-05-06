package fr.bescouvois.department.model

import org.springframework.data.annotation.Id

data class Region(@Id val name: String, val departments: List<String>)