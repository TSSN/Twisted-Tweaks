package uk.artdude.tweaks.twisted.common.addons.modifications;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.Level;
import uk.artdude.tweaks.twisted.TwistedTweaks;
import uk.artdude.tweaks.twisted.common.configuration.ConfigurationHelper;
import uk.artdude.tweaks.twisted.common.util.TTUtilities;

import java.util.ArrayList;
import java.util.List;

public class XPVoid {

    // Set the ArrayList which will contain the blocks to prevent XP from dropping from.
    private static List<Block> blockList = new ArrayList<Block>();

    /**
     * This is the main function call to this class.
     */
    public static void init() {
        // For each of the values set in the config check that the block is a valid block in the game.
        for (String blockOre : ConfigurationHelper.oreXPDisabled) {
            // Split the block information to the mod id and the block id.
            String[] oreInfo = blockOre.split(":");
            // Try and get the block information from the GameRegistry.
            Block oreBlock = TTUtilities.getBlock(oreInfo[0], oreInfo[1]);
            // Check to see if the block is not null.
            if (oreBlock != null) {
                // If the block is a Minecraft block add it to the arrayList.
                if (oreInfo[0].equals("minecraft")) {
                    // Add the block to the arrayList.
                    blockList.add(oreBlock);
                } else if (Loader.isModLoaded(oreInfo[0])) {
                    // If the block is from a mod check (above) that the mod is loaded, if so add the block to the arrayList.
                    blockList.add(oreBlock);
                }
            } else {
                // Log an error to the game to alert the user.
                TwistedTweaks.logger.log(Level.ERROR, blockOre + " is not a valid block in the game. Please check that the " +
                        "config entry is correct and that the relevant mod with the block/ore is installed");
            }
        }
    }

    /**
     * This event gets fired on the block break event which will void any XP dropping to the player.
     * @param event The event of the block break.
     */
    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event) {
        // Set a block variable for easy referencing.
        Block blockMined = event.block;
        // For each of the blocks in the config list see if the block being mined is in the config.
        for (Block oreBlock : blockList) {
            // If the current block being mined is in the config list, prevent any XP from dropping from the ore.
            if (oreBlock == blockMined) {
                // Set the XP to drop to 0 (None)
                event.setExpToDrop(0);
            }
        }
    }
}