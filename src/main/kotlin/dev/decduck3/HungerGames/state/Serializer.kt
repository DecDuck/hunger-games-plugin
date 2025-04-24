@file:Suppress("UNCHECKED_CAST")

package dev.decduck3.HungerGames.state

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import dev.decduck3.HungerGames.HungerGames
import org.bukkit.Location
import org.bukkit.Material
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.Serializable

object Serializer {

    val kryo = Kryo().also { kryo ->
        kryo.addDefaultSerializer(Location::class.java, LocationSerializer::class.java)
        kryo.addDefaultSerializer(Material::class.java, MaterialSeralizer::class.java)
        kryo.register(ArenaWorld::class.java)
        kryo.register(ArrayList::class.java)
        kryo.register(Location::class.java)
        kryo.register(Material::class.java)
        kryo.register(LinkedHashMap::class.java)
    }


    fun <T> serialize(filename: String, data: T) {
        val filename = File(HungerGames.instance!!.dataFolder, filename);
        val output = Output(FileOutputStream(filename))
        kryo.writeObject(output, data)
        output.close()
    }

    fun <T> deserialize(filename: String, clazz: Class<T>): T? {
        val filename = File(HungerGames.instance!!.dataFolder, filename);
        if (!filename.exists()) {
            return null
        }
        val input = Input(FileInputStream(filename))
        val data = kryo.readObject(input, clazz)
        input.close()

        return data;
    }
}