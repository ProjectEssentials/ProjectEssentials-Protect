package com.mairwunnx.projectessentials.protect.configurations

data class ProtectConfigurationModel(
    var regionNamesCacheSize: Int = 128,
    var regionPositionsCacheSize: Int = 64
)
