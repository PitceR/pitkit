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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.bukkit.Material;
import org.junit.Test;

public class ItemSerializationTest
{
	@Test
	public void testSerialization() throws Exception
	{
		Item diamond = Item.of(Material.DIAMOND);
		File file = new File("diamond.item");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(diamond);
		objectOutputStream.close();
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
		Object object = inputStream.readObject();
		inputStream.close();
		assert diamond.equals(object);
	}
}