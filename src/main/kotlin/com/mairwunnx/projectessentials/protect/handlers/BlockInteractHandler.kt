package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.protect.*
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object BlockInteractHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: PlayerInteractEvent.RightClickBlock) {
        if (!configuration.take().generalSettings.handleBlockInteract) return
        val player = event.player as ServerPlayerEntity
        val conf = configuration.take()
        if (hasPermission(player, "ess.protect.bypass", 4)) return
        with(event.pos) { getLastRegionAtPos(x, y, z, player.currentDimensionId) }.also {
            if (
                it != null && it.creator != player.name.string &&
                player.name.string !in participantsAsMap(it.participants).keys &&
                FLAG_ALLOW_BLOCK_INTERACT !in getRegionFlags(it)
            ) {
                event.world.getBlockState(event.pos).also { state ->
                    if (state.getContainer(event.world, event.pos) != null) {
                        if (conf.blockInteractSettings.preventInteractBlockHasContainer) {
                            restricted(player) { ACTION_BLOCK_INTERACT }.also {
                                event.isCanceled = true
                            }.let { return }
                        } else return
                    }
                    if (state.block.stateContainer.properties.count() == 0) {
                        if (conf.blockInteractSettings.preventInteractBlockHasZeroState) {
                            restricted(player) { ACTION_BLOCK_INTERACT }.also {
                                event.isCanceled = true
                            }.let { return }
                        } else return
                    }
                    restricted(player) { ACTION_BLOCK_INTERACT }.also { event.isCanceled = true }
                }
            }
        }
    }
}
