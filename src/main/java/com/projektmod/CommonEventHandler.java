package com.projektmod;

import static com.projektmod.projektMod.LOGGER;
import static com.projektmod.projektMod.MODID;

import com.projektmod.ui.MarketScreen;
import com.projektmod.ui.ModMenus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = MODID)
public class CommonEventHandler {

  @SubscribeEvent
  public static void onRegisterCommands(RegisterCommandsEvent event) {
    LOGGER.debug("Registered mod commands");
    DebugCommand.register(event.getDispatcher());
  }

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(ModMenus.MARKET_MENU.get(), MarketScreen::new);
  }
}
