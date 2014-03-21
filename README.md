# EECS 391 - Programming Assignment 3
### Authors: Josh Braun (jxb532), Wes Rupert (wkr3)

This is third programming assignment for Case Western Reserve University's EECS 391 Introduction to Artificial Intelligence course. The project requires CWRU's SEPIA AI engine to run.

To run the assignment parts, execute the following:

```bat
sh buildMidasGame1.sh
sh buildMidasGame2.sh
sh buildMidasGame3.sh
sh buildMidasGame4.sh
```

The different build scripts will build different scenarios.

Scenario 1:
	Single peasant; Goal: 200 Gold, 200 Wood

Scenario 2:
	Single peasant; Goal: 1000 Gold, 1000 Wood
	
Scenario 3:
	Multiple peasants; Goal: 1000 Gold, 1000 Wood
	
Scenario 4:
	Multiple peasants; Goal: 3000 Gold, 2000 Wood
	
Each scenario will generate the series of actions required to reach the goal.
The actions will be printed in a file named "Scenario_X_plan.txt" where "X" is the scenario number.