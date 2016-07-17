package tomson124.hazardousresearch.power;

import tomson124.hazardousresearch.config.HazardousResearchCoreConfig;
import tomson124.hazardousresearch.power.tesla.TeslaManager;

public class PowerSystem
{
    public static String getLocaliszedPower(double eu)
    {
        return getLocaliszedPower((int) eu);
    }

    public static String getLocaliszedPower(int eu)
    {
        if (HazardousResearchCoreConfig.getHazardousResearchPower().eu())
        {
            return getRoundedString(eu, "EU");
        } else if (TeslaManager.isTeslaEnabled(HazardousResearchCoreConfig.getHazardousResearchPower()))
        {
            return TeslaManager.manager.getDisplayableTeslaCount(eu);
        }else
        {
            return getRoundedString(eu / HazardousResearchCoreConfig.euPerRF, "RF");
        }
    }

    private static String getRoundedString(double euValue, String units)
    {
        if (euValue >= 1000000)
        {
            double tenX = Math.round(euValue / 100000);
            return Double.toString(tenX / 10.0).concat(" m " + units);
        } else if (euValue >= 1000)
        {
            double tenX = Math.round(euValue / 100);
            return Double.toString(tenX / 10.0).concat(" k " + units);
        } else
        {
            return Double.toString(Math.floor(euValue)).concat(" " + units);
        }
    }

}
