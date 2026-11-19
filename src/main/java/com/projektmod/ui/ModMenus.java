package com.projektmod.ui;

import static com.projektmod.projektMod.MODID;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
  public static final DeferredRegister<MenuType<?>> MENUS =
      DeferredRegister.create(Registries.MENU, MODID);

  public static final DeferredHolder<MenuType<?>, MenuType<MarketMenu>> MARKET_MENU =
      MENUS.register("market_menu", () -> IMenuTypeExtension.create(MarketMenu::new));

  public static void register(IEventBus bus) {
    MENUS.register(bus);
  }
}
