# All Around To-Do List


Redesign the Entire UI, both the Editor and Paper itself. 
Ultimately learn how to use FXML, and Switch everything to it. Make designing the UI 10,000 times easier and faster.

Think about using Iternationalization (i18n)

Think about swapping to a Gradle Multi-project setup.
- Ink - The Editor
- Pen - Core Library
- Paper - The "Engine"

Think about swapping around Pen and Ink's names. As the Core library would make more sense to be called Ink and the Editor called Pen. That way using Ink (core), you can use Pen (editor) to write on Paper ("engine")

Refactor EVERY Class so the code is more cleaner and more readable. Try to setup a coding style and stick to it. 

#### Paper

[] Refactor a lot of the code, separate UI from Logic. 

[] Add in Audio and Music. Probably need to use 3 different MediaPlayers unless JavaFX allows you to play multiple sounds/music at once. 
* Ambient Audio - For background noises
* Background Music - Background Music that will kick in every so often
* Sound Effect - For any sound effects that need to play. 

#### INK

[] Maybe swap the Lists in InkFX, for a EditorDatabase class? Makes the main InkFX class a little cluttered. 

[] Add "Test from Engine" to the Editor

[] Clean up the PluginMetadata Screen. It's kinda of a mess. 

[] Add Validation to the Save Object buttons on each of the tabs. Tighten up each tab in the editor, make any record change mark the tab as dirty. 

[] Get Started on the Item, Lootlist, Quest and Building Editor

#### PLUGIN STUFF

[] Tighten Up Plugin Sorting, introduce more checks and quite possibly a tagging system. The Bethesda game modding tool, LOOT, can be checked out for more information. 

[] Add a flag for non-editable MAIN Plugins. I.e. Game Plugins that are published so they can't be tampered. They can still be used as dependencies, but their internally data can not be edited, only modified. (Modifying any record from another plugin saves it into the current active plugin, not the plugin the record originally came from.)

[~] Figure out why the plugin corrupts seemingly out of random. CRITICAL
* Not happening due to removing records, though testing needs to be done with plugins with >=30 records. 
* Seems to be because the Actor Type never gets set, things have been changed internally from an Actor hashmap to a Npc hashmap. Hopefully this fixes that completely.




#### SCRIPTING STUFF

[] Swap the "Global" Variables for methods 

