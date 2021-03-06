/*
 * Copyright 2017 PitceR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.pitkour.pitkit.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MaterialData;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.utility.Builder;

public final class Item implements Serializable
{
	private static final long serialVersionUID = 974963540133314215L;
	private int id;
	private int amount = 1;
	private short damage;
	private byte data;
	private boolean unbreakable;
	private boolean glow;
	private Text name = Text.of("Item");
	private List<Text> description = new ArrayList<>();
	private Map<Integer, Integer> enchantments = new HashMap<>();
	private Set<ItemFlag> flags = EnumSet.noneOf(ItemFlag.class);
	private transient List<Consumer<ItemMeta>> metadataAppliers = new ArrayList<>();

	private Item()
	{}

	private Item(Item item)
	{
		this(item.id);
		this.amount = item.amount;
		this.damage = item.damage;
		this.data = item.data;
		this.unbreakable = item.unbreakable;
		this.glow = item.glow;
		this.name = Text.of(item.name);
		this.description = item.description.stream().map(Text::of).collect(Collectors.toList());
		this.enchantments = new HashMap<>(item.enchantments);
		this.flags = EnumSet.copyOf(item.flags);
		this.metadataAppliers = new ArrayList<>(item.metadataAppliers);
	}

	@SuppressWarnings("deprecation")
	private Item(ItemStack itemStack)
	{
		this(itemStack.getTypeId());
		this.amount = itemStack.getAmount();
		this.damage = itemStack.getDurability();
		this.data = itemStack.getData().getData();
		if(itemStack.hasItemMeta())
		{
			ItemMeta metadata = itemStack.getItemMeta();
			this.unbreakable = metadata.isUnbreakable();
			if(metadata.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && metadata.hasEnchant(Enchantment.LUCK))
			{
				this.glow = true;
			}
			if(metadata.hasDisplayName())
			{
				this.name = Text.of(metadata.getDisplayName());
			}
			if(metadata.hasLore())
			{
				this.description = metadata.getLore().stream().map(Text::of).collect(Collectors.toList());
			}
			if(metadata.hasEnchants())
			{
				this.enchantments = metadata.getEnchants().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
			}
			this.flags = metadata.getItemFlags();
		}
	}

	@SuppressWarnings("deprecation")
	private Item(Material material)
	{
		this(material.getId());
	}

	private Item(int id)
	{
		this.id = id;
	}

	public static Item empty()
	{
		return new Item();
	}

	public static Item of(Item item)
	{
		Objects.requireNonNull(item, "item must not be null");
		return new Item(item);
	}

	public static Item of(ItemStack itemStack)
	{
		Objects.requireNonNull(itemStack, "itemStack must not be null");
		return new Item(itemStack);
	}

	public static Item of(Material material)
	{
		Objects.requireNonNull(material, "material must not be null");
		return new Item(material);
	}

	public static Item of(int id)
	{
		return new Item(id);
	}

	public static ItemBuilder builder()
	{
		return new ItemBuilder(new Item());
	}

	public static ItemBuilder builder(Item item)
	{
		Objects.requireNonNull(item, "item must not be null");
		return new ItemBuilder(new Item(item));
	}

	public static ItemBuilder builder(ItemStack itemStack)
	{
		Objects.requireNonNull(itemStack, "itemStack must not be null");
		return new ItemBuilder(new Item(itemStack));
	}

	public static ItemBuilder builder(Material material)
	{
		Objects.requireNonNull(material, "material must not be null");
		return new ItemBuilder(new Item(material));
	}

	public static ItemBuilder builder(int id)
	{
		return new ItemBuilder(new Item(id));
	}

	@SuppressWarnings("deprecation")
	public ItemStack asItemStack()
	{
		ItemStack item = new ItemStack(this.id, this.amount, this.damage, this.data);
		ItemMeta metadata = item.getItemMeta();
		this.metadataAppliers.forEach(metadataApplier -> metadataApplier.accept(metadata));
		metadata.setUnbreakable(this.unbreakable);
		if(this.glow)
		{
			metadata.addEnchant(Enchantment.LUCK, 1, false);
			metadata.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		metadata.setDisplayName(this.name.toString());
		metadata.setLore(this.description.stream().map(Text::toString).collect(Collectors.toList()));
		this.enchantments.forEach((enchantment, level) -> metadata.addEnchant(Enchantment.getById(enchantment), level, true));
		this.flags.forEach(metadata::addItemFlags);
		item.setItemMeta(metadata);
		return item;
	}

	@Override
	public boolean equals(Object object)
	{
		if(this == object)
		{
			return true;
		}
		if(object == null || getClass() != object.getClass())
		{
			return false;
		}
		Item that = (Item)object;
		return this.id == that.id && this.amount == that.amount && this.damage == that.damage && this.data == that.data && this.unbreakable == that.unbreakable && this.glow == that.glow && Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.enchantments, that.enchantments) && Objects.equals(this.flags, that.flags);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.id, this.amount, this.damage, this.data, this.unbreakable, this.glow, this.name, this.description, this.enchantments, this.flags);
	}

	@Override
	public String toString()
	{
		return "Item{" + "id=" + this.id + ", amount=" + this.amount + ", damage=" + this.damage + ", data=" + this.data + ", unbreakable=" + this.unbreakable + ", glow=" + this.glow + ", name=" + this.name + ", description=" + this.description + ", enchantments=" + this.enchantments + ", flags=" + this.flags + '}';
	}

	public int getID()
	{
		return this.id;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public short getDamage()
	{
		return this.damage;
	}

	public byte getData()
	{
		return this.data;
	}

	public boolean isUnbreakable()
	{
		return this.unbreakable;
	}

	public boolean hasGlow()
	{
		return this.glow;
	}

	public Text getName()
	{
		return this.name;
	}

	public List<Text> getDescription()
	{
		return Collections.unmodifiableList(this.description);
	}

	@SuppressWarnings("deprecation")
	public Map<Enchantment, Integer> getEnchantments()
	{
		return Collections.unmodifiableMap(this.enchantments.entrySet().stream().collect(Collectors.toMap(entry -> Enchantment.getById(entry.getKey()), Entry::getValue)));
	}

	public Set<ItemFlag> getFlags()
	{
		return Collections.unmodifiableSet(this.flags);
	}

	public static final class ItemBuilder implements Builder<Item>
	{
		private Item item;

		private ItemBuilder(Item item)
		{
			this.item = item;
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder material(Material material)
		{
			Objects.requireNonNull(material, "material must not be null");
			return id(material.getId());
		}

		public ItemBuilder id(int id)
		{
			this.item.id = id;
			return this;
		}

		public ItemBuilder amount(int amount)
		{
			this.item.amount = amount;
			return this;
		}

		public ItemBuilder damage(int damage)
		{
			this.item.damage = (short)damage;
			return this;
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder woolColor(MaterialData data)
		{
			Objects.requireNonNull(data, "data must not be null");
			return data(data.getData());
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder woolColor(DyeColor woolColor)
		{
			Objects.requireNonNull(woolColor, "woolColor must not be null");
			return data(woolColor.getWoolData());
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder dyeColor(DyeColor dyeColor)
		{
			Objects.requireNonNull(dyeColor, "dyeColor must not be null");
			return data(dyeColor.getDyeData());
		}

		public ItemBuilder data(int data)
		{
			this.item.data = (byte)data;
			return this;
		}

		public ItemBuilder unbreakable()
		{
			return unbreakable(true);
		}

		public ItemBuilder unbreakable(boolean unbreakable)
		{
			this.item.unbreakable = unbreakable;
			return this;
		}

		public ItemBuilder glow()
		{
			return glow(true);
		}

		public ItemBuilder glow(boolean glow)
		{
			this.item.glow = glow;
			return this;
		}

		public ItemBuilder name(Text name)
		{
			Objects.requireNonNull(name, "name must not be null");
			this.item.name = name;
			return this;
		}

		public ItemBuilder description(Text... lines)
		{
			Objects.requireNonNull(lines, "lines must not be null");
			Arrays.stream(lines).forEach(this::description);
			return this;
		}

		public ItemBuilder description(Text line)
		{
			Objects.requireNonNull(line, "line must not be null");
			this.item.description.add(line);
			return this;
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder enchantment(Enchantment enchantment, int level)
		{
			Objects.requireNonNull(enchantment, "enchantment must not be null");
			this.item.enchantments.put(enchantment.getId(), level);
			return this;
		}

		public ItemBuilder flag(ItemFlag... flags)
		{
			Objects.requireNonNull(flags, "flags must not be null");
			Arrays.stream(flags).forEach(this::flag);
			return this;
		}

		public ItemBuilder flag(ItemFlag flag)
		{
			Objects.requireNonNull(flag, "flag must not be null");
			this.item.flags.add(flag);
			return this;
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder skull(String owner)
		{
			Objects.requireNonNull(owner, "owner must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof SkullMeta))
				{
					throw new IllegalStateException("item metadata must be a SkullMeta");
				}
				SkullMeta skullMetadata = (SkullMeta)metadata;
				skullMetadata.setOwner(owner);
			});
		}

		@SuppressWarnings("deprecation")
		public ItemBuilder banner(DyeColor baseColor, Pattern... patterns)
		{
			Objects.requireNonNull(baseColor, "baseColor must not be null");
			Objects.requireNonNull(patterns, "patterns must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof BannerMeta))
				{
					throw new IllegalStateException("item metadata must be a BannerMeta");
				}
				BannerMeta bannerMetadata = (BannerMeta)metadata;
				bannerMetadata.setBaseColor(baseColor);
				Arrays.stream(patterns).forEach(bannerMetadata::addPattern);
			});
		}

		public ItemBuilder armorColor(Color color)
		{
			Objects.requireNonNull(color, "color must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof LeatherArmorMeta))
				{
					throw new IllegalStateException("item metadata must be a LeatherArmorMeta");
				}
				LeatherArmorMeta armorMetadata = (LeatherArmorMeta)metadata;
				armorMetadata.setColor(color);
			});
		}

		public ItemBuilder fireworkEffect(FireworkEffect effect)
		{
			Objects.requireNonNull(effect, "effect must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof FireworkEffectMeta))
				{
					throw new IllegalStateException("item metadata must be a FireworkEffectMeta");
				}
				FireworkEffectMeta effectMetadata = (FireworkEffectMeta)metadata;
				effectMetadata.setEffect(effect);
			});
		}

		public ItemBuilder firework(int power, FireworkEffect... effects)
		{
			Objects.requireNonNull(effects, "effects must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof FireworkMeta))
				{
					throw new IllegalStateException("item metadata must be a FireworkMeta");
				}
				FireworkMeta fireworkMetadata = (FireworkMeta)metadata;
				fireworkMetadata.addEffects(effects);
				fireworkMetadata.setPower(power);
			});
		}

		public ItemBuilder spawnEgg(EntityType entityType)
		{
			Objects.requireNonNull(entityType, "entityType must not be null");
			return metadata(metadata ->
			{
				if(!(metadata instanceof SpawnEggMeta))
				{
					throw new IllegalStateException("item metadata must be a SpawnEggMeta");
				}
				SpawnEggMeta spawnEggMetadata = (SpawnEggMeta)metadata;
				spawnEggMetadata.setSpawnedType(entityType);
			});
		}

		public ItemBuilder metadata(Consumer<ItemMeta> metadataApplier)
		{
			Objects.requireNonNull(metadataApplier, "metadataApplier must not be null");
			this.item.metadataAppliers.add(metadataApplier);
			return this;
		}

		@Override
		public Item build()
		{
			return this.item;
		}
	}
}
