package kPsychoMod.client.entity;

import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTrailMixFX extends EntityFX {

   private int txIndex;
   private int colour;


   public EntityTrailMixFX(World world, double d, double d1, double d2, int clr, EntityPig pig) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      double dx = 0.05D;
      double mX = (double)(-MathHelper.sin(pig.renderYawOffset / 180.0F * 3.1415927F) * MathHelper.cos(0.0F));
      double mZ = (double)(MathHelper.cos(pig.renderYawOffset / 180.0F * 3.1415927F) * MathHelper.cos(0.0F));
      double mY = (double)(-MathHelper.sin(0.0F));
      float mag = MathHelper.sqrt_double(mX * mX + mY * mY + mZ * mZ);
      mX /= (double)mag;
      mY /= (double)mag;
      mZ /= (double)mag;
      double velo = 0.1D;
      mX *= velo;
      mY *= velo;
      mZ *= velo;
      this.motionX = mX * -1.0D;
      this.motionY = mY * -1.0D;
      this.motionZ = mZ * -1.0D;
      this.motionX += (Math.random() - Math.random()) * dx;
      this.motionY += (Math.random() - Math.random()) * dx;
      this.motionZ += (Math.random() - Math.random()) * dx;
      this.particleRed = 0.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.0F;
      this.particleScale *= 1.2F;
      this.colour = clr;
      this.txIndex = (int)(Math.random() * 5.0D) + 2;
      this.setParticleTextureIndex(this.txIndex);
      this.setSize(0.1F, 0.1F);
      this.particleGravity = 0.06F;
      this.particleMaxAge = (int)(12.0D / (Math.random() * 0.8D + 0.2D));
   }

   public EntityTrailMixFX(World world, double d, double d1, double d2, int clr, EntityPig pig, boolean isNyan) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      double dx = 0.05D;
      this.motionX = this.motionY = this.motionZ = 0.0D;
      this.particleRed = 0.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.0F;
      this.particleScale *= 1.2F;
      this.colour = clr;
      this.txIndex = (int)(Math.random() * 5.0D) + 2;
      this.setParticleTextureIndex(this.txIndex);
      this.setSize(0.1F, 0.1F);
      this.particleGravity = 0.0F;
      this.particleMaxAge = 20;
   }

   public EntityTrailMixFX(World world, double d, double d1, double d2, double mX, double mY, double mZ, int clr) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.motionX = mX;
      this.motionY = mY;
      this.motionZ = mZ;
      this.particleRed = 0.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.0F;
      this.particleScale *= 1.2F;
      this.colour = clr;
      this.txIndex = (int)(Math.random() * 5.0D) + 2;
      this.setParticleTextureIndex(this.txIndex);
      this.setSize(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.particleMaxAge = (int)(12.0D / (Math.random() * 0.8D + 0.2D));
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float f6 = (float)(this.txIndex % 16) / 16.0F;
      float f7 = f6 + 0.0624375F;
      float f8 = (float)(this.txIndex / 16) / 16.0F;
      float f9 = f8 + 0.0624375F;
      float f10 = 0.1F * this.particleScale;
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
      tessellator.setColorRGBA_F(ff10 * ff19, ff13 * ff19, ff16 * ff19, 0.6F);
      tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)f7, (double)f9);
      tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)f7, (double)f8);
      tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)f6, (double)f8);
      tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)f6, (double)f9);
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
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;
      if(this.particleMaxAge-- <= 0) {
         this.setDead();
      }

      if(this.onGround) {
         if(Math.random() < 0.5D) {
            this.setDead();
         }

         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

      Material material = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
      if(material.isLiquid() || material.isSolid()) {
         double d = (double)((float)(MathHelper.floor_double(this.posY) + 1) - BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));
         if(this.posY < d) {
            this.setDead();
         }
      }

   }
}
