import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

data class MutablePos3(var x: Int, var y: Int, var z: Int)

class RegionMatchingTests {
    @Test
    fun `match simple inner region`() {
        val innerMin = MutablePos3(30, 70, 30)
        val innerMax = MutablePos3(60, 80, 70)
        val outerMin = MutablePos3(0, 70, 0)
        val outerMax = MutablePos3(50, 75, 44)
        assert(regionsIsIntersect(outerMax, outerMin, innerMax, innerMin))
    }

    @Test
    fun `no match simple inner region`() {
        val innerMin = MutablePos3(30, 70, 30)
        val innerMax = MutablePos3(60, 80, 70)
        val outerMin = MutablePos3(-30, 70, -30)
        val outerMax = MutablePos3(0, 70, 0)
        shouldNotBe(regionsIsIntersect(outerMax, outerMin, innerMax, innerMin))
    }

    private fun regionsIsIntersect(
        outerRegionMax: MutablePos3,
        outerRegionMin: MutablePos3,
        innerRegionMax: MutablePos3,
        innerRegionMin: MutablePos3
    ) = outerRegionMax.x > innerRegionMin.x && outerRegionMin.x < innerRegionMax.x &&
            outerRegionMax.y > innerRegionMin.y && outerRegionMin.y < innerRegionMax.y &&
            outerRegionMax.z > innerRegionMin.z && outerRegionMin.z < innerRegionMax.z
}


