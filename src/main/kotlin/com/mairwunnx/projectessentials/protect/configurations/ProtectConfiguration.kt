package com.mairwunnx.projectessentials.protect.configurations

import com.mairwunnx.projectessentials.core.api.v1.configuration.IConfiguration

object ProtectConfiguration : IConfiguration<ProtectConfigurationModel> {
    override val configuration: ProtectConfigurationModel
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")
    override val path: String
        get() = TODO("Not yet implemented")
    override val version: Int
        get() = TODO("Not yet implemented")

    override fun load() {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun take(): ProtectConfigurationModel {
        TODO("Not yet implemented")
    }
}
