package com.mairwunnx.projectessentials.protect.managers

import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import net.minecraftforge.fml.server.ServerLifecycleHooks
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

val dbPath by lazy {
    val config = projectConfigDirectory.replace("\\", "/")
    "jdbc:sqlite${config + '/' + ServerLifecycleHooks.getCurrentServer().folderName}/regions.db"
}

fun initializeDatabase() {
    Database.connect(dbPath, "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    initializeTable()
}

private fun initializeTable() {

}
