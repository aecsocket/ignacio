package io.github.aecsocket.ignacio.paper.util

import io.github.aecsocket.ignacio.core.math.Vec3d
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

fun Location.position() = Vec3d(x, y, z)
fun Vec3d.location(world: World, pitch: Float = 0f, yaw: Float = 0f) = Location(world, x, y, z, yaw, pitch)

fun Vector.vec3d() = Vec3d(x, y, z)
fun Vec3d.vector() = Vector(x, y, z)
