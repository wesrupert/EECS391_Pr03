/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
//package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.model.history.History;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;

/**
 * 
 * 
 * @author wkr3, jxb532
 * 
 */
public class PlannerAgent extends Agent {
    private static final long serialVersionUID = -4047208702628325380L;
    private int step;
    private int scenario;
    List<State> plan;

    public PlannerAgent(int playernum, String[] arguments) {
        super(playernum);
        
        scenario = Integer.parseInt(arguments[0]);
    }

    @Override
    public Map<Integer, edu.cwru.sepia.action.Action> initialStep(StateView newstate, History.HistoryView statehistory) {
        step = 0;
        
        List<Integer> allUnitIds = newstate.getAllUnitIds();
		List<Integer> peasantIds = new ArrayList<Integer>();
		for (int i = 0; i < allUnitIds.size(); i++) {
			int id = allUnitIds.get(i);
			UnitView unit = newstate.getUnit(id);
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("Peasant")) {
				peasantIds.add(id);
			}
		}
		
        List<PlanAction> actions = getActions();
        State startState = getStartState(peasantIds.get(0));
        State goalState = getGoalState();
        
        Planner planner = new Planner(actions, startState, goalState);
        plan = planner.createPlan();
        
        return middleStep(newstate, statehistory);
    }

	private List<PlanAction> getActions() {
    	List<PlanAction> actions = new ArrayList<PlanAction>();
    	
		switch (scenario) {
		case 1:
		case 2:
			// Single peasant actions
			
			actions.add(getMoveAction());
			actions.add(getHarvestAction());
		case 3:
		case 4:
			// Multiple peasant actions
			
		default:
			return actions;
		}
	}

	private static PlanAction getMoveAction() {
		// Add the precondition At(id, from)
		List<Condition> preconditions = new ArrayList<>();
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));

		// Add At(id, to) to the Add list
		List<Condition> add = new ArrayList<>();
		add.add(new Condition("At", new Value[] {new Value("id"), new Value("to")}));
		
		// Add At(id, from) to the delete list
		List<Condition> delete = new ArrayList<>();
		delete.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));
		
		// Move action is Move(id, from, to)
		PlanAction moveAction = new PlanAction("Move", new HashSet<String>(Arrays.asList(new String[] {"id", "from", "to"})),
				preconditions, add, delete);
		return moveAction;
	}
	
	private static PlanAction getHarvestAction() {
		// Add the precondition Holding(id, Nothing)
		List<Condition> preconditions = new ArrayList<>();
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		
		// Add the precondition At(id, pos)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("pos")}));
		
		// Add the precondition Contains(pos, type)
		preconditions.add(new Condition("Contains", new Value[] {new Value("pos"), new Value("type")}));
		
		// Add Holding(id, type) to the Add list
		List<Condition> add = new ArrayList<>();
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Add Holding(id, Nothing) to the delete list
		List<Condition> delete = new ArrayList<>();
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		
		// Harvest action is Harvest(id, pos, type)
		PlanAction harvestAction = new PlanAction("Harvest", new HashSet<String>(Arrays.asList(new String[] {"id", "pos", "type"})),
				preconditions, add, delete);
		return harvestAction;
	}
	
	private static PlanAction getDepositAction() {
		// Add the precondition Holding(id, type)
		List<Condition> preconditions = new ArrayList<>();
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Add the precondition At(id, Townhall)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value(Condition.TOWNHALL)}));

		// Add Holding(id, Nothing) to the Add list
		List<Condition> add = new ArrayList<>();
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		
		// Add +Has(type, amt) to the Add list
		add.add(new Condition("Has", new Value[] {new Value("type"), new Value("amt", Value.Type.ADD)}));
		
		// Add Holding(id, type) to the delete list
		List<Condition> delete = new ArrayList<>();
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Deposit action is Deposit(id, type)
		PlanAction harvestAction = new PlanAction("Deposit", new HashSet<String>(Arrays.asList(new String[] {"id", "type"})),
				preconditions, add, delete);
		return harvestAction;
	}
	
	//TODO fix this after the refactor
	private State getStartState(int peasantId) {
		List<Condition> conditions = new ArrayList<>();
		
//		Map<String, Value> holdingMap = new HashMap<>();
//		holdingMap.put("id", new Value(peasantId));
//		holdingMap.put("type", Condition.NOTHING);
//		conditions.add(new Condition(Condition.HOLDING, holdingMap));
//		
//		Map<String, Value> atMap = new HashMap<>();
//		atMap.put("id", new Value(peasantId));
//		atMap.put("pos", Condition.TOWNHALL);
//		conditions.add(new Condition(Condition.AT, atMap));
//		
//		Map<String, Value> hasMap = new HashMap<>();
//		hasMap.put("type", Condition.WOOD);
//		hasMap.put("amt", new Value(0));
//		conditions.add(new Condition(Condition.HAS, hasMap));
//		
//		Map<String, Value> hasMap2 = new HashMap<>();
//		hasMap2.put("type", Condition.GOLD);
//		hasMap2.put("amt", new Value(0));
//		conditions.add(new Condition(Condition.HAS, hasMap2));
//		
//		Map<String, Value> containsMap = new HashMap<>();
//		containsMap.put("pos", Condition.GOLDMINE);
//		containsMap.put("type", Condition.GOLD);
//		conditions.add(new Condition(Condition.CONTAINS, containsMap));
//		
//		Map<String, Value> containsMap2 = new HashMap<>();
//		containsMap2.put("pos", Condition.FOREST);
//		containsMap2.put("type", Condition.WOOD);
//		conditions.add(new Condition(Condition.CONTAINS, containsMap2));
		
		return new State(conditions);
	}
	
	private State getGoalState() {
		List<Condition> conditions = new ArrayList<>();
		
		int wood = 0;
		int gold = 0;
		switch (scenario) {
		case 1:
			wood = 200;
			gold = 200;
		case 2:
		case 3:
			wood = 1000;
			gold = 1000;
		case 4:
			wood = 2000;
			gold = 3000;
		}
		
		//TODO fix this after the refactor
//		Map<String, Value> hasMap = new HashMap<>();
//		hasMap.put("type", Condition.WOOD);
//		hasMap.put("amt", new Value(wood));
//		conditions.add(new Condition(Condition.HAS, hasMap));
//		
//		Map<String, Value> hasMap2 = new HashMap<>();
//		hasMap2.put("type", Condition.GOLD);
//		hasMap2.put("amt", new Value(gold));
//		conditions.add(new Condition(Condition.HAS, hasMap2));
		
		return new State(conditions);
	}

	@Override
    public Map<Integer, edu.cwru.sepia.action.Action> middleStep(StateView newState, History.HistoryView statehistory) {
        step++;
        Map<Integer,edu.cwru.sepia.action.Action> builder = new HashMap<Integer,edu.cwru.sepia.action.Action>();
        
        List<Integer> allUnitIds = newState.getAllUnitIds();
		List<Integer> peasantIds = new ArrayList<Integer>();
		List<Integer> townhallIds = new ArrayList<Integer>();
		for(int i=0; i<allUnitIds.size(); i++) {
			int id = allUnitIds.get(i);
			UnitView unit = newState.getUnit(id);
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("TownHall"))
				townhallIds.add(id);
			if(unitTypeName.equals("Peasant"))
				peasantIds.add(id);
		}
		
		Action b = null;
		State action = plan.remove(0);
//		
//		if (name.equalsIgnoreCase("Move")) {
//			action.
//		}
//		b = new TargetedAction(peasantId, ActionType.COMPOUNDDEPOSIT, townhallId);
//		
//		List<Integer> resourceIds = currentState.getResourceNodeIds(Type.TREE);
//		b = new TargetedAction(peasantId, ActionType.COMPOUNDGATHER, resourceIds.get(0));
//
//		builder.put(peasantId, b);
//        
        return builder;
    }

    @Override
    public void terminalStep(StateView newstate, History.HistoryView statehistory) {
        step++;
    }

    public static String getUsage() {
        return "Plans a thing.";
    }

    @Override
    public void savePlayerData(OutputStream os) {
        // this agent lacks learning and so has nothing to persist.
    }

    @Override
    public void loadPlayerData(InputStream is) {
        // this agent lacks learning and so has nothing to persist.
    }

    //
    // Alpha Beta Algorithms
    //
}
