package tomson124.hazardousresearch.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tomson124.hazardousresearch.HazardousResearch;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel() {
        HazardousResearch.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
