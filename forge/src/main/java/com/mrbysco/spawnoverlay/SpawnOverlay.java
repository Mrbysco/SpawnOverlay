package com.mrbysco.spawnoverlay;

import com.mojang.logging.LogUtils;
import com.mrbysco.spawnoverlay.network.PayloadHandler;
import com.mrbysco.spawnoverlay.network.SetKnownStructurePayload;
import it.unimi.dsi.fastutil.longs.LongSet;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod(value = SpawnOverlay.MOD_ID)
public class SpawnOverlay {
	public static final String MOD_ID = "spawnoverlay";
	public static final Logger LOGGER = LogUtils.getLogger();

	public SpawnOverlay(IEventBus eventBus) {
		NeoForge.EVENT_BUS.addListener(this::playerTick);
		eventBus.addListener(this::setupPackets);
	}

	private final Map<UUID, StructureCache> structureCache = new HashMap<>();

	private void playerTick(PlayerTickEvent.Post event) {
		final Player player = event.getEntity();
		final Level level = player.level();
		if (!level.isClientSide() && level.getGameTime() % 40 == 0) {
			ServerLevel serverLevel = (ServerLevel) level;
			ServerPlayer serverPlayer = (ServerPlayer) player;
			if (!serverPlayer.connection.hasChannel(SetKnownStructurePayload.ID)) return;

			BlockPos playerPos = player.blockPosition();
			StructureCache cached = structureCache.get(player.getUUID());

			if (cached != null && cached.boundingBoxes().stream().anyMatch(box -> box.inflatedBy(15).isInside(playerPos))) {
				return;
			}

			ResourceKey<Structure> foundStructure = null;
			List<BoundingBox> foundBoxes = null;

			ChunkPos chunkPos = ChunkPos.containing(playerPos);
			int chunkRadius = 2;
			for (int x = -chunkRadius; x <= chunkRadius; x++) {
				for (int z = -chunkRadius; z <= chunkRadius; z++) {
					BlockPos checkedPos = new BlockPos((chunkPos.x() + x) << 4, playerPos.getY(), (chunkPos.z() + z) << 4);
					Map<Structure, LongSet> structures = serverLevel.structureManager().getAllStructuresAt(checkedPos);
					for (Map.Entry<Structure, LongSet> entry : structures.entrySet()) {
						Structure structure = entry.getKey();
						StructureStart start = serverLevel.structureManager().getStructureWithPieceAt(checkedPos, structure);
						if (start != StructureStart.INVALID_START && start.getBoundingBox().inflatedBy(15).isInside(playerPos)) {
							foundStructure = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getResourceKey(structure).orElse(null);
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

			PacketDistributor.sendToPlayer(serverPlayer, new SetKnownStructurePayload(Optional.ofNullable(foundStructure), Optional.ofNullable(foundBoxes)));
		}
	}

	private void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MOD_ID).optional();
		registrar.playToClient(SetKnownStructurePayload.ID, SetKnownStructurePayload.CODEC, PayloadHandler.getInstance()::handleStructureData);
	}

	private record StructureCache(ResourceKey<Structure> structure, List<BoundingBox> boundingBoxes) {
	}
}
