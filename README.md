# OptimisationAlbumPhoto
This project was created to generate a photo album with an organization depending of an evaluation function.


## Requirements
 - Java 8 or more : lambda are used ([see Oracle documentation](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html))

___
## How to install the project
- ``` ~$ git clone git@github.com:geoffreyp/OptimisationAlbumPhoto.git ```

___
## How to use the project
Execute the file execute.sh with these parameters : 
 - Required : the name of the evaluation function
 - Required : the name of the algorithm
 - Required : the number of execution
 - Required : the number of iteration for ILS algorithm, use 0 for other algorithm
 - Optional : boolean to use debug mode

``` ~$ ./execute.sh <evaluation_function> <algorithm> <nb_of_execution> <nb_of_ILS_iteration> <debug_mode> ```

Example :
- For launch once Hill Climber with the tags function : <br>
``` ~$ ./execute.sh tags hc 1 0 false ```

- For launch 5 times a Iterated Local Search with 2000 iterations with the ahash function :<br>
``` ~$ ./execute.sh ahash ils 5 2000 false   ```

- For launch 40 times a Hill Climber with greyavg function in debug mode : <br>
``` ~$ ./execute.sh greyavg hc 40 0 true```

You can now open in yout favorite web browser the website in the folder html/ 

____
## Algorithms used
|Code       | Name |
|-----------|-------------------|
| hc| Hill Climber First Improvement|
| ils|Iterated Local Search|
| In progress, don't used for the moment | Evolutionnay Algorithm |
___
## Functions used
|Code       | Name |
|-----------|-------------------|
| ahash| Use the average hash|
| tags|Use Ahash function with a tag system|
| greyavg | Use grey average |

 
 ___
## Licence
GNU GPL version 3
