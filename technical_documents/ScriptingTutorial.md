# Putting Pen to Paper - A Shitty Half Assed Scripting Tutorial 

PaperFX Uses Lua as it's scripting engine, the Scripting Todo List.txt holds all of the Globals that are introduced with the engine. Here is how to use each new added feature along with how to create scripts that work with PaperFX. 

# The Main Script Template

If you're creating a game for Paper, chances are you're going to want to actually start the game or create some system critical things for it. Well to do so you're going to need to create a Main Script File. It can be called whatever you want, and you're able to select the script when you create a plugin using Ink. The Main Script has some special functions that need to be present in order for it to be able to operate correctly. 

* The onInit() function
* The onNewGame() function

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

The onInit method is called once the Engine has fully loaded all the plugins and is ready to switch to the Main Menu. Right before switching, it initializes the Main Script. 

**WARNING IF YOU LOAD MULTIPLE MAIN PLUGINS WITH A MAIN SCRIPT FILE, THE LAST LOADED PLUGIN WILL HAVE ITS SCRIPT TAKE EFFECT, THIS IS TO ENSURE COMPATIBILITY**

## What can I not do inside onInit()? 

onInit() is called after Plugin Load, but before game start. You have access to the JavaFX thread and components, but the Game World is not yet loaded in.


## What can I not do inside onNewGame()?

onNewGame() is called once the New Game Button in the Main Menu is clicked. You should set the location and setup anything you think needs to be setup before the player gets to create their character. Once the new game button is clicked and the player creates their character, most of the engines features should be available to the scripting engine. 