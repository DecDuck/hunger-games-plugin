package dev.decduck3.HungerGames.state

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.bukkit.Material

class MaterialSeralizer : Serializer<Material>() {
    override fun write(
        kryo: Kryo?, output: Output?, `object`: Material?
    ) {
        output!!.writeString(`object`!!.name)
    }

    override fun read(
        kryo: Kryo?, input: Input?, type: Class<out Material?>?
    ): Material? {
        val name = input!!.readString()!!
        return Material.valueOf(name)
    }
}