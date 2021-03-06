package solitudetraveler.chemcraftmod.item;

import net.minecraft.item.Item;

public class ItemList {
    // Tools
    public static Item irradiator_sword;
    public static Item quick_bow;
    // Tool Materials
    public static Item silver_platinum_ingot;
    public static Item silver_platinum_blade;
    public static Item radioactive_coating;
    // Armor
    public static Item gas_mask;
    public static Item lead_coat;
    public static Item lead_pants;
    public static Item rubber_boots;
    // Blocks
    public static Item dolostone;
    // Ore Blocks
    public static Item copper_ore;
    public static Item nickel_ore;
    public static Item aluminium_ore;
    public static Item tin_ore;
    public static Item silver_ore;
    public static Item lead_ore;
    public static Item platinum_ore;
    public static Item chromium_ore;
    // New Ingots
    public static Item silicon_ingot;
    public static Item copper_ingot;
    public static Item nickel_ingot;
    public static Item aluminium_ingot;
    public static Item tin_ingot;
    public static Item silver_ingot;
    public static Item lead_ingot;
    public static Item platinum_ingot;
    public static Item chromium_ingot;
    // Machine Blocks
    public static Item generator;
    public static Item reconstructor;
    public static Item deconstructor;
    public static Item electromagnet;
    public static Item accelerator;
    public static Item assembler;
    public static Item volcano;
    // Minerals
    public static Item aragonite;
    public static Item calcite;
    public static Item sodalite;
    public static Item fluorite;
    public static Item andradite;
    public static Item zircon;
    public static Item ilmenite;
    // New Items
    public static Item water;
    public static Item salt;
    public static Item soap;
    public static Item baking_soda;
    public static Item vinegar;
    public static Item bleach;
    public static Item sap;
    public static Item rubber;
    public static Item copper_wire;
    public static Item electromagnetic_coil;
    public static Item heating_coil;
    public static Item machine_core;
    public static Item advanced_machine_core;
    // Covalent Compounds
    public static Item sulfate;
    public static Item sulfite;
    public static Item nitrate;
    public static Item nitrite;
    public static Item carbonate;
    public static Item bicarbonate;
    public static Item hydroxide;
    public static Item acetate;
    public static Item methyl_group;
    public static Item methylene_group;
    public static Item propane;
    public static Item hydrogen_peroxide;
    // Ionic Compounds
    public static Item zinc_oxide;
    public static Item sodium_chloride;
    public static Item sodium_bicarbonate;
    public static Item sodium_hydroxide;
    public static Item acetic_acid;
    public static Item silver_sulfide;
    // Elements
    public static Item proton;
    public static Item neutron;
    public static Item electron;
    public static ElementItem unknown;
    public static ElementItem[] elementList = new ElementItem[118];

    public static ElementItem getElementNumber(int n) {
        if(n >= elementList.length || n < 1) return unknown;
        else return elementList[n - 1];
    }
}
