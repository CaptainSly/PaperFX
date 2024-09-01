### Scripting Globals

1. **paper Table**
   - Description: Contains various references and functions related to the Paper engine.
   
2. **Globals**
   - `paper._VERSION` — Version of the Paper engine.
   - `paper.database` — Reference to `Database.java`.
   - `paper.ppl` — Reference to `PaperPluginLoader.java`.
   - `paper.ini` — Reference to `PaperIni.java`.
   - `paper.calendar` — Reference to `Calendar.java`.
   - `paper.world` — Reference to `World.java`.
   - `paper.player` — Reference to `Player.java`.

3. **Functions**
   - `paper.setLocation("locationId")` — Sets the current location by ID.
   - `paper.setCalendarDM(days, months)` — Sets calendar days and months. 
     - `days`: Array of strings representing days.
     - `months`: Array of strings representing months.
   - `paper.setCalendarYear(year)` — Sets the calendar year.
   - `paper.setCalendarTime(time)` — Sets the calendar time.
   - `paper.setCalendarDate(date)` — Sets the calendar date.
   - `paper.playMusic("trackName")` — Plays a music track.
   - `paper.playAmbience("trackName")` — Plays an ambience track.
   - `paper.playSoundEffect("trackName")` — Plays a sound effect.

### Script Templates

#### Main Script Lua Template
```lua
--[[

    Name: Paper Engine Main Script File Template
    Author: [Your Name Here]
    Type: Main Script File
    Plugin: [Plugin Id]

--]]

--- Called when the Plugin is initialized
function onInit()
    -- Initialization code here
end

--- Called when a new game is started
function onNewGame()
    -- Code for starting a new game here
end
```

#### Item Script Lua Template
```lua
--[[

    Name: Paper Engine Item Script File Template
    Author: [Your Name Here]
    Type: Item Script File
    Plugin: [Plugin Id]

--]]

--- Called when the item is used
function onUse(actor)
    -- Code for item usage here
end
```

#### Action Script Lua Template
```lua
--[[

    Name: Paper Engine Action Script File Template
    Author: [Your Name Here]
    Type: Action Script File
    Plugin: [Plugin Id]

--]]

--- Called when an action occurs
function onAction()
    -- Code for action here
end

--- OR

--- Called repeatedly during an action
function onRepeatedAction()
    -- Code for repeated action here
end
```

#### Location Script Lua Template
```lua
--[[

    Name: Paper Engine Location Script File Template
    Author: [Your Name Here]
    Type: Location Script File
    Plugin: [Plugin Id]

--]]

--- Called when the location is visited
function onVisit()
    -- Code for visiting location here
end

--- Called when leaving the location
function onLeave()
    -- Code for leaving location here
end
```

#### Quest Script Lua Template
```lua
--[[

    Name: Paper Engine Quest Script File Template
    Author: [Your Name Here]
    Type: Quest Script File
    Plugin: [Plugin Id]

--]]

--- Called when a quest starts
function onQuestStart(quest)
    -- Code for starting a quest here
end

--- Called when a quest stage advances
function onQuestStageAdvance(stage)
    -- Code for advancing quest stage here
end
```
