package kPsychoMod.common.core;

import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import klukule.core.ObfHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import kPsychoMod.client.entity.EntityAttackFX;
import kPsychoMod.client.entity.EntityTrailMixFX;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.potion.PotionTrailMix;

public class MapPacketHandler implements ITinyPacketHandler {

   public void handle(NetHandler handler, Packet131MapData mapData) {
      short id = mapData.uniqueID;
      if(handler instanceof NetServerHandler) {
         this.handleServerPacket((NetServerHandler)handler, mapData.uniqueID, mapData.itemData, (EntityPlayerMP)handler.getPlayer());
      } else {
         this.handleClientPacket((NetClientHandler)handler, mapData.uniqueID, mapData.itemData);
      }

   }

   public void handleServerPacket(NetServerHandler handler, short id, byte[] data, EntityPlayerMP player) {
      DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

      try {
         switch(id) {
         case 0:
            ItemStack e = player.getCurrentEquippedItem();
            byte key = stream.readByte();
            boolean pressed = stream.readBoolean();
            switch(key) {
            case 0:
               if(player.isPotionActive(kPsychoMod.potionEffect) && kPsychoMod.potFireball == 1 && player.getHeldItem() == null) {
                  PotionEffect effect = player.getActivePotionEffect(kPsychoMod.potionEffect);
                  if(effect.getDuration() > kPsychoMod.potFireballMinReq && kPsychoMod.proxy.tickHandlerServer.canShootFireball(player)) {
                     double motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F));
                     double motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F));
                     double motionY = (double)(-MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F));
                     float mag = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ + motionY * motionY);
                     motionX /= (double)mag;
                     motionY /= (double)mag;
                     motionZ /= (double)mag;
                     motionX *= 4.0D;
                     motionY *= 4.0D;
                     motionZ *= 4.0D;
                     float var10 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                     float yaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / 3.141592653589793D);
                     float pitch = (float)(Math.atan2(motionY, (double)var10) * 180.0D / 3.141592653589793D);
                     EntityLargeFireball var17 = new EntityLargeFireball(player.worldObj, player, motionX, motionY, motionZ);
                     double var18 = 1.0D;
                     Vec3 var20 = player.getLook(1.0F);
                     var17.posX = player.posX + var20.xCoord * var18;
                     var17.posY = player.posY + (double)(player.getEyeHeight() / 2.0F) + 0.5D;
                     var17.posZ = player.posZ + var20.zCoord * var18;
                     var17.posX -= (double)(MathHelper.cos(player.rotationYaw / 180.0F * 3.1415927F) * 0.35F);
                     var17.posY -= 0.5D;
                     var17.posZ -= (double)(MathHelper.sin(player.rotationYaw / 180.0F * 3.1415927F) * 0.35F);
                     var17.posX -= motionX / 5.0D;
                     var17.posY -= motionY / 5.0D;
                     var17.posZ -= motionZ / 5.0D;
                     var17.accelerationX = motionX / 8.0D;
                     var17.accelerationY = motionY / 8.0D;
                     var17.accelerationZ = motionZ / 8.0D;
                     player.worldObj.spawnEntityInWorld(var17);
                     PacketDispatcher.sendPacketToAllInDimension(new Packet18Animation(player, 1), player.dimension);
                     ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                     DataOutputStream stream1 = new DataOutputStream(bytes1);

                     try {
                        stream1.writeByte(0);
                        PacketDispatcher.sendPacketToPlayer(new Packet131MapData((short)kPsychoMod.getNetId(), (short)0, bytes1.toByteArray()), (Player)player);
                     } catch (IOException var27) {
                        var27.printStackTrace();
                     }

                     kPsychoMod.proxy.tickHandlerServer.cooldown.put(player.username, Integer.valueOf(kPsychoMod.potFireballCooldown));
                     player.addPotionEffect(new PotionEffect(kPsychoMod.potionEffect.id, effect.getDuration() - kPsychoMod.potFireballUse, effect.getAmplifier() + 1));
                     player.worldObj.playSoundAtEntity(player, "mob.ghast.fireball", 0.4F, 1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F);
                  }
               }
               break;
            default:
               kPsychoMod.proxy.tickHandlerServer.handleKeyToggle(player, key, pressed);
            }
         }
      } catch (IOException var28) {
         ;
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleClientPacket(NetClientHandler handler, short id, byte[] data) {
      DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

      try {
         Entity ent1;
         double e1;
         double var4;
         Entity var22;
         int var25;
         EntityPig var24;
         switch(id) {
         case 0:
            stream.readByte();
            kPsychoMod.proxy.tickHandlerClient.fireballCooldown = 10;
            break;
         case 1:
            Minecraft var27 = Minecraft.getMinecraft();
            ent1 = var27.theWorld.getEntityByID(stream.readInt());
            if(ent1 != null) {
               var27.renderGlobal.spawnParticle("largeexplode", ent1.posX, (ent1.boundingBox.minY + ent1.boundingBox.maxY) / 2.0D, ent1.posZ, 0.0D, 0.0D, 0.0D);
               int var32 = 16777215;
               String var29;
               if(var27.theWorld.rand.nextFloat() <= 0.05F) {
                  if(ent1 instanceof EntityChicken) {
                     var29 = "/mods/kPsychoMod/textures/fx/chicken.png";
                     var32 = 12686147;
                  } else if(ent1 instanceof EntityCow) {
                     var29 = "/mods/kPsychoMod/textures/fx/cow.png";
                     if(ent1 instanceof EntityMooshroom) {
                        var32 = 10489616;
                     } else {
                        var32 = 4404517;
                     }
                  } else if(ent1 instanceof EntityOcelot) {
                     var29 = "/mods/kPsychoMod/textures/fx/ocelot.png";
                     var32 = 15193471;
                  } else if(ent1 instanceof EntityPig) {
                     var29 = "/mods/kPsychoMod/textures/fx/pig.png";
                     var32 = 15704998;
                  } else if(ent1 instanceof EntitySheep) {
                     var29 = "/mods/kPsychoMod/textures/fx/sheep.png";
                     var32 = 13487565;
                  } else if(ent1 instanceof EntitySquid) {
                     var29 = "/mods/kPsychoMod/textures/fx/squid.png";
                     var32 = 2243405;
                  } else if(ent1 instanceof EntityWolf) {
                     var29 = "/mods/kPsychoMod/textures/fx/wolf.png";
                     var32 = 13545366;
                  } else {
                     var29 = "/mods/kPsychoMod/textures/fx/pika.png";
                     var32 = 16773632;
                  }
               } else {
                  int var33 = (int)(Math.random() * 12.0D) + 1;
                  var29 = "/mods/kPsychoMod/textures/fx/tx" + var33 + ".png";
                  int var35 = Minecraft.getMinecraft().theWorld.rand.nextInt(8);
                  switch(var35) {
                  case 0:
                     var32 = 13828096;
                     break;
                  case 1:
                     var32 = 3428042;
                     break;
                  case 2:
                     var32 = 15483668;
                     break;
                  case 3:
                     var32 = 629315;
                     break;
                  case 4:
                     var32 = 8849818;
                     break;
                  case 5:
                     var32 = 10128905;
                     break;
                  case 6:
                     var32 = 603546;
                     break;
                  case 7:
                     var32 = 7313929;
                  }
               }

               Minecraft.getMinecraft().effectRenderer.addEffect(new EntityAttackFX(ent1.worldObj, ent1.posX, ent1.posY + (double)ent1.getEyeHeight(), ent1.posZ, var32, var29));
            }
            break;
         case 2:
            float var23 = stream.readFloat();
            if(var23 > 10.0F) {
               var23 = 10.0F;
            }

            EntityPlayer var26 = (EntityPlayer)Minecraft.getMinecraft().theWorld.getEntityByID(stream.readInt());
            if(var26 != null) {
               var26.worldObj.spawnParticle("largeexplode", var26.posX, var26.boundingBox.minY, var26.posZ, 0.0D, 0.0D, 0.0D);

               for(var25 = 0; (float)var25 < 2.0F * var23 * var23 * 0.7F; ++var25) {
                  float var28 = var26.worldObj.rand.nextFloat() * 0.2F * var23;
                  float var31 = var26.worldObj.rand.nextFloat() * 3.1415927F * 2.0F;
                  var26.worldObj.spawnParticle("explode", var26.posX, var26.boundingBox.minY + 0.2D, var26.posZ, (double)(-MathHelper.sin(var31) * var28), -0.5D, (double)(MathHelper.cos(var31) * var28));
               }
            }
            break;
         case 3:
            kPsychoMod.proxy.tickHandlerClient.pigInfo = new double[]{stream.readDouble(), stream.readDouble(), stream.readDouble()};
            kPsychoMod.proxy.tickHandlerClient.timeRemaining = stream.readInt();
            break;
         case 4:
            var22 = Minecraft.getMinecraft().theWorld.getEntityByID(stream.readInt());
            if(var22 != null && var22 instanceof EntityPig) {
               var24 = (EntityPig)var22;
               var24.removePotionEffectClient(kPsychoMod.potionEffect.id);
            }
            break;
         case 5:
            var22 = Minecraft.getMinecraft().theWorld.getEntityByID(stream.readInt());
            if(var22 != null && var22 instanceof EntityPig) {
               var24 = (EntityPig)var22;
               var24.setDead();

               for(var25 = 0; var25 < 20; ++var25) {
                  double var30 = var24.getRNG().nextGaussian() * 0.04D;
                  double var34 = var24.getRNG().nextGaussian() * 0.04D;
                  e1 = var24.getRNG().nextGaussian() * 0.04D;
                  var4 = 10.0D;
                  Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTrailMixFX(var24.worldObj, var24.posX + (double)(var24.getRNG().nextFloat() * var24.width * 2.0F) - (double)var24.width - var30 * var4, var24.posY + (double)(var24.getRNG().nextFloat() * var24.height) - var34 * var4, var24.posZ + (double)(var24.getRNG().nextFloat() * var24.width * 2.0F) - (double)var24.width - e1 * var4, var30, var34, e1, PotionTrailMix.getRandEffectColour(var24.getRNG())));
               }
            }
            break;
         case 6:
            int e = stream.readInt();
            if(e == Minecraft.getMinecraft().thePlayer.entityId) {
               EntityClientPlayerMP var10000 = Minecraft.getMinecraft().thePlayer;
               var10000.renderArmPitch += 100.0F;
            }

            ent1 = Minecraft.getMinecraft().theWorld.getEntityByID(e);
            float yaw = stream.readFloat();
            if(ent1 instanceof EntityZombie) {
               ((EntityZombie)ent1).renderYawOffset = ent1.rotationYaw = yaw;
            }

            Entity ent = Minecraft.getMinecraft().theWorld.getEntityByID(stream.readInt());
            if(ent != null && ent instanceof EntityPig) {
               EntityPig pig = (EntityPig)ent;
               pig.renderYawOffset = stream.readFloat();
               boolean hasTrailMix = stream.readBoolean();
               int isNyan;
               double var6;
               double var8;
               if(!hasTrailMix) {
                  for(isNyan = 0; isNyan < 20; ++isNyan) {
                     e1 = pig.getRNG().nextGaussian() * 0.01D;
                     var4 = pig.getRNG().nextGaussian() * 0.01D;
                     var6 = pig.getRNG().nextGaussian() * 0.01D;
                     var8 = 1.0D;
                     pig.worldObj.spawnParticle("smoke", pig.posX + (double)(pig.getRNG().nextFloat() * pig.width * 2.0F) - (double)pig.width - e1 * var8, pig.posY + (double)(pig.getRNG().nextFloat() * pig.height) - var4 * var8, pig.posZ + (double)(pig.getRNG().nextFloat() * pig.width * 2.0F) - (double)pig.width - var6 * var8, e1, var4, var6);
                  }
               } else {
                  for(isNyan = 0; isNyan < 15; ++isNyan) {
                     e1 = pig.getRNG().nextGaussian() * 0.01D;
                     var4 = pig.getRNG().nextGaussian() * 0.01D;
                     var6 = pig.getRNG().nextGaussian() * 0.01D;
                     var8 = 10.0D;
                     Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTrailMixFX(pig.worldObj, pig.posX + (double)(pig.getRNG().nextFloat() * pig.width * 2.0F) - (double)pig.width - e1 * var8, pig.posY + (double)(pig.getRNG().nextFloat() * pig.height) - var4 * var8, pig.posZ + (double)(pig.getRNG().nextFloat() * pig.width * 2.0F) - (double)pig.width - var6 * var8, e1, var4, var6, PotionTrailMix.getRandEffectColour(pig.getRNG())));
                  }

                  boolean var36 = stream.readBoolean();
                  if(var36) {
                     try {
                        ReflectionHelper.setPrivateValue(EntityLiving.class, pig, "/mods/kPsychoMod/textures/model/pig_nyan" + (pig.getRNG().nextFloat() > 0.9F?1:2) + ".png", ObfHelper.texture);
                     } catch (Exception var20) {
                        ObfHelper.obfWarning();
                        var20.printStackTrace();
                     }
                  }
               }
            }
         }
      } catch (IOException var21) {
         ;
      }

   }
}
