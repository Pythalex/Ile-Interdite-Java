# L'Île Interdite

### Java implementation of the board game L'Île interdite (Forbidden Island)



---



![alt text](http://www.cyberfab.fr/gfx/lile_interdite/lile_interdite.jpg)



---



## Game conception



The game modules are designed in 4 main modules:



[![Ile_Interdite_Diagram.png](https://s14.postimg.cc/ezux4ppyp/Ile_Interdite_Diagram.png)](https://postimg.cc/image/4psi5h031/)



Game manages the game's rules and has references of the players and the isle.



The isle is basically a grid of cases, which are a grid case with a state, containing event or not.



The player is a marker of the player position, collected artifacts and keys, etc ...