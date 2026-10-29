package com.projektmod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.projektmod.projektMod.LOGGER;
import static com.projektmod.projektMod.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.debug("Registered mod commands");
        DebugCommand.register(event.getDispatcher());
    }
}
