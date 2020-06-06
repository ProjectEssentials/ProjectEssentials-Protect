package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.dao.RegionEntity
import com.soywiz.kds.CacheMap
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger()

/**
 * Region cache, key is a region name, value is a region entity.
 *
 * @since 1.0.0.
 */
var regionCache = CacheMap<String, RegionEntity>(64)

/**
 * Initializing cache, creating [CacheMap] instance with defining
 * max size of cache of regions.
 *
 * @since 1.0.0.
 */
fun initializeCache() = logger.debug("Initializing cache of regions").also {
    regionCache = CacheMap(validateSize(configuration.take().regionCacheSize))
}

/**
 * Cache target region, adds [RegionEntity] instance to [regionCache] map.
 *
 * @param region region to cache.
 * @return cached region instance.
 * @since 1.0.0.
 */
fun cache(region: RegionEntity) = synchronized(regionCache) { regionCache.put(region.name, region) }

/**
 * Removed target region by name from cache map.
 *
 * @param name region to remove from cache.
 * @return cached region instance.
 * @since 1.0.0.
 */
fun invalidate(name: String) = synchronized(regionCache) { regionCache.remove(name) }

/**
 * Invalidate all cache, it will do simply clear cache map.
 * @since 1.0.0.
 */
fun invalidateAll() = logger.warn("Invalidating cache of regions").also {
    synchronized(regionCache) { regionCache.clear() }
}

private fun validateSize(size: Int) = when {
    size < 1 -> logger.error("Cache size can't be lower than 1!").let { 32 }
    else -> size
}
