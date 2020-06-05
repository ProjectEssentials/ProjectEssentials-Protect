package com.mairwunnx.projectessentials.protect

import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName
import com.mairwunnx.projectessentials.protect.configurations.ProtectConfiguration
import org.jetbrains.exposed.sql.transactions.transaction

internal val configuration by lazy {
    getConfigurationByName<ProtectConfiguration>("protect")
}

internal inline fun <T> ask(crossinline body: () -> T) = transaction { body() }


