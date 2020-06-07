package com.mairwunnx.projectessentials.protect.handlers

import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.protect.*
import com.mairwunnx.projectessentials.protect.managers.getLastRegionAtPos
import com.mairwunnx.projectessentials.protect.managers.getRegionFlags
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object BucketUseHandler : ActivityHandler {
    override fun init() = EVENT_BUS.register(this)

    @SubscribeEvent
    fun handle(event: FillBucketEvent) {
        if (!configuration.take().generalSettings.handleBucketUsing) return
        if (event.entity !is ServerPlayerEntity) return
        val player = event.entity as ServerPlayerEntity
        if (hasPermission(player, "ess.protect.bypass", 4)) return
        with(player.position) { getLastRegionAtPos(x, y, z, player.currentDimensionId) }?.also {
            if (
                it.creator != player.name.string &&
                player.name.string !in participantsAsMap(it.participants).keys &&
                FLAG_ALLOW_BUCKET_USING !in getRegionFlags(it)
            ) restricted(player) { ACTION_BUCKET_USING }.also { event.isCanceled = true }
        } ?: run {
            if (configuration.take().globalRegionSettings.restrictBucketUsing) {
                restricted(player) { ACTION_BUCKET_USING }.also { event.isCanceled = true }
            }
        }
    }
}