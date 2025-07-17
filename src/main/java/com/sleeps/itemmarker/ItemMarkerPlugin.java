package com.sleeps.itemmarker;


package net.runelite.client.plugins.itemmarker;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.TileItem;
import net.runelite.api.Tile;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.ItemDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
        name = "Item Marker",
        description = "Marks certain ground items with a highlight box",
        tags = {"item", "highlight", "ground"}
)
public class ItemMarkerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ItemMarkerOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ItemMarkerConfig config;

    private final Map<TileItem, Tile> trackedItems = new HashMap<>();

    @Override
    protected void startUp()
    {
        overlay.setItems(trackedItems);
        overlayManager.add(overlay);
        log.info("Item Marker plugin started.");
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        trackedItems.clear();
        log.info("Item Marker plugin stopped.");
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned event)
    {
        TileItem item = event.getItem();
        String itemName = client.getItemDefinition(item.getId()).getName();

        Set<String> markedNames = Arrays.stream(config.markedItems().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (markedNames.contains(itemName.toLowerCase()))
        {
            trackedItems.put(item, event.getTile());
        }
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned event)
    {
        trackedItems.entrySet().removeIf(e ->
                e.getKey().getId() == event.getItem().getId()
                        && e.getValue().getWorldLocation().equals(event.getTile().getWorldLocation()));
    }

    @Provides
    ItemMarkerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ItemMarkerConfig.class);
    }
}