package net.shadowmage.ancientwarfare.npc.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.shadowmage.ancientwarfare.core.container.ContainerBase;
import net.shadowmage.ancientwarfare.npc.orders.UpkeepOrder;

public class ContainerUpkeepOrder extends ContainerBase {

    public final UpkeepOrder upkeepOrder;
    private boolean hasChanged;

    public ContainerUpkeepOrder(EntityPlayer player, int x, int y, int z) {
        super(player);
        @Nonnull ItemStack stack = player.getCurrentEquippedItem();
        if (stack.isEmpty() || stack.getItem() == null) {
            throw new IllegalArgumentException("Cannot open Work Order GUI for null stack/item.");
        }
        upkeepOrder = UpkeepOrder.getUpkeepOrder(stack);
        if (upkeepOrder == null) {
            throw new IllegalArgumentException("Upkeep orders was null for some reason");
        }
        addPlayerSlots();
        removeSlots();
    }

    @Override
    public void handlePacketData(NBTTagCompound tag) {
        if (tag.hasKey("upkeepOrder")) {
            upkeepOrder.readFromNBT(tag.getCompoundTag("upkeepOrder"));
            hasChanged = true;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (hasChanged && !player.world.isRemote) {
            upkeepOrder.write(player.getCurrentEquippedItem());
        }
    }

    public void onClose() {
        NBTTagCompound outer = new NBTTagCompound();
        outer.setTag("upkeepOrder", upkeepOrder.writeToNBT(new NBTTagCompound()));
        sendDataToServer(outer);
    }
}
