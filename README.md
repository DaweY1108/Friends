# Friends Plugin

A comprehensive Friends system for Velocity proxy servers, supporting both MySQL and SQLite for data storage.

## Permissions (LuckPerms)

The plugin uses a simple permission structure.

| Permission Node | Description |
| :--- | :--- |
| `friends.player` | Grants access to all basic friend commands (add, remove, list, msg, etc.) |
| `friends.admin` | Grants access to administrative commands and bypasses friend limits |
| `friends.limit.<number>` | Sets the maximum friend limit for the player (e.g. `friends.limit.10`) |

