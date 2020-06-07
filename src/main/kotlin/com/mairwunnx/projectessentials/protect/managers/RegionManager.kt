package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.protect.ask
import com.mairwunnx.projectessentials.protect.dao.RegionEntity
import com.mairwunnx.projectessentials.protect.flagsAsSet
import com.mairwunnx.projectessentials.protect.regionsIsIntersect
import com.mairwunnx.projectessentials.protect.structs.Position

fun getAllRegions() = ask { RegionEntity.all().asSequence() }

fun getRegionByName(name: String) = getAllRegions().find { it.name == name }

fun getRegionsByCreator(creator: String) = getAllRegions().filter { it.creator == creator }

fun getRegionsAtPos(x: Int, y: Int, z: Int, dim: Int) = getAllRegions().filter {
    regionsIsIntersect(
        Position(x, y, z), Position(x, y, z),
        Position(it.maxX, it.maxY, it.maxZ), Position(it.minX, it.minY, it.minZ)
    ) && it.dimension == dim
}.sortedBy { it.priority }

fun getLastRegionAtPos(x: Int, y: Int, z: Int, dim: Int) = getRegionsAtPos(x, y, z, dim).let {
    if (it.count() == 0) null else it.last()
}

fun getRegionFlags(region: RegionEntity): Set<String> {
    return if (region.priority > 0) {
        getAllRegions().filter {
            regionsIsIntersect(
                Position(region.maxX, region.maxY, region.maxZ),
                Position(region.minX, region.minY, region.minZ),
                Position(it.maxX, it.maxY, it.maxZ),
                Position(it.minX, it.minY, it.minZ)
            ) && region.dimension == it.dimension && it.priority < region.priority
        }.let { flagsAsSet(region.flags).plus(it.map { flagsAsSet(it.flags) }.flatten().toSet()) }
    } else flagsAsSet(region.flags)
}
