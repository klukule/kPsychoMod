package kPsychoMod.common.core;

import cpw.mods.fml.common.network.PacketDispatcher;
import klukule.core.EntityHelperBase;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.item.ItemLauncher;

public class EntityHelper extends EntityHelperBase {

   public static void launchPig(EntityLiving living) {
      ItemStack is = living.getHeldItem();
      if(is != null && is.getItem() instanceof ItemLauncher && is.getItemDamage() < 9) {
         if(living instanceof EntityZombie) {
            is.setItemDamage(is.getItemDamage() + 1);
         } else if(living instanceof EntityPlayer && !((EntityPlayer)living).capabilities.isCreativeMode) {
            is.setItemDamage(is.getItemDamage() + 1);
            ((EntityPlayer)living).inventory.onInventoryChanged();
         }

         boolean hasTrailMix = living instanceof EntityZombie || living instanceof EntityPlayer && (((EntityPlayer)living).capabilities.isCreativeMode || ((EntityPlayer)living).inventory.consumeInventoryItem(kPsychoMod.itemTrailMix.itemID));
         EntityPig pig = new EntityPig(living.worldObj);
         pig.setLocationAndAngles(living.posX, living.posY + (double)living.getEyeHeight(), living.posZ, living.rotationYaw, living.rotationPitch);
         pig.posX -= (double)(MathHelper.cos(living.rotationYaw / 180.0F * 3.1415927F) * 0.26F);
         pig.posY -= 0.45D;
         pig.posZ -= (double)(MathHelper.sin(living.rotationYaw / 180.0F * 3.1415927F) * 0.26F);
         pig.setPosition(pig.posX, pig.posY, pig.posZ);
         pig.motionX = (double)(-MathHelper.sin(pig.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(pig.rotationPitch / 180.0F * 3.1415927F));
         pig.motionZ = (double)(MathHelper.cos(pig.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(pig.rotationPitch / 180.0F * 3.1415927F));
         pig.motionY = (double)(-MathHelper.sin(pig.rotationPitch / 180.0F * 3.1415927F));
         float par7 = 0.85F;
         float var9 = MathHelper.sqrt_double(pig.motionX * pig.motionX + pig.motionZ * pig.motionZ + pig.motionY * pig.motionY);
         pig.motionX /= (double)var9;
         pig.motionY /= (double)var9;
         pig.motionZ /= (double)var9;
         pig.motionX *= (double)par7;
         pig.motionY *= (double)par7;
         pig.motionZ *= (double)par7;
         float var10 = MathHelper.sqrt_double(pig.motionX * pig.motionX + pig.motionZ * pig.motionZ);
         pig.renderYawOffset = pig.prevRotationYaw = pig.rotationYaw = living.rotationYaw;
         pig.prevRotationPitch = pig.rotationPitch = (float)(Math.atan2(pig.motionY, (double)var10) * 180.0D / 3.141592653589793D);
         pig.setPosition(pig.posX + pig.motionX * 1.2D, pig.posY + pig.motionY * 1.2D, pig.posZ + pig.motionZ * 1.2D);
         if(hasTrailMix) {
            pig.addPotionEffect(new PotionEffect(kPsychoMod.potionEffect.id, kPsychoMod.potDuration * 20 * 3, 0));
            double[] bytes = kPsychoMod.proxy.tickHandlerServer.addPig(pig);
            bytes[6] = 0.7D;
         }

         living.worldObj.spawnEntityInWorld(pig);
         living.worldObj.playSoundAtEntity(pig, "mob.pig.say", 0.4F, 1.0F + (living.worldObj.rand.nextFloat() - living.worldObj.rand.nextFloat()) * 0.2F);
         living.worldObj.playSoundAtEntity(living, "tile.piston.out", 0.2F, 1.0F);
         ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
         DataOutputStream stream = new DataOutputStream(bytes1);

         try {
            stream.writeInt(living.entityId);
            stream.writeFloat(living.rotationYaw);
            stream.writeInt(pig.entityId);
            stream.writeFloat(pig.renderYawOffset);
            stream.writeBoolean(hasTrailMix);
            stream.writeBoolean(is.itemID == kPsychoMod.itemLauncherNyanPig.itemID);
            PacketDispatcher.sendPacketToAllAround(pig.posX, pig.posY, pig.posZ, 512.0D, pig.dimension, new Packet131MapData((short)kPsychoMod.getNetId(), (short)6, bytes1.toByteArray()));
         } catch (IOException var101) {
            var101.printStackTrace();
         }
      }

   }
}
