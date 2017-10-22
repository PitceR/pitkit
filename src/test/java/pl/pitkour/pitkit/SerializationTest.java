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

package pl.pitkour.pitkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import pl.pitkour.pitkit.item.Item;
import pl.pitkour.pitkit.text.Text;
import pl.pitkour.pitkit.text.message.Message;
import pl.pitkour.pitkit.text.message.event.action.ClickAction;
import pl.pitkour.pitkit.text.message.event.action.HoverAction;

public class SerializationTest
{
	@Test
	public void testTextSerialization() throws Exception
	{
		Text text = Text.builder("Foo").space().regular("Bar").space().text("&9FooBar").colored().build();
		checkSerialization(text);
	}

	@Test
	public void testMessageSerialization() throws Exception
	{
		long now = System.currentTimeMillis();
		Message message = Message.builder("Foo").color(ChatColor.BLUE).bold(true).space().brackets("Bar").hover(HoverAction.SHOW_TEXT, Text.colorize("&7FooBar")).click(ClickAction.RUN_COMMAND, "/foo bar").space().text(Text.colorize("&9FooBar")).space().number(1337).line().date(now).line().time(now).build();
		checkSerialization(message);
	}

	@Test
	public void testItemSerialization() throws Exception
	{
		Item item = Item.builder(Material.DIAMOND).woolColor(DyeColor.BLUE).amount(32).unbreakable().glow().name(Text.builder("&9FooBar").colored().build()).description(Text.of("Foo"), Text.builder().color(ChatColor.GRAY).text("Bar").build(), Text.empty()).enchantment(Enchantment.SILK_TOUCH, 1).enchantment(Enchantment.MENDING, 111).flag(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES).spawnEgg(EntityType.GIANT).metadata(metadata -> metadata.setUnbreakable(false)).build();
		checkSerialization(item);
	}

	@Test
	public void testParticlesSerialization() throws Exception
	{
		Particles particles = Particles.builder(Particle.PORTAL).location(1.0, 0.5, 0).offset(0.2, 0.3, 0.4).extra(1).visibilityDistance(64).data(new ItemStack(Material.EMERALD)).build();
		checkSerialization(particles);
	}

	private static <T extends Serializable> void checkSerialization(T object)
	{
		File file = new File("src/test/resources/" + object.getClass().getSimpleName() + ".ser");
		try(FileOutputStream fileOutputStream = new FileOutputStream(file); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream); FileInputStream fileInputStream = new FileInputStream(file); ObjectInputStream inputStream = new ObjectInputStream(fileInputStream))
		{
			objectOutputStream.writeObject(object);
			Object deserializedObject = inputStream.readObject();
			assert Objects.equals(object, deserializedObject);
		}
		catch(IOException | ClassNotFoundException exception)
		{
			exception.printStackTrace();
		}
	}
}