package com.mrbysco.spawnoverlay.network;

import com.mrbysco.spawnoverlay.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.Optional;

public record SetKnownStructurePayload(Optional<ResourceKey<Structure>> structure,
                                       Optional<List<BoundingBox>> boxes) implements CustomPacketPayload {
	public static final StreamCodec<ByteBuf, BoundingBox> BBOX_STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			box -> new BlockPos(box.minX(), box.minY(), box.minZ()),
			BlockPos.STREAM_CODEC,
			box -> new BlockPos(box.maxX(), box.maxY(), box.maxZ()),
			(min, max) -> new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ())
	);

	public static final StreamCodec<FriendlyByteBuf, SetKnownStructurePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.STRUCTURE)),
			SetKnownStructurePayload::structure,
			ByteBufCodecs.optional(BBOX_STREAM_CODEC.apply(ByteBufCodecs.list())),
			SetKnownStructurePayload::boxes,
			SetKnownStructurePayload::new
	);
	public static final Type<SetKnownStructurePayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "set_known_structure"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
