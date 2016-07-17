package tomson124.hazardousresearch.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import tomson124.hazardousresearch.HazardousResearch;

public class BlockBase extends Block {

    protected String name;

    public BlockBase(Material material, String name) {
        super(material);

        this.name = name;

        setUnlocalizedName(HazardousResearch.MOD_ID + "." + name);
        setRegistryName(name);
    }

    public void registerItemModel(ItemBlock itemBlock) {
        HazardousResearch.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
