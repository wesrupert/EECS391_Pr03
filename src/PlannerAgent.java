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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.model.history.History;
import edu.cwru.sepia.environment.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.environment.model.state.ResourceNode.Type;
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
		
        List<PlanAction> actions = PlanAction.getActions(scenario);
        State startState = State.getStartState(peasantIds.get(0));
        State goalState = State.getGoalState(scenario);
        
        Planner planner = new Planner(actions, startState, goalState);
        plan = planner.createPlan();
        
        return middleStep(newstate, statehistory);
    }
	
	@Override
    public Map<Integer, edu.cwru.sepia.action.Action> middleStep(StateView newState, History.HistoryView statehistory) {
        step++;
        Map<Integer,edu.cwru.sepia.action.Action> builder = new HashMap<Integer,edu.cwru.sepia.action.Action>();
        
        List<Integer> allUnitIds = newState.getAllUnitIds();
		List<UnitView> peasants = new ArrayList<>();
		List<UnitView> townhalls = new ArrayList<>();
		for(int i=0; i<allUnitIds.size(); i++) {
			int id = allUnitIds.get(i);
			UnitView unit = newState.getUnit(id);
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("TownHall"))
				townhalls.add(unit);
			if(unitTypeName.equals("Peasant"))
				peasants.add(unit);
		}
		
		Action b = null;
		State state = plan.remove(0);
		
		// TODO make sure that the state list doesn't contain the first state
		PlanAction action = state.getFromParent();
		int peasantId = 0;
		int location = 0;
		UnitView peasant = null;
		ResourceView resource = null;
		switch (action.getName()) {
		case "Move":
			//Move from peasent id to location
			peasantId = action.getConstants().get(0).getValue();
			location = action.getConstants().get(2).getValue();
			peasant = newState.getUnit(peasantId);
			resource = findClosest(peasant, location, newState);
			if (resource != null) {
				b = new LocatedAction(peasantId, ActionType.COMPOUNDMOVE, resource.getXPosition(), resource.getYPosition());
				builder.put(peasantId, b);
			} else {
				b = new LocatedAction(peasantId, ActionType.COMPOUNDMOVE, townhalls.get(0).getXPosition(), townhalls.get(0).getYPosition());
				builder.put(peasantId, b);
			}
			break;
		case "Harvest":
			peasantId = action.getConstants().get(0).getValue();
			location = action.getConstants().get(1).getValue();
			peasant = newState.getUnit(peasantId);
			resource = findClosest(peasant, location, newState);
			if (resource == null) {
				System.out.println("Issue with finding closest resource");
			}
			b = new TargetedAction(peasantId, ActionType.COMPOUNDGATHER, resource.getID());
			builder.put(peasantId, b);
			break;
		case "Deposit":
			peasantId = action.getConstants().get(0).getValue();
			b = new TargetedAction(peasantId, ActionType.COMPOUNDDEPOSIT, townhalls.get(0).getID());
			builder.put(peasantId, b);
			break;
		}

        return builder;
    }

    private ResourceView findClosest(UnitView peasant, int location, StateView currentState) {
    	List<ResourceView> resources = null;
		if (location == Condition.TOWNHALL.getValue()) {
			return null;
		} else if (location == Condition.GOLDMINE.getValue()) {
			resources = currentState.getResourceNodes(Type.GOLD_MINE);
		} else if (location == Condition.FOREST.getValue()) {
			resources = currentState.getResourceNodes(Type.TREE);
		} else {
			System.out.println("Something went wrong when finding closest resource");
		}
		
		double shortestDist = Double.MAX_VALUE;
		ResourceView closestResource = null;
		for (ResourceView resource : resources) {
			double dist = Math.sqrt(
					(peasant.getXPosition() * peasant.getXPosition()) - (resource.getXPosition() * resource.getXPosition()) +
					(peasant.getYPosition() * peasant.getYPosition()) - (resource.getYPosition() * resource.getYPosition()));
			if (dist < shortestDist) {
				shortestDist = dist;
				closestResource = resource;
			}
		}
		
		return closestResource;
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
