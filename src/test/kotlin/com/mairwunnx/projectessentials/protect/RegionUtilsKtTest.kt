package com.mairwunnx.projectessentials.protect

import com.winterbe.expekt.should
import net.minecraft.dispenser.Position
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class RegionUtilsKtTest {
    @Test
    fun `regions is intersects`() {
        measureTimeMillis {
            val innerMin = Position(30.0, 70.0, 30.0)
            val innerMax = Position(60.0, 80.0, 70.0)
            val outerMin = Position(0.0, 70.0, 0.0)
            val outerMax = Position(50.0, 75.0, 44.0)
            regionsIsIntersect(outerMax, outerMin, innerMax, innerMin).should.be.`true`
        }.also { println("`regions is intersects`: elapsed time ${it}ms") }
    }

    @Test
    fun `regions is not intersects`() {
        measureTimeMillis {
            val innerMin = Position(30.0, 70.0, 30.0)
            val innerMax = Position(60.0, 80.0, 70.0)
            val outerMin = Position(-30.0, 70.0, -30.0)
            val outerMax = Position(0.0, 70.0, 0.0)
            regionsIsIntersect(outerMax, outerMin, innerMax, innerMin).should.be.`false`
        }.also { println("`regions is not intersects`: elapsed time ${it}ms") }
    }

    @Test
    fun `participants string as map`() {
        measureTimeMillis {
            val input = "[MairwunNx:owner, JopaBibBib:member, Heldens:member]"
            val out = mapOf("MairwunNx" to "owner", "JopaBibBib" to "member", "Heldens" to "member")
            participantsAsMap(input).should.be.equal(out)
        }.also { println("`participants string as map`: elapsed time ${it}ms") }
    }

    @Test
    fun `participant string as map`() {
        measureTimeMillis {
            val input = "[MairwunNx:owner]"
            val out = mapOf("MairwunNx" to "owner")
            participantsAsMap(input).should.be.equal(out)
        }.also { println("`participant string as map`: elapsed time ${it}ms") }
    }

    @Test
    fun `flags string as set`() {
        measureTimeMillis {
            val input = "restrict-jump,restrict-block-breaking, restrict-mob-griefing"
            val out = setOf("restrict-jump", "restrict-block-breaking", "restrict-mob-griefing")
            flagsAsSet(input).should.equal(out)
        }.also { println("`flags string as set`: elapsed time ${it}ms") }
    }

    @Test
    fun `flag string as set`() {
        measureTimeMillis {
            flagsAsSet("restrict-jump").should.equal(setOf("restrict-jump"))
        }.also { println("`flag string as set`: elapsed time ${it}ms") }
    }
}
