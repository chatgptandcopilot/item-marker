package com.sleeps.itemmarker;


package net.runelite.client.plugins.itemmarker;

import net.runelite.api.Client;
import net.runelite.api.TileItem;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;

public class ItemMarkerOverlay extends Overlay
{
    private final Client client;
    private Map<TileItem, Tile> items;

    @Inject
    public ItemMarkerOverlay(Client client)
    {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.HIGH);
    }

    public void setItems(Map<TileItem, Tile> items)
    {
        this.items = items;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (items == null || items.isEmpty())
        {
            return null;
        }

        for (Map.Entry<TileItem, Tile> entry : items.entrySet())
        {
            TileItem item = entry.getKey();
            Tile tile = entry.getValue();
            LocalPoint localLocation = tile.getLocalLocation();
            if (localLocation == null)
            {
                continue;
            }

            Point runePoint = Perspective.getCanvasTextLocation(client, graphics, localLocation, "", 0);
            if (runePoint == null)
            {
                continue;
            }

            int x = runePoint.getX();
            int y = runePoint.getY();
            int boxWidth = 32;
            int boxHeight = 32;

            // Draw highlight box only
            graphics.setColor(Color.YELLOW);
            graphics.drawRect(x - boxWidth / 2, y - boxHeight / 2, boxWidth, boxHeight);
            graphics.setColor(new Color(255, 255, 0, 50));
            graphics.fillRect(x - boxWidth / 2, y - boxHeight / 2, boxWidth, boxHeight);
        }

        return null;
    }
}