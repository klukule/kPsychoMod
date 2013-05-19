package kPsychoMod.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import kPsychoMod.common.kPsychoMod;

public class EntityAttackFX extends EntityFX {

   private int txIndex;
   private int fxIndex;
   private int colour;
   private String text;


   public EntityAttackFX(World world, double d, double d1, double d2, int clr, String tx) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      double dx = 0.1D;
      this.motionX = (Math.random() - Math.random()) * dx;
      this.motionY = (Math.random() - Math.random()) * 0.2D + 0.1D;
      this.motionZ = (Math.random() - Math.random()) * dx;
      this.particleRed = 0.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.0F;
      this.particleScale *= 1.2F;
      this.colour = clr;
      this.text = tx;
      this.fxIndex = (int)(Math.random() * 4.0D) + 1;
      this.setSize(0.01F, 0.01F);
      this.particleScale = 5.0F;
      this.particleMaxAge = 20;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      Minecraft.getMinecraft().renderEngine.bindTexture("/mods/kPsychoMod/textures/fx/fx" + Integer.toString(this.fxIndex) + ".png");
      GL11.glPushMatrix();
      GL11.glDisable(2884);
      GL11.glDisable(2896);
      float f6 = 0.0F;
      float f7 = f6 + 1.0F;
      float f8 = 0.0F;
      float f9 = f8 + 1.0F;
      float f10 = 0.2F * this.particleScale;
      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float f14 = 1.0F;
      int ll = this.colour;
      float ff = (float)(ll >> 16 & 255) / 255.0F;
      float ff1 = (float)(ll >> 8 & 255) / 255.0F;
      float ff2 = (float)(ll & 255) / 255.0F;
      float ff3 = 0.5F;
      float ff10 = ff3 * ff;
      float ff13 = ff3 * ff1;
      float ff16 = ff3 * ff2;
      float ff19 = 1.8F;
      tessellator.setColorRGBA_F(ff10 * ff19, ff13 * ff19, ff16 * ff19, 1.0F);
      if(kPsychoMod.explosionBubble == 1) {
         tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)f7, (double)f9);
         tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)f7, (double)f8);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)f6, (double)f8);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)f6, (double)f9);
      }

      tessellator.draw();
      GL11.glDepthMask(false);
      Minecraft.getMinecraft().renderEngine.bindTexture(this.text);
      tessellator.startDrawingQuads();
      ll = 16777215;
      ff = (float)(ll >> 16 & 255) / 255.0F;
      ff1 = (float)(ll >> 8 & 255) / 255.0F;
      ff2 = (float)(ll & 255) / 255.0F;
      ff3 = 0.5F;
      float var10000 = ff3 * ff;
      var10000 = ff3 * ff1;
      var10000 = ff3 * ff2;
      ff19 = 1.0F;
      tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)f7, (double)f9);
      tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)f7, (double)f8);
      tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)f6, (double)f8);
      tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)f6, (double)f9);
      GL11.glDepthMask(true);
      GL11.glEnable(2884);
      GL11.glPopMatrix();
   }

   public float getEntityBrightness(float f) {
      float f1 = super.getBrightness(f);
      float f2 = (float)this.particleAge / (float)this.particleMaxAge;
      f2 *= f2;
      f2 *= f2;
      return f1 * (1.0F - f2) + f2;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ++this.particleAge;
      if(this.particleAge > this.particleMaxAge) {
         this.setDead();
      }

   }
}
