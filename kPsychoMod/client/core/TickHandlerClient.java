package kPsychoMod.client.core;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.ReflectionHelper;
import klukule.core.ObfHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.item.ItemLauncher;

public class TickHandlerClient implements ITickHandler {

   public int overlayAlpha;
   public int prevOverlayAlpha;
   public int fireballCooldown;
   public int timeRemaining;
   public int soundPlayed;
   public long clock;
   public double[] pigInfo = new double[3];
   public boolean hasScreen;
   public boolean fireballKeyDown;
   public boolean pitchUpKeyDown;
   public boolean pitchDownKeyDown;
   public boolean strafeLeftKeyDown;
   public boolean strafeRightKeyDown;
   public boolean speedUpKeyDown;
   public boolean speedDownKeyDown;
   public boolean tightTurnKeyDown;
   public boolean primaryKeyDown;
   public boolean secondaryKeyDown;
   private boolean currentItemIsLauncher;
   private int prevCurItem;


   public void tickStart(EnumSet type, Object ... tickData) {
      if(type.equals(EnumSet.of(TickType.RENDER)) && Minecraft.getMinecraft().theWorld != null) {
         this.preRenderTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld, ((Float)tickData[0]).floatValue());
      }

   }

   public void tickEnd(EnumSet type, Object ... tickData) {
      if(type.equals(EnumSet.of(TickType.CLIENT))) {
         if(Minecraft.getMinecraft().theWorld != null) {
            this.worldTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld);
         }
      } else if(type.equals(EnumSet.of(TickType.PLAYER))) {
         this.playerTick(((EntityPlayer)tickData[0]).worldObj, (EntityPlayer)tickData[0]);
      } else if(type.equals(EnumSet.of(TickType.RENDER)) && Minecraft.getMinecraft().theWorld != null) {
         this.renderTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld, ((Float)tickData[0]).floatValue());
      }

   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.CLIENT, TickType.PLAYER, TickType.RENDER);
   }

   public String getLabel() {
      return "TickHandlerClientkPsychoMod";
   }

   public void worldTick(Minecraft mc, WorldClient world) {
      this.prevOverlayAlpha = this.overlayAlpha;
      this.soundPlayed = 0;
      if(this.clock != world.getWorldTime()) {
         this.clock = world.getWorldTime();
         if(this.timeRemaining > 0) {
            --this.timeRemaining;
         }
      }

      if(mc.thePlayer.isPotionActive(kPsychoMod.potionEffect)) {
         if(this.overlayAlpha < 20) {
            ++this.overlayAlpha;
         } else {
            this.overlayAlpha = 20 + (int)(world.getWorldTime() % 80L >= 40L?80L - world.getWorldTime() % 80L:world.getWorldTime() % 80L);
            if(this.overlayAlpha - 1 > this.prevOverlayAlpha) {
               this.overlayAlpha = this.prevOverlayAlpha + 1;
            }
         }
      } else if(this.overlayAlpha > 0) {
         --this.overlayAlpha;
      }

      if(this.fireballCooldown > 0) {
         --this.fireballCooldown;
      }

      if(mc.currentScreen == null && !this.hasScreen) {
         if(isPressed(kPsychoMod.fireballKey) && !this.fireballKeyDown && mc.thePlayer.getHeldItem() == null) {
            this.sendKeybind(0, true);
         }

         if(mc.thePlayer.ridingEntity != null && mc.thePlayer.ridingEntity instanceof EntityPig) {
            EntityPig currentInv = (EntityPig)mc.thePlayer.ridingEntity;
            if(currentInv.isPotionActive(kPsychoMod.potionEffect) && currentInv.getActivePotionEffect(kPsychoMod.potionEffect).getDuration() > 0) {
               if(isPressed(kPsychoMod.pitchUpKey) && !this.pitchUpKeyDown) {
                  this.sendKeybind(1, true);
               }

               if(isPressed(kPsychoMod.pitchDownKey) && !this.pitchDownKeyDown) {
                  this.sendKeybind(2, true);
               }

               if(isPressed(kPsychoMod.strafeLeftKey) && !this.strafeLeftKeyDown) {
                  this.sendKeybind(3, true);
               }

               if(isPressed(kPsychoMod.strafeRightKey) && !this.strafeRightKeyDown) {
                  this.sendKeybind(4, true);
               }

               if(isPressed(kPsychoMod.speedUpKey) && !this.speedUpKeyDown) {
                  this.sendKeybind(5, true);
               }

               if(isPressed(kPsychoMod.speedDownKey) && !this.speedDownKeyDown) {
                  this.sendKeybind(6, true);
               }

               if(isPressed(kPsychoMod.tightTurnKey) && !this.tightTurnKeyDown) {
                  this.sendKeybind(7, true);
               }

               if(!isPressed(kPsychoMod.pitchUpKey) && this.pitchUpKeyDown) {
                  this.sendKeybind(1, false);
               }

               if(!isPressed(kPsychoMod.pitchDownKey) && this.pitchDownKeyDown) {
                  this.sendKeybind(2, false);
               }

               if(!isPressed(kPsychoMod.strafeLeftKey) && this.strafeLeftKeyDown) {
                  this.sendKeybind(3, false);
               }

               if(!isPressed(kPsychoMod.strafeRightKey) && this.strafeRightKeyDown) {
                  this.sendKeybind(4, false);
               }

               if(!isPressed(kPsychoMod.speedUpKey) && this.speedUpKeyDown) {
                  this.sendKeybind(5, false);
               }

               if(!isPressed(kPsychoMod.speedDownKey) && this.speedDownKeyDown) {
                  this.sendKeybind(6, false);
               }

               if(!isPressed(kPsychoMod.tightTurnKey) && this.tightTurnKeyDown) {
                  this.sendKeybind(7, false);
               }

               this.pitchUpKeyDown = isPressed(kPsychoMod.pitchUpKey);
               this.pitchDownKeyDown = isPressed(kPsychoMod.pitchDownKey);
               this.strafeLeftKeyDown = isPressed(kPsychoMod.strafeLeftKey);
               this.strafeRightKeyDown = isPressed(kPsychoMod.strafeRightKey);
               this.speedUpKeyDown = isPressed(kPsychoMod.speedUpKey);
               this.speedDownKeyDown = isPressed(kPsychoMod.speedDownKey);
               this.tightTurnKeyDown = isPressed(kPsychoMod.tightTurnKey);
               double currentInvDmg = (double)(-MathHelper.sin((float)this.pigInfo[0] / 180.0F * 3.1415927F) * MathHelper.cos((float)this.pigInfo[1] / 180.0F * 3.1415927F));
               double mZ = (double)(MathHelper.cos((float)this.pigInfo[0] / 180.0F * 3.1415927F) * MathHelper.cos((float)this.pigInfo[1] / 180.0F * 3.1415927F));
               double mY = (double)(-MathHelper.sin((float)this.pigInfo[1] / 180.0F * 3.1415927F));
               float mag = MathHelper.sqrt_double(currentInvDmg * currentInvDmg + mY * mY + mZ * mZ);
               currentInvDmg /= (double)mag;
               mY /= (double)mag;
               mZ /= (double)mag;
               currentInvDmg *= this.pigInfo[2];
               mY *= this.pigInfo[2];
               mZ *= this.pigInfo[2];
               currentInv.setVelocity(currentInvDmg, mY, mZ);
            }
         }

         ItemStack var11 = mc.thePlayer.inventory.getCurrentItem();
         if(var11 != null) {
            int var12 = var11.getItemDamage();
            if(var11.getItem() instanceof ItemLauncher) {
               if(isPressed(mc.gameSettings.keyBindAttack.keyCode) && !this.primaryKeyDown) {
                  this.sendKeybind(11, true);
               }

               if(isPressed(mc.gameSettings.keyBindUseItem.keyCode) && !this.secondaryKeyDown) {
                  this.sendKeybind(12, true);
               }

               if(!isPressed(mc.gameSettings.keyBindUseItem.keyCode) && this.secondaryKeyDown) {
                  this.sendKeybind(12, false);
               }
            }
         }
      }

      this.primaryKeyDown = isPressed(mc.gameSettings.keyBindAttack.keyCode);
      this.secondaryKeyDown = isPressed(mc.gameSettings.keyBindUseItem.keyCode);
      this.hasScreen = mc.currentScreen != null;
      this.fireballKeyDown = isPressed(kPsychoMod.fireballKey);
   }

   public void playerTick(World world, EntityPlayer player) {
      ItemStack is = player.getCurrentEquippedItem();
      if(is != null && is.getItem() instanceof ItemLauncher && (player != Minecraft.getMinecraft().renderViewEntity || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) && player.getItemInUseCount() <= 0) {
         player.clearItemInUse();
         player.setItemInUse(is, Integer.MAX_VALUE);
      }

   }

   public void preRenderTick(Minecraft mc, World world, float renderTick) {
      ItemStack currentInv = mc.thePlayer.getCurrentEquippedItem();
      if(currentInv != null && currentInv.getItem() instanceof ItemLauncher) {
         mc.playerController.resetBlockRemoving();
         if(this.prevCurItem == mc.thePlayer.inventory.currentItem) {
            try {
               ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, mc.entityRenderer.itemRenderer, Float.valueOf(1.0F), ObfHelper.equippedProgress);
               ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, mc.entityRenderer.itemRenderer, Float.valueOf(1.0F), ObfHelper.prevEquippedProgress);
               ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, mc.entityRenderer.itemRenderer, mc.thePlayer.inventory.getCurrentItem(), ObfHelper.itemToRender);
               ReflectionHelper.setPrivateValue(ItemRenderer.class, mc.entityRenderer.itemRenderer, Integer.valueOf(mc.thePlayer.inventory.currentItem), ObfHelper.equippedItemSlot);
            } catch (Exception var7) {
               ObfHelper.obfWarning();
               var7.printStackTrace();
            }
         }

         mc.thePlayer.isSwingInProgress = false;
         mc.thePlayer.swingProgressInt = 0;
         mc.thePlayer.swingProgress = 0.0F;
      }

      this.currentItemIsLauncher = currentInv != null && currentInv.getItem() instanceof ItemLauncher;
      if(this.prevCurItem != mc.thePlayer.inventory.currentItem) {
         if(mc.thePlayer.inventory.currentItem >= 0 && mc.thePlayer.inventory.currentItem <= 9) {
            try {
               if(((Float)((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, mc.entityRenderer.itemRenderer, ObfHelper.equippedProgress))).floatValue() >= 1.0F) {
                  this.prevCurItem = mc.thePlayer.inventory.currentItem;
               }
            } catch (Exception var6) {
               ObfHelper.obfWarning();
               var6.printStackTrace();
            }
         }

         this.currentItemIsLauncher = false;
      }

   }

   public void renderTick(Minecraft mc, World world, float renderTick) {
      if(mc.currentScreen == null || mc.currentScreen instanceof GuiChat) {
         int ll;
         if(kPsychoMod.showFlightTimer == 1 && mc.thePlayer.ridingEntity != null && mc.thePlayer.ridingEntity instanceof EntityPig) {
            EntityPig scaledresolution = (EntityPig)mc.thePlayer.ridingEntity;
            if(scaledresolution.isPotionActive(kPsychoMod.potionEffect)) {
               ScaledResolution width = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
               StringBuilder height = new StringBuilder();
               int overlay = 0;
               ll = 0;
               int ff = 0;
               int ff1 = this.timeRemaining;

               while(ff1 >= 20) {
                  ++ff;
                  ff1 -= 20;
                  if(ff >= 60) {
                     ff = 0;
                     ++ll;
                     if(ll >= 60) {
                        ll = 0;
                        ++overlay;
                     }
                  }
               }

               if(overlay > 0) {
                  height.append(overlay);
                  height.append(":");
                  if(ll < 10) {
                     height.append(0);
                  }
               }

               height.append(ll);
               height.append(":");
               if(ff < 10) {
                  height.append(0);
               }

               height.append(ff);
               int ff2 = 16777215;
               if(ff <= 10 && ll == 0 && overlay == 0) {
                  int ff3 = (int)this.clock % 20;
                  if(ff3 < 10) {
                     ff2 = 16711680;
                  }
               }

               mc.fontRenderer.drawStringWithShadow(height.toString(), 10, width.getScaledHeight() - 25, ff2);
            }
         }

         if(mc.thePlayer.isPotionActive(kPsychoMod.potionEffect) || this.overlayAlpha > 0) {
            ScaledResolution var18 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int var19 = var18.getScaledWidth();
            int var20 = var18.getScaledHeight();
            float var21 = ((float)this.overlayAlpha + (float)(this.prevOverlayAlpha - this.overlayAlpha) * renderTick) / 60.0F;
            ll = 15642443;
            float var22 = (float)(ll >> 16 & 255) / 255.0F;
            float var23 = (float)(ll >> 8 & 255) / 255.0F;
            float var25 = (float)(ll & 255) / 255.0F;
            float var24 = 0.5F;
            float ff10 = var24 * var22;
            float ff13 = var24 * var23;
            float ff16 = var24 * var25;
            float ff19 = 1.8F;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F * var21 * var21);
            GL11.glDisable(3008);
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/mods/kPsychoMod/textures/fx/glow.png"));
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA((int)(ff10 * ff19 * 255.0F), (int)(ff13 * ff19 * 255.0F), (int)(ff16 * ff19 * 255.0F), (int)(var21 * 255.0F));
            tessellator.addVertexWithUV(0.0D, (double)var20, -90.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)var19, (double)var20, -90.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)var19, 0.0D, -90.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

   }

   public void sendKeybind(int i, boolean pressed) {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream stream = new DataOutputStream(bytes);

      try {
         stream.writeByte(i);
         stream.writeBoolean(pressed);
         PacketDispatcher.sendPacketToServer(new Packet131MapData((short)kPsychoMod.getNetId(), (short)0, bytes.toByteArray()));
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static boolean isPressed(int key) {
      return key < 0?Mouse.isButtonDown(key + 100):Keyboard.isKeyDown(key);
   }
}
