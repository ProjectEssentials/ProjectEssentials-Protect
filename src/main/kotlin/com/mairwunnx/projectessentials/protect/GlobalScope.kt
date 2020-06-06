package com.mairwunnx.projectessentials.protect

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.protect.configurations.ProtectConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.minecraft.entity.player.ServerPlayerEntity
import org.jetbrains.exposed.sql.transactions.transaction

internal val configuration by lazy {
    getConfigurationByName<ProtectConfiguration>("protect")
}

internal inline fun <T> ask(crossinline ask: () -> T) = transaction { ask() }

internal inline fun <T> asyncAsk(crossinline callback: (T?) -> Unit, crossinline ask: () -> T) {
    CoroutineScope(Dispatchers.Default).launch {
        async {
            ask { return@ask ask() }
        }.await()?.let { callback(it) } ?: run { callback(null) }
    }
}

internal fun restricted(player: ServerPlayerEntity, action: () -> String) {
    MessagingAPI.sendMessage(
        player, "${MESSAGE_MODULE_PREFIX}protect.restricted.${action()}"
    )
}


