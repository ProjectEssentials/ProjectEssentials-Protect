package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_MOD_GRIEF
import com.mairwunnx.projectessentials.protect.configuration
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object EntityGriefHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: LivingDestroyBlockEvent) {
        if (!configuration.take().generalSettings.handleEntityGrief) return
        with(event) {
            getLastRegionAtPos(pos.x, pos.y, pos.z, entity.dimension.id)
        }?.also {
            if (FLAG_RESTRICT_MOD_GRIEF in getRegionFlags(it)) {
                { event.isCanceled = true }.let { return }
            }
        } ?: run {
            if (configuration.take().globalRegionSettings.restrictMobGrief) event.isCanceled = true
        }
    }
}
