package kPsychoMod.common.core;

import cpw.mods.fml.common.FMLLog;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerHelper {

   private static Logger logger = Logger.getLogger("kPsychoMod");


   public static void init() {
      logger.setParent(FMLLog.getLogger());
   }

   public static void log(Level logLevel, String message) {
      logger.log(logLevel, message);
   }

}
