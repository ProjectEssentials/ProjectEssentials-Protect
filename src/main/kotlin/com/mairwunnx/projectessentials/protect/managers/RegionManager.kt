package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.core.api.v1.extensions.empty
import com.mairwunnx.projectessentials.protect.*
import com.mairwunnx.projectessentials.protect.dao.RegionEntity
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
    if (region.priority > 0) {
        getAllRegions().filter {
            regionsIsIntersect(
                Position(region.maxX, region.maxY, region.maxZ),
                Position(region.minX, region.minY, region.minZ),
                Position(it.maxX, it.maxY, it.maxZ),
                Position(it.minX, it.minY, it.minZ)
            ) && region.dimension == it.dimension && it.priority < region.priority
        }.sortedBy { it.priority }.let {
            return getGlobalRegionFlags()
                .plus(flagsAsSet(region.flags))
                .plus(it.map { flagsAsSet(it.flags) }.flatten().toSet())
        }
    } else return flagsAsSet(region.flags)
}

fun getGlobalRegionFlags() = with(configuration.take()) {
    setOf(
        if (globalRegionSettings.restrictFallDamage) FLAG_RESTRICT_FALL_DAMAGE else String.empty,
        if (globalRegionSettings.restrictDamage) FLAG_RESTRICT_DAMAGE else String.empty,
        if (globalRegionSettings.restrictMobGrief) FLAG_RESTRICT_MOD_GRIEF else String.empty,
        if (globalRegionSettings.restrictFlowEffect) FLAG_RESTRICT_FLOW_EFFECT else String.empty
    ).filter { it.isNotBlank() }.toSet()
}
