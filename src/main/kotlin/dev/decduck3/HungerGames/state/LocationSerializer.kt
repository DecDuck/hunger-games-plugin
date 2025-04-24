package dev.decduck3.HungerGames.state

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.bukkit.Location

class LocationSerializer : Serializer<Location>() {
    override fun write(
        p0: Kryo?,
        p1: Output?,
        p2: Location?
    ) {
        p1!!.writeDouble(p2!!.x)
        p1.writeDouble(p2.y)
        p1.writeDouble(p2.z)
        p1.writeFloat(p2.yaw)
        p1.writeFloat(p2.pitch)
    }

    override fun read(
        p0: Kryo?,
        p1: Input?,
        p2: Class<out Location?>?
    ): Location? {
        val x = p1!!.readDouble()
        val y = p1.readDouble()
        val z = p1.readDouble()
        val yaw = p1.readFloat()
        val pitch = p1.readFloat()

        return Location(null, x, y, z, yaw, pitch)
    }
}