package net.shadowmage.ancientwarfare.structure.template.plugin.defaultplugins.blockrules;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowmage.ancientwarfare.core.util.BlockTools;
import net.shadowmage.ancientwarfare.core.util.WorldTools;
import net.shadowmage.ancientwarfare.structure.api.IStructureBuilder;
import net.shadowmage.ancientwarfare.structure.api.TemplateParsingException;

import java.util.List;

public class TemplateRuleBanner extends TemplateRuleBlockTile {
	public static final String PLUGIN_NAME = "banner";

	public TemplateRuleBanner(World world, BlockPos pos, IBlockState state, int turns) {
		super(world, pos, state.getBlock() == Blocks.STANDING_BANNER ? BlockTools.rotateFacing(state.withRotation(Rotation.values()[turns % 4]), turns) : state, turns);
	}

	public TemplateRuleBanner(int ruleNumber, List<String> lines) throws TemplateParsingException.TemplateRuleParsingException {
		super(ruleNumber, lines);
	}

	@Override
	protected ItemStack getStack() {
		EnumDyeColor baseColor = EnumDyeColor.byDyeDamage(tag.getInteger("Base"));
		NBTTagList patterns = tag.getTagList("Patterns", 10);

		return ItemBanner.makeBanner(baseColor, patterns);
	}

	@Override
	public void handlePlacement(World world, int turns, BlockPos pos, IStructureBuilder builder) {
		if (state.getBlock() == Blocks.STANDING_BANNER) {
			builder.placeBlock(pos, BlockTools.rotateFacing(state.withRotation(Rotation.values()[turns % 4]), turns), buildPass);
			WorldTools.getTile(world, pos).ifPresent(t -> {
				//TODO look into changing this so that the whole TE doesn't need reloading from custom NBT
				tag.setInteger("x", pos.getX());
				tag.setInteger("y", pos.getY());
				tag.setInteger("z", pos.getZ());
				t.readFromNBT(tag);
			});
		} else {
			super.handlePlacement(world, turns, pos, builder);
		}
	}

	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}
}
