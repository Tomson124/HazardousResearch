package tomson124.hazardousresearch.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tomson124.hazardousresearch.HazardousResearch;
import tomson124.hazardousresearch.block.BlockBase;
import tomson124.hazardousresearch.block.BlockOre;

public class ModBlocks {

    public static BlockOre oreChlorine;

    public static void init() {

        oreChlorine = register(new BlockOre("oreChlorine").setCreativeTab(HazardousResearch.TAB));
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockBase) {
            ((BlockBase)block).registerItemModel(itemBlock);
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }
}
