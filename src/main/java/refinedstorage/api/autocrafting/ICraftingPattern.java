package refinedstorage.api.autocrafting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Represents a crafting pattern.
 */
public interface ICraftingPattern {
    /**
     * @param world The world
     * @return The container where the pattern is in
     */
    ICraftingPatternContainer getContainer(World world);

    /**
     * @return The position of the container where the pattern is in
     */
    BlockPos getContainerPosition();

    /**
     * @return If this pattern is a processing pattern
     */
    boolean isProcessing();

    /**
     * @return The inputs
     */
    ItemStack[] getInputs();

    /**
     * @return The outputs
     */
    ItemStack[] getOutputs();

    /**
     * @return The byproducts
     */
    ItemStack[] getByproducts();

    /**
     * Writes this pattern to NBT.
     *
     * @param tag The NBT tag to write to
     * @return The written NBT tag
     */
    NBTTagCompound writeToNBT(NBTTagCompound tag);
}
