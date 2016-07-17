package tomson124.hazardousresearch;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tomson124.hazardousresearch.config.HazardousResearchCoreConfig;
import tomson124.hazardousresearch.init.ModBlocks;
import tomson124.hazardousresearch.proxy.CommonProxy;

@Mod(modid = HazardousResearch.MOD_ID, name = HazardousResearch.NAME, version = HazardousResearch.VERSION)

public class HazardousResearch {

    public static final String MOD_ID = "hazardousresearch";
    public static final String NAME = "Hazardous Research";
    public static final String VERSION = "0.0.0.1";

    @Mod.Instance(MOD_ID)
    public static HazardousResearch instance;

    public static HazardousResearchCoreConfig config;

    @SidedProxy(serverSide = "tomson124.hazardousresearch.proxy.CommonProxy", clientSide = "tomson124.hazardousresearch.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs TAB = new CreativeTabHR(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        config = HazardousResearchCoreConfig.initialize(event.getSuggestedConfigurationFile());

        ModBlocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
