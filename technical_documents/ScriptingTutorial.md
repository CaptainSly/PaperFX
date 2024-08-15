# Putting Pen to Paper - A Shitty Half Assed Scripting Tutorial 

PaperFX Uses LuaJ as it's scripting engine, the Scripting Todo List.txt holds all of the Globals that are introduced with the engine. Here is how to use each new added feature along with how to create scripts that work with PaperFX. 

# The Main Script Template

If you're creating a game for Paper, chances are you're going to want to actually start the game or create some system critical things for it. Well to do so you're going to need to create a Main Script File. It can be called whatever you want, and you're able to select the script when you create a plugin using Ink. The Main Script has some special functions that need to be present in order for it to be able to operate correctly. 

* The ```onInit()``` function
* The ```onNewGame()``` function

Below is a template for the Main Script File

```lua
--[[

    Name: Paper Engine Main Script File Template. 
    Author: Insert Your Name Here
    Type: Main Script File
    Plugin: Plugin Id

--]]


--- Called when the Plugin is intialized. 
function onInit()
   
end

--- Called when a new game is started
function onNewGame()
   
end
```

The ```onInit``` method is called once the Engine has fully loaded all the plugins and is ready to switch to the Main Menu. Right before switching, it initializes the Main Script. 

**WARNING IF YOU LOAD MULTIPLE MAIN PLUGINS WITH A MAIN SCRIPT FILE, THE LAST LOADED PLUGIN WILL HAVE ITS SCRIPT TAKE EFFECT, THIS IS TO ENSURE COMPATIBILITY**

## What can I not do inside ```onInit()```? 

```onInit()``` is called after Plugin Load, but before game start. You have access to the JavaFX thread and components, but the Game World is not yet loaded in.

DO NOT SET ANY OF THE FOLLOWING IN ```onInit()```!
    * calendar



## What can I not do inside ```onNewGame()```?

```onNewGame()``` is called once the New Game Button in the Main Menu is clicked. You should set the location and setup anything you think needs to be setup before the player gets to create their character. Once the new game button is clicked and the player creates their character, most of the engines features should be available to the scripting engine. 

## Default Scripting Globals

The "Engine" provides a few Scripting Globals that you can call straight from Lua without requiring any special files. Here is a list of them, if they state they provide a java object, look at the class that has the same name (if the global differs from the class name, the class name will be provided)

    * paper - The Lua Table that holds the Paper Scripting Globals
        * _VERSION - The current version of the Paper Engine | String
        * database - Reference to the database | Java Object
        * ppl - Reference to the Plugin Loader | Java Object | PaperPluginLoader
        * ini - Reference to the Ini file | Java Object | PaperIni
        * location - Reference to the Location Property | Java Object 
        * calendar - Reference to the Calendar | Java Object | Calendar

    * class - A small lua snippet that allows you to create class like objects

To call a Java Method from lua do the following

```lua
paper.database:getGlobal("")
paper.database:getLocation()
```

The equivalent java code would look like this
```java
Paper.DATABASE.getGlobal("")
Paper.DATABASE.getLocation("")
```

Java Methods are called with the ':' accessor, and the '.' accessors reference members. 

## Default Variable Globals

The "Engine" itself, contains a HashMap that holds various globals for game purposes. Here is a list of default ones that you can guarentee exists:

    * currentQuestId - String
    * currentQuestStage - String
    

To use one of the variable globals you would use the following depending on what you want to do

```lua
paper.database:addGlobal("") -- Adds a global (Best to only be done inside the onInit function, as this will not add it to the observable map)
paper.database:getGlobal("globalName") -- Getting the Global
paper.database:setGlobal("globalName", 42) -- Setting the Global
```

Setting the global updates it in two places, once in the main Database, and then once inside an Observable Map


## Calendar

The In Game Calendar System is used to control how Npcs interact with their world. It holds the date and time of the game world. When you create your main script file, you can do the following inside the onNewGame function


```lua
local days = { "day1", "day2", "day3", "day4", "day5", "dayEtc..."}
local months = { "month1", "month2", "month3", "etc..." }

function onNewGame()
    paper.calendar:setDays()
    paper.calendar:setMonths()
end
```

Theoretically you can add as many days and months as you like, though it'll make your years take forever as each month is 4 weeks. It's recommended to keep it to at least 7 days, 12 months or smaller. Days can not have differing number of days, as they the engine isn't programmed for that. Each month will have numOfDays * 4. 


## Items

Item scripts need to have an ```onUse(player)``` function to be able to work. 

Example Consumable Item Script

```lua

--[[

    Name: Paper Engine Item Script File Template. 
    Author: Insert Your Name Here
    Type: Item Script File
    Plugin: Plugin Id

--]]

local healAmount = 25

function onUse(player)
    player:getActorState():addHp(healAmount)
end

```