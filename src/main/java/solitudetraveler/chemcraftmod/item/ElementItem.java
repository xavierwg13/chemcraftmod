package solitudetraveler.chemcraftmod.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import solitudetraveler.chemcraftmod.ChemCraftMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ElementItem extends Item {

    private int aNum;

    public ElementItem(int atomicNumber) {
        super(new Item.Properties().group(ChemCraftMod.elementsGroup).maxStackSize(32).rarity(Rarity.RARE));

        aNum = atomicNumber;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        //noinspection NullableProblems
        tooltip.add(new TextComponent() {
            @Nonnull
            @Override
            public String getUnformattedComponentText() {
                return "Element number " + aNum + ".";
            }

            @Override
            public ITextComponent shallowCopy() {
                return null;
            }
        });
    }
}
