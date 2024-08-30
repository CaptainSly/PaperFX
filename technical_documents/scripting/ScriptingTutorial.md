## Scripting Globals

```lua
paper -- Table

-- Read the LuaSimpleClass.md file for more information
class(table, function() end) -- Function

paper._VERSION -- Variable
paper.database -- Reference | Database.java
paper.ppl -- Reference | PaperPluginLoader.java
paper.ini -- Reference | PaperIni.java
paper.calendar -- Reference | Calendar.java
paper.world -- Reference | World.java
paper.player -- Reference | Player.java

paper.setLocation("") -- Function

paper.setCalendarDM({}, {}) -- Function | Arg1: String[] of Days, Arg2: String[] of Months
paper.setCalendarYear(0) -- Function
paper.setCalendarTime(0) -- Function
paper.setCalendarDate(0) -- Function

paper.playMusic("trackName") -- Function 
paper.playAmbience("trackName") -- Function
paper.playSoundEffect("trackName") -- Function
```



## Script Templates


Main Script Lua Template
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

Item Script Lua Template
```lua

--[[

    Name: Paper Engine Item Script File Template. 
    Author: Insert Your Name Here
    Type: Item Script File
    Plugin: Plugin Id

--]]

function onUse(actor)

end
```

Action Script Lua Template

Actions can either have ```onAction()``` or ```onRepeatedAction()``` but not both.

```lua

--[[

    Name: Paper Engine Action Script File Template.
    Author: Insert Your Name Here
    Type: Item Script File
    Plugin: Plugin Id

--]]

function onAction()

end

-- OR

function onRepeatedAction()

end

```

Location Script Lua Template
```lua

--[[

    Name: Paper Engine Action Script File Template.
    Author: Insert Your Name Here
    Type: Item Script File
    Plugin: Plugin Id

--]]

function onVisit()

end

function onLeave()

end

```