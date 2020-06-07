package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.protect.FLAG_ALLOW_PVP
import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_DAMAGE
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object PlayerDamageHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: LivingHurtEvent) {
        if (!configuration.take().generalSettings.handlePlayerDamage) return
        if (event.entity !is ServerPlayerEntity) return
        val player = event.entity as ServerPlayerEntity
        with(player.position) { getLastRegionAtPos(x, y, z, player.currentDimensionId) }?.also {
            getRegionFlags(it).also { flags ->
                if (FLAG_ALLOW_PVP !in flags) event.isCanceled = FLAG_RESTRICT_DAMAGE in flags
            }
        } ?: run { event.isCanceled = configuration.take().globalRegionSettings.restrictDamage }
    }
}
