package kPsychoMod.common.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import kPsychoMod.client.entity.EntityTrailMixFX;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.core.EntityHelper;

public class PotionTrailMix extends Potion {

   public Random rand = new Random();
   public static final String nyanPiano = "12-114-10909-105080705-105-107-108-1080705070912140912070905070509-112-114091207090508090807050708-1050709120709070507-105-107-112-114-10909-105080705-105-107-108-1080705070912140912070905070509-112-114091207090508090807050708-1050709120709070507-105-107-105-1000205-10002050709051009101205-105-100020500100907050009100005-1000205-10002050507090500020005-10504050002051009101205-104-105-1000205-10002050709051009101205-105-100020500100907050009100005-1000205-10002050507090500020005-10504050002051009101205-107-1";
   public static final String nyanBassA = "10-122-112-124-109-121-114-114-107-119-112-124-105-117-105-117-110-122-107-119-109-121-114-114-107-119-112-124-105-117-105-117-110-122-112-124-109-121-114-114-107-119-112-124-105-117-105-117-110-122-107-119-109-121-114-114-107-119-112-124-105-117-105-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-1";


   public PotionTrailMix(int i, boolean bad, int j) {
      super(i, bad, j);
   }

   public void performEffect(EntityLiving living, int par2) {
      float f = kPsychoMod.potSpeed - living.landMovementFactor;
      if(f > 0.0F) {
         if(living.worldObj.isRemote && kPsychoMod.proxy.tickHandlerClient.fireballCooldown > 0 && living == Minecraft.getMinecraft().thePlayer) {
            if(kPsychoMod.proxy.tickHandlerClient.fireballCooldown < 9 && kPsychoMod.proxy.tickHandlerClient.fireballCooldown > 2) {
               living.landMovementFactor = 0.0F;
            } else {
               living.landMovementFactor = 0.06F;
            }
         } else {
            living.landMovementFactor += f;
         }
      }

      if(living instanceof EntityPig && !living.isChild()) {
         if(living.worldObj.isRemote && living.isEntityAlive()) {
            double posX = living.posX;
            double posY = living.posY + (double)living.getEyeHeight();
            double posZ = living.posZ;
            double dist = 0.5D;
            double var4 = living.motionX;
            double var8 = living.motionZ;
            double var6 = living.motionY;
            double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
            float var12 = (float)(Math.atan2(var8, var4) * 180.0D / 3.141592653589793D) - 90.0F;
            float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / 3.141592653589793D));
            posX += (double)MathHelper.sin(living.renderYawOffset / 180.0F * 3.1415927F) * dist;
            posZ -= (double)MathHelper.cos(living.renderYawOffset / 180.0F * 3.1415927F) * dist;
            posY -= 0.1D;
            living.rotationPitch = -EntityHelper.updateRotation(living.rotationPitch, var13, 15.0F);
            if(living == Minecraft.getMinecraft().thePlayer.ridingEntity) {
               living.renderYawOffset = living.rotationYaw = (float)kPsychoMod.proxy.tickHandlerClient.pigInfo[0];
            } else {
               living.renderYawOffset = living.rotationYaw = EntityHelper.updateRotation(living.renderYawOffset, var12, 15.0F);
            }

            living.renderDistanceWeight = 5.0D;
            if(living.riddenByEntity != null && living.riddenByEntity instanceof EntityLiving) {
               ((EntityLiving)living.riddenByEntity).renderYawOffset = living.renderYawOffset;
            }

            if(!living.getTexture().contains("nyan")) {
               this.spawnParticle(posX, posY, posZ, (EntityPig)living);
               this.spawnParticle(posX, posY, posZ, (EntityPig)living);
            } else {
               double mag = Math.sqrt(living.motionX * living.motionX + living.motionY * living.motionY + living.motionZ * living.motionZ);

               int pos;
               for(pos = 0; pos < 4; ++pos) {
                  double note = (double)pos * living.motionX / 4.0D;
                  double var7 = (double)pos * living.motionY / 4.0D;
                  double mZ = (double)pos * living.motionZ / 4.0D;
                  var7 -= living.riddenByEntity instanceof EntityZombie?0.4D:0.0D;
                  this.spawnParticle(posX - note, posY - var7 + 0.2D, posZ - mZ, (EntityPig)living, 16580608);
                  this.spawnParticle(posX - note, posY - var7 + 0.125D, posZ - mZ, (EntityPig)living, 16580608);
                  this.spawnParticle(posX - note, posY - var7 + 0.05D, posZ - mZ, (EntityPig)living, 16685056);
                  this.spawnParticle(posX - note, posY - var7 - 0.025D, posZ - mZ, (EntityPig)living, 16685056);
                  this.spawnParticle(posX - note, posY - var7 - 0.1D, posZ - mZ, (EntityPig)living, 16711168);
                  this.spawnParticle(posX - note, posY - var7 - 0.175D, posZ - mZ, (EntityPig)living, 16711168);
                  this.spawnParticle(posX - note, posY - var7 - 0.25D, posZ - mZ, (EntityPig)living, 3407104);
                  this.spawnParticle(posX - note, posY - var7 - 0.325D, posZ - mZ, (EntityPig)living, 3407104);
                  this.spawnParticle(posX - note, posY - var7 - 0.4D, posZ - mZ, (EntityPig)living, '\u98fe');
                  this.spawnParticle(posX - note, posY - var7 - 0.475D, posZ - mZ, (EntityPig)living, '\u98fe');
                  this.spawnParticle(posX - note, posY - var7 - 0.55D, posZ - mZ, (EntityPig)living, 6697981);
                  this.spawnParticle(posX - note, posY - var7 - 0.625D, posZ - mZ, (EntityPig)living, 6697981);
               }

               if(kPsychoMod.proxy.tickHandlerClient.soundPlayed <= 10 && this.getRenderViewEntity() != null && (double)living.getDistanceToEntity(this.getRenderViewEntity()) <= 20.0D) {
                  pos = (int)living.worldObj.getWorldTime() % 512;
                  if(pos % 2 == 0) {
                     int var31 = Integer.parseInt("12-114-10909-105080705-105-107-108-1080705070912140912070905070509-112-114091207090508090807050708-1050709120709070507-105-107-112-114-10909-105080705-105-107-108-1080705070912140912070905070509-112-114091207090508090807050708-1050709120709070507-105-107-105-1000205-10002050709051009101205-105-100020500100907050009100005-1000205-10002050507090500020005-10504050002051009101205-104-105-1000205-10002050709051009101205-105-100020500100907050009100005-1000205-10002050507090500020005-10504050002051009101205-107-1".substring(pos, pos + 2));
                     int note1 = Integer.parseInt("10-122-112-124-109-121-114-114-107-119-112-124-105-117-105-117-110-122-107-119-109-121-114-114-107-119-112-124-105-117-105-117-110-122-112-124-109-121-114-114-107-119-112-124-105-117-105-117-110-122-107-119-109-121-114-114-107-119-112-124-105-117-105-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-110-114-117-122-109-112-117-121-107-110-114-117-105-109-112-117-1".substring(pos, pos + 2));
                     float var32;
                     float var10004;
                     float var10003;
                     float var10002;
                     if(var31 >= 0) {
                        var32 = (float)Math.pow(2.0D, (double)(var31 - 12) / 12.0D);
                        var10002 = (float)living.posX;
                        var10003 = (float)(living.posY - (double)living.yOffset);
                        var10004 = (float)living.posZ;
                        Minecraft.getMinecraft().sndManager.playSound("note.harp", var10002, var10003, var10004, 1.0F, var32);
                     }

                     if(note1 >= 0) {
                        var32 = (float)Math.pow(2.0D, (double)(note1 - 12) / 12.0D);
                        var10002 = (float)living.posX;
                        var10003 = (float)(living.posY - (double)living.yOffset);
                        var10004 = (float)living.posZ;
                        Minecraft.getMinecraft().sndManager.playSound("note.bassattack", var10002, var10003, var10004, 1.0F, var32);
                     }
                  }

                  ++kPsychoMod.proxy.tickHandlerClient.soundPlayed;
               }
            }
         } else {
            kPsychoMod.proxy.tickHandlerServer.addPig((EntityPig)living);
         }
      }

   }

   public boolean isReady(int par1, int par2) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void spawnParticle(double posX, double posY, double posZ, EntityPig pig) {
      Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTrailMixFX(Minecraft.getMinecraft().theWorld, posX, posY, posZ, this.getLiquidColor(), pig));
   }

   @SideOnly(Side.CLIENT)
   public void spawnParticle(double posX, double posY, double posZ, EntityPig pig, int clr) {
      Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTrailMixFX(Minecraft.getMinecraft().theWorld, posX, posY, posZ, clr, pig, true));
   }

   @SideOnly(Side.CLIENT)
   public EntityLiving getRenderViewEntity() {
      return Minecraft.getMinecraft().renderViewEntity;
   }

   @SideOnly(Side.CLIENT)
   public boolean hasStatusIcon() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public int getStatusIconIndex() {
      Minecraft.getMinecraft().renderEngine.bindTexture("/mods/kPsychoMod/textures/potion/potion.png");
      return 0;
   }

   public int getLiquidColor() {
      return getRandEffectColour(this.rand);
   }

   public static int getRandEffectColour(Random rand) {
      int chance = rand.nextInt(5);
      switch(chance) {
      case 0:
         return 13138688;
      case 1:
         return 16776960;
      case 2:
         return 7471104;
      case 3:
         return 16777215;
      default:
         return 9237730;
      }
   }
}
