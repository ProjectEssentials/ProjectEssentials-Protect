package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_ENTITY_SPAWN
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.living.LivingSpawnEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object EntitySpawnHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: LivingSpawnEvent.SpecialSpawn) {
        if (!configuration.take().generalSettings.handleEntitySpawn) return
        with(event) {
            getLastRegionAtPos(x.toInt(), y.toInt(), z.toInt(), entity.dimension.id)
        }.also { event.isCanceled = it != null && FLAG_RESTRICT_ENTITY_SPAWN in getRegionFlags(it) }
    }
}
