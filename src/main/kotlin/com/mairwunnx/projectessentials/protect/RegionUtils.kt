package com.mairwunnx.projectessentials.protect

import com.mairwunnx.projectessentials.core.api.v1.extensions.empty
import com.mairwunnx.projectessentials.protect.structs.Position

/**
 * @return true if regions intersect, or outer object positions
 * intersects with inner region.
 * @since 1.0.0.
 */
fun regionsIsIntersect(
    outerMax: Position, outerMin: Position, innerMax: Position, innerMin: Position
): Boolean {
    return outerMax.x > innerMin.x && outerMin.x < innerMax.x && outerMax.y > innerMin.y && outerMin.y < innerMax.y && outerMax.z > innerMin.z && outerMin.z < innerMax.z
}

/**
 * @param participants input string for parsing.
 * @return participants as map, from string `[MairwunNx:owner]`.
 * Where key is user name, value participants group (owner, member).
 * @since 1.0.0.
 */
fun participantsAsMap(participants: String) = with(participants) {
    replace(Regex("\\[|\\]"), String.empty).split(',').map {
        it.trim().split(":").let { pair -> pair[0] to pair[1] }
    }.toMap()
}

/**
 * @param flags input string for parsing string
 * with format `flag1,test-flag-1`
 * @return set of flags represented as strings.
 * @since 1.0.0.
 */
fun flagsAsSet(flags: String) = flags.split(",").map { it.trim() }.toSet()
