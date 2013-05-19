package kPsychoMod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import kPsychoMod.common.kPsychoMod;

public class ItemTrailMix extends ItemFood {

   public ItemTrailMix(int par1, int par2, float par3, boolean par4) {
      super(par1, par2, par3, par4);
   }

   protected void func_77849_c(ItemStack is, World world, EntityPlayer player) {
      if(!world.isRemote) {
         PotionEffect effect = player.getActivePotionEffect(kPsychoMod.potionEffect);
         int duration = 0;
         int amp = 0;
         if(effect != null) {
            duration = effect.getDuration();
            amp = effect.getAmplifier();
         }

         player.addPotionEffect(new PotionEffect(kPsychoMod.potionEffect.id, duration + kPsychoMod.potDuration * 20, amp));
         if(duration + kPsychoMod.potDuration * 20 > kPsychoMod.potPoisoning && kPsychoMod.potPoisoning > 0) {
            player.addPotionEffect(new PotionEffect(Potion.wither.id, duration + kPsychoMod.potDuration * 20 - kPsychoMod.potPoisoning, (duration + kPsychoMod.potDuration * 20 - kPsychoMod.potPoisoning) / 20));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, duration + kPsychoMod.potDuration * 20 - kPsychoMod.potPoisoning, (duration + kPsychoMod.potDuration * 20 - kPsychoMod.potPoisoning) / 20));
         }
      }

   }

   public boolean itemInteractionForEntity(ItemStack is, EntityLiving living) {
      if(living instanceof EntityPig) {
         PotionEffect effect = living.getActivePotionEffect(kPsychoMod.potionEffect);
         int duration = 0;
         int amp = 0;
         if(effect != null) {
            duration = effect.getDuration();
            amp = effect.getAmplifier();
         }

         living.addPotionEffect(new PotionEffect(kPsychoMod.potionEffect.id, duration + kPsychoMod.potDuration * 20 * 3, amp));
         if(living.isChild()) {
            living.addPotionEffect(new PotionEffect(Potion.poison.id, duration + kPsychoMod.potDuration * 20 * 3, 0));
         }

         --is.stackSize;
         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_94581_a(IconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("kPsychoMod:trailmix");
   }
}
