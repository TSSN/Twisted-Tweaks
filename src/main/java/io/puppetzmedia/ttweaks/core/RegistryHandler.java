package io.puppetzmedia.ttweaks.core;

import io.puppetzmedia.ttweaks.TwistedTweaks;
import io.puppetzmedia.ttweaks.tileentity.LiquidVoidTileEntity;
import io.puppetzmedia.ttweaks.tileentity.TorchTileEntity;
import io.puppetzmedia.ttweaks.tileentity.TorchLitTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = TwistedTweaks.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
	
	@SubscribeEvent
	@SuppressWarnings("ConstantConditions")
	public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {

		TileEntityType<?> type = TileEntityType.Builder.create(
				TorchLitTileEntity::new, TorchLitTileEntity.VALID_BLOCKS).build(null);

		type.setRegistryName(TwistedTweaks.MODID, "torch_lit_te");
		evt.getRegistry().register(type);

		type = TileEntityType.Builder.create(
				TorchTileEntity::new, TorchTileEntity.VALID_BLOCKS).build(null);

		type.setRegistryName(TwistedTweaks.MODID, "torch_te");
		evt.getRegistry().register(type);

		type = TileEntityType.Builder.create(LiquidVoidTileEntity::new,
				LiquidVoidTileEntity.VALID_BLOCKS).build(null);

		type.setRegistryName(TwistedTweaks.MODID, "liquid_void_te");
		evt.getRegistry().register(type);
	}

	/**
	 * Prepare given registry entry for registration process.
	 *
	 * @param entry registry entry to process.
	 * @param name path to use for registry name creation.
	 * @param <T> Forge registry entry type
	 *
	 * @return given registry entry
	 */
	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {

		entry.setRegistryName(TwistedTweaks.getResourceLocation(name));
		return entry;
	}
}