package solitudetraveler.chemcraftmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import solitudetraveler.chemcraftmod.creativetab.CreativeTabList;

public class HazmatArmorItem extends ArmorItem {
    public HazmatArmorItem(ResourceLocation name, ArmorMaterial material, EquipmentSlotType type, int damage) {
        super(material, type, new Item.Properties().maxDamage(damage).group(CreativeTabList.basicItemGroup));

        setRegistryName(name);
    }

    @Override
    public int getDamageReduceAmount() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(stack.getDamage() < stack.getMaxDamage())
            // Heal by 1 on tick when in inventory
            stack.damageItem(-1, (LivingEntity) entityIn, (entity) -> {});
    }
}
