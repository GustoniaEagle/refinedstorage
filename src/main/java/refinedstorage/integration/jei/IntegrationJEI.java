package refinedstorage.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import refinedstorage.RefinedStorageBlocks;

@JEIPlugin
public class IntegrationJEI implements IModPlugin {
    public static IntegrationJEI INSTANCE;

    private IJeiRuntime runtime;

    @Override
    public void register(IModRegistry registry) {
        INSTANCE = this;

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new RecipeTransferHandlerGrid());

        registry.addRecipeCategories(new RecipeCategorySolderer(registry.getJeiHelpers().getGuiHelper()));

        registry.addRecipeHandlers(new RecipeHandlerSolderer());

        registry.addRecipes(RecipeMakerSolderer.getRecipes());

        registry.addRecipeCategoryCraftingItem(new ItemStack(RefinedStorageBlocks.SOLDERER), RecipeCategorySolderer.ID);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        this.runtime = runtime;
    }

    public IJeiRuntime getRuntime() {
        return runtime;
    }

    public static boolean isLoaded() {
        return Loader.isModLoaded("JEI");
    }
}
