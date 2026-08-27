package com.mrbysco.spawnoverlay;

import com.mrbysco.spawnoverlay.network.SetKnownStructurePayload;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpawnOverlay implements ModInitializer {
	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(SetKnownStructurePayload.ID, SetKnownStructurePayload.CODEC);

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var player : server.getPlayerList().getPlayers())
				playerTick(player);
		});
	}

	private final Map<UUID, StructureCache> structureCache = new HashMap<>();

	private void playerTick(Player player) {
		final Level level = player.level();
		if (!level.isClientSide() && level.getGameTime() % 40 == 0) {
			ServerLevel serverLevel = (ServerLevel) level;
			ServerPlayer serverPlayer = (ServerPlayer) player;

			BlockPos playerPos = player.blockPosition();
			StructureCache cached = structureCache.get(player.getUUID());

			if (cached != null && cached.boundingBoxes().stream().anyMatch(box -> box.inflatedBy(15).isInside(playerPos))) {
				return;
			}

			ResourceKey<Structure> foundStructure = null;
			List<BoundingBox> foundBoxes = null;

			ChunkPos chunkPos = new ChunkPos(playerPos);
			int chunkRadius = 2;
			for (int x = -chunkRadius; x <= chunkRadius; x++) {
				for (int z = -chunkRadius; z <= chunkRadius; z++) {
					BlockPos checkedPos = new BlockPos((chunkPos.x + x) << 4, playerPos.getY(), (chunkPos.z + z) << 4);
					Map<Structure, LongSet> structures = serverLevel.structureManager().getAllStructuresAt(checkedPos);
					for (Map.Entry<Structure, LongSet> entry : structures.entrySet()) {
						Structure structure = entry.getKey();
						StructureStart start = serverLevel.structureManager().getStructureWithPieceAt(checkedPos, structure);
						if (start != StructureStart.INVALID_START && start.getBoundingBox().inflatedBy(15).isInside(playerPos)) {
							foundStructure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getResourceKey(structure).orElse(null);
							List<BoundingBox> boxes = new ArrayList<>();
							start.getPieces().forEach(piece -> boxes.add(piece.getBoundingBox()));
							foundBoxes = boxes;
							break;
						}
					}
				}
			}

			ResourceKey<Structure> cachedStructure = cached != null ? cached.structure() : null;
			boolean changed = foundStructure != cachedStructure;
			if (!changed) return;

			if (foundStructure != null && foundBoxes != null) {
				structureCache.put(player.getUUID(), new StructureCache(foundStructure, foundBoxes));
			} else {
				structureCache.remove(player.getUUID());
			}

			ServerPlayNetworking.send(serverPlayer, new SetKnownStructurePayload(Optional.ofNullable(foundStructure), Optional.ofNullable(foundBoxes)));
		}
	}

	private record StructureCache(ResourceKey<Structure> structure, List<BoundingBox> boundingBoxes) {
	}
}
