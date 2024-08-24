# The Paper Engine "Quote" System
Quote is a Domain-Specific Language (DSL) for creating dialogue in Paper. This guide explains the syntax and features available for writing dialogue scripts. 

### Syntax Overview


1. `SCRIPT` - Run a Script
    * Description - Executes a specified script. This can be used to trigger additional functionality or logic.
    * Syntax - `SCRIPT scriptName`
    * Example
    ```PEQF
    SCRIPT startMainQuest
    ```

2. `NPC` - Set Dialogue
    * Description - Defines what the NPC will say
    * Syntax - `NPC -> "Hello how are you doing today!"`
    * Example
    ```PEQF
    NPC -> "Hello there $playerName! I'm going to destroy the world!"
    ```

3. Conditional Statements
    * Description - Controls the flow of dialogue based on conditions
    * Keywords:
        * `IF`, `ELSE`, `ELSEIF`, `THEN`, `END`
        * Conditional Keywords:
            * `NOT (!)`
            * `AND`
            * `OR`
            * `GREATER THAN (>)`
            * `GREATER THAN OR EQUAL TO (>=)`
            * `LESS THAN (<)`
            * `LESS THAN OR EQUAL TO (<=)`
            * `EQUAL TO (==)`


Engine Global Variables can be accessed by using the '$' variable indicator. The variable's value will be returned as a String. 

### Detailed Examples

#### Using SCRIPT

```PEQF
NPC -> "Let's begin your quest."
SCRIPT startQuest
```
#### Complex Conditional Logic

```PEQF
IF $playerLevel > 10 AND $hasAmulet THEN
    NPC -> "You are well-prepared for the challenge!"
ELSEIF $playerLevel <= 10 OR !$hasAmulet THEN
    NPC -> "You might need more preparation."
ELSE
    NPC -> "Good luck on your journey!"
END
```