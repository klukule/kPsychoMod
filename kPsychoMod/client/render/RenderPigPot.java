package kPsychoMod.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.core.EntityHelper;

@SideOnly(Side.CLIENT)
public class RenderPigPot extends RenderPig {

   public RenderPigPot(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
      super(par1ModelBase, par2ModelBase, par3);
   }

   public void renderLivingPig(EntityPig pig, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      if(pig.isPotionActive(kPsychoMod.potionEffect) && pig.getActivePotionEffect(kPsychoMod.potionEffect).getDuration() > 0 && pig.ridingEntity == null) {
         if(!pig.onGround) {
            double height = (pig.boundingBox.maxY - pig.boundingBox.minY) / 2.0D;
            GL11.glTranslated(par2, par4 + height, par6);
            par6 = 0.0D;
            par2 = 0.0D;
            par4 = -height;
            float pitch = 1.0F;
            double var4 = pig.motionX;
            double var8 = pig.motionZ;
            double var6 = pig.motionY;
            if(pig == Minecraft.getMinecraft().thePlayer.ridingEntity) {
               var4 = (double)(-MathHelper.sin((float)kPsychoMod.proxy.tickHandlerClient.pigInfo[0] / 180.0F * 3.1415927F) * MathHelper.cos((float)kPsychoMod.proxy.tickHandlerClient.pigInfo[1] / 180.0F * 3.1415927F));
               var8 = (double)(MathHelper.cos((float)kPsychoMod.proxy.tickHandlerClient.pigInfo[0] / 180.0F * 3.1415927F) * MathHelper.cos((float)kPsychoMod.proxy.tickHandlerClient.pigInfo[1] / 180.0F * 3.1415927F));
               var6 = (double)(-MathHelper.sin((float)kPsychoMod.proxy.tickHandlerClient.pigInfo[1] / 180.0F * 3.1415927F));
            }

            double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
            float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / 3.141592653589793D));
            GL11.glRotatef(-EntityHelper.updateRotation(0.0F, var13, 180.0F), -MathHelper.cos(pig.renderYawOffset / 180.0F * 3.1415927F), 0.0F, -MathHelper.sin(pig.renderYawOffset / 180.0F * 3.1415927F));
         }

         if(pig.riddenByEntity instanceof EntityZombie) {
            GL11.glTranslated(0.0D, 0.4D, 0.0D);
         }
      }

      super.renderLivingPig(pig, par2, par4, par6, par8, par9);
      GL11.glPopMatrix();
   }

   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return super.shouldRenderPass((EntityPig)par1EntityLiving, par2, par3);
   }

   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingPig((EntityPig)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingPig((EntityPig)par1Entity, par2, par4, par6, par8, par9);
   }
}
