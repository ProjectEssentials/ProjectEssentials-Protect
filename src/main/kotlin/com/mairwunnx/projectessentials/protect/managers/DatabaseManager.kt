package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import com.mairwunnx.projectessentials.protect.entities.RegionTable
import net.minecraftforge.fml.server.ServerLifecycleHooks
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

private fun path() =
    "jdbc:sqlite:/${projectConfigDirectory + '/' + ServerLifecycleHooks.getCurrentServer().folderName}/regions.db"

fun initializeDatabase() {
    File(projectConfigDirectory + File.separator + ServerLifecycleHooks.getCurrentServer().folderName).mkdirs()
    Database.connect(path(), "org.sqlite.JDBC").also {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction { SchemaUtils.create(RegionTable) }
    }
}
