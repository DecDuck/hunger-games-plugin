# 🦆 DecDuck's Hunger Games

A fully configurable, multi-arena Hunger Games plugin for Minecraft servers.
Built for simplicity — no databases, just pure configuration.

---

## ✨ Features

- 🖥️ **Dedicated Server**
  Designed to run on a standalone Minecraft server for optimal performance and isolation.

- 🏟️ **Multiple Arenas & Simultaneous Games**
  Host multiple matches at the same time across different arenas.

- ⚙️ **Fully Config-Driven**
  No internal database or complex setup — configure everything through YAML files.

---

## 🚀 Getting Started

### 🏗️ Setting Up an Arena

To create and add a new arena to the plugin, follow these steps:

1. **Copy the Arena World Files**
   Copy the complete world folder for your arena (e.g. arena_1, contains `level.dat`, `region/`, etc.) into your server’s root directory.

2. **Edit the Configuration**
   Open your plugin’s `config.yml` file and add the world/folder name to the `arenas` list.

```yaml
arenas:
  - arena1
  - arena2
  - your_new_arena  # ← Add your arena folder name here

# ... rest of the config
````

3. **Restart the Server**
   Restart your server. The plugin will automatically import the listed arenas as it starts up.

✅ Your arena is now active and ready for Hunger Games matches!


📘 Need more help building your arena?
See the [Arena Creation Guide](./GUIDE.md) for detailed instructions on spawnpoints, chests, and world borders.

---

## 📣 Contribute

Found a bug or want to help improve the plugin?
Open a pull request or start a discussion in the Issues tab!

---

## 📄 License

This project is open-source. See [LICENSE](./LICENSE) for details.

---

🔧 Built with ❤️ by **DecDuck**
