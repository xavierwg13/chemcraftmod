package solitudetraveler.chemcraftmod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import solitudetraveler.chemcraftmod.block.BlockList;
import solitudetraveler.chemcraftmod.tileentity.DeconstructorTileEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DeconstructorContainer extends Container {

    TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    public DeconstructorContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(BlockList.DECONSTRUCTOR_CONTAINER, id);

        tileEntity = world.getTileEntity(pos);
        playerEntity = player;
        playerInventory = new InvWrapper(playerInv);

        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_INPUT, 44, 35));

                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_1, 98, 17));
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_2, 98, 35));
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_3, 98, 53));
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_4, 116, 17));
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_5, 116, 35));
                addSlot(new SlotItemHandler(h, DeconstructorTileEntity.DECONSTRUCTOR_OUTPUT_6, 116, 53));
            });
        }
        layoutPlayerInventorySlots(8, 84);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(Objects.requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerEntity, BlockList.deconstructor);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(index == 0) {
                if(!this.mergeItemStack(stack, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if(index < DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 27) {
                    if(!this.mergeItemStack(stack, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 27, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(index < DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 36 && !this.mergeItemStack(stack, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS, DeconstructorTileEntity.NUMBER_DECONSTRUCTOR_SLOTS + 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if(stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if(stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack);
        }
        return itemStack;
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
