package kPsychoMod.common.behaviour;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import kPsychoMod.common.core.EntityHelper;

public class BehaviorDispenseLauncher extends BehaviorDefaultDispenseItem {

   private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;
   final MinecraftServer mcServer;


   public BehaviorDispenseLauncher(MinecraftServer server) {
      this.mcServer = server;
      this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
   }

   public ItemStack func_82487_b(IBlockSource iblocksource, ItemStack is) {
      if(is.getItemDamage() >= 9) {
         return this.defaultItemDispenseBehavior.dispense(iblocksource, is);
      } else {
         World world = iblocksource.getWorld();
         EntityZombie zombie = new EntityZombie(world);
         EnumFacing facing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
         zombie.setLocationAndAngles(iblocksource.getX() + (double)facing.getFrontOffsetZ() * 0.3D + (double)facing.getFrontOffsetX() * 1.125D + Math.abs((double)facing.getFrontOffsetY() * 0.5D), iblocksource.getY() + (double)facing.getFrontOffsetY() * 0.3D - 1.45D, iblocksource.getZ() + (double)facing.getFrontOffsetX() * -0.3D + (double)((float)facing.getFrontOffsetZ() * 1.125F) + Math.abs((double)facing.getFrontOffsetY() * 0.5D), facing == EnumFacing.EAST?90.0F:(facing == EnumFacing.NORTH?180.0F:(facing == EnumFacing.WEST?270.0F:0.0F)), facing == EnumFacing.UP?-90.0F:(facing == EnumFacing.DOWN?90.0F:0.0F));
         zombie.setCurrentItemOrArmor(0, is);
         EntityHelper.launchPig(zombie);
         return is;
      }
   }

   protected void func_82485_a(IBlockSource par1IBlockSource) {}
}
