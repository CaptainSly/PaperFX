# PaperFX

PaperFX is the JavaFX Version of the Paper TextBased Game "Engine". Notice "Engine" being put in quotes, as I have a tendency to not finish game engines I start writing, this is not going to be called one up front. Game Player/ Game Editor might be more fitting, but I digress. 

Paper is a Textbased RPG Game Editor/Player.

This "Engine", as I'll keep putting quotes, was inspired by a few different titles while it is being built. A list can be seen below:

#### PaperFX's Inspiration
* The Elder Scrolls and Fallout - (Inspired NpcAI, NpcStats, NpcSkills, Plugin Loading)
* Shadowrun Returns - (Plugin Loading)
* Dungeons and Dragons 5e - (NpcStats, Magic Schools, NpcSkills)
* Call of Cthulhu 7e (TTRPG) - (NpcSkills)

Someway somehow I've taken inspiration from the following titles when I build each system. (Granted how they say they're used as inspirations probably are stretching the truth, I can guarentee these games were all thought of when writing the code.) 

One of the main focuses for PaperFX is to provide a way for people to play Textbased "Open World" RPGs. Paper will allow the user to load Plugin files (*.pepm or *.pepf) to play games, similar to the way The Elder Scrolls and Fallout allow modding, though without the need to pay attention to your load order. 


## How to build

Run the Gradle Tasks - inkFX-FatJar and paperFX-FatJar respectively for each program. Run them through a console using java -jar *.jar. Will update when a better option is present. 

## How to use the Editor
A work in progress document is being made as the features and functionality are added. 
The document can be viewed [here](technical_documents/Editor%20Documentation.md)

## How to play a game using Paper

WIP


## Technical Documents

A [markdown document](technical_documents/LuaSimpleClass.md) that describes the Lua Simple Class implementation. 

A [doc](technical_documents/File%20Formats.txt) that describes the File Formats used by both Paper and Ink.

A [TODO](technical_documents/todolist.md) list detailing what I want to tackle for a myriad of different topics. 

A [Script Tutorial](technical_documents/ScriptingTutorial.md) of sorts. I'll try to write it as I fill out the engine. 


## Used Libraries
* Gson
* Ini4J
* ZStd
* JavaFX
* TinyLog2
* LuaJ 3.0.1 (Needs to be swapped for a reliable fork)
* Gluon RichTextArea