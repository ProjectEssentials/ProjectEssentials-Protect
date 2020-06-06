package com.mairwunnx.projectessentials.protect

import com.winterbe.expekt.should
import com.mairwunnx.projectessentials.protect.structs.Position
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class RegionUtilsKtTest {
    @Test
    fun `regions is intersects`() {
        var result = false
        measureTimeMillis {
            val innerMin = Position(30, 70, 30)
            val innerMax = Position(60, 80, 70)
            val outerMin = Position(0, 70, 0)
            val outerMax = Position(50, 75, 44)
            result = regionsIsIntersect(outerMax, outerMin, innerMax, innerMin)
        }.also { println("`regions is intersects`: elapsed time ${it}ms") }
        result.should.be.`true`
    }

    @Test
    fun `regions is not intersects`() {
        var result = true
        measureTimeMillis {
            val innerMin = Position(30, 70, 30)
            val innerMax = Position(60, 80, 70)
            val outerMin = Position(-30, 70, -30)
            val outerMax = Position(0, 70, 0)
            result = regionsIsIntersect(outerMax, outerMin, innerMax, innerMin)
        }.also { println("`regions is not intersects`: elapsed time ${it}ms") }
        result.should.be.`false`
    }

    @Test
    fun `participants string as map`() {
        var result = mapOf<String, String>()
        val input = "[MairwunNx:owner, JopaBibBib:member, Heldens:member]"
        val out = mapOf("MairwunNx" to "owner", "JopaBibBib" to "member", "Heldens" to "member")
        measureTimeMillis {
            result = participantsAsMap(input)
        }.also { println("`participants string as map`: elapsed time ${it}ms") }
        result.should.be.equal(out)
    }

    @Test
    fun `participant string as map`() {
        val input = "[MairwunNx:owner]"
        val out = mapOf("MairwunNx" to "owner")
        var result = emptyMap<String, String>()
        measureTimeMillis {
            result = participantsAsMap(input)
        }.also { println("`participant string as map`: elapsed time ${it}ms") }
        result.should.be.equal(out)
    }

    @Test
    fun `flags string as set`() {
        val input = "restrict-jump,restrict-block-breaking, restrict-mob-griefing"
        val out = setOf("restrict-jump", "restrict-block-breaking", "restrict-mob-griefing")
        var result = setOf<String>()
        measureTimeMillis {
            result = flagsAsSet(input)
        }.also { println("`flags string as set`: elapsed time ${it}ms") }
        result.should.equal(out)
    }

    @Test
    fun `flag string as set`() {
        var result = setOf<String>()
        measureTimeMillis {
            result = flagsAsSet("restrict-jump")
        }.also { println("`flag string as set`: elapsed time ${it}ms") }
        result.should.equal(setOf("restrict-jump"))
    }
}
