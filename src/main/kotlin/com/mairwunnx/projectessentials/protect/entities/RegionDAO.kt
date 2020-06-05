package com.mairwunnx.projectessentials.protect.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RegionTable : IntIdTable() {
    val name = varchar("name", 24).index()
    val creator = varchar("creator", 16).default("#server")
    val participants = text("participants").default("[]")
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

    var name by RegionTable.name
    var creator by RegionTable.creator
    var participants by RegionTable.participants
    var flags by RegionTable.flags
    var priority by RegionTable.priority
    var dimension by RegionTable.dimension
    var minX by RegionTable.minX
    var minY by RegionTable.minY
    var minZ by RegionTable.minZ
    var maxX by RegionTable.maxX
    var maxY by RegionTable.maxY
    var maxZ by RegionTable.maxZ
}
