# L'Île Interdite

### Java implementation of the board game L'Île interdite (Forbidden Island)



---



![Forbidden Island game's box](http://www.cyberfab.fr/gfx/lile_interdite/lile_interdite.jpg)



---

## What does it look like ?

Appearance since 5/8/2018 update (May the 5th)
![game screenshot](https://i.gyazo.com/7b85960c731986e5bf5abca9d83c95da.png)


## Game conception



The game modules are designed in 4 main modules:


![Ile_Interdite-_UML.png](https://image.ibb.co/mXaboy/UMLfinal.png)

Game manages the game's rules and has references of the players and the isle.



The isle is basically a grid of cases, which are a grid case with a state, containing event or not.



The player is a marker of the player position, collected artifacts and keys, etc ...
