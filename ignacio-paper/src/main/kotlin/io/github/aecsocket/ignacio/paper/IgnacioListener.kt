package io.github.aecsocket.ignacio.paper

import io.papermc.paper.event.player.PlayerTrackEntityEvent
import io.papermc.paper.event.player.PlayerUntrackEntityEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLocaleChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent

internal class IgnacioListener(private val ignacio: Ignacio) : Listener {
    @EventHandler
    fun on(event: PlayerQuitEvent) {
        ignacio.removePlayerData(event.player)
    }

    @EventHandler
    fun on(event: PlayerLocaleChangeEvent) {
        ignacio.playerData(event.player).updateMessages(event.player.locale())
    }

    @EventHandler
    fun on(event: PlayerTrackEntityEvent) {
        ignacio.primitiveBodies.track(event.player, event.entity)
    }

    @EventHandler
    fun on(event: PlayerUntrackEntityEvent) {
        ignacio.primitiveBodies.untrack(event.player, event.entity)
    }

    @EventHandler
    fun on(event: ChunkLoadEvent) {
        val world = ignacio.physicsInOr(event.world) ?: return
        world.load(event.chunk)
    }

    @EventHandler
    fun on(event: ChunkUnloadEvent) {
        val world = ignacio.physicsInOr(event.world) ?: return
        world.unload(event.chunk)
    }
}