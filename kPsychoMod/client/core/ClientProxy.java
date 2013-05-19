package kPsychoMod.client.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.model.ModelPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraftforge.client.MinecraftForgeClient;
import kPsychoMod.client.core.TickHandlerClient;
import kPsychoMod.client.render.ItemRenderLauncher;
import kPsychoMod.client.render.RenderPigPot;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.core.CommonProxy;
import kPsychoMod.common.core.TickHandlerServer;

public class ClientProxy extends CommonProxy {

   public void initMod() {
      super.initMod();
      if(kPsychoMod.replaceRender == 1) {
         RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, new RenderPigPot(new ModelPig(), new ModelPig(0.5F), 0.7F));
      }

      MinecraftForgeClient.registerItemRenderer(kPsychoMod.itemLauncherTMPP.itemID, new ItemRenderLauncher());
      MinecraftForgeClient.registerItemRenderer(kPsychoMod.itemLauncherNyanPig.itemID, new ItemRenderLauncher());
   }

   public void initTickHandlers() {
      this.tickHandlerServer = new TickHandlerServer();
      TickRegistry.registerTickHandler(this.tickHandlerServer, Side.SERVER);
      this.tickHandlerClient = new TickHandlerClient();
      TickRegistry.registerTickHandler(this.tickHandlerClient, Side.CLIENT);
   }
}
