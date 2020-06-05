package com.mairwunnx.projectessentials.protect.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RegionTable : IntIdTable() {
    val name = varchar("name", 16).uniqueIndex().index()
    val creator = varchar("creator", 16).default("#server")
    val participants = text("participants").default("[test:member]")
    val flags = text("flags").default("[]")
    val priority = integer("priority").default(0)
    val dimension = RegionTable.integer("dimension")
    val minX = RegionTable.integer("minX")
    val minY = RegionTable.integer("minY")
    val minZ = RegionTable.integer("minZ")
    val maxX = RegionTable.integer("maxX")
    val maxY = RegionTable.integer("maxY")
    val maxZ = RegionTable.integer("maxZ")
}

class RegionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RegionEntity>(RegionTable)

    val name by RegionTable.name
    val creator by RegionTable.creator
    val participants by RegionTable.participants
    val flags by RegionTable.flags
    val priority by RegionTable.priority
    val dimension by RegionTable.dimension
    val minX by RegionTable.minX
    val minY by RegionTable.minY
    val minZ by RegionTable.minZ
    val maxX by RegionTable.maxX
    val maxY by RegionTable.maxY
    val maxZ by RegionTable.maxZ
}
