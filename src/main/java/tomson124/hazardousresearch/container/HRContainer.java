package tomson124.hazardousresearch.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tomson124.hazardousresearch.container.slot.SlotBase;
import tomson124.hazardousresearch.container.slot.SlotFake;
import tomson124.hazardousresearch.api.tile.IContainerLayout;

import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class HRContainer extends Container
{

    public HashMap<Integer, SlotBase> slotMap = new HashMap<>();

    @Override
    protected Slot addSlotToContainer(Slot slotIn) {
        Slot slot = super.addSlotToContainer(slotIn);
        if(slot instanceof SlotBase){
            //TODO remove player slots
            slotMap.put(slot.getSlotIndex(), (SlotBase) slot);
        }
        return slot;
    }

    private static HashMap<String, HRContainer> containerMap = new HashMap<>();

    public static @Nullable HRContainer getContainerFromClass(Class<? extends HRContainer> clazz, TileEntity tileEntity){
        if(containerMap.containsKey(clazz.getCanonicalName())){
            return containerMap.get(clazz.getCanonicalName());
        } else {
            try {
                //TODO think hard about how to fix this one
                HRContainer container = clazz.newInstance();
                if(container instanceof IContainerLayout){
                    ((IContainerLayout) container).setTile(tileEntity);
                    ((IContainerLayout) container).addInventorySlots();
                }
                containerMap.put(clazz.getCanonicalName(), container);
                return container;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static HRContainer createContainer(Class<? extends HRContainer> clazz, TileEntity tile, EntityPlayer player){
        try {
            HRContainer container = clazz.newInstance();
            if(container instanceof IContainerLayout){
                ((IContainerLayout) container).setPlayer(player);
                ((IContainerLayout) container).setTile(tile);
                ((IContainerLayout) container).addInventorySlots();
                ((IContainerLayout) container).addPlayerSlots();
            }
            return container;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        ItemStack originalStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        int numSlots = inventorySlots.size();
        if (slot != null && slot.getHasStack())
        {
            ItemStack stackInSlot = slot.getStack();
            originalStack = stackInSlot.copy();
            if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots))
            {
                // NOOP
            } else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9)
            {
                if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots))
                {
                    return null;
                }
            } else if (slotIndex >= numSlots - 9 && slotIndex < numSlots)
            {
                if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9))
                {
                    return null;
                }
            } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots))
            {
                return null;
            }
            slot.onSlotChange(stackInSlot, originalStack);
            if (stackInSlot.stackSize <= 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
            if (stackInSlot.stackSize == originalStack.stackSize)
            {
                return null;
            }
            slot.onPickupFromSlot(player, stackInSlot);
        }
        return originalStack;
    }

    protected boolean shiftItemStack(ItemStack stackToShift, int start, int end)
    {
        boolean changed = false;
        if (stackToShift.isStackable())
        {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++)
            {
                Slot slot = (Slot) inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot != null && canStacksMerge(stackInSlot, stackToShift))
                {
                    int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    if (resultingStackSize <= max)
                    {
                        stackToShift.stackSize = 0;
                        stackInSlot.stackSize = resultingStackSize;
                        slot.onSlotChanged();
                        changed = true;
                    } else if (stackInSlot.stackSize < max)
                    {
                        stackToShift.stackSize -= max - stackInSlot.stackSize;
                        stackInSlot.stackSize = max;
                        slot.onSlotChanged();
                        changed = true;
                    }
                }
            }
        }
        if (stackToShift.stackSize > 0)
        {
            for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++)
            {
                Slot slot = (Slot) inventorySlots.get(slotIndex);
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot == null)
                {
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                    stackInSlot = stackToShift.copy();
                    stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
                    stackToShift.stackSize -= stackInSlot.stackSize;
                    slot.putStack(stackInSlot);
                    slot.onSlotChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean tryShiftItem(ItemStack stackToShift, int numSlots)
    {
        for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++)
        {
            Slot slot = (Slot) inventorySlots.get(machineIndex);
            if (slot instanceof SlotFake)
            {
                continue;
            }
            if (!slot.isItemValid(stackToShift))
                continue;
            if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1))
                return true;
        }
        return false;
    }

    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2)
    {
        if (stack1 == null || stack2 == null)
        {
            return false;
        }
        if (!stack1.isItemEqual(stack2))
        {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2))
        {
            return false;
        }
        return true;

    }
}
