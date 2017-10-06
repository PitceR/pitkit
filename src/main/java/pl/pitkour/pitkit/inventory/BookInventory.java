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

package pl.pitkour.pitkit.inventory;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.pitkour.pitkit.text.message.Message;

public class BookInventory
{
	private List<Message> pages;
	private ItemStack book;

	public BookInventory(Message... pages)
	{
		this(Arrays.asList(pages));
	}

	public BookInventory(List<Message> pages)
	{
		this.pages = pages;
		createBook();
	}

	private void createBook()
	{
		this.book = new ItemStack(Material.WRITTEN_BOOK);
		ItemMeta metadata = this.book.getItemMeta();
		CraftMetaBook bookMetadata = (CraftMetaBook)metadata;
		List<IChatBaseComponent> pages = bookMetadata.pages;
		this.pages.forEach(page -> pages.add(ChatSerializer.a(page.toString())));
		this.book.setItemMeta(bookMetadata);
	}

	public void open(Player player)
	{
		CraftPlayer craftPlayer = (CraftPlayer)player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();
		net.minecraft.server.v1_12_R1.ItemStack bookItemNMS = CraftItemStack.asNMSCopy(this.book);
		EntityEquipment equipment = player.getEquipment();
		ItemStack itemInHand = equipment.getItemInMainHand();
		equipment.setItemInMainHand(this.book);
		entityPlayer.a(bookItemNMS, EnumHand.MAIN_HAND);
		equipment.setItemInMainHand(itemInHand);
	}
}
