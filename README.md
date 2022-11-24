# Old School Runescape bots built using Dreambot client APIs.
To run any of these bots, please make sure you have the latest version of the [Dreambot](https://dreambot.org/) client running.
If you're new to RS Botting or have any suggestions, feel free to tweet me [@pashmerepat](https://twitter.com/pashmerepat).
Some of you asked to add pre-compiled versions that you can directly run in Dreambot, you can find them all in the [/ExportedScripts](https://github.com/pashpashpash/RunescapeBots/tree/master/ExportedScripts) folder. If you'd like to buy [OSRS gold](https://www.playerauctions.com/osrs-gold/), check out PlayerAuctions.


## [Fishing & Woodcutting](https://github.com/pashpashpash/RunescapeBots/tree/master/src/FishingAndWoodcuttingBot) Bot
Easily level your bot from 1-99 fishing and 1-99 woodcutting. Enjoy!

![ ](https://i.imgur.com/ZDa1n38.jpg)

After a couple weeks of testing, I found that alternating activities is a tremendously effective anti-ban technique. I have been running this script successfully without being banned for multiple days now (with breaks between runs), so please enjoy & post proggies if y’all don’t mind 🙂

![ ](https://i.imgur.com/eEyUpls.png)

Fishing Options Supported: Shrimps & Anchovies, Sardines & Herrings, Salmon & Pike, Lobster
Tree Options Supported: Trees, Oaks, Willows, Yews

If you’d like to check out the code, here it is!

Feature List

* Sexy, easy to understand script config UI
* 1-99 Fishing, banking or selling at store (& world hopping if store limit reached)
* Supports Karamja for lobster fishing
* 1-99 Woodcutting, banking or selling at store (& world hopping if store limit reached)
* Uses an unpopular yew-cutting spot to avoid ban
* You won’t get banned if you use this script reasonably. I have >60 fishing and >60 woodcutting from using this script over the past couple days while developing this script and it works great
* Hops worlds if mod present
* Reply if you’re interested in me adding an option to choose your own timer for the fishing/wc activities

Antiban, by Zen
Robust anti-pattern system that randomizes various activities while botting.
* Examine random entities (objects, npcs, ground and inventory items etc)
* Check a random stat (you can tell it which stats to check with antiban.setStatsToCheck(Skill.ATTACK, Skill.DEFENCE);)
* Type something random (gibberish)
* Move the mouse to a random location (and sometimes click the left or right button)
* Walk to a random location nearby
* Chop a random tree nearby
* Click on a random entity (object, npc, item etc.)
* Go AFK for a while (turns off autologin and random solvers temporarily)
* Open your inventory
* Open your stats menu
* Open your magic menu
* Open your combat menu (only if you have included Melee stats in your Stats to Check)
* Move the mouse off-screen for a while
* Move the camera randomly


## [Oak Cutting](https://github.com/pashpashpash/RunescapeBots/tree/master/src/OakCutter) Bot
Cuts Oak trees near Draynor Village and deposits them to the bank nearby.

##  [Willow Cutting](https://github.com/pashpashpash/RunescapeBots/tree/master/src/WillowCutter) Bot
Cuts Willow trees south of Rimmington and sells them to the general store nearby.

![ ](https://user-images.githubusercontent.com/20898225/185938695-b8e320d1-1d9d-4cd6-881e-854cae3a52ba.png)

## [Yew Cutting](https://github.com/pashpashpash/RunescapeBots/tree/master/src/YewCutter) Bot
Cuts Yews next to the Grand Exchange.

WARNING : After exploit-test-botting for 24 hours straight, I got banned while using the YewCutting script. Use at your own risk, stay safe.
