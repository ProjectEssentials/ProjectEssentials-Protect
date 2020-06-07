package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_FLOW_EFFECT
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object FlowEffectsHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: BlockEvent.FluidPlaceBlockEvent) {
        fun cancel() = { event.newState = event.originalState }.also { event.isCanceled = true }
        if (!configuration.take().generalSettings.handleFlowEffects) return
        with(event.pos) {
            getLastRegionAtPos(x, y, z, (event.world as ServerWorld).dimension.type.id)?.let {
                if (FLAG_RESTRICT_FLOW_EFFECT in getRegionFlags(it)) cancel()
            } ?: let { if (configuration.take().globalRegionSettings.restrictFlowEffect) cancel() }
        }
    }
}
