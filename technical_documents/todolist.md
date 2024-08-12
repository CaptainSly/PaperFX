# All Around To-Do List

Redesign the Entire UI, both the Editor and Paper itself. 
Ultimately learn how to use FXML, and Switch everything to it. Make designing the UI 10,000 times easier and faster.


INK

Add "Test from Engine" to the Editor in Version 1.0.0

When selecting items from the lists to edit their data, prioritize pulling the data from the current plugin if it exists instead of just pulling from the database. That way we never really edit the database. 

Tighten up each tab in the editor, make any record change mark the tab as dirty. 
Give the editor fields a bit of logic so an object with conflicting and/or illegal data doesn't get sent out. 


PLUGIN STUFF

Add a flag for non-editable MAIN Plugins. I.e. Game Plugins that are published so they can't be tampered. They can still be used as dependencies, but their internally data can not be edited, only modified. (Modifying any record from another plugin saves it into the current active plugin, not the plugin the record originally came from.)

Figure out why the plugin corrupts seemingly out of random. CRITICAL
    -> Not happening due to removing records, though testing needs to be done with plugins with >=30 records. 



SCRIPTING STUFF

Keep working on the Documentation for the Scripting Engine
