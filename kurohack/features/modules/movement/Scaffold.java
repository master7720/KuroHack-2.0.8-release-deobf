//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\User\Desktop\1.12 stable mappings"!

//Decompiled by Procyon!

package kurohack.features.modules.movement;

import kurohack.features.modules.*;
import kurohack.features.setting.*;
import java.util.*;
import kurohack.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import kurohack.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import kurohack.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;

public class Scaffold extends Module
{
    private final Setting<Mode> mode;
    public Setting<Boolean> rotation;
    private final Setting<Boolean> swing;
    private final Setting<Boolean> bSwitch;
    private final Setting<Boolean> center;
    private final Setting<Boolean> keepY;
    private final Setting<Boolean> sprint;
    private final Setting<Boolean> replenishBlocks;
    private final Setting<Boolean> down;
    private final Setting<Float> expand;
    private final List<Block> invalid;
    private final Timer timerMotion;
    private final Timer itemTimer;
    private BlockData blockData;
    private int lastY;
    private BlockPos pos;
    private boolean teleported;
    private final Timer timer;
    
    public Scaffold() {
        super("Scaffold", "Places Blocks underneath you.", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Legit));
        this.rotation = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false, v -> this.mode.getValue() == Mode.Fast));
        this.swing = (Setting<Boolean>)this.register(new Setting("Swing Arm", (T)false, v -> this.mode.getValue() == Mode.Legit));
        this.bSwitch = (Setting<Boolean>)this.register(new Setting("Switch", (T)false, v -> this.mode.getValue() == Mode.Legit));
        this.center = (Setting<Boolean>)this.register(new Setting("Center", (T)false, v -> this.mode.getValue() == Mode.Legit));
        this.keepY = (Setting<Boolean>)this.register(new Setting("KeepYLevel", (T)false, v -> this.mode.getValue() == Mode.Legit));
        this.sprint = (Setting<Boolean>)this.register(new Setting("UseSprint", (T)true, v -> this.mode.getValue() == Mode.Legit));
        this.replenishBlocks = (Setting<Boolean>)this.register(new Setting("ReplenishBlocks", (T)true, v -> this.mode.getValue() == Mode.Legit));
        this.down = (Setting<Boolean>)this.register(new Setting("Down", (T)false, v -> this.mode.getValue() == Mode.Legit));
        this.expand = (Setting<Float>)this.register(new Setting("Expand", (T)1.0f, (T)1.0f, (T)6.0f, v -> this.mode.getValue() == Mode.Legit));
        this.invalid = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, (Block)Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, (Block)Blocks.WATER, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.FLOWING_LAVA, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, (Block)Blocks.RED_MUSHROOM, (Block)Blocks.BROWN_MUSHROOM, (Block)Blocks.YELLOW_FLOWER, (Block)Blocks.RED_FLOWER, Blocks.ANVIL, (Block)Blocks.CACTUS, Blocks.LADDER, Blocks.ENDER_CHEST);
        this.timerMotion = new Timer();
        this.itemTimer = new Timer();
        this.timer = new Timer();
    }
    
    public void onEnable() {
        this.timer.reset();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.Fast) {
            if (this.isOff() || fullNullCheck() || event.getStage() == 0) {
                return;
            }
            if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                this.timer.reset();
            }
            final BlockPos playerBlock;
            if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).add(0, -1, 0))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                    this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                    this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                    }
                    this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                        this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                    }
                    this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                    }
                    this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
                }
                else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                    }
                    this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
                }
            }
        }
    }
    
    public void onUpdate() {
        if (this.mode.getValue() == Mode.Legit) {
            if (KuroHack.moduleManager.isModuleEnabled("Sprint") && ((this.down.getValue() && Scaffold.mc.gameSettings.keyBindSneak.isKeyDown()) || !this.sprint.getValue())) {
                Scaffold.mc.player.setSprinting(false);
                Sprint.getInstance().disable();
            }
            if (this.replenishBlocks.getValue() && !(Scaffold.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) && this.getBlockCountHotbar() <= 0 && this.itemTimer.passedMs(100L)) {
                for (int i = 9; i < 45; ++i) {
                    final ItemStack is;
                    if (Scaffold.mc.player.inventoryContainer.getSlot(i).getHasStack() && (is = Scaffold.mc.player.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemBlock && !this.invalid.contains(Block.getBlockFromItem(is.getItem()))) {
                        if (i < 36) {
                            swap(getItemSlot(Scaffold.mc.player.inventoryContainer, is.getItem()), 44);
                        }
                    }
                }
            }
            if (this.keepY.getValue()) {
                if ((!isMoving((EntityLivingBase)Scaffold.mc.player) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) || Scaffold.mc.player.collidedVertically || Scaffold.mc.player.onGround) {
                    this.lastY = MathHelper.floor(Scaffold.mc.player.posY);
                }
            }
            else {
                this.lastY = MathHelper.floor(Scaffold.mc.player.posY);
            }
            this.blockData = null;
            double x = Scaffold.mc.player.posX;
            double z = Scaffold.mc.player.posZ;
            final double y = this.keepY.getValue() ? this.lastY : Scaffold.mc.player.posY;
            final double forward = Scaffold.mc.player.movementInput.moveForward;
            final double strafe = Scaffold.mc.player.movementInput.moveStrafe;
            final float yaw = Scaffold.mc.player.rotationYaw;
            if (!Scaffold.mc.player.collidedHorizontally) {
                final double[] coords = this.getExpandCoords(x, z, forward, strafe, yaw);
                x = coords[0];
                z = coords[1];
            }
            if (this.canPlace(Scaffold.mc.world.getBlockState(new BlockPos(Scaffold.mc.player.posX, Scaffold.mc.player.posY - ((Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && this.down.getValue()) ? 2 : 1), Scaffold.mc.player.posZ)).getBlock())) {
                x = Scaffold.mc.player.posX;
                z = Scaffold.mc.player.posZ;
            }
            BlockPos blockBelow = new BlockPos(x, y - 1.0, z);
            if (Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && this.down.getValue()) {
                blockBelow = new BlockPos(x, y - 2.0, z);
            }
            this.pos = blockBelow;
            if (Scaffold.mc.world.getBlockState(blockBelow).getBlock() == Blocks.AIR) {
                this.blockData = this.getBlockData2(blockBelow);
            }
            if (this.blockData != null) {
                if (this.getBlockCountHotbar() <= 0 || (!this.bSwitch.getValue() && !(Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock))) {
                    return;
                }
                final int heldItem = Scaffold.mc.player.inventory.currentItem;
                if (this.bSwitch.getValue()) {
                    for (int j = 0; j < 9; ++j) {
                        Scaffold.mc.player.inventory.getStackInSlot(j);
                        if (Scaffold.mc.player.inventory.getStackInSlot(j).getCount() != 0 && Scaffold.mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock && !this.invalid.contains(((ItemBlock)Scaffold.mc.player.inventory.getStackInSlot(j).getItem()).getBlock())) {
                            Scaffold.mc.player.inventory.currentItem = j;
                            break;
                        }
                    }
                }
                if (this.mode.getValue() == Mode.Legit) {
                    if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && Scaffold.mc.player.moveForward == 0.0f && Scaffold.mc.player.moveStrafing == 0.0f && !Scaffold.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        if (!this.teleported && this.center.getValue()) {
                            this.teleported = true;
                            final BlockPos pos = new BlockPos(Scaffold.mc.player.posX, Scaffold.mc.player.posY, Scaffold.mc.player.posZ);
                            Scaffold.mc.player.setPosition(pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5);
                        }
                        if (this.center.getValue() && !this.teleported) {
                            return;
                        }
                        Scaffold.mc.player.motionY = 0.41999998688697815;
                        Scaffold.mc.player.motionZ = 0.0;
                        Scaffold.mc.player.motionX = 0.0;
                        if (this.timerMotion.sleep(1500L)) {
                            Scaffold.mc.player.motionY = -0.28;
                        }
                    }
                    else {
                        this.timerMotion.reset();
                        if (this.teleported && this.center.getValue()) {
                            this.teleported = false;
                        }
                    }
                }
                if (Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, this.blockData.position, this.blockData.face, new Vec3d(this.blockData.position.getX() + Math.random(), this.blockData.position.getY() + Math.random(), this.blockData.position.getZ() + Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
                    if (this.swing.getValue()) {
                        Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    else {
                        Scaffold.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    }
                }
                Scaffold.mc.player.inventory.currentItem = heldItem;
            }
        }
    }
    
    public static void swap(final int slot, final int hotbarNum) {
        Scaffold.mc.playerController.windowClick(Scaffold.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.player);
        Scaffold.mc.playerController.windowClick(Scaffold.mc.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.player);
        Scaffold.mc.playerController.windowClick(Scaffold.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.player);
        Scaffold.mc.playerController.updateController();
    }
    
    public static int getItemSlot(final Container container, final Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            if (container.getSlot(i).getHasStack()) {
                final ItemStack is;
                if ((is = container.getSlot(i).getStack()).getItem() == item) {
                    slot = i;
                }
            }
        }
        return slot;
    }
    
    public static boolean isMoving(final EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }
    
    public double[] getExpandCoords(final double x, final double z, final double forward, final double strafe, final float YAW) {
        BlockPos underPos = new BlockPos(x, Scaffold.mc.player.posY - ((Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && this.down.getValue()) ? 2 : 1), z);
        Block underBlock = Scaffold.mc.world.getBlockState(underPos).getBlock();
        double xCalc = -999.0;
        double zCalc = -999.0;
        double dist = 0.0;
        final double expandDist = this.expand.getValue() * 2.0f;
        while (!this.canPlace(underBlock)) {
            xCalc = x;
            zCalc = z;
            if (++dist > expandDist) {
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if (dist == expandDist) {
                break;
            }
            underPos = new BlockPos(xCalc, Scaffold.mc.player.posY - ((Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && this.down.getValue()) ? 2 : 1), zCalc);
            underBlock = Scaffold.mc.world.getBlockState(underPos).getBlock();
        }
        return new double[] { xCalc, zCalc };
    }
    
    public boolean canPlace(final Block block) {
        return (block instanceof BlockAir || block instanceof BlockLiquid) && Scaffold.mc.world != null && Scaffold.mc.player != null && this.pos != null && Scaffold.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(this.pos)).isEmpty();
    }
    
    private int getBlockCountHotbar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (Scaffold.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Scaffold.mc.player.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    if (!this.invalid.contains(((ItemBlock)item).getBlock())) {
                        blockCount += is.getCount();
                    }
                }
            }
        }
        return blockCount;
    }
    
    private BlockData getBlockData2(final BlockPos pos) {
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos10 = pos.add(0, -1, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos10.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos11.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos12.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos13.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains(Scaffold.mc.world.getBlockState(pos14.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    
    public void place(final BlockPos posI, final EnumFacing face) {
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        }
        else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        }
        else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        }
        else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        }
        else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        final int oldSlot = Scaffold.mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Scaffold.mc.player.inventory.getStackInSlot(i);
            if (!InventoryUtil.isNull(stack) && stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
                newSlot = i;
                break;
            }
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        final Block block;
        if (!Scaffold.mc.player.isSneaking() && BlockUtil.blackList.contains(block = Scaffold.mc.world.getBlockState(pos).getBlock())) {
            Scaffold.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(Scaffold.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            Scaffold.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(newSlot));
            Scaffold.mc.player.inventory.currentItem = newSlot;
            Scaffold.mc.playerController.updateController();
        }
        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = Scaffold.mc.player;
            player.motionX *= 0.3;
            final EntityPlayerSP player2 = Scaffold.mc.player;
            player2.motionZ *= 0.3;
            Scaffold.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                Scaffold.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue()) {
            final float[] angle = MathUtil.calcAngle(Scaffold.mc.player.getPositionEyes(Scaffold.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            Scaffold.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.normalizeAngle((int)angle[1], 360), Scaffold.mc.player.onGround));
        }
        Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
        Scaffold.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
        Scaffold.mc.player.inventory.currentItem = oldSlot;
        Scaffold.mc.playerController.updateController();
        if (crouched) {
            Scaffold.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    public enum Mode
    {
        Legit, 
        Fast;
    }
    
    private static class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
