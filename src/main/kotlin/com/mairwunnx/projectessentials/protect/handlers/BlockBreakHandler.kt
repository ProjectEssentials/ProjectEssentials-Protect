package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.protect.ACTION_BLOCK_BREAK
import com.mairwunnx.projectessentials.protect.FLAG_ALLOW_BLOCK_BREAK
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import com.mairwunnx.projectessentials.protect.participantsAsMap
import com.mairwunnx.projectessentials.protect.restricted
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object BlockBreakHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: BlockEvent.BreakEvent) {
        if (event.player !is ServerPlayerEntity) return
        val player = event.player as ServerPlayerEntity
        if (hasPermission(player, "ess.protect.bypass", 4)) return
        with(event.pos) { getLastRegionAtPos(x, y, z, event.player.currentDimensionId) }.also {
            if (
                it != null && it.creator != player.name.string &&
                player.name.string !in participantsAsMap(it.participants).keys &&
                FLAG_ALLOW_BLOCK_BREAK !in getRegionFlags(it)
            ) restricted(player) { ACTION_BLOCK_BREAK }.also { event.isCanceled = true }
        }
    }
}