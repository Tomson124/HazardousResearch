package tomson124.hazardousresearch;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabHR extends CreativeTabs {

    public CreativeTabHR(String label)
    {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(Items.BAKED_POTATO);
    }

    @Override
    public Item getTabIconItem()
    {
        return Items.BAKED_POTATO;
    }
}
