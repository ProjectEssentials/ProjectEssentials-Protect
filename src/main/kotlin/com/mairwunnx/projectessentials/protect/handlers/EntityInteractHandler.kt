package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.protect.*
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.ProjectileImpactEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object EntityInteractHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: PlayerInteractEvent.EntityInteract) {
        if (!configuration.take().generalSettings.handleEntityInteract) return
        val player = event.player as ServerPlayerEntity
        if (hasPermission(player, "ess.protect.bypass", 4)) return
        with(event.pos) { getLastRegionAtPos(x, y, z, player.currentDimensionId) }?.also {
            if (
                it.creator != player.name.string &&
                player.name.string !in participantsAsMap(it.participants).keys &&
                FLAG_ALLOW_ENTITY_INTERACT !in getRegionFlags(it)
            ) restricted(player) { ACTION_ENTITY_INTERACT }.also { event.isCanceled = true }
        }
    }

    @SubscribeEvent
    fun handleProjectile(event: ProjectileImpactEvent) {
        if (
            !configuration.take().generalSettings.handleEntityInteract ||
            !configuration.take().generalSettings.handleEntityInteractProjectile
        ) return

        if (event.entity is ServerPlayerEntity) {
            val player = event.entity as ServerPlayerEntity
            if (hasPermission(player, "ess.protect.bypass", 4)) return
        }

        with(event.rayTraceResult.hitVec) {
            getLastRegionAtPos(x.toInt(), y.toInt(), z.toInt(), event.entity.dimension.id)
        }?.also {
            if (event.entity is ServerPlayerEntity) {
                val player = event.entity as ServerPlayerEntity
                if (
                    it.creator != player.name.string &&
                    player.name.string !in participantsAsMap(it.participants).keys &&
                    FLAG_ALLOW_ENTITY_INTERACT !in getRegionFlags(it) &&
                    event.rayTraceResult.type == RayTraceResult.Type.ENTITY
                ) restricted(event.entity as ServerPlayerEntity) { ACTION_ENTITY_INTERACT }.also {
                    event.isCanceled = true
                }
            } else if (FLAG_ALLOW_ENTITY_INTERACT !in getRegionFlags(it)) event.isCanceled = true
        }
    }
}
