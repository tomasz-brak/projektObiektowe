import json

with open("items.json", "r") as f:
    items = json.load(f)

def get_item_price(name):
    # Air
    if name == 'air':
        return 0, 0, 0

    # 1. Creative / Unobtainable / Admin Blocks
    admin_unobtainable = {
        'bedrock': 50000,
        'barrier': 50000,
        'structure_block': 50000,
        'structure_void': 50000,
        'jigsaw': 50000,
        'light': 50000,
        'command_block': 75000,
        'chain_command_block': 75000,
        'repeating_command_block': 75000,
        'command_block_minecart': 75000,
        'debug_stick': 50000,
        'knowledge_book': 50000,
        'end_portal_frame': 50000,
        'spawner': 20000,
        'vault': 25000,
        'trial_spawner': 25000,
        'test_block': 50000,
        'test_instance_block': 50000,
    }
    if name in admin_unobtainable:
        p = admin_unobtainable[name]
        return round(p * 0.6), p, round(p * 1.5)

    # 2. Epic & Ultra-Rare Treasures / Boss Drops
    epic_items = {
        'dragon_egg': 10000,
        'mace': 5000,
        'heavy_core': 4000,
        'elytra': 3500,
        'beacon': 2800,
        'nether_star': 2500,
        'silence_armor_trim_smithing_template': 2500,
        'dragon_head': 2000,
        'enchanted_golden_apple': 2000,
        'lodestone': 1600,
        'recovery_compass': 1500,
        'netherite_upgrade_smithing_template': 1200,
        'conduit': 1200,
        'trident': 900,
        'totem_of_undying': 800,
        'heart_of_the_sea': 750,
        'creaking_heart': 500,
        'wither_skeleton_skull': 400,
        'sniffer_egg': 400,
        'ancient_debris': 400,
        'netherite_scrap': 350,
        'ominous_trial_key': 350,
        'ender_chest': 350,
        'enchanting_table': 350,
        'shulker_shell': 300,
        'respawn_anchor': 250,
        'golden_apple': 220,
        'end_crystal': 200,
        'echo_shard': 180,
        'turtle_helmet': 180,
        'nautilus_shell': 150,
        'ominous_bottle': 150,
        'sponge': 150,
        'wet_sponge': 140,
        'trial_key': 120,
        'saddle': 120,
        'sculk_catalyst': 200,
        'sculk_shrieker': 250,
        'calibrated_sculk_sensor': 180,
        'sculk_sensor': 120,
        'clock': 110,
        'cauldron': 105,
        'crafter': 90,
        'dragon_breath': 100,
        'name_tag': 80,
        'ghast_tear': 80,
        'sea_lantern': 80,
        'blue_ice': 80,
        'spore_blossom': 80,
        'goat_horn': 80,
        'wolf_armor': 80,
        'enchanted_book': 80,
        'dried_ghast': 80,
        'breeze_rod': 60,
        'wither_rose': 60,
        'rabbit_foot': 60,
        'eye_of_ender': 60,
        'ender_eye': 60,
        'turtle_egg': 50,
        'experience_bottle': 50,
        'brewing_stand': 50,
        'ender_pearl': 40,
        'bookshelf': 40,
        'chiseled_bookshelf': 40,
        'blaze_rod': 35,
        'scute': 30,
        'turtle_scute': 30,
        'golden_carrot': 35,
        'glistering_melon_slice': 30,
        'crying_obsidian': 30,
        'obsidian': 15,
        'armadillo_scute': 15,
        'wind_charge': 15,
        'glow_item_frame': 25,
        'item_frame': 12,
        'painting': 15,
        'armor_stand': 15,
        'lead': 25,
        'compass': 65,
        'spyglass': 25,
        'brush': 15,
        'bell': 150,
        'jukebox': 135,
        'lectern': 25,
        'decorated_pot': 25,
        'cobweb': 8,
        'fire_charge': 15,
        'frogspawn': 15,
        'stick': 1,
        'bowl': 1,
        'ladder': 2,
        'scaffolding': 2,
    }
    if name in epic_items:
        p = epic_items[name]
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 3. Spawn Eggs
    if name.endswith('_spawn_egg'):
        if any(k in name for k in ['warden', 'ender_dragon', 'wither', 'elder_guardian']):
            p = 4000
        elif any(k in name for k in ['wither_skeleton', 'evoker', 'vindicator', 'ravager', 'breeze', 'piglin_brute', 'ghast', 'shulker', 'guardian', 'iron_golem']):
            p = 1200
        elif any(k in name for k in ['creeper', 'enderman', 'witch', 'blaze', 'phantom', 'drowned', 'zombie_villager', 'pillager', 'hoglin', 'zoglin', 'bogged', 'creaking']):
            p = 800
        elif any(k in name for k in ['allay', 'sniffer', 'camel', 'armadillo', 'axolotl', 'frog', 'tadpole', 'mooshroom', 'horse', 'donkey', 'mule', 'llama', 'villager', 'snow_golem']):
            p = 600
        elif any(k in name for k in ['zombie', 'skeleton', 'spider', 'cave_spider', 'slime', 'magma_cube', 'silverfish', 'endermite', 'husk', 'stray', 'piglin', 'zombified_piglin']):
            p = 500
        else:
            p = 350
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 4. Music Discs
    if name.startswith('music_disc_'):
        if any(k in name for k in ['pigstep', 'otherside', 'relic', '5', 'creator', 'precipice', 'tears']):
            p = 800
        elif 'creator_music_box' in name:
            p = 600
        else:
            p = 300
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 5. Smithing Templates
    if name.endswith('_smithing_template'):
        p = 500
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 6. Pottery Sherds
    if name.endswith('_pottery_sherd'):
        p = 200
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 7. Banner Patterns
    if name.endswith('_banner_pattern'):
        p = 300
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 8. Mob Heads
    if any(name == k for k in ['skeleton_skull', 'zombie_head', 'creeper_head', 'piglin_head', 'player_head']):
        p = 350
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 9. Shulker Boxes
    if 'shulker_box' in name:
        p = 650
        return max(1, round(p * 0.65)), p, round(p * 1.5)

    # 10. Netherite gear & materials
    if 'netherite' in name:
        if name == 'netherite_ingot': p = 1500
        elif name == 'netherite_block': p = 13500
        elif name == 'netherite_chestplate': p = 1900
        elif name == 'netherite_leggings': p = 1800
        elif name == 'netherite_helmet': p = 1600
        elif name == 'netherite_boots': p = 1500
        elif name == 'netherite_sword': p = 1600
        elif name in ['netherite_pickaxe', 'netherite_axe']: p = 1700
        elif name == 'netherite_shovel': p = 1550
        elif name == 'netherite_hoe': p = 1600
        elif name == 'netherite_spear': p = 1650
        else: p = 1500
        return max(1, round(p * 0.65)), p, round(p * 1.5)

    # 11. Diamond gear & materials
    if 'diamond' in name:
        if name == 'diamond': p = 120
        elif name == 'diamond_block': p = 1080
        elif 'ore' in name: p = 200
        elif name == 'diamond_chestplate': p = 960
        elif name == 'diamond_leggings': p = 840
        elif name == 'diamond_helmet': p = 600
        elif name == 'diamond_boots': p = 480
        elif name == 'diamond_horse_armor': p = 600
        elif name in ['diamond_pickaxe', 'diamond_axe']: p = 360
        elif name == 'diamond_sword': p = 240
        elif name == 'diamond_hoe': p = 240
        elif name == 'diamond_spear': p = 260
        elif name == 'diamond_shovel': p = 120
        else: p = 150
        return max(1, round(p * 0.65)), p, round(p * 1.5)

    # 12. Golden gear & materials
    if 'golden_' in name or 'gold_' in name or name.startswith('raw_gold') or name in ['gold_ingot', 'gold_nugget', 'gold_block', 'light_weighted_pressure_plate']:
        if name == 'gold_ingot': p = 25
        elif name == 'raw_gold': p = 20
        elif name == 'raw_gold_block': p = 180
        elif name == 'gold_block': p = 225
        elif name == 'gold_nugget': p = 3
        elif 'ore' in name: p = 40
        elif name == 'golden_chestplate': p = 200
        elif name == 'golden_leggings': p = 175
        elif name == 'golden_helmet': p = 125
        elif name == 'golden_boots': p = 100
        elif name == 'golden_horse_armor': p = 250
        elif name in ['golden_pickaxe', 'golden_axe']: p = 75
        elif name == 'golden_sword': p = 50
        elif name == 'golden_hoe': p = 50
        elif name == 'golden_spear': p = 55
        elif name == 'golden_shovel': p = 25
        elif name == 'gilded_blackstone': p = 40
        elif name == 'light_weighted_pressure_plate': p = 50
        else: p = 30
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 13. Iron gear & materials
    if 'iron_' in name or 'raw_iron' in name or name in ['iron_ingot', 'iron_nugget', 'iron_block', 'chain', 'iron_chain', 'heavy_weighted_pressure_plate']:
        if name == 'iron_ingot': p = 15
        elif name == 'raw_iron': p = 12
        elif name == 'raw_iron_block': p = 108
        elif name == 'iron_block': p = 135
        elif name == 'iron_nugget': p = 2
        elif 'ore' in name: p = 25
        elif name == 'iron_bars': p = 6
        elif name == 'iron_door': p = 30
        elif name == 'iron_trapdoor': p = 60
        elif name == 'iron_chestplate': p = 120
        elif name == 'iron_leggings': p = 105
        elif name == 'iron_helmet': p = 75
        elif name == 'iron_boots': p = 60
        elif name in ['iron_pickaxe', 'iron_axe']: p = 45
        elif name == 'iron_sword': p = 30
        elif name == 'iron_hoe': p = 30
        elif name == 'iron_spear': p = 35
        elif name == 'iron_shovel': p = 15
        elif name == 'iron_horse_armor': p = 150
        elif name in ['iron_chain', 'chain']: p = 8
        elif name == 'heavy_weighted_pressure_plate': p = 30
        else: p = 20
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 14. Anvil variants
    if 'anvil' in name:
        if name == 'anvil': p = 465
        elif name == 'chipped_anvil': p = 350
        elif name == 'damaged_anvil': p = 200
        else: p = 300
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 15. Chainmail gear
    if 'chainmail_' in name:
        if name == 'chainmail_chestplate': p = 100
        elif name == 'chainmail_leggings': p = 85
        elif name == 'chainmail_helmet': p = 60
        elif name == 'chainmail_boots': p = 50
        else: p = 70
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 16. Emerald items
    if 'emerald' in name:
        if name == 'emerald': p = 50
        elif name == 'emerald_block': p = 450
        elif 'ore' in name: p = 150
        else: p = 50
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 17. Lapis Lazuli
    if 'lapis' in name:
        if name == 'lapis_lazuli': p = 8
        elif name == 'lapis_block': p = 72
        elif 'ore' in name: p = 20
        else: p = 10
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 18. Redstone items
    if 'redstone' in name:
        if name == 'redstone': p = 8
        elif name == 'redstone_block': p = 72
        elif 'ore' in name: p = 20
        elif name == 'redstone_torch': p = 10
        elif name == 'redstone_lamp': p = 40
        else: p = 15
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 19. Amethyst items
    if 'amethyst' in name:
        if name == 'amethyst_shard': p = 12
        elif name == 'amethyst_block': p = 48
        elif name == 'budding_amethyst': p = 150
        elif name == 'amethyst_cluster': p = 40
        elif 'large_amethyst' in name: p = 30
        elif 'medium_amethyst' in name: p = 20
        elif 'small_amethyst' in name: p = 10
        else: p = 20
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 20. Quartz items
    if 'quartz' in name:
        if name == 'quartz': p = 10
        elif 'ore' in name: p = 18
        elif any(k in name for k in ['slab']): p = 20
        elif any(k in name for k in ['stairs']): p = 30
        else: p = 40
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 21. Coal & Charcoal
    if name in ['coal', 'charcoal', 'coal_block', 'coal_ore', 'deepslate_coal_ore', 'torch', 'soul_torch', 'campfire', 'soul_campfire']:
        if name in ['coal', 'charcoal']: p = 4
        elif name == 'coal_block': p = 36
        elif 'ore' in name: p = 10
        elif name == 'torch': p = 2
        elif name == 'soul_torch': p = 3
        elif name == 'campfire': p = 15
        elif name == 'soul_campfire': p = 18
        else: p = 5
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 22. Copper items & variations
    if 'copper' in name or 'raw_copper' in name or 'lightning_rod' in name:
        if name == 'copper_ingot': p = 5
        elif name == 'raw_copper': p = 4
        elif name == 'raw_copper_block': p = 36
        elif name == 'copper_nugget': p = 1
        elif 'ore' in name: p = 12
        elif 'bulb' in name: p = 20
        elif 'grate' in name: p = 15
        elif 'door' in name: p = 12
        elif 'trapdoor' in name: p = 24
        elif 'spear' in name: p = 15
        elif 'golem_statue' in name: p = 50
        elif 'torch' in name: p = 8
        elif 'slab' in name: p = 20
        elif 'stairs' in name: p = 35
        elif 'lightning_rod' in name: p = 15
        else: p = 45 # blocks, cut blocks, waxed, oxidized
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 23. Leather gear & materials
    if 'leather' in name:
        if name == 'leather': p = 8
        elif name == 'leather_chestplate': p = 40
        elif name == 'leather_leggings': p = 35
        elif name == 'leather_helmet': p = 25
        elif name == 'leather_boots': p = 20
        elif name == 'leather_horse_armor': p = 60
        else: p = 15
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 24. Stone & Wooden Tools
    if name.startswith('stone_') and any(name.endswith(k) for k in ['sword', 'pickaxe', 'axe', 'shovel', 'hoe', 'spear']):
        if name.endswith('shovel'): p = 4
        elif name.endswith('hoe') or name.endswith('sword'): p = 6
        elif name.endswith('spear'): p = 7
        else: p = 8
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    if name.startswith('wooden_') and any(name.endswith(k) for k in ['sword', 'pickaxe', 'axe', 'shovel', 'hoe', 'spear']):
        if name.endswith('shovel'): p = 2
        elif name.endswith('hoe') or name.endswith('sword'): p = 3
        elif name.endswith('spear'): p = 4
        else: p = 4
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 25. Minecarts & Rails & Vehicles
    if 'minecart' in name:
        if name == 'minecart': p = 75
        elif name in ['chest_minecart', 'furnace_minecart']: p = 85
        elif name == 'tnt_minecart': p = 115
        elif name == 'hopper_minecart': p = 155
        else: p = 80
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    if 'rail' in name:
        if name == 'powered_rail': p = 25
        elif name in ['detector_rail', 'activator_rail']: p = 15
        elif name == 'rail': p = 8
        else: p = 10
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    if 'boat' in name or 'raft' in name:
        if 'chest' in name: p = 12
        else: p = 8
        return max(1, round(p * 0.6)), p, round(p * 1.5)

    # 26. Redstone Mechanisms & Workstations
    mech_workstations = {
        'repeater': 25, 'comparator': 35, 'observer': 40, 'piston': 35,
        'sticky_piston': 55, 'dispenser': 45, 'dropper': 30, 'hopper': 80,
        'target': 20, 'daylight_detector': 35, 'lever': 2, 'tripwire_hook': 10,
        'trapped_chest': 20, 'chest': 10, 'barrel': 10, 'tnt': 40,
        'note_block': 20, 'crafting_table': 4, 'furnace': 10, 'smoker': 25,
        'blast_furnace': 80, 'smithing_table': 35, 'fletching_table': 20,
        'cartography_table': 25, 'loom': 20, 'stonecutter': 25, 'grindstone': 25,
        'composter': 15, 'beehive': 30, 'bee_nest': 45, 'flower_pot': 6,
        'shield': 25, 'bow': 15, 'crossbow': 35, 'fishing_rod': 12,
        'shears': 30, 'flint_and_steel': 20, 'carrot_on_a_stick': 15,
        'warped_fungus_on_a_stick': 15, 'bucket': 45, 'water_bucket': 50,
        'milk_bucket': 50, 'lava_bucket': 65, 'powder_snow_bucket': 55,
        'glass_bottle': 2, 'potion': 25, 'splash_potion': 35, 'lingering_potion': 50,
        'arrow': 4, 'spectral_arrow': 12, 'tipped_arrow': 25
    }
    if name in mech_workstations:
        p = mech_workstations[name]
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 27. Nether materials & blocks
    if any(k in name for k in ['netherrack', 'nether_brick', 'soul_sand', 'soul_soil', 'basalt', 'blackstone', 'magma_block', 'glowstone', 'shroomlight', 'ochre_froglight', 'verdant_froglight', 'pearlescent_froglight']):
        if name == 'netherrack': p = 1
        elif name == 'nether_brick': p = 3
        elif 'bricks' in name:
            if 'slab' in name: p = 6
            elif 'stairs' in name: p = 10
            elif 'wall' in name: p = 8
            else: p = 12
        elif 'blackstone' in name or 'basalt' in name:
            if 'slab' in name: p = 1
            elif 'stairs' in name: p = 2
            elif 'wall' in name: p = 2
            else: p = 2
        elif name in ['soul_sand', 'soul_soil']: p = 3
        elif name == 'magma_block': p = 8
        elif name == 'glowstone_dust': p = 10
        elif name == 'glowstone': p = 40
        elif name == 'shroomlight': p = 20
        elif 'froglight' in name: p = 60
        else: p = 5
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 28. End & Ocean materials
    if any(k in name for k in ['end_stone', 'purpur', 'prismarine', 'sea_pickle', 'sponge', 'wet_sponge', 'chorus', 'end_rod']):
        if 'end_stone' in name:
            if 'slab' in name: p = 2
            elif 'stairs' in name: p = 3
            elif 'wall' in name: p = 3
            else: p = 3
        elif 'purpur' in name:
            if 'slab' in name: p = 3
            elif 'stairs' in name: p = 5
            else: p = 6
        elif 'prismarine' in name:
            if name == 'prismarine_shard': p = 15
            elif name == 'prismarine_crystals': p = 25
            elif 'slab' in name: p = 15
            elif 'stairs' in name: p = 25
            elif 'wall' in name: p = 20
            elif 'dark' in name: p = 50
            elif 'bricks' in name: p = 45
            else: p = 30
        elif name == 'sea_pickle': p = 15
        elif name == 'end_rod': p = 25
        elif name == 'chorus_flower': p = 25
        elif name == 'chorus_plant': p = 10
        elif name == 'chorus_fruit': p = 8
        elif name == 'popped_chorus_fruit': p = 9
        else: p = 10
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 29. Corals
    if 'coral' in name:
        if 'dead' in name:
            if 'block' in name: p = 4
            else: p = 2
        else:
            if 'block' in name: p = 12
            else: p = 6
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 30. Buckets with mob / fish
    if name.endswith('_bucket'):
        if 'axolotl' in name: p = 120
        elif 'pufferfish' in name: p = 70
        elif 'tropical_fish' in name: p = 60
        elif 'tadpole' in name: p = 60
        else: p = 55
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 31. Bundles & Harnesses
    if 'bundle' in name:
        p = 25
        return max(1, round(p * 0.6)), p, round(p * 1.6)
    if 'harness' in name:
        p = 30
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 32. Resin & Pale Garden
    if 'resin' in name or 'creaking' in name or 'pale_' in name:
        if name == 'resin_clump': p = 10
        elif name == 'resin_brick': p = 10
        elif name in ['resin_block', 'resin_bricks', 'chiseled_resin_bricks']: p = 40
        elif 'slab' in name: p = 20
        elif 'stairs' in name: p = 35
        elif 'wall' in name: p = 30
        elif name == 'pale_hanging_moss': p = 8
        elif name == 'pale_moss_block': p = 6
        elif name == 'pale_moss_carpet': p = 4
        elif 'log' in name or 'wood' in name: p = 4
        elif 'planks' in name: p = 2
        elif 'sapling' in name: p = 4
        else: p = 10
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 33. Sculk
    if 'sculk' in name:
        if name == 'sculk': p = 15
        elif name == 'sculk_vein': p = 10
        else: p = 20
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 34. Wood types: logs, planks, leaves, saplings, signs, slabs, stairs, fences, etc.
    wood_types = ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'cherry', 'pale_oak', 'mangrove', 'bamboo', 'crimson', 'warped']
    if any(k in name for k in wood_types):
        if 'sapling' in name or 'propagule' in name: p = 4
        elif 'leaves' in name: p = 2
        elif 'log' in name or 'wood' in name or 'stem' in name or 'hyphae' in name or name in ['bamboo_block', 'stripped_bamboo_block']: p = 4
        elif name == 'bamboo': p = 2
        elif 'planks' in name or 'bamboo_mosaic' in name: p = 2
        elif 'slab' in name: p = 1
        elif 'stairs' in name: p = 2
        elif 'fence_gate' in name: p = 4
        elif 'fence' in name: p = 2
        elif 'hanging_sign' in name: p = 6
        elif 'sign' in name: p = 3
        elif 'door' in name: p = 4
        elif 'trapdoor' in name: p = 4
        elif 'button' in name: p = 1
        elif 'pressure_plate' in name: p = 2
        elif 'boat' in name: p = 8
        else: p = 3
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 35. Rocks, Stones, Mud, Bricks, Tuff, Deepslate, Granite, Diorite, Andesite, Calcite, Dripstone, Sandstone
    stone_types = ['stone', 'granite', 'diorite', 'andesite', 'deepslate', 'tuff', 'calcite', 'dripstone', 'sandstone', 'mud', 'brick', 'cobble']
    if any(k in name for k in stone_types):
        if 'slab' in name: p = 1
        elif 'stairs' in name: p = 2
        elif 'wall' in name: p = 2
        elif 'bricks' in name or 'tiles' in name: p = 2
        elif 'polished' in name or 'chiseled' in name or 'smooth' in name or 'cut' in name: p = 2
        elif name in ['dirt', 'mud', 'clay', 'gravel', 'sand', 'cobblestone', 'cobbled_deepslate', 'tuff']: p = 2
        elif 'pointed_dripstone' in name: p = 3
        else: p = 2
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 36. Earth, Soils, Sands, Gravel, Clay, Ice, Snow
    earth_types = ['dirt', 'sand', 'gravel', 'clay', 'ice', 'snow', 'podzol', 'mycelium', 'grass', 'path', 'farmland', 'flint']
    if any(k in name for k in earth_types):
        if name == 'flint': p = 4
        elif name == 'clay_ball': p = 2
        elif name == 'clay': p = 6
        elif name in ['packed_ice']: p = 20
        elif name in ['ice']: p = 4
        elif name in ['snowball', 'snow']: p = 1
        elif name in ['snow_block']: p = 3
        elif name in ['grass_block', 'podzol', 'mycelium']: p = 4
        else: p = 2
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 37. Glass, Tinted Glass, Glass Panes
    if 'glass' in name:
        if name == 'tinted_glass': p = 15
        elif 'pane' in name: p = 1
        elif 'stained' in name: p = 3
        else: p = 2
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 38. Terracotta, Concrete, Wool, Carpets, Beds, Candles, Banners
    if any(k in name for k in ['terracotta', 'concrete', 'wool', 'carpet', 'bed', 'candle', 'banner']):
        if 'glazed_terracotta' in name: p = 5
        elif 'terracotta' in name: p = 3
        elif 'concrete_powder' in name: p = 2
        elif 'concrete' in name: p = 3
        elif 'carpet' in name: p = 2
        elif 'wool' in name: p = 4
        elif 'bed' in name: p = 20
        elif 'candle' in name: p = 6
        elif 'banner' in name: p = 25
        else: p = 4
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 39. Foods, Crops, Farming, Mob drops, Flora
    farm_food_mob = {
        'apple': 5, 'bread': 10, 'wheat': 4, 'wheat_seeds': 1, 'carrot': 3,
        'potato': 3, 'baked_potato': 4, 'poisonous_potato': 5, 'beetroot': 3,
        'beetroot_seeds': 1, 'beetroot_soup': 12, 'sweet_berries': 2,
        'glow_berries': 4, 'melon_slice': 2, 'melon': 10, 'melon_seeds': 2,
        'pumpkin': 8, 'carved_pumpkin': 10, 'jack_o_lantern': 12, 'pumpkin_seeds': 2,
        'cocoa_beans': 5, 'sugar_cane': 4, 'sugar': 4, 'cactus': 3,
        'kelp': 2, 'dried_kelp': 3, 'dried_kelp_block': 27, 'egg': 3,
        'blue_egg': 3, 'brown_egg': 3,
        'honey_bottle': 15, 'honeycomb': 12, 'honey_block': 60, 'honeycomb_block': 48,
        'cookie': 3, 'cake': 35, 'pumpkin_pie': 20, 'brown_mushroom': 5,
        'red_mushroom': 5, 'mushroom_stew': 15, 'suspicious_stew': 25, 'rabbit_stew': 30,
        'brown_mushroom_block': 4, 'red_mushroom_block': 4, 'mushroom_stem': 4,
        'beef': 6, 'cooked_beef': 8, 'porkchop': 6, 'cooked_porkchop': 8,
        'mutton': 6, 'cooked_mutton': 8, 'chicken': 5, 'cooked_chicken': 7,
        'rabbit': 6, 'cooked_rabbit': 8, 'cod': 5, 'cooked_cod': 7,
        'salmon': 5, 'cooked_salmon': 7, 'tropical_fish': 15, 'pufferfish': 25,
        'rotten_flesh': 3, 'bone': 4, 'bone_meal': 2, 'bone_block': 18,
        'string': 5, 'spider_eye': 8, 'fermented_spider_eye': 20,
        'gunpowder': 10, 'slime_ball': 20, 'slime_block': 180, 'magma_cream': 25,
        'feather': 3, 'rabbit_hide': 4, 'ink_sac': 6, 'glow_ink_sac': 15,
        'phantom_membrane': 40, 'blaze_powder': 18, 'disc_fragment_5': 70,
        'torchflower_seeds': 30, 'pitcher_pod': 30, 'torchflower': 35, 'pitcher_plant': 35,
        'lily_pad': 6, 'seagrass': 2, 'sea_pickle': 15, 'vine': 4, 'weeping_vines': 4,
        'twisting_vines': 4, 'nether_wart': 10, 'nether_wart_block': 25, 'warped_wart_block': 25,
        'crimson_fungus': 6, 'warped_fungus': 6, 'crimson_roots': 3, 'warped_roots': 3,
        'nether_sprouts': 3, 'fern': 2, 'large_fern': 3, 'dandelion': 3, 'poppy': 3,
        'blue_orchid': 4, 'allium': 4, 'azure_bluet': 3, 'red_tulip': 3, 'orange_tulip': 3,
        'white_tulip': 3, 'pink_tulip': 3, 'oxeye_daisy': 3, 'cornflower': 3, 'lily_of_the_valley': 4,
        'sunflower': 6, 'lilac': 6, 'rose_bush': 6, 'peony': 6,
        'azalea_leaves': 3, 'flowering_azalea_leaves': 4, 'azalea': 6, 'flowering_azalea': 8,
        'bush': 2, 'dead_bush': 2, 'firefly_bush': 5, 'open_eyeblossom': 8, 'closed_eyeblossom': 8,
        'pink_petals': 2, 'wildflowers': 3, 'leaf_litter': 2, 'moss_block': 4, 'hanging_roots': 2,
        'big_dripleaf': 6, 'small_dripleaf': 4, 'cactus_flower': 4, 'glow_lichen': 3, 'hay_block': 36,
        'paper': 4, 'book': 15, 'writable_book': 25, 'written_book': 30,
        'map': 25, 'empty_map': 25, 'filled_map': 35, 'firework_rocket': 15,
        'firework_star': 20, 'lantern': 20, 'soul_lantern': 25,
    }
    if name in farm_food_mob:
        p = farm_food_mob[name]
        return max(1, round(p * 0.6)), p, round(p * 1.6)

    # 40. Dyes
    if name.endswith('_dye'):
        p = 4
        return 2, p, 7

    # If anything unexpected slipped through, fallback gracefully
    p = 5
    return 2, p, 10

# Test run
output = []
for item in items:
    name = item["name"]
    min_p, def_p, max_p = get_item_price(name)
    output.append({
        "item_name": f"minecraft:{name}",
        "item_default_price": def_p,
        "item_min_price": min_p,
        "item_max_price": max_p
    })

print(f"Generated {len(output)} price entries.")
with open("market_prices.json", "w") as f:
    json.dump(output, f, indent=2)

print("Saved to market_prices.json successfully!")
