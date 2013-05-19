package kPsychoMod.common.core;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import kPsychoMod.client.core.TickHandlerClient;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.core.TickHandlerServer;
import kPsychoMod.common.item.ItemLauncher;
import kPsychoMod.common.item.ItemTrailMix;
import kPsychoMod.common.potion.PotionTrailMix;

public class CommonProxy {

   public TickHandlerClient tickHandlerClient = null;
   public TickHandlerServer tickHandlerServer = null;


   public void initMod() {
      int id = kPsychoMod.potID;
      if(id == 0) {
         for(int i = 1; i < Potion.potionTypes.length; ++i) {
            if(Potion.potionTypes[i] == null) {
               id = i;
               break;
            }
         }
      }

      kPsychoMod.potionEffect = (new PotionTrailMix(id, false, 13138688)).setPotionName("potion.trailmix");
      kPsychoMod.itemTrailMix = (ItemFood)(new ItemTrailMix(kPsychoMod.itemTrailMixID, 2, 0.3F, false)).setAlwaysEdible().setPotionEffect(id, kPsychoMod.potDuration, 0, 1.0F).setUnlocalizedName("trailmix").setCreativeTab(CreativeTabs.tabFood);
      kPsychoMod.itemLauncherTMPP = (new ItemLauncher(kPsychoMod.itemLauncherTMPPID)).setFull3D().setUnlocalizedName("TMPPLauncher").setCreativeTab(CreativeTabs.tabTools);
      kPsychoMod.itemLauncherNyanPig = (new ItemLauncher(kPsychoMod.itemLauncherNyanPigID)).setFull3D().setUnlocalizedName("NyanPigLauncher").setCreativeTab(CreativeTabs.tabTools);
      GameRegistry.addShapelessRecipe(new ItemStack(kPsychoMod.itemTrailMix, 4), new Object[]{Item.sugar, Item.gunpowder, Item.redstone, Item.lightStoneDust});
      GameRegistry.addRecipe(new ItemStack(kPsychoMod.itemLauncherTMPP, 1, 9), new Object[]{"LID", "IGI", "OIP", Character.valueOf('L'), new ItemStack(Item.dyePowder, 1, 9), Character.valueOf('I'), Item.ingotIron, Character.valueOf('O'), Block.obsidian, Character.valueOf('D'), Block.dispenser, Character.valueOf('G'), Item.gunpowder, Character.valueOf('P'), Block.pistonBase});
      GameRegistry.addRecipe(new ItemStack(kPsychoMod.itemLauncherNyanPig, 1, 9), new Object[]{"ABC", "DLG", "XYZ", Character.valueOf('A'), new ItemStack(Item.dyePowder, 1, 1), Character.valueOf('B'), new ItemStack(Item.dyePowder, 1, 14), Character.valueOf('C'), new ItemStack(Item.dyePowder, 1, 11), Character.valueOf('D'), Item.diamond, Character.valueOf('L'), new ItemStack(kPsychoMod.itemLauncherTMPP, 1, 9), Character.valueOf('G'), Block.thinGlass, Character.valueOf('X'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 12), Character.valueOf('Z'), new ItemStack(Item.dyePowder, 1, 10)});
      LanguageRegistry.instance();
      LanguageRegistry.addName(kPsychoMod.itemTrailMix, "Drug");
      LanguageRegistry.instance();
      LanguageRegistry.addName(kPsychoMod.itemLauncherTMPP, "TMPP Launcher");
      LanguageRegistry.instance();
      LanguageRegistry.addName(kPsychoMod.itemLauncherNyanPig, "Nyan Pig Launcher");
      LanguageRegistry.instance().addStringLocalization("potion.trailmix", "Drugged!");
   }

   public void initTickHandlers() {
      this.tickHandlerServer = new TickHandlerServer();
      TickRegistry.registerTickHandler(this.tickHandlerServer, Side.SERVER);
   }
}
