package kPsychoMod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import kPsychoMod.client.model.ModelLauncher;
import kPsychoMod.common.kPsychoMod;

public class ItemRenderLauncher
  implements IItemRenderer
{
  protected ModelLauncher launcherModel;
  protected ModelPig pigRend;

  public ItemRenderLauncher()
  {
    this.launcherModel = new ModelLauncher();

    this.pigRend = new ModelPig(0.7F);
    this.pigRend.body.rotateAngleX = 1.570796F;
  }

  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
  {
    switch (type.ordinal()) {
    case 1:
      return true;
    }return false;
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
  {
    return false;
  }

  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
  {
    switch (type.ordinal())
    {
    case 1:
      GL11.glPushMatrix();

      if (item.itemID == kPsychoMod.itemLauncherTMPP.itemID)
      {
        Minecraft.getMinecraft().renderEngine.bindTexture("/mods/kPsychoMod/textures/model/skin_normal.png");
      }
      else
      {
        Minecraft.getMinecraft().renderEngine.bindTexture("/mods/kPsychoMod/textures/model/skin_nyan.png");
      }

      GL11.glRotatef(100.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-55.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-80.0F, 0.0F, 0.0F, 1.0F);

      boolean isFirstPerson = false;

      if ((data[1] != null) && ((data[1] instanceof EntityPlayer)))
      {
        if (((EntityPlayer)data[1] != Minecraft.getMinecraft().renderViewEntity) || (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) || ((((Minecraft.getMinecraft().currentScreen instanceof GuiInventory)) || ((Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative))) && (RenderManager.instance.playerViewY == 180.0F)))
        {
          GL11.glTranslatef(0.095F, 0.1F, -0.66F);
        }
        else
        {
          isFirstPerson = true;
          GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
          GL11.glRotatef(-30.0F, 0.0F, 0.0F, 1.0F);
          GL11.glTranslatef(0.295F, 0.25F, -0.56F);
        }
      }
      else
      {
        GL11.glTranslatef(0.095F, 0.1F, -0.66F);
      }

      float scale = 1.2F;
      GL11.glScalef(scale, scale, scale);

      this.launcherModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, isFirstPerson);

      Minecraft.getMinecraft().renderEngine.bindTexture("/mob/pig.png");

      scale = 0.2F;
      GL11.glScalef(scale, scale, scale);

      GL11.glTranslatef(0.0F, -3.4F, -0.03F);

      GL11.glPushMatrix();

      int dmg = item.getItemDamage();

      if (dmg < 1)
      {
        dmg = 1;
      }

      for (int i = dmg - 1; i < 8; i++)
      {
        GL11.glTranslatef(0.0F, -0.62F, 0.0F);

        float f = 22.5F * (i - (item.getItemDamage() - 1));

        GL11.glRotatef(f * f, 0.0F, 1.0F, 0.0F);

        this.pigRend.head.render(0.0625F);
        this.pigRend.body.render(0.0625F);
        this.pigRend.leg1.render(0.0625F);
        this.pigRend.leg2.render(0.0625F);
        this.pigRend.leg3.render(0.0625F);
        this.pigRend.leg4.render(0.0625F);
      }

      GL11.glPopMatrix();

      GL11.glPopMatrix();
    }
  }
}