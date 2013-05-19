package kPsychoMod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import kPsychoMod.common.kPsychoMod;

public class ItemLauncher extends Item {

   public ItemLauncher(int i) {
      super(i);
      this.maxStackSize = 1;
      this.setMaxDamage(9);
   }

   public boolean onBlockStartBreak(ItemStack itemstack, int i, int j, int k, EntityPlayer player) {
      return true;
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      return true;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      return true;
   }

   public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
      if(this.itemID != Item.potion.itemID && this.itemID != Item.monsterPlacer.itemID) {
         itemList.add(new ItemStack(this, 1, 1));
      }

   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag) {}

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.bow;
   }

   @SideOnly(Side.CLIENT)
   public void func_94581_a(IconRegister par1IconRegister) {
      if(this == kPsychoMod.itemLauncherTMPP) {
         this.itemIcon = par1IconRegister.registerIcon("kPsychoMod:tmpplauncher");
      } else {
         this.itemIcon = par1IconRegister.registerIcon("kPsychoMod:nyanpiglauncher");
      }

   }
}
