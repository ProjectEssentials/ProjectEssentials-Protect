package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_ENTITY_SPAWN
import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_KIND_ENTITY_SPAWN
import com.mairwunnx.projectessentials.protect.FLAG_RESTRICT_UNKIND_ENTITY_SPAWN
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
        }.also {
            if (it != null) {
                val regionFlags = getRegionFlags(it)
                if (FLAG_RESTRICT_ENTITY_SPAWN in regionFlags) {
                    { event.isCanceled = true }.let { return }
                } else {
                    with(event.entity.type.classification) {
                        if (peacefulCreature || animal) {
                            if (FLAG_RESTRICT_KIND_ENTITY_SPAWN in regionFlags) {
                                { event.isCanceled = true }.let { return }
                            }
                        } else {
                            if (FLAG_RESTRICT_UNKIND_ENTITY_SPAWN in regionFlags) {
                                { event.isCanceled = true }.let { return }
                            }
                        }
                    }
                }
            }
        }
    }
}
