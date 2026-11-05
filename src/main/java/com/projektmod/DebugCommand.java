package com.projektmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.projektmod.ui.MarketScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DebugCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
        Commands.literal("debugMod")
            .executes(DebugCommand::execute));
  }

  public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    if (context.getSource().getEntity() instanceof ServerPlayer player) {
      player.sendSystemMessage(Component.literal("Screen Opening Heyo! Yo Heyo!"));
      net.minecraft.client.Minecraft.getInstance().execute(() -> {
        net.minecraft.client.Minecraft.getInstance().setScreenAndShow(new MarketScreen());
      });
    }
    return 1;
  }
}
