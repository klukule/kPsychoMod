package kPsychoMod.common.core;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import klukule.core.EntityHelperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import kPsychoMod.common.kPsychoMod;
import kPsychoMod.common.core.EntityHelper;
import kPsychoMod.common.item.ItemLauncher;

public class TickHandlerServer implements ITickHandler {

	public ArrayList knockbackList = new ArrayList();
	public HashMap cooldown = new HashMap();
	public HashMap pigs = new HashMap();
	public HashMap pigsKeys = new HashMap();
	public ArrayList holdingKey = new ArrayList();

	@Override
	public void tickStart(EnumSet type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.WORLD))) {
			this.worldTick((World) tickData[0]);
		} else if (type.equals(EnumSet.of(TickType.PLAYER))) {
			this.playerTick(((EntityPlayer) tickData[0]).worldObj,
					(EntityPlayerMP) tickData[0]);
		}

	}

	@Override
	public EnumSet ticks() {
		return EnumSet.of(TickType.WORLD, TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "TickHandlerServerTrailMix";
	}

	public void worldTick(World world) {
		if (world == DimensionManager.getWorld(0)) {
			HashMap i = this.pigs;
			synchronized (this.pigs) {
				Iterator ent = this.pigs.entrySet().iterator();

				while (ent.hasNext()) {
					Entry zombie = (Entry) ent.next();
					if (((EntityPig) zombie.getKey())
							.isPotionActive(kPsychoMod.potionEffect)
							&& (((EntityPig) zombie.getKey()).isCollided || ((EntityPig) zombie
									.getKey()).isEntityAlive())) {
						EntityPig currentItemIsLauncher = (EntityPig) zombie
								.getKey();
						boolean sendInfo = false;
						double[] info = (double[]) zombie.getValue();
						++info[12];
						if (currentItemIsLauncher.riddenByEntity != null
								&& !(currentItemIsLauncher.riddenByEntity instanceof EntityZombie)) {
							if (currentItemIsLauncher.riddenByEntity instanceof EntityPlayer) {
								info[8] = info[0];
								info[9] = info[1];
								info[10] = info[6];
								boolean[] var34 = (boolean[]) this.pigsKeys
										.get(currentItemIsLauncher);
								info[4] = 0.0D;
								if (var34 != null) {
									if (var34[1]) {
										info[3] += 0.5D;
										if (var34[6]) {
											if (info[3] > 12.0D) {
												info[3] = 12.0D;
											}
										} else if (info[3] > 6.5D) {
											--info[3];
										} else if (info[3] > 6.0D) {
											info[3] = 6.0D;
										}
									} else if (!var34[0]) {
										info[3] -= 0.5D;
										if (info[3] < 0.0D) {
											info[3] = 0.0D;
										}
									}

									if (var34[0]) {
										info[3] -= 0.5D;
										if (var34[6]) {
											if (info[3] < -12.0D) {
												info[3] = -12.0D;
											}
										} else if (info[3] < -6.5D) {
											++info[3];
										} else if (info[3] < -6.0D) {
											info[3] = -6.0D;
										}
									} else if (!var34[1]) {
										info[3] += 0.5D;
										if (info[3] > 0.0D) {
											info[3] = 0.0D;
										}
									}

									if (var34[2]) {
										info[2] -= 0.5D;
										if (var34[6]) {
											if (info[2] < -12.0D) {
												info[2] = -12.0D;
											}
										} else if (info[2] < -6.5D) {
											++info[2];
										} else if (info[2] < -6.0D) {
											info[2] = -6.0D;
										}
									} else if (!var34[3]) {
										info[2] += 0.5D;
										if (info[2] > 0.0D) {
											info[2] = 0.0D;
										}
									}

									if (var34[3]) {
										info[2] += 0.5D;
										if (var34[6]) {
											if (info[2] > 12.0D) {
												info[2] = 12.0D;
											}
										} else if (info[2] > 6.5D) {
											--info[2];
										} else if (info[2] > 6.0D) {
											info[2] = 6.0D;
										}
									} else if (!var34[2]) {
										info[2] -= 0.5D;
										if (info[2] < 0.0D) {
											info[2] = 0.0D;
										}
									}

									if (var34[4]) {
										info[6] += 0.05D;
										if (info[6] > 1.4D) {
											info[6] = 1.4D;
										}
									}

									if (var34[5]) {
										info[6] -= 0.05D;
										if (info[6] < 0.4D) {
											info[6] = 0.4D;
										}
									}
								}
							}
						} else {
							if (info[4] >= info[5]) {
								Random mX = currentItemIsLauncher.worldObj.rand;
								info[4] = 0.0D;
								info[5] = Math.ceil(mX.nextDouble() * 100.0D);
								info[2] = mX.nextDouble()
										* 6.0D
										* (mX.nextDouble() > 0.5D ? 1.0D
												: -1.0D);
								info[3] = mX.nextDouble()
										* 6.0D
										* (mX.nextDouble() > 0.5D ? 1.0D
												: -1.0D);
							}

							++info[4];
							if (info[12] <= 60.0D) {
								Vec3 var32 = currentItemIsLauncher.worldObj
										.getWorldVec3Pool().getVecFromPool(
												currentItemIsLauncher.posX,
												currentItemIsLauncher.posY,
												currentItemIsLauncher.posZ);
								Vec3 var3 = currentItemIsLauncher.worldObj
										.getWorldVec3Pool()
										.getVecFromPool(
												currentItemIsLauncher.posX
														+ currentItemIsLauncher.motionX,
												currentItemIsLauncher.posY
														+ currentItemIsLauncher.motionY,
												currentItemIsLauncher.posZ
														+ currentItemIsLauncher.motionZ);
								MovingObjectPosition mZ = currentItemIsLauncher.worldObj
										.rayTraceBlocks_do_do(var32, var3, false, true);
								var32 = currentItemIsLauncher.worldObj
										.getWorldVec3Pool().getVecFromPool(
												currentItemIsLauncher.posX,
												currentItemIsLauncher.posY,
												currentItemIsLauncher.posZ);
								var3 = currentItemIsLauncher.worldObj
										.getWorldVec3Pool()
										.getVecFromPool(
												currentItemIsLauncher.posX
														+ currentItemIsLauncher.motionX,
												currentItemIsLauncher.posY
														+ currentItemIsLauncher.motionY,
												currentItemIsLauncher.posZ
														+ currentItemIsLauncher.motionZ);
								if (mZ != null) {
									var3 = currentItemIsLauncher.worldObj
											.getWorldVec3Pool()
											.getVecFromPool(
													mZ.hitVec.xCoord,
													mZ.hitVec.yCoord,
													mZ.hitVec.zCoord);
								}

								Entity var5 = null;
								List mY = currentItemIsLauncher.worldObj
										.getEntitiesWithinAABBExcludingEntity(
												currentItemIsLauncher,
												currentItemIsLauncher.boundingBox
														.addCoord(
																currentItemIsLauncher.motionX,
																currentItemIsLauncher.motionY,
																currentItemIsLauncher.motionZ)
														.expand(1.0D,
																1.0D, 1.0D));
								double var7 = 0.0D;

								try {
									for (int effect = mY.size() - 1; effect >= 0; --effect) {
										Entity e1 = (Entity) mY.get(effect);
										if (e1 instanceof EntityZombie
												&& e1.canBeCollidedWith()) {
											float duration = 0.3F;
											AxisAlignedBB bytes = e1.boundingBox
													.expand(
															(double) duration,
															(double) duration,
															(double) duration);
											MovingObjectPosition stream = bytes
													.calculateIntercept(var32, var3);
											if (stream != null) {
												double e11 = var32
														.distanceTo(stream.hitVec);
												if (e11 < var7 || var7 == 0.0D) {
													var5 = e1;
													var7 = e11;
												}
											}
										}
									}
								} catch (IndexOutOfBoundsException var24) {
									;
								}

								if (var5 != null) {
									mZ = new MovingObjectPosition(var5);
								}

								if (mZ != null
										&& mZ.entityHit != null
										&& mZ.entityHit instanceof EntityZombie
										&& world.rand.nextFloat() < 0.5F) {
									mZ.entityHit
											.mountEntity(currentItemIsLauncher);
								}
							}
						}

						if (currentItemIsLauncher.isCollided
								&& (!currentItemIsLauncher.onGround
										|| currentItemIsLauncher.onGround
										&& currentItemIsLauncher.motionY < -0.3D || !((EntityPig) zombie
											.getKey()).isEntityAlive())) {
							if (info[7] > 0.8D) {
								float var33 = 2.5F * (float) info[7];
								if (var33 > 7.0F) {
									var33 = 7.0F;
								}

								currentItemIsLauncher.worldObj
										.createExplosion(
												currentItemIsLauncher,
												currentItemIsLauncher.posX,
												currentItemIsLauncher.posY,
												currentItemIsLauncher.posZ,
												var33,
												kPsychoMod.pigExplosion == 0 ? false
														: (kPsychoMod.pigExplosion == 1 ? true
																: currentItemIsLauncher.worldObj
																		.getGameRules()
																		.getGameRuleBooleanValue(
																				"mobGriefing")));
								currentItemIsLauncher.setDead();
								this.pigsKeys.remove(zombie.getKey());
								ent.remove();
							} else if (!((EntityPig) zombie.getKey())
									.isEntityAlive()) {
								this.pigsKeys.remove(zombie.getKey());
								ent.remove();
							}
						}

						info[7] = Math.sqrt(currentItemIsLauncher.motionX
								* currentItemIsLauncher.motionX
								+ currentItemIsLauncher.motionY
								* currentItemIsLauncher.motionY
								+ currentItemIsLauncher.motionZ
								* currentItemIsLauncher.motionZ);
						info[0] += info[2];
						info[1] += info[3];
						double var36 = (double) (-MathHelper
								.sin((float) info[0] / 180.0F * 3.1415927F) * MathHelper
								.cos((float) info[1] / 180.0F * 3.1415927F));
						double var35 = (double) (MathHelper
								.cos((float) info[0] / 180.0F * 3.1415927F) * MathHelper
								.cos((float) info[1] / 180.0F * 3.1415927F));
						double var37 = (double) (-MathHelper
								.sin((float) info[1] / 180.0F * 3.1415927F));
						if (currentItemIsLauncher.motionY > 0.0D) {
							currentItemIsLauncher.fallDistance = 0.0F;
						} else if (!currentItemIsLauncher.onGround) {
							currentItemIsLauncher.fallDistance = 18.0F * (float) (-currentItemIsLauncher.motionY);
						}

						if (currentItemIsLauncher.riddenByEntity != null
								&& !(currentItemIsLauncher.riddenByEntity instanceof EntityZombie)) {
							if (currentItemIsLauncher.riddenByEntity instanceof EntityPlayer) {
								currentItemIsLauncher.rotationYaw = (float) info[0];
								if (info[1] > 90.0D) {
									info[1] = 90.0D;
								}

								if (info[1] < -90.0D) {
									info[1] = -90.0D;
								}

								if (currentItemIsLauncher.onGround
										&& info[1] > 0.0D) {
									info[1] = 0.0D;
								}

								float mag = MathHelper
										.sqrt_double(var36 * var36 + var37
												* var37 + var35 * var35);
								var36 /= mag;
								var37 /= mag;
								var35 /= mag;
								var36 *= info[6];
								var37 *= info[6];
								var35 *= info[6];
								this.setVelocity(currentItemIsLauncher, var36,
										var37, var35);
								if (currentItemIsLauncher.ridingEntity != null) {
									;
								}

								PotionEffect var39 = currentItemIsLauncher
										.getActivePotionEffect(kPsychoMod.potionEffect);
								double var38 = info[11];
								if (var39 != null) {
									var38 = (double) var39.getDuration();
								}

								if (info[8] != info[0] || info[9] != info[1]
										|| info[10] != info[6]
										|| info[11] != var38) {
									ByteArrayOutputStream var41 = new ByteArrayOutputStream();
									DataOutputStream var40 = new DataOutputStream(
											var41);

									try {
										var40.writeDouble(info[0]);
										var40.writeDouble(info[1]);
										var40.writeDouble(info[6]);
										var40.writeInt((int) info[11]);
										PacketDispatcher
												.sendPacketToPlayer(
														new Packet131MapData(
																(short) kPsychoMod
																		.getNetId(),
																(short) 3,
																var41.toByteArray()),
														(Player) currentItemIsLauncher.riddenByEntity);
									} catch (IOException var23) {
										var23.printStackTrace();
									}
								}

								info[11] = (double) var39.getDuration();
							}
						} else {
							var36 /= 10.0D;
							var37 /= 10.0D;
							var35 /= 10.0D;
							currentItemIsLauncher.addVelocity(var36, var37,
									var35);
							if (currentItemIsLauncher.ridingEntity != null) {
								currentItemIsLauncher.ridingEntity
										.addVelocity(var36, var37, var35);
							}
						}
					} else {
						if (!((EntityPig) zombie.getKey()).onGround) {
							((EntityPig) zombie.getKey()).fallDistance = 0.0F;
						}

						this.pigsKeys.remove(zombie.getKey());
						ent.remove();
					}
				}
			}
		}

		Iterator var26 = this.knockbackList.iterator();

		Entity var28;
		while (var26.hasNext()) {
			var28 = (Entity) var26.next();
			double var29 = 7.0D;
			var28.motionX *= var29;
			var28.motionZ *= var29;
			this.spawnParticleEffect(var28);
		}

		if (!this.knockbackList.isEmpty()) {
			this.knockbackList.clear();
		}

		for (int var27 = 0; var27 < world.loadedEntityList.size(); ++var27) {
			var28 = (Entity) world.loadedEntityList.get(var27);
			if (var28 instanceof EntityZombie) {
				EntityZombie var30 = (EntityZombie) var28;
				boolean var31 = var30.getHeldItem() != null
						&& var30.getHeldItem().getItem() instanceof ItemLauncher;
				if (var31 && var30.getRNG().nextFloat() < 0.005F) {
					EntityHelper.launchPig(var30);
				}
			}
		}

	}

	public void setVelocity(Entity ent, double mX, double mY, double mZ) {
		ent.motionX = mX;
		ent.motionY = mY;
		ent.motionZ = mZ;
	}

	public void playerTick(World world, EntityPlayerMP player) {
		if (this.cooldown.containsKey(player.username)) {
			if (player.isPotionActive(kPsychoMod.potionEffect)) {
				int is = ((Integer) this.cooldown.get(player.username))
						.intValue();
				--is;
				if (is <= 0) {
					this.cooldown.remove(player.username);
				} else {
					this.cooldown.put(player.username,
							Integer.valueOf(is));
				}
			} else {
				this.cooldown.remove(player.username);
			}
		}

		if (this.holdingKey.contains(player)) {
			ItemStack var19 = player.getCurrentEquippedItem();
			boolean currentItemIsLauncher = var19 != null
					&& var19.getItem() instanceof ItemLauncher;
			if (currentItemIsLauncher && var19.getItemDamage() > 1) {
				MovingObjectPosition mop = EntityHelperBase.getEntityLook(
						player, 5.0D);
				if (mop != null
						&& mop.entityHit != null
						&& mop.entityHit instanceof EntityPig
						&& !((EntityPig) mop.entityHit).isChild()
						&& !((EntityPig) mop.entityHit)
								.isPotionActive(kPsychoMod.potionEffect.id)) {
					double dist = (double) player
							.getDistanceToEntity(mop.entityHit);
					if (dist <= 1.8D) {
						EntityPig distX = (EntityPig) mop.entityHit;
						distX.setDead();
						if (distX.getSaddled()) {
							distX.dropItem(
									Item.saddle.itemID, 1);
						}

						player.worldObj
								.playSoundAtEntity(
										distX,
										"mob.pig.death",
										0.3F,
										1.0F + (player.worldObj.rand
												.nextFloat() - player.worldObj.rand
												.nextFloat()) * 0.2F);
						player.worldObj.playSoundAtEntity(player, "tile.piston.in",
								0.2F, 1.0F);
						var19.setItemDamage(var19.getItemDamage() - 1);
						player.inventory.onInventoryChanged();
						ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
						DataOutputStream distY = new DataOutputStream(bytes1);

						try {
							distY.writeInt(distX.entityId);
							PacketDispatcher.sendPacketToAllAround(
									player.posX,
									player.posY,
									player.posZ,
									512.0D,
									player.dimension,
									new Packet131MapData((short) kPsychoMod
											.getNetId(), (short) 5, bytes1
											.toByteArray()));
						} catch (IOException var18) {
							var18.printStackTrace();
						}
					} else {
						double var20 = player.posX - mop.entityHit.posX;
						double var21 = player.posY
								+ (double) player.getEyeHeight()
								- (mop.entityHit.boundingBox.maxY + mop.entityHit.boundingBox.minY)
								/ 2.0D;
						double distZ = player.posZ - mop.entityHit.posZ;
						double mag = 0.01D;
						double mag1 = 10.0D / dist;
						mag *= mag1;
						var20 *= mag;
						var21 *= mag;
						distZ *= mag;
						mop.entityHit.addVelocity(var20, var21, distZ);
					}
				}
			} else {
				this.holdingKey.remove(player);
			}
		}

	}

	public boolean canShootFireball(EntityPlayer player) {
		try {
			int e = ((Integer) this.cooldown.get(player.username))
					.intValue();
			return e == 0;
		} catch (NullPointerException var3) {
			return true;
		}
	}

	public void handleKeyToggle(EntityPlayer player, int key, boolean pressed) {
		if (key >= 11) {
			if (key == 11) {
				EntityHelper.launchPig(player);
			} else if (key == 12) {
				if (pressed && !this.holdingKey.contains(player)) {
					this.holdingKey.add(player);
				} else {
					this.holdingKey.remove(player);
				}
			}
		} else if (player.ridingEntity instanceof EntityPig) {
			EntityPig pig = (EntityPig) player.ridingEntity;
			if (pig.isPotionActive(kPsychoMod.potionEffect)) {
				boolean[] keys = (boolean[]) this.pigsKeys.get(pig);
				if (keys != null) {
					keys[key - 1] = pressed;
				}
			}
		}

	}

	public double[] addPig(EntityPig pig) {
		double[] pigStats = (double[]) this.pigs.get(pig);
		if (pigStats == null) {
			pigStats = new double[] { pig.renderYawOffset,
					(double) pig.rotationPitch, 0.0D, 0.0D, 0.0D, 0.0D, 0.5D,
					0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
			boolean[] keys = new boolean[7];
			HashMap var4 = this.pigs;
			synchronized (this.pigs) {
				this.pigs.put(pig, pigStats);
			}

			this.pigsKeys.put(pig, keys);
		}

		return pigStats;
	}

	public void spawnParticleEffect(Entity ent) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);

		try {
			stream.writeInt(ent.entityId);
			stream.writeBoolean(ent.isEntityAlive());
			PacketDispatcher.sendPacketToAllAround(ent.posX, ent.posY,
					ent.posZ, 512.0D, ent.dimension,
					new Packet131MapData((short) kPsychoMod.getNetId(),
							(short) 1, bytes.toByteArray()));
		} catch (IOException var5) {
			var5.printStackTrace();
		}

	}
}
