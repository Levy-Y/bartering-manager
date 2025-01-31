<h1 align="center">
  <br>
  <a href="/"><img src="logo.png" alt="Project Logo"></a>
  <br>
</h1>

## Overview

[![Gradle Build](https://github.com/Levy-Y/bartering-manager/actions/workflows/gradle.yml/badge.svg)](https://github.com/Levy-Y/bartering-manager/actions/workflows/gradle.yml)

The Piglin Trade Manager plugin is a Spigot Minecraft plugin that allows server administrators to manage and customize the bartering system for Piglins. With this plugin, you can define custom trade items and probabilities for both vanilla and Oraxen items, providing enhanced control over Piglin interactions.

## Features

- Customizable Trades: <br>
Define custom items, their drop chances and the amount to drop, for Piglin bartering.
- Supports for different items: <br>
Add vanilla, MMOItems and Oraxen items to the Piglin trade table.

<b style=color:red;>Disclaimer</b>
- The plugin doesn't add the items to the piglin trades, it replaces them (the vanilla ones), if you want the vanilla trades aswell, you have to add them manually <br>
`This will be fixed in a future version!`

## Installation

1. Download the Plugin:
    - Download the latest release of the Piglin Trade Manager plugin from the [releases tab](https://github.com/Levy-Y/bartering-manager/releases) (spigot aswell in the future).

2. Install the Plugin:
    - Place the PiglinTrades.jar file in the plugins folder of your Minecraft server.

3. Start the Server:
    - Start or restart your Minecraft server to load the plugin.

4. Configure the Plugin:
    - After the first startup, a config.yml file will be generated in the plugins/PiglinTrades directory. Customize this file according to your needs (see Configuration).

5. Reload the Plugin:
    - Use the /piglinreload command to reload the plugin configuration after making changes.

## Commands
### /piglinreload

- Description: <br>
Reloads the plugin's configuration file without needing to restart the server.
- Usage: <br>
`/piglinreload`
- Permissions: <br>
This command requires appropriate permissions to use (`piglinTrades.reload`).

## Configuration

The plugin uses a `config.yml` file for configuration. Below is a sample configuration with explanations:

```yaml

enabled: true # Should these trades be registered?
log: true # Enable debug logging (for development)
override_vanilla: false # should it override the vanilla trades with the custom ones

trades:
   vanilla_trades: # vanilla trades here
     - stone_sword: # vanilla item ID, not case sensitive
         min_amount: 1 # minimum amount to drop
         max_amount: 3 # maximum amount to drop
         chance: 50 # chance in %
     - iron_sword:
         min_amount: 1
         max_amount: 3
         chance: 30
     - diamond_sword:
         min_amount: 1
         max_amount: 3
         chance: 10
   oraxen_trades: # oraxen trades here
     - obsidian_pickaxe: # oraxen item ID
         min_amount: 1 # minimum amount to drop
         max_amount: 3 # maximum amount to drop
         chance: 10 # chance in %
     - amethyst_hammer:
         min_amount: 1
         max_amount: 3
         chance: 10
   mmoitems_trades: # mmoitems trades
     - SWORD: # mmoitems category name
         item: "TESTSWORD" # mmoitems item ID
         min_amount: 1
         max_amount: 1
         chance: 50 # chance in %
     - AXE:
         item: "AXETEST"
         min_amount: 1
         max_amount: 1
         chance: 50

```

### Configuration Parameters

`enabled: (boolean)` Enable or disable the plugin's trade functionality. <br>
`log: (boolean)` Toggle logging. Set to false if you don't want to see any info/warning from the plugin.
`override_vanilla (boolean)` Enable or disable vanilla trades 

### Trades Section

    vanilla_trades: Define trades for vanilla Minecraft items.
        Item ID: The ID of the vanilla item.
        min_amount: The minimum number of items to drop.
        max_amount: The maximum number of items to drop.
        chance: The percentage chance of this item being selected during a Piglin barter.
        
###

    oraxen_trades: Define trades for Oraxen items.
        Item ID: The ID of the Oraxen item.
        min_amount: The minimum number of items to drop.
        max_amount: The maximum number of items to drop.
        chance: The percentage chance of this item being selected during a Piglin barter.

###

    mmoitems_trades: Define trades for MMOItems
       - CATEGORY_NAME: Category of the item, ex.: SWORD, ARMOR 
         min_amount: The minimum number of items to drop.
         max_amount: The maximum number of items to drop.
         item: "ITEM_NAME" name of the item (case sensitive)
         chance: The percentage chance of this item being selected during a Piglin barter.


## Usage

#### Once the plugin is installed and configured:

- Barter with Piglins:
When a player throws a gold ingot to a Piglin, the plugin will handle the item drop based on the configured trades.
The items dropped are selected according to the probabilities defined in the `config.yml`.

- Monitor Logs (Optional):
If logging is enabled, the plugin will log trade information, including any errors or warnings about misconfigured items.

- Update Configuration:
Edit the `config.yml` file to adjust trades as needed.
Reload the configuration using the `/piglinreload` command.

## Troubleshooting

1. Piglin Reload Command Not Working: <br>
    - Check the server logs for any errors during plugin initialization.
    - Make sure, that you have the `piglinTrades.reload` permission

2. Items Not Dropping as Expected:
    - Verify that the item IDs and chances are correctly defined in the config.yml.
If using Oraxen items, ensure that Oraxen is properly installed on the server.

### Note for troubleshooting:
These are just basic solutions, if none of the above can solve your problem, open a ticket here on [Github](https://github.com/Levy-Y/bartering-manager/issues)

## Trello

We have a trello board, where we keep all the tasks, and requests for future versions. If you are interested check it out [here](https://trello.com/b/AdUkr5wt/trademanager)

## License

This project is licensed under the MIT License. See the LICENSE file for details.
Contributions

Contributions are welcome! Please fork the repository and submit a pull request for any features or fixes.
Support

If you encounter any issues or have questions, please open an issue on the GitHub repository.

