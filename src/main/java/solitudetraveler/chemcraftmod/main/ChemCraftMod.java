package solitudetraveler.chemcraftmod.main;

import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solitudetraveler.chemcraftmod.block.*;
import solitudetraveler.chemcraftmod.container.*;
import solitudetraveler.chemcraftmod.effect.EffectList;
import solitudetraveler.chemcraftmod.effect.RadiationEffect;
import solitudetraveler.chemcraftmod.generation.Config;
import solitudetraveler.chemcraftmod.generation.OreGeneration;
import solitudetraveler.chemcraftmod.handler.ChemCraftEventHandler;
import solitudetraveler.chemcraftmod.item.*;
import solitudetraveler.chemcraftmod.item.armor.HazmatArmorItem;
import solitudetraveler.chemcraftmod.item.tools.IrradiatorSword;
import solitudetraveler.chemcraftmod.item.tools.QuickBow;
import solitudetraveler.chemcraftmod.tileentity.*;

import java.util.ArrayList;
import java.util.Objects;

@Mod("chemcraftmod")
public class ChemCraftMod {
    public static final String MOD_ID = "chemcraftmod";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public ChemCraftMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG, MOD_ID + "-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG, MOD_ID + "-client.toml");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);

        Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-server.toml").toString());
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-client.toml").toString());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
        // Generate ores in world
        OreGeneration.setupOreGeneration();
        // Register in-game events
        MinecraftForge.EVENT_BUS.register(ChemCraftEventHandler.class);

