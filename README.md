# RandomChest

RandomChest is a Minecraft plugin that allows for the creation of random chests with customizable items and spawn chances.

## Features

- Create chests with random items.
- Customize the spawn chances of each item.
- Refresh the chests at a configurable interval.
- Commands for creating, refreshing, and deleting chests.

## Installation

1. Download the latest release from the GitHub repository.
2. Place the downloaded .jar file into your server's `plugins` directory.
3. Restart your server.

## Usage

- `/randomchest addchest <chest_name> <world> <x> <y> <z>`: Creates a new chest at the specified location.
- `/randomchest reload`: Reloads the plugin configuration.
- `/refreshchest <chestName>`: Refreshes the specified chest.

## Permissions

- `randomchest.command`: Allow the player to use the randomchest command. Default: op
- `randomchest.reload`: Allow the player to reload the plugin. Default: op
- `randomchest.refresh`: Allow the player to refresh the chests. Default: op
- `randomchest.delete`: Allow the player to delete the chests. Default: op

## Configuration

The plugin's configuration can be found in the `config.yml` file in the `RandomChest` directory in your server's `plugins` directory. Here you can customize the items and spawn chances for each chest.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
