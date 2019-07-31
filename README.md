# Edgequest

Edgequest is a rougelike inspired, top down game. The game takes place in a infinite sandbox that contains an above ground and multiple levels of underground dungeons. This demo has been written in Java and OpenGL to help illustrate the above ground mechanics of the game. Due to its pre-alpha state, many features have yet to be added and optimizations still need to be made.

## Playing Edgequest
In order to play Edgequest, you'll need Java 8 or newer. 
From there, download the Edgequest launcher [here](https://github.com/keco185/EdgeQuestLauncher/releases/download/v0.1/edgequest.jar) ([source code](https://github.com/keco185/EdgeQuestLauncher))

From there, run the .jar file!

## Controls
w,a,s,d - walk\
shift - sprint\
right click - aim\
windows/cmd key - aim (for single button trackpads)\
left click - break item/use weapon/place item\
q - enter/leave inventory\
e - pickup item\
r - enter/leave menu\
t - bring up chat\
f - place torch\
tab - walk to location
up arrow - zoom in
down arrow - zoom out

## Cheats
type /help into chat to see a list of commands. There are a lot of fun ones.\
consider /give pistol then /give bullet 50 then put the pistol into your left hand. Then /spawn troll and have an epic battle.

## Items
#### Usable
- dagger (melee combat item, place in hand spot, aim, and attack)
- pistol (ranged combat item, place in hand spot, aim, and shoot)
- bullet (you need these in your inventory to use the pistol)
- torch (place down to light the way)
- lantern (place in hand to have portable light)

#### Blocks
- lilypad (great way to not get wet)
- dirt
- grass
- ice (slippery)
- sand
- snow (creates footprints)
- stone
- water (hard to walk through)
- tree
- dungeon (press e to climb down)
- ladder (press e to climb up)
- wood
- asphalt (follow the road)
- ground
- dark wood
- light wood
- sandy stone
- snowy stone

## Entities
- ant (passive creature that will eat through walls, spawns in caves)\
- troll (throws swords at you, spawns deep in caves)\
- you (I hope I don't need to explain this one)

## Getting Started
Launch the game and click new game. In the field you can enter a save name. Remember it. The save name is also used as a random seed for the world. The world itself is infinite and generated using simplex noise. It's full of lakes, fields, deserts, wastelands, tundras, etc. Because areas are generated using multiple parameters (temperature, elevation, etc) you can have great combinations of terrain. There is a day/night cycle but no monsters spawn above ground. Feel free to explore. Maybe you'll come across a village. 

Nearby villages are connected with roads. After a while, you might stumble upon a hole in the ground. This is the entry to a dungeon. Press e to go down. In the dungeon, you are given a light soure no matter what (this is just there for development purposes). In the full version, you will need to have a lantern to see. Dungeons go down hundreds of layers and there is no limit to their full size. 

Place torches to light up the space (look at controls and use /help to find the /give command in chat). You will notice that you can have infinite light sources in a scene and the lighting is CPU based with a 2D ray tracing algorithm I designed. The lighting supports full RGB which you can configure using one of the commands listed in /help. 

In the dungeon you may find ants. These guys are harmless, but will eat blocks in their way (including your torches). Use a dagger or pistol to kill them. If you go deep enough, you will find trolls. These guys will try and kill you! avoid their throwing knives and either kill them first or run away. The deeper you go, the more of them you will find. Make sure you play with all the commands in /help and have fun!

## Features
- Day/Night cycle for ambiance ;)
- Weather (rain and snow)
- Infinite world with simplex noise terrain generation
- Villages scattered about, connected by roads
- Randomly generated dungeons that go 100 levels deep
- Real time 2D RGB raytracing for realistic lighting
- Custom shaders to help with lighting and drawing terrain
- Passive and aggressive entities to keep you entertained
- A* pathfinding for both you and trolls (tab key to use)
- Footprints in snow and a wake in water
- Animated blocks (torches, water, etc)
- Weapons (pistol and dagger)
- Fun commands to try (/help in chat)

## Future Plans
Future plans for edgequest are currently on hold. Here were some of the planned features: 
- Adding sub-biome level block generation
- AI
- Sound
- Foliage 
- Animals 
- Online multiplayer
- Crafting
- Food
- And more!
