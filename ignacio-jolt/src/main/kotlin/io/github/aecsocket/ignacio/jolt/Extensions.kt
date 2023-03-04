package io.github.aecsocket.ignacio.jolt

import io.github.aecsocket.ignacio.core.math.Quat
import io.github.aecsocket.ignacio.core.math.Ray
import io.github.aecsocket.ignacio.core.math.Vec3d
import io.github.aecsocket.ignacio.core.math.Vec3f
import jolt.math.*
import jolt.physics.collision.DRayCast
import java.lang.foreign.MemorySession
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

typealias JQuat = jolt.math.Quat

@OptIn(ExperimentalContracts::class)
fun <R> useArena(block: MemorySession.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return MemorySession.openConfined().use(block)
}

context(MemorySession)
fun FVec3() = FVec3.of(this@MemorySession)
context(MemorySession)
fun Vec3f.toJolt() = FVec3.of(this@MemorySession, x, y, z)
fun FVec3.toIgnacio() = Vec3f(x, y, z)

context(MemorySession)
fun DVec3() = DVec3.of(this@MemorySession)
context(MemorySession)
fun Vec3d.toJolt() = DVec3.of(this@MemorySession, x, y, z)
fun DVec3.toIgnacio() = Vec3d(x, y, z)

context(MemorySession)
fun JQuat() = JQuat.of(this@MemorySession)
context(MemorySession)
fun Quat.toJolt() = JQuat.of(this@MemorySession, x, y, z, w)
fun JQuat.toIgnacio() = Quat(x, y, z, w)

context(MemorySession)
fun Ray.toJolt(distance: Float) = DRayCast.of(this@MemorySession, origin.toJolt(), (direction * distance).toJolt())
