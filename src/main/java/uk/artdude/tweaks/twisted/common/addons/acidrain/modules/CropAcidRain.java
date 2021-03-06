package uk.artdude.tweaks.twisted.common.addons.acidrain.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import uk.artdude.tweaks.twisted.TwistedTweaks;
import uk.artdude.tweaks.twisted.common.addons.acidrain.AcidRainCore;
import uk.artdude.tweaks.twisted.common.configuration.TTConfiguration;

import java.util.Random;

@Mod.EventBusSubscriber
public class CropAcidRain {

    @SubscribeEvent
    public static void growthTickAllowed(BlockEvent.CropGrowEvent.Pre event)
    {
        // If player acid rain is disabled via the config return.
        if (!TTConfiguration.acid_rain.crops.enableCropAcidRain) {
            return;
        }

        Block crop = event.getWorld().getBlockState(event.getPos()).getBlock();
        World world = event.getWorld();


        if (world.isRemote) {
            return;
        }

        if (!AcidRainCore.getIsAcidRain(world)) {
            return;
        }

        // Check to see if the crop is a cactus or reed, we won't handle these crops yet. (Maybe later down the line)
        if (crop instanceof BlockCactus || crop instanceof BlockReed || crop instanceof BlockSapling) {
            return;
        }

        boolean isCropUnderSky = world.isRainingAt(event.getPos());

        if (isCropUnderSky) {

            IBlockState currentState = world.getBlockState(event.getPos());
            Block currentCrop = currentState.getBlock();
            int currentAge = currentCrop.getMetaFromState(currentState);

            double seedDropChance = TTConfiguration.settings.enableDebug ? 1.0 : TTConfiguration.acid_rain.crops.acidRainSeedDropChance;

            if (currentAge > 0)
            {
                // If the chance is lower then the current random drop the seed for the current crop and replace the crop with air.
                if (world.rand.nextFloat() < seedDropChance)
                {

                    // If debugging is enabled log the activity.
                    if (TTConfiguration.settings.enableDebug) {
                        TwistedTweaks.logger.log(Level.INFO, "Seed Drop: " + crop.getLocalizedName() + " Cords: " +
                                event.getPos().getX() + ", " + event.getPos().getY() + ", " + event.getPos().getZ());
                    }

                    // Break the crop and return the seed (Dependant on the chance of getting back the seed (Per mod)).
                    crop.dropBlockAsItem(world, event.getPos(), world.getBlockState(event.getPos()), 0);

                    // Set the position of where the crop was to air.
                    world.setBlockToAir(event.getPos());

                }
                else {
                    // Create the random.
                    Random random = world.rand;

                    // Get the chance of turning back a stage from the config.
                    double returnStageChance = TTConfiguration.acid_rain.crops.acidRainCropReturnChance;

                    // Check to see if the chance meets to return a growth stage of a crop.
                    if (random.nextDouble() < returnStageChance) {

                        // If debugging is enabled log the activity.
                        if (TTConfiguration.settings.enableDebug) {
                            TwistedTweaks.logger.log(Level.INFO, "Crop Growth Backwards: " + crop.getLocalizedName() + " Cords: " +
                                    event.getPos().getX() + ", " + event.getPos().getY() + ", " + event.getPos().getZ());
                        }

                        // Update the crops meta value.
                        for (IProperty property : currentState.getProperties().keySet()) {
                            if (property.getName().equals("age"))
                            {
                                world.setBlockState(event.getPos(), currentState.withProperty(property, currentAge - 1), 2);
                            }
                        }

                    }
                }
            }
            event.setResult(Event.Result.DENY);
        }
    }
}