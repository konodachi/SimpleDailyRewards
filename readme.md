# SimpleDailyRewards

A high-performance, asynchronous daily rewards plugin for Minecraft servers. Designed to be lightweight and crash-safe.

## Features

*   **High Performance**: Built with asynchronous I/O to ensure the main thread is never blocked.
*   **SQLite Database**: Uses WAL (Write-Ahead Logging) mode for robust data handling without file locking issues.
*   **Atomic Operations**: Prevents race conditions, ensuring data integrity even during rapid player joins/quits.
*   **Crash Safety**: Implements safe caching mechanisms to prevent data loss during unexpected server stops.
*   **Customizable Rewards**: Fully configurable 7-day reward cycle via `config.yml`.

## Installation

1.  Download the latest release of `SimpleDailyRewards`.
2.  Place the `.jar` file into your server's `plugins` folder.
3.  Restart your server.
4.  Edit the `config.yml` to set up your desired rewards.

## Commands

| Command | Description |
| :--- | :--- |
| `/daily` | Opens the daily rewards GUI menu to claim rewards. |
| `/reloadConfig` | Reloads the plugin configuration and loot tables from disk. |

## Configuration

The `config.yml` allows you to define rewards for each of the 7 days. You can specify the material and the amount for multiple items per day.

### Example Configuration

```yaml
loot-table:
  "Day 1":
    items:
      - material: OAK_PLANKS
        amount: 64
  "Day 2":
    items:
      - material: COPPER_INGOT
        amount: 32
  "Day 3":
    items:
      - material: IRON_INGOT
        amount: 16
  "Day 4":
    items:
      - material: GOLD_INGOT
        amount: 8
  "Day 5":
    items:
      - material: IRON_INGOT
        amount: 16
  "Day 6":
    items:
        - material: OBSIDIAN
          amount: 64
        - material: DIAMOND
          amount: 8
  "Day 7":
    items:
      - material: NETHERITE_INGOT
        amount: 1
```

## Requirements

*   **Minecraft Version**: 1.21+

---
*Current Version: 0.0.3-alpha*
