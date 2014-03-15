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

    public PlannerAgent(int playernum, String[] arguments) {
        super(playernum);
    }

    @Override
    public Map<Integer, edu.cwru.sepia.action.Action> initialStep(StateView newstate, History.HistoryView statehistory) {
        step = 0;
        return middleStep(newstate, statehistory);
    }

    @Override
    public Map<Integer, edu.cwru.sepia.action.Action> middleStep(StateView newState, History.HistoryView statehistory) {
        step++;
        Map<Integer,edu.cwru.sepia.action.Action> builder = new HashMap<Integer,edu.cwru.sepia.action.Action>();
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
