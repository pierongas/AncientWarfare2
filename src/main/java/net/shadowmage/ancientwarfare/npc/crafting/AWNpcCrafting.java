package net.shadowmage.ancientwarfare.npc.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.shadowmage.ancientwarfare.core.api.AWItems;
import net.shadowmage.ancientwarfare.core.crafting.AWCraftingManager;
import net.shadowmage.ancientwarfare.npc.item.AWNpcItemLoader;
import net.shadowmage.ancientwarfare.npc.item.ItemNpcSpawner;


public class AWNpcCrafting {

    /**
     * load any recipes for automation module recipes
     */
    public static void loadRecipes() {
        RecipeSorter.register("ancientwarfare:order_copy", OrderCopyingRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        GameRegistry.addRecipe(new OrderCopyingRecipe(AWItems.upkeepOrder));
        GameRegistry.addRecipe(new OrderCopyingRecipe(AWItems.routingOrder));
        GameRegistry.addRecipe(new OrderCopyingRecipe(AWItems.combatOrder));
        GameRegistry.addRecipe(new OrderCopyingRecipe(AWItems.workOrder));

        //worker spawner
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("worker", ""), "leadership",
                "gf",
                "gt",
                'f', "foodBundle",
                't', Items.WOODEN_PICKAXE,
                'g', "ingotGold");
        //combat spawner
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("combat", ""), "conscription",
                "gf",
                "gt",
                'f', "foodBundle",
                't', Items.WOODEN_SWORD,
                'g', "ingotGold");
        //courier bundle
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("courier", ""), "trade",
                "gf",
                "gt",
                'f', "foodBundle",
                't', Blocks.WOOL,
                'g', "ingotGold");
        //trader spawner
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("trader", ""), "trade",
                "gf_",
                "gtb",
                'f', "foodBundle",
                't', Blocks.WOOL,
                'g', "ingotGold",
                'b', Items.BOOK);
        //priest spawner
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("priest", ""), "leadership",
                "gf",
                "gb",
                'f', "foodBundle",
                'g', "ingotGold",
                'b', Items.BOOK);
        //bard spawner
        AWCraftingManager.INSTANCE.createRecipe(ItemNpcSpawner.getStackForNpcType("bard", ""), "leadership",
                "gf",
                "gb",
                'f', "foodBundle",
                'g', "ingotGold",
                'b', new ItemStack(AWNpcItemLoader.bardInstrument, 1, 0));
    }


    private static class OrderCopyingRecipe implements IRecipe {
        private final Item item;

        private OrderCopyingRecipe(Item item) {
            this.item = item;
        }

        @Override
        public boolean matches(InventoryCrafting var1, World var2) {
            @Nonnull ItemStack order1 = ItemStack.EMPTY, order2 = ItemStack.EMPTY;
            boolean foundOtherStuff = false;
            @Nonnull ItemStack stack;
            for (int i = 0; i < var1.getSizeInventory(); i++) {
                stack = var1.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() == item) {
                    if (order1 == null) {
                        order1 = stack;
                    } else if (order2 == null) {
                        order2 = stack;
                    } else {
                        foundOtherStuff = true;
                        break;
                    }
                } else {
                    foundOtherStuff = true;
                    break;
                }
            }
            return !foundOtherStuff && order1 != null && order2 != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting var1) {
            @Nonnull ItemStack order1 = ItemStack.EMPTY, order2 = ItemStack.EMPTY;
            boolean foundOtherStuff = false;
            @Nonnull ItemStack stack;
            for (int i = 0; i < var1.getSizeInventory(); i++) {
                stack = var1.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() == item) {
                    if (order1 == null) {
                        order1 = stack;
                    } else if (order2 == null) {
                        order2 = stack;
                    } else {
                        foundOtherStuff = true;
                        break;
                    }
                } else {
                    foundOtherStuff = true;
                    break;
                }
            }
            if (foundOtherStuff || order1 == null || order2 == null) {
                return null;
            }
            @Nonnull ItemStack retStack = order2.copy();
            if (order1.stackTagCompound != null) {
                retStack.setTagCompound((NBTTagCompound) order1.stackTagCompound.copy());
            } else {
                retStack.setTagCompound(null);
            }
            retStack.setCount(2);
            return retStack;
        }

        @Override
        public int getRecipeSize() {
            return 2;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return new ItemStack(item);
        }
    }
}
