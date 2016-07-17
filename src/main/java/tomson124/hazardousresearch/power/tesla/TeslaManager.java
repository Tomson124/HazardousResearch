package tomson124.hazardousresearch.power.tesla;

import net.minecraftforge.fml.common.Loader;
import tomson124.hazardousresearch.api.power.IPowerConfig;

public class TeslaManager {

    public static ITeslaPowerManager manager;

    private static boolean isTeslaEnabled = false;

    public static void load(){
        isTeslaEnabled = Loader.isModLoaded("Tesla");
        if(isTeslaEnabled){
            manager = TeslaPowerManager.getPowerManager();
        }
    }

    public static boolean isTeslaEnabled(IPowerConfig config){
        return isTeslaEnabled && config.tesla() && manager != null;
    }

}
