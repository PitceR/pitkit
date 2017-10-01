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
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import com.github.pitcer.shorts.Conditions;
import com.github.pitcer.shorts.Loops;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import pl.pitkour.pitkit.item.Item.ItemBuilder;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.utility.Buildable;
import pl.pitkour.pitkit.utility.Builder;

public final class Item implements Buildable<ItemBuilder>, Serializable
{
	private int id;
	private int amount = 1;
	private short damage;
	private byte data;
	private boolean unbreakable;
	private boolean glow;
	private Text name;
	private List<Text> description = new ArrayList<>();
	private Map<Enchantment, Integer> enchantments = new TreeMap<>();
	private Set<ItemFlag> flags = EnumSet.noneOf(ItemFlag.class);
	private Consumer<ItemMeta> metadataApplier;

	public Item()
	{}

	@SuppressWarnings("deprecation")
	public Item(ItemStack itemStack)
	{
		this(itemStack.getTypeId());
		this.amount = itemStack.getAmount();
		this.damage = itemStack.getDurability();
		this.data = itemStack.getData().getData();
		if(itemStack.hasItemMeta())
		{
			ItemMeta metadata = itemStack.getItemMeta();
			this.unbreakable = metadata.isUnbreakable();
			Conditions.ifThen(metadata.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && metadata.hasEnchant(Enchantment.LUCK), () -> this.glow = true);
			Conditions.ifThen(metadata.hasDisplayName(), () -> this.name = new Text(metadata.getDisplayName()));
			Conditions.ifThen(metadata.hasLore(), () -> this.description = Loops.transform(metadata.getLore(), Text::new));
			Conditions.ifThen(metadata.hasEnchants(), () -> this.enchantments = new TreeMap<>(metadata.getEnchants()));
			this.flags = metadata.getItemFlags();
		}
	}

	@SuppressWarnings("deprecation")
	public Item(Material material)
	{
		this(material.getId());
	}

	public Item(int id)
	{
		this.id = id;
	}

	public static Item empty()
	{
		return new Item();
	}

	public static Item of(ItemStack itemStack)
	{
		return new Item(itemStack);
	}

	public static Item of(Material material)
	{
		return new Item(material);
	}

	public static Item of(int id)
	{
		return new Item(id);
	}

	public static ItemBuilder builder()
	{
		return new ItemBuilder();
	}

	public static ItemBuilder builder(ItemStack itemStack)
	{
		return new ItemBuilder(itemStack);
	}

	public static ItemBuilder builder(Material material)
	{
		return new ItemBuilder(material);
	}

	public static ItemBuilder builder(int id)
	{
		return new ItemBuilder(id);
	}

	@Override
	public ItemBuilder toBuilder()
	{
		return new ItemBuilder(this);
	}

