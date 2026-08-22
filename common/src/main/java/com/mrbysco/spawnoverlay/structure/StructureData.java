package com.mrbysco.spawnoverlay.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StructureData {
	public static ResourceKey<Structure> knownStructure = null;
	public static List<BoundingBox> boundingBoxList = null;

	public static void setKnownStructure(@Nullable ResourceKey<Structure> structure, @Nullable List<BoundingBox> box) {
		knownStructure = structure;
		boundingBoxList = box;
	}

	public static boolean hasKnownStructure() {
		return knownStructure != null && boundingBoxList != null;
	}

	public static boolean isInsideStructure(BlockPos pos) {
		return boundingBoxList != null && boundingBoxList.stream().anyMatch(box -> box.isInside(pos));
	}
}
