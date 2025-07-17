package com.sleeps.itemmarker;


package net.runelite.client.plugins.itemmarker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("itemmarker")
public interface ItemMarkerConfig extends Config
{
    @ConfigItem(
            keyName = "markedItems",
            name = "Items to Mark",
            description = "Comma-separated list of item names to highlight",
            position = 1
    )
    default String markedItems()
    {
        return "Coins,Rune arrow,Baguette,Fire rune,Spade,Hammer";
    }
}