	@SuppressWarnings("deprecation")
	public ItemStack toItemStack()
	{
		ItemStack item = new ItemStack(this.id, this.amount, this.damage, this.data);
		ItemMeta metadata = item.getItemMeta();
		Conditions.ifThen(this.metadataApplier != null, () -> this.metadataApplier.accept(metadata));
		metadata.setUnbreakable(this.unbreakable);
		if(this.glow)
		{
			metadata.addEnchant(Enchantment.LUCK, 1, false);
			metadata.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		Conditions.ifThen(this.name != null, () -> metadata.setDisplayName(this.name.toString()));
		metadata.setLore(Loops.transform(this.description, Text::toString));
		this.enchantments.forEach((enchantment, level) -> metadata.addEnchant(enchantment, level, true));
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
		return this.id == that.id && this.amount == that.amount && this.damage == that.damage && this.data == that.data && this.unbreakable == that.unbreakable && this.glow == that.glow && this.name.equals(that.name) && this.description.equals(that.description) && this.enchantments.equals(that.enchantments) && this.flags.equals(that.flags);
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

	@SuppressWarnings("deprecation")
	public void setMaterial(Material material)
	{
		this.id = material.getId();
	}

	public void setID(int id)
	{
		this.id = id;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public short getDamage()
	{
		return this.damage;
	}

	public void setDamage(int damage)
	{
		this.damage = (short)damage;
	}

	public byte getData()
	{
		return this.data;
	}

	@SuppressWarnings("deprecation")
	public void setData(MaterialData data)
	{
		this.data = data.getData();
	}

	@SuppressWarnings("deprecation")
	public void setWoolColor(DyeColor dyeColor)
	{
		this.data = dyeColor.getWoolData();
	}

	@SuppressWarnings("deprecation")
	public void setDyeColor(DyeColor dyeColor)
	{
		this.data = dyeColor.getDyeData();
	}

	public void setData(int data)
	{
		this.data = (byte)data;
	}

	public boolean isUnbreakable()
	{
		return this.unbreakable;
	}

	public void setUnbreakable(boolean unbreakable)
	{
		this.unbreakable = unbreakable;
	}

	public boolean hasGlow()
	{
		return this.glow;
	}

	public void setGlow(boolean glow)
	{
		this.glow = glow;
	}

	public Text getName()
	{
		return this.name;
	}

	public void setName(Text name)
	{
		this.name = name;
	}

	public List<Text> getDescription()
	{
		return Collections.unmodifiableList(this.description);
	}

	public void addDescriptionLines(Text... lines)
	{
		Loops.forEach(lines, this::addDescriptionLine);
	}

	public void addDescriptionLine(Text line)
	{
		this.description.add(line);
	}

	public void setDescription(List<Text> lore)
	{
		this.description = lore;
	}

	public Map<Enchantment, Integer> getEnchantments()
	{
		return Collections.unmodifiableMap(this.enchantments);
	}

	public void addEnchantment(Enchantment enchantment, int level)
	{
		this.enchantments.put(enchantment, level);
	}

	public void setEnchantments(Map<Enchantment, Integer> enchantments)
	{
		this.enchantments = enchantments;
	}

	public Set<ItemFlag> getFlags()
	{
		return Collections.unmodifiableSet(this.flags);
	}

	public void addFlags(ItemFlag... flags)
	{
		Loops.forEach(flags, this::addFlag);
	}

	public void addFlag(ItemFlag flag)
	{
		this.flags.add(flag);
	}

	public void setFlags(Set<ItemFlag> flags)
	{
		this.flags = flags;
	}

	public void setMetadataApplier(Consumer<ItemMeta> metadataApplier)
	{
		this.metadataApplier = metadataApplier;
	}

	public static final class ItemBuilder implements Builder<Item>
	{
		private Item item;

		private ItemBuilder()
		{
			this(new Item());
		}

		private ItemBuilder(ItemStack itemStack)
		{
			this(new Item(itemStack));
		}

		@SuppressWarnings("deprecation")
		private ItemBuilder(Material material)
		{
			this(new Item(material));
		}

		private ItemBuilder(int id)
		{
			this(new Item(id));
		}

		private ItemBuilder(Item item)
		{
			this.item = item;
		}

		public ItemBuilder material(Material material)
		{
			this.item.setMaterial(material);
			return this;
		}

		public ItemBuilder id(int id)
		{
			this.item.setID(id);
			return this;
		}

		public ItemBuilder amount(int amount)
		{
			this.item.setAmount(amount);
			return this;
		}

		public ItemBuilder damage(int damage)
		{
			this.item.setDamage(damage);
			return this;
		}

		public ItemBuilder woolColor(MaterialData data)
		{
			this.item.setData(data);
			return this;
		}

		public ItemBuilder woolColor(DyeColor dyeColor)
		{
			this.item.setWoolColor(dyeColor);
			return this;
		}

		public ItemBuilder dyeColor(DyeColor dyeColor)
		{
			this.item.setDyeColor(dyeColor);
			return this;
		}

		public ItemBuilder data(int data)
		{
			this.item.setData(data);
			return this;
		}

		public ItemBuilder unbreakable()
		{
			this.item.setUnbreakable(true);
			return this;
		}

		public ItemBuilder glow()
		{
			this.item.setGlow(true);
			return this;
		}

		public ItemBuilder name(Text name)
		{
			this.item.setName(name);
			return this;
		}

		public ItemBuilder description(Text... lines)
		{
			this.item.addDescriptionLines(lines);
			return this;
		}

		public ItemBuilder enchantment(Enchantment enchantment, int level)
		{
			this.item.addEnchantment(enchantment, level);
			return this;
		}

		public ItemBuilder flags(ItemFlag... flags)
		{
			this.item.addFlags(flags);
			return this;
		}

		public ItemBuilder metadata(Consumer<ItemMeta> metadataApplier)
		{
			this.item.setMetadataApplier(metadataApplier);
			return this;
		}

		@Override
		public Item build()
		{
			return this.item;
		}
	}
}
