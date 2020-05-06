package fr.bescouvois.department.model

import com.google.gson.annotations.SerializedName
import com.mongodb.client.model.Aggregates
import org.bson.BSON
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class DepartmentJSON(
        @SerializedName("num_dep") @Id val num: String,
        @SerializedName("dep_name") val name: String,
        @SerializedName("region_name") val region: String
) {
    override fun toString(): String {
        return "DepartementJSON [num: ${this.num}, name: ${this.name}, region: ${this.region}]"
    }
}