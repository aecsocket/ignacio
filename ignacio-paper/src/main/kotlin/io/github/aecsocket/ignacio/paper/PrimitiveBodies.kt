package io.github.aecsocket.ignacio.paper

import io.github.aecsocket.ignacio.core.PhysicsBody
import io.github.aecsocket.ignacio.core.PhysicsSpace
import io.github.aecsocket.ignacio.core.bodies
import io.github.aecsocket.ignacio.core.math.Transform
import io.github.aecsocket.ignacio.paper.display.*
import io.github.aecsocket.ignacio.paper.util.location
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent

class PrimitiveBodies internal constructor(private val ignacio: Ignacio) {
    private data class Instance(
        val physics: PhysicsSpace,
        val body: PhysicsBody,
        val render: WorldRender?,
    )

    private val bodies = HashMap<Entity, Instance>()

    fun create(
        world: World,
        transform: Transform,
        addBody: (physics: PhysicsSpace) -> PhysicsBody,
        createRender: ((playerTracker: PlayerTracker) -> WorldRender)?,
    ) {
        world.spawnEntity(
            transform.position.location(world), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.COMMAND
        ) { entity ->
            entity as ArmorStand
            entity.isVisible = false
            entity.isMarker = true
            entity.isPersistent = false
            entity.setCanTick(false)

            val (physics) = ignacio.worlds.getOrCreate(world)
            val body = addBody(physics)
            val render = createRender?.invoke(entity.playerTracker())
            bodies[entity] = Instance(physics, body, render)
        }
    }

    internal fun update() {
        bodies.toMap().forEach { (entity, instance) ->
            fun destroy() {
                entity.remove()
                instance.physics.bodies {
                    remove(instance.body)
                    destroy(instance.body)
                }
                instance.render?.despawn()
                bodies.remove(entity)
            }

            if (!entity.isValid || !instance.body.isValid) {
                destroy()
                return@forEach
            }

            instance.body.read { body ->
                val transform = body.transform
                entity.teleport(transform.position.location(entity.world))
                instance.render?.transform = transform
            }
        }
    }

    internal fun track(player: Player, entity: Entity) {
        val body = bodies[entity] ?: return
        body.render?.spawn(player)
    }

    internal fun untrack(player: Player, entity: Entity) {
        val body = bodies[entity] ?: return
        body.render?.despawn(player)
    }

    fun numBodies() = bodies.size

    fun removeAll() {
        bodies.forEach { (entity, instance) ->
            instance.physics.bodies {
                remove(instance.body)
                destroy(instance.body)
            }
            instance.render?.despawn()
            entity.remove()
        }
        bodies.clear()
    }
}
