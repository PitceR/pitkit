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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import pl.pitkour.pitkit.utility.Builder;
import pl.pitkour.pitkit.utility.NumberUtility;

public final class Particles implements Serializable
{
	private static final long serialVersionUID = -8207190707919660602L;
	private Particle particle;
	private float x;
	private float y;
	private float z;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private int count = 1;
	private float extra;
	private double visibilityDistance = 32;
	private int[] data = new int[0];

	private Particles(Particles particles)
	{
		this(particles.particle);
		this.x = particles.x;
		this.y = particles.y;
		this.z = particles.z;
		this.offsetX = particles.offsetX;
		this.offsetY = particles.offsetY;
		this.offsetZ = particles.offsetZ;
		this.count = particles.count;
		this.extra = particles.extra;
		this.visibilityDistance = particles.visibilityDistance;
		this.data = Arrays.copyOf(particles.data, particles.data.length);
	}

	private Particles(Particle particle)
	{
		this.particle = particle;
	}

	public static Particles of(Particles particles)
	{
		Objects.requireNonNull(particles, "particles must not be null");
		return new Particles(particles);
	}

	public static Particles of(Particle particle)
	{
		Objects.requireNonNull(particle, "particle must not be null");
		return new Particles(particle);
	}

	public static ParticlesBuilder builder(Particles particles)
	{
		Objects.requireNonNull(particles, "particles must not be null");
		return new ParticlesBuilder(new Particles(particles));
	}

	public static ParticlesBuilder builder(Particle particle)
	{
		Objects.requireNonNull(particle, "particle must not be null");
		return new ParticlesBuilder(new Particles(particle));
	}

	public void send(Player... receivers)
	{
		Objects.requireNonNull(receivers, "receivers must not be null");
		PacketPlayOutWorldParticles packet = asPacket();
		Arrays.stream(receivers).forEach(receiver -> sendPacket(receiver, packet));
	}

	public void send(Player receiver)
	{
		Objects.requireNonNull(receiver, "receiver must not be null");
		sendPacket(receiver, asPacket());
	}

	private PacketPlayOutWorldParticles asPacket()
	{
		EnumParticle particle = CraftParticle.toNMS(this.particle);
		return new PacketPlayOutWorldParticles(particle, true, this.x, this.y, this.z, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.count, this.data);
	}

	private void sendPacket(Player receiver, PacketPlayOutWorldParticles packet)
	{
		if(canSeeParticles(receiver))
		{
			CraftPlayer craftPlayer = (CraftPlayer)receiver;
			EntityPlayer entityPlayer = craftPlayer.getHandle();
			PlayerConnection connection = entityPlayer.playerConnection;
			connection.sendPacket(packet);
		}
	}

