package com.mrbysco.spawnoverlay.network;

import com.mrbysco.spawnoverlay.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.Optional;

public record SetKnownStructurePayload(Optional<ResourceKey<Structure>> structure,
                                       Optional<List<BoundingBox>> boxes) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SetKnownStructurePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.STRUCTURE)),
			SetKnownStructurePayload::structure,
			ByteBufCodecs.optional(BoundingBox.STREAM_CODEC.apply(ByteBufCodecs.list())),
			SetKnownStructurePayload::boxes,
			SetKnownStructurePayload::new
	);
	public static final Type<SetKnownStructurePayload> ID = new Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "set_known_structure"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
