package solitudetraveler.chemcraftmod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import solitudetraveler.chemcraftmod.block.BlockList;
import solitudetraveler.chemcraftmod.block.BlockVariables;
import solitudetraveler.chemcraftmod.handler.DeconstructorRecipeHandler;
import solitudetraveler.chemcraftmod.tileentity.DeconstructorTileEntity;

import javax.annotation.Nonnull;

public class DeconstructorContainer extends Container {
    public DeconstructorTileEntity tileEntity;

    private IItemHandler playerInventory;

    public DeconstructorContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(BlockVariables.DECONSTRUCTOR_CONTAINER, id);

        tileEntity = (DeconstructorTileEntity) world.getTileEntity(pos);
        playerInventory = new InvWrapper(playerInv);

        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_INPUT, 44, 35);

        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_1, 98, 17);
        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_2, 98, 35);
        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_3, 98, 53);
        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_4, 116, 17);
        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_5, 116, 35);
        addDeconstructorSlot(DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_6, 116, 53);
        layoutPlayerInventorySlots(8, 84);
    }

    private void addDeconstructorSlot(int slotID, int xPos, int yPos) {
        if(slotID == DeconstructorTileEntity.DECONSTRUCTOR_INPUT) {
            addSlot(new Slot(tileEntity, slotID, xPos, yPos) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return DeconstructorRecipeHandler.getRecipeForInputItem(stack.getItem()) != null;
                }

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }
            });
        }
        else {
            addSlot(new Slot(tileEntity, slotID, xPos, yPos) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return false;
                }
            });
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack previousStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        // If slot contains some itemstack
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            previousStack = stackInSlot.copy();

            // If clicked slot is in deconstructor
            if(index < DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS) {
                // Container to inventory
                if (!this.mergeItemStack(stackInSlot, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Inventory to container
                if(!this.mergeItemStack(stackInSlot, DeconstructorTileEntity.DECONSTRUCTOR_INPUT, DeconstructorTileEntity.DECONSTRUCTOR_INPUT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if(stackInSlot.getCount() == previousStack.getCount()) return ItemStack.EMPTY;
        }

        return previousStack;
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, BlockList.deconstructor);
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hot bar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
}
