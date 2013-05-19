package kPsychoMod.common.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.item.ItemLauncher;
import kPsychoMod.common.item.ItemTrailMix;

public class EventHandler {

   @ForgeSubscribe
   public void onEntityHurt(LivingHurtEvent event) {
      if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && event.source instanceof EntityDamageSource && !(event.source instanceof EntityDamageSourceIndirect) && event.source.getEntity() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.source.getEntity();
         if(player.isPotionActive(kPsychoMod.potionEffect)) {
            event.ammount *= 4;
            if(!kPsychoMod.proxy.tickHandlerServer.knockbackList.contains(event.entityLiving)) {
               kPsychoMod.proxy.tickHandlerServer.knockbackList.add(event.entityLiving);
            }
         }
      }

   }

   @ForgeSubscribe
   public void onLivingFall(LivingFallEvent event) {
      if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && event.entityLiving.isPotionActive(kPsychoMod.potionEffect)) {
         float oriDist = event.distance;
         event.distance /= (float)kPsychoMod.fallDampening + 1.0F;
         if(event.entityLiving instanceof EntityPlayer && oriDist >= 3.0F) {
            ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
            DataOutputStream stream1 = new DataOutputStream(bytes1);

            try {
               stream1.writeFloat(oriDist);
               stream1.writeInt(event.entityLiving.entityId);
               PacketDispatcher.sendPacketToAllInDimension(new Packet131MapData((short)kPsychoMod.getNetId(), (short)2, bytes1.toByteArray()), event.entityLiving.dimension);
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         }
      }

   }

   @ForgeSubscribe
   public void onInteract(EntityInteractEvent event) {
      if(event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemLauncher) {
         event.setCanceled(true);
      }

      if(event.target instanceof EntityPig) {
         EntityPig pig = (EntityPig)event.target;
         ItemStack is = event.entityPlayer.getHeldItem();
         if(is != null) {
            if(is.getItem() instanceof ItemBucketMilk && pig.isPotionActive(kPsychoMod.potionEffect)) {
               if(!event.entityPlayer.capabilities.isCreativeMode) {
                  --is.stackSize;
               }

               if(!pig.worldObj.isRemote) {
                  pig.curePotionEffects(is);
                  ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                  DataOutputStream stream1 = new DataOutputStream(bytes1);

                  try {
                     stream1.writeInt(pig.entityId);
                     PacketDispatcher.sendPacketToAllAround(pig.posX, pig.posY, pig.posZ, 512.0D, event.entityLiving.dimension, new Packet131MapData((short)kPsychoMod.getNetId(), (short)4, bytes1.toByteArray()));
                  } catch (IOException var7) {
                     var7.printStackTrace();
                  }

                  PacketDispatcher.sendPacketToAllAround(event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, 512.0D, event.entityPlayer.dimension, new Packet18Animation(event.entityPlayer, 1));
               }

               if(is.stackSize <= 0) {
                  event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] = new ItemStack(Item.bucketEmpty);
                  event.entityPlayer.inventory.onInventoryChanged();
               }

               event.setCanceled(true);
            }

            if(is.getItem() instanceof ItemTrailMix) {
               if(!pig.worldObj.isRemote) {
                  PacketDispatcher.sendPacketToAllAround(event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, 512.0D, event.entityPlayer.dimension, new Packet18Animation(event.entityPlayer, 1));
               }

               if(pig.riddenByEntity == event.entityPlayer) {
                  ((ItemTrailMix)kPsychoMod.itemTrailMix).itemInteractionForEntity(is, pig);
                  pig.worldObj.playSoundAtEntity(pig, "random.burp", 0.3F, 1.0F + (pig.getRNG().nextFloat() - pig.getRNG().nextFloat()) * 0.2F);
                  event.setCanceled(true);
               } else if(pig.getSaddled() && pig.riddenByEntity == null) {
                  pig.interact(event.entityPlayer);
                  event.setCanceled(true);
               }
            }
         }
      }

   }
}
