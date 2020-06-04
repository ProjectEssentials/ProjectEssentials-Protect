import org.junit.jupiter.api.Test

data class MutablePos3(var x: Int, var y: Int, var z: Int)

class RegionMatchingTests {
    @Test
    fun `match simple inner region`() {
        val innerRegionMin = MutablePos3(30, 70, 30)
        val innerRegionMax = MutablePos3(60, 80, 70)
        val outerRegionMin = MutablePos3(0, 70, 0)
        val outerRegionMax = MutablePos3(50, 75, 44)

        // @formatter:off
        val innerPresent =
            outerRegionMax.x > innerRegionMin.x && outerRegionMin.x < innerRegionMax.x &&
            outerRegionMax.y > innerRegionMin.y && outerRegionMin.y < innerRegionMax.y &&
            outerRegionMax.z > innerRegionMin.z && outerRegionMin.z < innerRegionMax.z
        println(innerPresent)
        assert(innerPresent)
        // @formatter:on
    }

    @Test
    fun `no match simple inner region`() {
        val innerRegionMin = MutablePos3(30, 70, 30)
        val innerRegionMax = MutablePos3(60, 80, 70)
        val outerRegionMin = MutablePos3(-30, 70, -30)
        val outerRegionMax = MutablePos3(0, 70, 0)

        // @formatter:off
        val innerPresent =
            outerRegionMax.x > innerRegionMin.x && outerRegionMin.x < innerRegionMax.x &&
            outerRegionMax.y > innerRegionMin.y && outerRegionMin.y < innerRegionMin.y &&
            outerRegionMax.z > innerRegionMin.z && outerRegionMin.z < innerRegionMax.z

        assert(!innerPresent)
        // @formatter:on
    }

//    @Test
//    suspend fun `match inner regions`() {
//        forAll(
//            row(MutablePos3(0, 60, 13), MutablePos3(100, 70, 90)),
//            row(MutablePos3(1647, 60, 13), MutablePos3(9713, 70, 90)),
//            row(MutablePos3(-130, 75, 13), MutablePos3(6, 85, 90))
//        ) { min, max ->
//
//
//
//            assert(true)
//        }
//    }
}


