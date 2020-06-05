import com.mairwunnx.projectessentials.protect.entities.RegionEntity
import com.mairwunnx.projectessentials.protect.entities.RegionTable
import com.soywiz.kds.CacheMap
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.sql.Connection
import kotlin.system.measureTimeMillis

private val regionNameCache = CacheMap<String, RegionEntity>(64)

private fun initializeConnection() {
    File("data/").mkdirs()
    Database.connect("jdbc:sqlite:./data/regions.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE.also { transaction { SchemaUtils.create(RegionTable) } }
}

private fun addToCache(entity: RegionEntity) {
    synchronized(regionNameCache) { regionNameCache.put(entity.name, entity) }
}

class RegionCachingTests {
    companion object {
        @BeforeAll
        @JvmStatic
        fun init() {
            initializeConnection()
            transaction { RegionEntity.all().forEach { it.delete() } }
            transaction {
                repeat(2000) {
                    RegionEntity.new {
                        name = "GeneratedRegion$it"
                        creator =
                            if (it <= 50) "MairwunNx" else if (it <= 100) "WhoIs" else "Server"
                        dimension = 0
                        minX = 169 + it.also { minY = 169 - it }.also { minZ = 169 * it }
                        maxX = 169 + it.also { maxY = 169 - it }.also { maxZ = 169 * it }
                    }
                }
            }
        }
    }

    @Test
    fun getRegionByNameCached() {
        measureTimeMillis {
            for (i in 0..2000) {
                repeat(3) {
                    val name = "GeneratedRegion$i"
                    regionNameCache[name] ?: transaction {
                        RegionEntity.all().asSequence().find { region ->
                            region.name == name
                        }?.let { v -> addToCache(v) } ?: println("Region $name does not exists.")
                    }
                }
            }
        }.also { println("Elapsed time with caching for finding 2000 regions is ${it}ms") }
    }

    @Test
    fun getRegionByNameNotCached() {
        measureTimeMillis {
            for (i in 0..2000) {
                repeat(3) {
                    val name = "GeneratedRegion$i"
                    transaction {
                        RegionEntity.all().asSequence().find { region ->
                            region.name == name
                        } ?: println("Region $name does not exists.")
                    }
                }
            }
        }.also { println("Elapsed time without caching for finding 2000 regions is ${it}ms") }
    }
}

