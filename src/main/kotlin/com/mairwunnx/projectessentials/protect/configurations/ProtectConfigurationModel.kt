package com.mairwunnx.projectessentials.protect.configurations

data class ProtectConfigurationModel(
    val generalSettings: GeneralSettings = GeneralSettings(),
    val blockInteractSettings: BlockInteractSettings = BlockInteractSettings(),
    val globalRegionSettings: GlobalRegionSettings = GlobalRegionSettings()
) {
    data class GeneralSettings(
        var handleBlockBreak: Boolean = true,
        var handleBlockPlace: Boolean = true,
        var handleBlockInteract: Boolean = true,
        var handleEntityInteract: Boolean = true,
        var handleEntityInteractProjectile: Boolean = true,
        var handleEntitySpawn: Boolean = true,
        var handleEntityGrief: Boolean = true,
        var handlePlayerDamage: Boolean = true,
        var handleFarmlandDamage: Boolean = true
    )

    data class BlockInteractSettings(
        var preventInteractBlockHasContainer: Boolean = true,
        var preventInteractBlockHasZeroState: Boolean = true
    )

    data class GlobalRegionSettings(
        var restrictBlockBreak: Boolean = false,
        var restrictBlockPlace: Boolean = false,
        var restrictFlowEffect: Boolean = false,
        var restrictMobGrief: Boolean = false,
        var restrictDamage: Boolean = false
    )
}