        LOGGER.info("Chemcraft setup complete!");
    }

    private void clientRegistries(final FMLClientSetupEvent event) {
        LOGGER.info("Client registries method complete!");
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            // Sub atomic particles
            event.getRegistry().registerAll(
                    ItemList.proton = new AtomicItem(location("proton")),
                    ItemList.neutron = new AtomicItem(location("neutron")),
                    ItemList.electron = new AtomicItem(location("electron")),
                    ItemList.unknown = new ElementItem(location("unknown"), -1)
                    );
            // Initialize elements
            for(int i = 1; i <= ElementInfo.getCount(); i++) {
                event.getRegistry().register(ItemList.elementList[i - 1] = new ElementItem(location(ElementInfo.getName(i)), i));
            }
            // Register items
            event.getRegistry().registerAll(
                    // Tools
                    ItemList.radioactive_coating = new BasicItem(location("radioactive_coating"), 16),
                    ItemList.silver_platinum_blade = new BasicItem(location("silver_platinum_blade"), 1),
                    ItemList.irradiator_sword = new IrradiatorSword(location("irradiator_sword")),
                    ItemList.quick_bow = new QuickBow(location("quick_bow")),
                    // Hazmat suit
                    ItemList.gas_mask = new HazmatArmorItem(location("gas_mask"), EquipmentSlotType.HEAD, 300),
                    ItemList.lead_coat = new HazmatArmorItem(location("lead_coat"), EquipmentSlotType.CHEST, 1100),
                    ItemList.lead_pants = new HazmatArmorItem(location("lead_pants"), EquipmentSlotType.LEGS, 800),
                    ItemList.rubber_boots = new HazmatArmorItem(location("rubber_boots"), EquipmentSlotType.FEET, 450),
                    // Blocks
                    ItemList.dolostone = new BasicBlockItem(BlockList.dolostone),
                    ItemList.copper_ore = new BasicBlockItem(BlockList.copper_ore),
                    ItemList.nickel_ore = new BasicBlockItem(BlockList.nickel_ore),
                    ItemList.aluminium_ore = new BasicBlockItem(BlockList.aluminium_ore),
                    ItemList.tin_ore = new BasicBlockItem(BlockList.tin_ore),
                    ItemList.silver_ore = new BasicBlockItem(BlockList.silver_ore),
                    ItemList.lead_ore = new BasicBlockItem(BlockList.lead_ore),
                    ItemList.platinum_ore = new BasicBlockItem(BlockList.platinum_ore),
                    ItemList.chromium_ore = new BasicBlockItem(BlockList.chromium_ore),
                    // Machines
                    ItemList.electromagnet = new BasicBlockItem(BlockList.electromagnet),
                    ItemList.reconstructor = new BasicBlockItem(BlockList.reconstructor, Rarity.UNCOMMON),
                    ItemList.deconstructor = new BasicBlockItem(BlockList.deconstructor, Rarity.UNCOMMON),
                    ItemList.generator = new BasicBlockItem(BlockList.generator),
                    ItemList.accelerator = new BasicBlockItem(BlockList.accelerator, Rarity.UNCOMMON),
                    ItemList.assembler = new BasicBlockItem(BlockList.assembler, Rarity.UNCOMMON),
                    // Experiments
                    ItemList.volcano = new BasicBlockItem(BlockList.volcano, Rarity.EPIC),
                    // Minerals
                    ItemList.aragonite = new MineralItem(location("aragonite")),
                    ItemList.calcite = new MineralItem(location("calcite")),
                    ItemList.sodalite = new MineralItem(location("sodalite")),
                    ItemList.fluorite = new MineralItem(location("fluorite")),
                    ItemList.andradite = new MineralItem(location("andradite")),
                    ItemList.zircon = new MineralItem(location("zircon")),
                    ItemList.ilmenite = new MineralItem(location("ilmenite")),
                    // Ingots
                    ItemList.copper_ingot = new BasicItem(location("copper_ingot")),
                    ItemList.silicon_ingot = new BasicItem(location("silicon_ingot")),
                    ItemList.aluminium_ingot = new BasicItem(location("aluminium_ingot")),
                    ItemList.tin_ingot = new BasicItem(location("tin_ingot")),
                    ItemList.nickel_ingot = new BasicItem(location("nickel_ingot")),
                    ItemList.silver_ingot = new BasicItem(location("silver_ingot")),
                    ItemList.lead_ingot = new BasicItem(location("lead_ingot")),
                    ItemList.platinum_ingot = new BasicItem(location("platinum_ingot")),
                    ItemList.chromium_ingot = new BasicItem(location("chromium_ingot")),
                    ItemList.silver_platinum_ingot = new BasicItem(location("silver_platinum_ingot")),
                    // Items
                    ItemList.salt = new BasicItem(location("salt")),
                    ItemList.soap = new BasicItem(location("soap")),
                    ItemList.baking_soda = new BasicItem(location("baking_soda")),
                    ItemList.vinegar = new BasicItem(location("vinegar")),
                    ItemList.bleach = new BasicItem(location("bleach")),
                    ItemList.sap = new BasicItem(location("sap")),
                    ItemList.rubber = new BasicItem(location("rubber")),
                    ItemList.electromagnetic_coil = new BasicItem(location("electromagnetic_coil")),
                    ItemList.heating_coil = new BasicItem(location("heating_coil")),
                    ItemList.copper_wire = new BasicItem(location("copper_wire")),
                    ItemList.machine_core = new BasicItem(location("machine_core")),
                    ItemList.advanced_machine_core = new BasicItem(location("advanced_machine_core")),
                    // Covalent Compounds
                    ItemList.water = new CompoundItem(location("water"), "H2O"),
                    ItemList.hydrogen_peroxide = new CompoundItem(location("hydrogen_peroxide"), "H2O2"),
                    ItemList.acetate = new CompoundItem(location("acetate"), "C2H3O2"),
                    ItemList.sulfate = new CompoundItem(location("sulfate"), "SO4"),
                    ItemList.sulfite = new CompoundItem(location("sulfite"), "SO3"),
                    ItemList.nitrate = new CompoundItem(location("nitrate"), "NO3"),
                    ItemList.nitrite = new CompoundItem(location("nitrite"), "NO2"),
                    ItemList.carbonate = new CompoundItem(location("carbonate"), "CO3"),
                    ItemList.bicarbonate = new CompoundItem(location("bicarbonate"), "HCO3"),
                    ItemList.hydroxide = new CompoundItem(location("hydroxide"), "OH"),
                    ItemList.methyl_group = new CompoundItem(location("methyl_group"), "CH3"),
                    ItemList.methylene_group = new CompoundItem(location("methylene_group"), "CH2"),
                    ItemList.propane = new CompoundItem(location("propane"), "C3H8"),
                    // Ionic Compounds
                    ItemList.zinc_oxide = new CompoundItem(location("zinc_oxide"), "ZnO"),
                    ItemList.sodium_chloride = new CompoundItem(location("sodium_chloride"), "NaCl"),
                    ItemList.sodium_bicarbonate = new CompoundItem(location("sodium_bicarbonate"), "NaHCO3"),
                    ItemList.sodium_hydroxide = new CompoundItem(location("sodium_hydroxide"), "NaOH"),
                    ItemList.acetic_acid = new CompoundItem(location("acetic_acid"), "C2H4O2"),
                    ItemList.silver_sulfide = new CompoundItem(location("silver_sulfide"), "Ag2S")
            );

            LOGGER.info("Chemcraft items registered!");
        }

        @SubscribeEvent
        public static void registerPotionEffects(final RegistryEvent.Register<Effect> event) {
            event.getRegistry().registerAll(
                    EffectList.radiation = new RadiationEffect(location("radiation"))
            );

            LOGGER.info("Chemcraft potion effects registered!");
        }

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    BlockList.dolostone = new Block(BlockVariables.rockProperties).setRegistryName(location("dolostone")),
                    // Ores
                    BlockList.copper_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("copper_ore")),
                    BlockList.nickel_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("nickel_ore")),
                    BlockList.aluminium_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("aluminium_ore")),
                    BlockList.tin_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("tin_ore")),
                    BlockList.silver_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("silver_ore")),
                    BlockList.lead_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("lead_ore")),
                    BlockList.platinum_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("platinum_ore")),
                    BlockList.chromium_ore = new Block(BlockVariables.rockProperties).setRegistryName(location("chromium_ore")),
                    // Machines
                    BlockList.generator = new GeneratorBlock(location("generator"), BlockVariables.machineProperties),
                    BlockList.reconstructor = new ReconstructorBlock(location("reconstructor"), BlockVariables.machineProperties),
                    BlockList.deconstructor = new DeconstructorBlock(location("deconstructor"), BlockVariables.machineProperties),
                    BlockList.electromagnet = new ElectromagnetBlock(location("electromagnet"), BlockVariables.rockProperties),
                    BlockList.accelerator = new AcceleratorBlock(location("accelerator"), BlockVariables.machineProperties),
                    BlockList.assembler = new AssemblerBlock(location("assembler"), BlockVariables.machineProperties),
                    BlockList.volcano = new VolcanoBlock(location("volcano"), BlockVariables.rockProperties)
            );

            LOGGER.info("Chemcraft blocks registered!");
        }

        @SubscribeEvent
        public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().registerAll(
                    TileEntityType.Builder.create(GeneratorTileEntity::new, BlockList.generator).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.generator.getRegistryName())),
                    TileEntityType.Builder.create(ReconstructorTileEntity::new, BlockList.reconstructor).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.reconstructor.getRegistryName())),
                    TileEntityType.Builder.create(DeconstructorTileEntity::new, BlockList.deconstructor).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.deconstructor.getRegistryName())),
                    TileEntityType.Builder.create(VolcanoTileEntity::new, BlockList.volcano).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.volcano.getRegistryName())),
                    TileEntityType.Builder.create(AcceleratorTileEntity::new, BlockList.accelerator).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.accelerator.getRegistryName())),
                    TileEntityType.Builder.create(AssemblerTileEntity::new, BlockList.assembler).build(null)
                            .setRegistryName(Objects.requireNonNull(BlockList.assembler.getRegistryName()))
            );

            LOGGER.info("Chemcraft tile entities registered!");
        }

        @SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
            ArrayList<ContainerType<?>> containers = new ArrayList<>();

            containers.add(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new GeneratorContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            }).setRegistryName(Objects.requireNonNull(BlockList.generator.getRegistryName())));

            containers.add(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new ReconstructorContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            }).setRegistryName(Objects.requireNonNull(BlockList.reconstructor.getRegistryName())));

            containers.add(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new DeconstructorContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            }).setRegistryName(Objects.requireNonNull(BlockList.deconstructor.getRegistryName())));

            containers.add(IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new VolcanoContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            })).setRegistryName(Objects.requireNonNull(BlockList.volcano.getRegistryName())));

            containers.add(IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new AcceleratorContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            })).setRegistryName(Objects.requireNonNull(BlockList.accelerator.getRegistryName())));

            containers.add(IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new AssemblerContainer(windowId, ChemCraftMod.proxy.getClientWorld(), pos, inv, ChemCraftMod.proxy.getClientPlayer());
            })).setRegistryName(Objects.requireNonNull(BlockList.assembler.getRegistryName())));

            for(ContainerType<?> type : containers) {
                event.getRegistry().register(type);
            }

            LOGGER.info("Chemcraft containers registered!");
        }

        private static ResourceLocation location(String name) {
            return new ResourceLocation(MOD_ID, name);
        }
    }
}
