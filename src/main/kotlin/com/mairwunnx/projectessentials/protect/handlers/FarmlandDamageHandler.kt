package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.protect.FLAG_ALLOW_FARMLAND_DAMAGE
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import com.mairwunnx.projectessentials.protect.participantsAsMap
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object FarmlandDamageHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: BlockEvent.FarmlandTrampleEvent) {
        if (!configuration.take().generalSettings.handleFarmlandDamage) return
        if (event.entity is ServerPlayerEntity) {
            val player = event.entity as ServerPlayerEntity
            if (hasPermission(player, "ess.protect.bypass", 4)) return
        }
        with(event.pos) { getLastRegionAtPos(x, y, z, event.entity.dimension.id) }?.also {
            if (event.entity is ServerPlayerEntity) {
                val player = event.entity as ServerPlayerEntity
                if (
                    it.creator != player.name.string &&
                    player.name.string !in participantsAsMap(it.participants).keys &&
                    FLAG_ALLOW_FARMLAND_DAMAGE !in getRegionFlags(it)
                ) event.isCanceled = true
            } else event.isCanceled = FLAG_ALLOW_FARMLAND_DAMAGE !in getRegionFlags(it)
        } ?: run {
            event.isCanceled = configuration.take().globalRegionSettings.restrictFarmlandDamage
        }
    }
}
