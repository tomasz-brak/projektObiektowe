package com.projektmod.chart;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public final class PriceChartCommand {
  private static final PriceChartMapFactory MAP_FACTORY = new PriceChartMapFactory();
  private static PriceHistoryRepository repository;

  private PriceChartCommand() {}

  private static PriceHistoryRepository getRepository() {
    if (repository == null) {
      repository = PriceHistoryRepository.createDefault();
    }
    return repository;
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
        Commands.literal("pricechart")
            .then(
                Commands.argument("item", IdentifierArgument.id())
                    .suggests(PriceChartCommand::suggestAvailableItems)
                    .executes(PriceChartCommand::execute)));
  }

  private static CompletableFuture<Suggestions> suggestAvailableItems(
      CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
    List<String> available = getRepository().getAvailableItemIds();
    return SharedSuggestionProvider.suggest(available, builder);
  }

  private static int execute(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    CommandSourceStack source = context.getSource();
    ServerPlayer player = source.getPlayerOrException();
    Identifier itemId = IdentifierArgument.getId(context, "item");

    PriceHistoryRepository repo = getRepository();
    Optional<PriceHistoryEntry> entryOpt = repo.findByItemId(itemId);
    if (entryOpt.isEmpty()) {
      List<String> available = repo.getAvailableItemIds();
      if (available.isEmpty()) {
        source.sendFailure(
            Component.literal(
                "No price history entries found. Make sure "
                    + PriceHistoryRepository.FILE_NAME
                    + " exists and is valid JSON."));
      } else {
        String sampleList = String.join(", ", available.subList(0, Math.min(available.size(), 5)));
        source.sendFailure(
            Component.literal(
                "No price history found for '" + itemId + "'. Available: " + sampleList));
      }
      return 0;
    }

    PriceHistoryEntry entry = entryOpt.get();
    ServerLevel level = source.getLevel();
    PriceChartMapFactory.CreatedChartMap created = MAP_FACTORY.createChartMap(level, entry);
    ItemStack mapStack = created.stack();

    giveItemToPlayer(player, mapStack);

    // Immediately register the player as carrier and sync the map pixels to the client
    created.mapData().tickCarriedBy(player, mapStack, null);
    Packet<?> packet = created.mapData().getUpdatePacket(created.mapId(), player);
    if (packet != null) {
      player.connection.send(packet);
    }

    source.sendSuccess(
        () ->
            Component.literal(
                "Created price chart map for "
                    + entry.itemName()
                    + " ("
                    + entry.prices().size()
                    + " data points)."),
        false);
    return 1;
  }

  private static void giveItemToPlayer(ServerPlayer player, ItemStack stack) {
    ItemStack toGive = stack.copy();
    boolean added = player.getInventory().add(toGive);
    if (added && toGive.isEmpty()) {
      player
          .level()
          .playSound(
              null,
              player.getX(),
              player.getY(),
              player.getZ(),
              SoundEvents.ITEM_PICKUP,
              SoundSource.PLAYERS,
              0.2F,
              ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F)
                  * 2.0F);
      player.containerMenu.broadcastChanges();
    } else {
      ItemEntity drop = player.createItemStackToDrop(toGive, false, false);
      if (drop != null) {
        drop.setNoPickUpDelay();
        drop.setTarget(player.getUUID());
        drop.level().addFreshEntity(drop);
      }
    }
  }
}
