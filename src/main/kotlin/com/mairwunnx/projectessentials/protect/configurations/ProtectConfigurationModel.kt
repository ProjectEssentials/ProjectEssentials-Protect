package com.mairwunnx.projectessentials.protect.configurations

data class ProtectConfigurationModel(
    val generalSettings: GeneralSettings = GeneralSettings(),
    val blockInteractSettings: BlockInteractSettings = BlockInteractSettings(),
    val globalRegionSettings: GlobalRegionSettings = GlobalRegionSettings(),
    val experimentalSettings: ExperimentalSettings = ExperimentalSettings()
) {
    data class GeneralSettings(
        var handleBlockBreak: Boolean = true,
        var handleBlockPlace: Boolean = true,
        var handleBlockInteract: Boolean = true,
        var handleEntityInteract: Boolean = true,
        var handleEntityInteractProjectile: Boolean = true
    )

    data class BlockInteractSettings(
        var preventInteractBlockHasContainer: Boolean = true,
        var preventInteractBlockHasZeroState: Boolean = false
    )

    data class GlobalRegionSettings(
        var restrictBlockBreak: Boolean = false,
        var restrictBlockPlace: Boolean = false,
        var restrictFlowEffect: Boolean = false,
        var restrictFallDamage: Boolean = false,
        var restrictMobGriefing: Boolean = false
    )

    data class ExperimentalSettings(
        val _comment: String = "This experimental settings, change values this can cause unstable server work!",
        val generalExSettings: GeneralExSettings = GeneralExSettings(),
        val globalRegionExSettings: GlobalRegionExSettings = GlobalRegionExSettings(),
        val flagRegionExSettings: FlagRegionExSettings = FlagRegionExSettings()
    ) {
        data class GeneralExSettings(val handleFireSpread: Boolean = false)
        data class GlobalRegionExSettings(val restrictFireSpread: Boolean = false)
        data class FlagRegionExSettings(val enableRestrictFireSpreadFlag: Boolean = false)
    }
}
