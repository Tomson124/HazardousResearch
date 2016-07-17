package tomson124.hazardousresearch.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tomson124.hazardousresearch.HazardousResearch;

public final class LogHelper {

    public static final Logger LOGGER = LogManager.getLogger(HazardousResearch.MOD_ID);

    public static void warn(String msg) {
        LOGGER.warn(msg);
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void debug(String msg) {
        LOGGER.debug(msg);
    }

    private LogHelper() {
    }
}
