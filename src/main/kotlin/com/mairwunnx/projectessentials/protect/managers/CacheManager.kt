package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.protect.RegionPositions
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.dao.RegionEntity
import com.soywiz.kds.CacheMap
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger()

internal var regionNameCache = CacheMap<String, RegionEntity>(64)
internal var regionPositionsCache = CacheMap<RegionPositions, RegionEntity>(32)

fun initializeCache() = logger.debug("Initializing cache of regions").also {
    regionNameCache = CacheMap(validateSize(configuration.take().regionNamesCacheSize))
    regionPositionsCache = CacheMap(validateSize(configuration.take().regionPositionsCacheSize))
}

fun invalidateCache() = logger.warn("Invalidating cache of regions").also {
    regionNameCache.clear().also { regionPositionsCache.clear() }
}

fun cache(region: () -> RegionEntity) = cache(region())

fun cache(region: RegionEntity) {
    synchronized(regionNameCache) { regionNameCache.put(region.name, region) }
    synchronized(regionPositionsCache) {
        with(region) {
            regionPositionsCache.put(RegionPositions(minX, minY, minZ, maxX, maxY, maxZ), this)
        }
    }
}

private fun validateSize(size: Int) = when {
    size < 1 -> logger.error("Cache size can't be lower than 1!").let { 32 }
    else -> size
}
