# 🏗️ Arena Creation Guide — DecDuck's Hunger Games

This guide will help you create and prepare new arenas for use in **DecDuck's Hunger Games**. All arena setup is done directly in the server files — no in-game commands or plugin GUIs required!

---

## 🎯 Step 1: Create or Import a World

You can build your own arena or download a pre-made one.

- Use any Minecraft world editor (e.g. WorldEdit, MCEdit, or in-game building).
- Once complete, ensure the world folder is copied into your Minecraft server directory.

---

## 🧱 Step 2: Define Spawnpoints

Players will spawn on top of **Gold Blocks (`gold_block`)** placed in the arena.

- Place one or more gold blocks in safe, accessible areas.
- During a match, players will be **randomly assigned** to these spawnpoints.
- Try to place spawnpoints equidistant from central loot or combat zones.

📌 **Tip:** Surround each gold block with space to prevent suffocation or falling hazards.

---

## 📦 Step 3: Place Chests for Loot

Simply place standard Minecraft **Chests (`chest`)** throughout your arena.

- The plugin will automatically fill them with loot when a match begins.
- You do not need to manually fill the chests.
- Spread them strategically — consider both high-traffic and hidden locations.

📌 **Tip:** Double chests work too!

---

## 🧱 Step 4: Limit the World Border (Recommended)

To ensure players interact and the match doesn't drag on, it's highly recommended to **restrict the playable area**.

- Use Minecraft’s `/worldborder` command to define a boundary:

  ```bash
  /worldborder set <diameter>
````

* Set the world border **centered around your arena** spawnpoints.

📌 **Example:**

```bash
/worldborder center 0 0
/worldborder set 250
```

This limits the play area to a 250x250 region centered at coordinates 0, 0.

---

## 📝 Step 5: Add Arena to Config

Once your world is built and prepped:

1. Add the world folder name to the plugin’s `config.yml` file:

   ```yaml
   arenas:
     - forest_arena
     - new_pvp_valley  # ← Add your new arena here
   ```

2. Restart your Minecraft server.

The plugin will automatically import the arena and prepare it for matches!

---

## ✅ You're Done!

Your arena is now ready to host Hunger Games matches!
Spawnpoints, chests, and world boundaries will all function automatically once configured.

---

Need help or want to share your arena? Open an issue or start a discussion on the [GitHub repo](./)!
