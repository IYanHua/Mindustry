package io.anuke.mindustry.type;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.anuke.mindustry.game.Content;
import io.anuke.mindustry.game.UnlockableContent;
import io.anuke.mindustry.graphics.Palette;
import io.anuke.mindustry.ui.ContentDisplay;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Bundles;
import io.anuke.ucore.util.Log;
import io.anuke.ucore.util.Strings;
import io.anuke.ucore.util.ThreadArray;

public class Item implements Comparable<Item>, UnlockableContent{
    private static final ThreadArray<Item> items = new ThreadArray<>();

    public final int id;
    public final String name;
    public final String description;
    public final Color color;
    public TextureRegion region;

    /**
     * type of the item; used for tabs and core acceptance. default value is {@link ItemType#resource}.
     */
    public ItemType type = ItemType.resource;
    /**
     * how explosive this item is.
     */
    public float explosiveness = 0f;
    /**
     * flammability above 0.3 makes this eleigible for item burners.
     */
    public float flammability = 0f;
    /**
     * how radioactive this item is. 0=none, 1=chernobyl ground zero
     */
    public float radioactivity;
    /**
     * how effective this item is as flux for smelting. 0 = not a flux, 0.5 = normal flux, 1 = very good
     */
    public float fluxiness = 0f;
    /**
     * drill hardness of the item
     */
    public int hardness = 0;
    /**
     * the burning color of this item
     */
    public Color flameColor = Palette.darkFlame.cpy();
    /**
     * base material cost of this item, used for calculating place times
     * 1 cost = 1 tick added to build time
     */
    public float cost = 3f;

    public Item(String name, Color color){
        this.id = items.size;
        this.name = name;
        this.color = color;
        this.description = Bundles.getOrNull("item." + this.name + ".description");

        items.add(this);

        if(!Bundles.has("item." + this.name + ".name")){
            Log.err("Warning: item '" + name + "' is missing a localized name. Add the follow to bundle.properties:");
            Log.err("item." + this.name + ".name=" + Strings.capitalize(name.replace('-', '_')));
        }
    }

    public static Array<Item> all(){
        return Item.items;
    }

    public static Item getByID(int id){
        return items.get(id);
    }

    public void load(){
        this.region = Draw.region("item-" + name);
    }

    @Override
    public void displayInfo(Table table){
        ContentDisplay.displayItem(table, this);
    }

    @Override
    public String localizedName(){
        return Bundles.get("item." + this.name + ".name");
    }

    @Override
    public TextureRegion getContentIcon(){
        return region;
    }

    @Override
    public String toString(){
        return localizedName();
    }

    @Override
    public int compareTo(Item item){
        return Integer.compare(id, item.id);
    }

    @Override
    public String getContentName(){
        return name;
    }

    @Override
    public String getContentTypeName(){
        return "item";
    }

    @Override
    public Array<? extends Content> getAll(){
        return all();
    }
}
