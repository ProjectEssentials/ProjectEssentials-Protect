package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.protect.FLAG_ALLOW_EXPLOSIONS
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.world.ExplosionEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object ExplosionHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: ExplosionEvent.Start) {
        if (!configuration.take().generalSettings.handleExplosions) return
        event.explosion.affectedBlockPositions.removeIf {
            getLastRegionAtPos(
                it.x, it.y, it.z, (event.world as ServerWorld).dimension.type.id
            )?.let { region ->
                FLAG_ALLOW_EXPLOSIONS !in getRegionFlags(region)
            } ?: let { configuration.take().globalRegionSettings.restrictExplosions }
        }
    }
}
