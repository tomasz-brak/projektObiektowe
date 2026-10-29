package com.projektmod;

import com.mojang.brigadier.CommandDispatcher;
import com.projektmod.ui.ExampleScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("debugMod")
                        .executes(context -> {
                            if (context.getSource().getEntity() instanceof ServerPlayer player) {
                                player.sendSystemMessage(Component.literal("Screen Opening"));
                                net.minecraft.client.Minecraft.getInstance().execute(() -> {
                                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(new ExampleScreen());
                                });

                            }
                            return 1;
                        })
        );
    }
}