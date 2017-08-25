package net.shadowmage.ancientwarfare.automation.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class ContainerWorksiteQuarry extends ContainerWorksiteBase {

    public ContainerWorksiteQuarry(EntityPlayer player, BlockPos pos) {
        super(player, pos);

        int layerY = 8;
        int labelGap = 12;
        topLabel = layerY;
        layerY += labelGap;

        layerY = addSlots(8, layerY, 0, 27) + 4;
        playerLabel = layerY;
        layerY += labelGap;
        guiHeight = addPlayerSlots(layerY) + 8;
    }

}
