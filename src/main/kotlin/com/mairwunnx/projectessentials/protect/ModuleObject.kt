@file:Suppress("unused")

package com.mairwunnx.projectessentials.protect

import com.mairwunnx.projectessentials.core.api.v1.extensions.commandName
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.core.api.v1.providers.ProviderAPI
import com.mairwunnx.projectessentials.protect.managers.initializeCache
import com.mairwunnx.projectessentials.protect.managers.initializeDatabase
import com.sk89q.worldedit.LocalSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.math.BlockVector3
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.CommandEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("project_essentials_protect")
class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 11

    override fun init() {
        initializeDatabase()
        initializeCache()
    }

    init {
        EVENT_BUS.register(this)
        initProviders()
        initLocalization()
    }

    private fun initProviders() {
        listOf(ModuleObject::class.java).forEach(ProviderAPI::addProvider)
    }

    private fun initLocalization() {
//        LocalizationAPI.apply(this.javaClass) {
//            mutableListOf()
//        }
    }

    @SubscribeEvent
    fun onPlayerCommand(event: CommandEvent) {
        if (event.isPlayerSender()) {
            if (event.commandName == "test1") {
                WorldEdit.getInstance().sessionManager.findByName(
                    event.getPlayer()!!.uniqueID.toString()
                ).also {
                    if (it == null) {
                        WorldEdit.getInstance().sessionManager.findByName(
                            event.getPlayer()!!.name.string
                        ).also {
                            if (it == null) {
                                LogManager.getLogger().error("failed")
                            } else test(it)
                        }
                    } else {
                        test(it)
                    }
                }
            }
        }
    }

    lateinit var max: BlockVector3
    lateinit var min: BlockVector3
    val logger = LogManager.getLogger()

    @SubscribeEvent
    public fun onBlockBreak(event: BlockEvent.BreakEvent) {
        BlockVector3.at(event.pos.x, event.pos.y, event.pos.z).containedWithin(
            min, max
        ).also { event.isCanceled = it }
    }


    private fun test(session: LocalSession) {
        val selector = session.getRegionSelector(session.selectionWorld)
        logger.info("Selector typeName ${selector.typeName}")
        if (selector.typeName == "cuboid") {
            if (selector.isDefined) {
                val region = selector.region
                logger.info("Completed region")

                logger.info("Min: ${region.minimumPoint}")
                logger.info("Max: ${region.maximumPoint}")

                max = region.maximumPoint
                min = region.minimumPoint
            } else {
                logger.info("Incompleted region")
            }
        } else {
        }


//        LogManager.getLogger().info(session.getSelection(session.selectionWorld).area)
    }
}