	private boolean canSeeParticles(Player receiver)
	{
		Vector vector = new Vector(this.x, this.y, this.z);
		Location receiverLocation = receiver.getLocation();
		Vector receiverVector = receiverLocation.toVector();
		double distance = vector.distance(receiverVector);
		return distance <= this.visibilityDistance;
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
		Particles that = (Particles)object;
		return this.particle == that.particle && NumberUtility.equals(this.x, that.x) && NumberUtility.equals(this.y, that.y) && NumberUtility.equals(this.z, that.z) && NumberUtility.equals(this.offsetX, that.offsetX) && NumberUtility.equals(this.offsetY, that.offsetY) && NumberUtility.equals(this.offsetZ, that.offsetZ) && this.count == that.count && NumberUtility.equals(this.extra, that.extra) && NumberUtility.equals(this.visibilityDistance, that.visibilityDistance) && Arrays.equals(this.data, that.data);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.particle, this.x, this.y, this.z, this.offsetX, this.offsetY, this.offsetZ, this.count, this.extra, this.visibilityDistance, this.data);
	}

	@Override
	public String toString()
	{
		return "Particles{" + "particle=" + this.particle + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", offsetX=" + this.offsetX + ", offsetY=" + this.offsetY + ", offsetZ=" + this.offsetZ + ", count=" + this.count + ", extra=" + this.extra + ", visibilityDistance=" + this.visibilityDistance + ", data=" + Arrays.toString(this.data) + '}';
	}

	public Particle getParticle()
	{
		return this.particle;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public double getZ()
	{
		return this.z;
	}

	public double getOffsetX()
	{
		return this.offsetX;
	}

	public double getOffsetY()
	{
		return this.offsetY;
	}

	public double getOffsetZ()
	{
		return this.offsetZ;
	}

	public int getCount()
	{
		return this.count;
	}

	public double getExtra()
	{
		return this.extra;
	}

	public double getVisibilityDistance()
	{
		return this.visibilityDistance;
	}

	public int[] getData()
	{
		return Arrays.copyOf(this.data, this.data.length);
	}

	public static final class ParticlesBuilder implements Builder<Particles>
	{
		private Particles particles;

		private ParticlesBuilder(Particles particles)
		{
			this.particles = particles;
		}

		public ParticlesBuilder particle(Particle particle)
		{
			Objects.requireNonNull(particle, "particle must not be null");
			this.particles.particle = particle;
			return this;
		}

		public ParticlesBuilder location(Location location)
		{
			Objects.requireNonNull(location, "location must not be null");
			return location(location.getX(), location.getY(), location.getZ());
		}

		public ParticlesBuilder location(double x, double y, double z)
		{
			return x(x).y(y).z(z);
		}

		public ParticlesBuilder x(double x)
		{
			this.particles.x = (float)x;
			return this;
		}

		public ParticlesBuilder y(double y)
		{
			this.particles.y = (float)y;
			return this;
		}

		public ParticlesBuilder z(double z)
		{
			this.particles.z = (float)z;
			return this;
		}

		public ParticlesBuilder color(Color color)
		{
			Objects.requireNonNull(color, "color must not be null");
			return color(color.getRed(), color.getGreen(), color.getBlue());
		}

		public ParticlesBuilder color(int red, int green, int blue)
		{
			return offset(red / 255.0, green / 255.0, blue / 255.0);
		}

		public ParticlesBuilder offset(double offsetX, double offsetY, double offsetZ)
		{
			return offsetX(offsetX).offsetY(offsetY).offsetZ(offsetZ);
		}

		public ParticlesBuilder offsetX(double offsetX)
		{
			this.particles.offsetX = (float)offsetX;
			return this;
		}

		public ParticlesBuilder offsetY(double offsetY)
		{
			this.particles.offsetY = (float)offsetY;
			return this;
		}

		public ParticlesBuilder offsetZ(double offsetZ)
		{
			this.particles.offsetZ = (float)offsetZ;
			return this;
		}

		public ParticlesBuilder count(int count)
		{
			this.particles.count = count;
			return this;
		}

		public ParticlesBuilder extra(double extra)
		{
			this.particles.extra = (float)extra;
			return this;
		}

		public ParticlesBuilder visibilityDistance(double visibilityDistance)
		{
			this.particles.visibilityDistance = visibilityDistance;
			return this;
		}

		@SuppressWarnings("deprecation")
		public ParticlesBuilder data(ItemStack data)
		{
			Objects.requireNonNull(data, "data must not be null");
			return data(new int[]{data.getType().getId(), data.getDurability()});
		}

		@SuppressWarnings("deprecation")
		public ParticlesBuilder data(MaterialData data)
		{
			Objects.requireNonNull(data, "data must not be null");
			return data(new int[]{data.getItemTypeId() + (data.getData() << 12)});
		}

		public ParticlesBuilder data(int[] data)
		{
			Objects.requireNonNull(data, "data must not be null");
			this.particles.data = data;
			return this;
		}

		@Override
		public Particles build()
		{
			return this.particles;
		}
	}
}
