package kPsychoMod.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkMod;
import klukule.core.SettingsHelper;
import java.util.logging.Level;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import kPsychoMod.common.behaviour.BehaviorDispenseLauncher;
import kPsychoMod.common.core.CommonProxy;
import kPsychoMod.common.core.EventHandler;
import kPsychoMod.common.core.LoggerHelper;
import kPsychoMod.common.core.MapPacketHandler;

@Mod(
   modid = "kPsychoMod",
   name = "klukule's Psycho Mod",
   version = "1.5.0.1",
   dependencies = "required-after:klukuleUtil@[1.0.0,)"
)
@NetworkMod(
   clientSideRequired = true,
   serverSideRequired = false,
   tinyPacketHandler = MapPacketHandler.class
)
public class kPsychoMod {

   public static final String version = "1.5.0v1";
   @Instance("kPsychoMod")
   public static kPsychoMod instance;
   @SidedProxy(
      clientSide = "kPsychoMod.client.core.ClientProxy",
      serverSide = "kPsychoMod.common.core.CommonProxy"
   )
   public static CommonProxy proxy;
   public static int itemTrailMixID;
   public static int itemLauncherTMPPID;
   public static int itemLauncherNyanPigID;
   public static int potID;
   public static int potFireball;
   public static int potFireballUse;
   public static int potFireballMinReq;
   public static int potFireballCooldown;
   public static int potDuration;
   public static int potPoisoning;
   public static int fallDampening;
   public static float potSpeed;
   public static int pigExplosion;
   public static int explosionBubble;
   public static int fireballKey;
   public static int pitchUpKey;
   public static int pitchDownKey;
   public static int strafeLeftKey;
   public static int strafeRightKey;
   public static int speedUpKey;
   public static int speedDownKey;
   public static int tightTurnKey;
   public static int showFlightTimer;
   public static int replaceRender;
   public static ItemFood itemTrailMix;
   public static Item itemLauncherTMPP;
   public static Item itemLauncherNyanPig;
   public static Potion potionEffect;


   @PreInit
   public void preLoad(FMLPreInitializationEvent event) {
      LoggerHelper.init();
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();
      itemTrailMixID = SettingsHelper.addCommentAndReturnItemID(config, "itemid", "trailmix", "Item ID for trail mix", 13180);
      itemLauncherTMPPID = SettingsHelper.addCommentAndReturnItemID(config, "itemid", "TMPP Launcher", "Item ID for Trail Mix Propelled Pig Launcher", 13181);
      itemLauncherNyanPigID = SettingsHelper.addCommentAndReturnItemID(config, "itemid", "NyanPig Launcher", "Item ID for Nyan Pig Launcher", 13182);
      potID = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "ID", "The ID for the potion effect.\n0 = Auto take first available slot\nMaximum is 31.", 0);
      potFireball = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "fireball", "Allow the fireball skill?\n0 = No\n1 = Yes", 1);
      potFireballUse = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "potFireballUse", "Time from effect used when firing a fireball (in seconds)", 5) * 20;
      potFireballMinReq = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "potFireballMinReq", "Minimum Trail Mix effect time required to launch fireballs (in seconds)", 60) * 20;
      potFireballCooldown = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "fireballCooldown", "Trail Mix effect time used when firing a fireball (in ticks [20 ticks a second])", 20);
      potDuration = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "duration", "Duration the effect lasts (in seconds)", 200);
      potPoisoning = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "poisoning", "How much can you take before overdosing (in seconds)\nSet to 0 to disable", 180) * 20;
      potSpeed = (float)SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "speed", "Walking speed of the effect", 3) / 10.0F;
      fallDampening = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "fallDampening", "Dampen fall damage?\nFall damage / (This value + 1)", 1);
      pigExplosion = SettingsHelper.addCommentAndReturnInt(config, "potionEffect", "pigExplosion", "Do pig explosions damage blocks?\n0 = No\n1 = Yes\n2 = World Game Rules", 2);
      explosionBubble = SettingsHelper.addCommentAndReturnInt(config, "client.effect", "explosionBubble", "", 1);
      config.addCustomCategoryComment("client.keybind", "These settings are keybindings for the mod.\nMouse binds are possible, starting from -100 and higher.\nFor info on Key codes, check here: http://www.minecraftwiki.net/wiki/Key_codes");
      fireballKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "fireball", "", -98);
      pitchUpKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "pitchUp", "", 31);
      pitchDownKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "pitchDown", "", 17);
      strafeLeftKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "strafeLeft", "", 30);
      strafeRightKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "strafeRight", "", 32);
      speedUpKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "speedUp", "", 42);
      speedDownKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "speedDown", "", 29);
      tightTurnKey = SettingsHelper.addCommentAndReturnInt(config, "client.keybind", "tightTurn", "", 57);
      showFlightTimer = SettingsHelper.addCommentAndReturnInt(config, "client.render", "showFlightTimer", "Show the time remaining on flight?\n0 = No\n1 = Yes", 1);
      replaceRender = SettingsHelper.addCommentAndReturnInt(config, "client.render", "replaceRender", "Replace pig render with custom render?\nIf you have issues with invisible pigs etc, disable this.\n0 = No\n1 = Yes", 1);
      if(potFireballCooldown < 0) {
         potFireballCooldown = 0;
      }

      if(potID > 31) {
         potID = 31;
      } else if(potID < 0) {
         potID = 0;
      }

      config.save();
   }

   @Init
   public void load(FMLInitializationEvent event) {
      proxy.initMod();
      proxy.initTickHandlers();
      MinecraftForge.EVENT_BUS.register(new EventHandler());
   }

   @ServerStarting
   public void serverStarting(FMLServerStartingEvent event) {
      BlockDispenser.dispenseBehaviorRegistry.putObject(itemLauncherTMPP, new BehaviorDispenseLauncher(event.getServer()));
      BlockDispenser.dispenseBehaviorRegistry.putObject(itemLauncherNyanPig, new BehaviorDispenseLauncher(event.getServer()));
   }

   @ServerStopping
   public void serverStopping(FMLServerStoppingEvent event) {
      proxy.tickHandlerServer.cooldown.clear();
      proxy.tickHandlerServer.knockbackList.clear();
      proxy.tickHandlerServer.pigs.clear();
      proxy.tickHandlerServer.pigsKeys.clear();
      proxy.tickHandlerServer.holdingKey.clear();
   }

   public static int getNetId() {
      return FMLNetworkHandler.instance().findNetworkModHandler(instance).getNetworkId();
   }

   public static void console(String s, boolean warning) {
      StringBuilder sb = new StringBuilder();
      LoggerHelper.log(warning?Level.WARNING:Level.INFO, sb.append("[").append("1.5.0v1").append("] ").append(s).toString());
   }

   public static void console(String s) {
      console(s, false);
   }
}
