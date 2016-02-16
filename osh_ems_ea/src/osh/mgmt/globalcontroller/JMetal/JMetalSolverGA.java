/*
 ************************************************************************************************* 
 * OrganicSmartHome [Version 2.0] is a framework for energy management in intelligent buildings
 * Copyright (C) 2014  Florian Allerding (florian.allerding@kit.edu) and Kaibin Bao and
 *                     Ingo Mauser and Till Schuberth
 * 
 * 
 * This file is part of the OrganicSmartHome.
 * 
 * OrganicSmartHome is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * 
 * OrganicSmartHome is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OrganicSmartHome.  
 * 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 *************************************************************************************************
 */

package osh.mgmt.globalcontroller.JMetal;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import osh.core.OSHGlobalLogger;
import osh.core.OSHRandomGenerator;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.observer.EAProblemPartExchange;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class JMetalSolverGA extends JMetalSolver {
	
	/**
	 * INNER CLASS
	 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
	 *
	 */
	public static class GAParameters implements Cloneable {
		public int numEvaluations = 1000;
		public int popSize = 50;
		public double crossoverProbability = 0.7;
		public double mutationProbability = 0.1;
		
		@Override
		public GAParameters clone() {
			try {
				return (GAParameters) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private GAParameters gaparameters;
	private long timestamp;
	
	/**
	 * CONSTRUCTOR
	 * @param globalLogger
	 * @param randomGenerator
	 * @param showDebugMessages
	 * @param parameters
	 */
	public JMetalSolverGA(
			OSHGlobalLogger globalLogger,
			OSHRandomGenerator randomGenerator,
			boolean showDebugMessages,
			GAParameters parameters,
			long timestamp) {
		super(globalLogger, randomGenerator, showDebugMessages);
		
		this.gaparameters = parameters.clone();
		this.timestamp = timestamp;
	}
	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SolutionWithFitness getSolutionAndFitness(
			List<EAProblemPartExchange<?>> problemparts, 
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals,
			long ignoreLoadProfileBefore,
			IFitness fitnessFunction) throws Exception {

		
		//TODO: find something to get a fast fitness
//		// return if all DOF=0
		int numberOfBits = 0;
		for (EAProblemPartExchange i : problemparts) {
			numberOfBits = numberOfBits + i.getBitCount();
		}
		
		// DECLARATION
	    Problem problem;			// The problem to solve
	    Algorithm algorithm;		// The algorithm to use
	    Operator mutation;			// Mutation operator
	    Operator  crossover;
	    Operator  selection;
		HashMap parameters;			// Operator parameters

		// calculate ignoreLoadProfileAfter (Optimizaion Horizon)
		long ignoreLoadProfileAfter = ignoreLoadProfileBefore;
		for (EAProblemPartExchange ex : problemparts) {
			ignoreLoadProfileAfter = Math.max(ignoreLoadProfileAfter, ex.getOptimizationHorizon());
		}
	    
	    // INITIALIZATION
	    problem = new MyProblem(
	    		problemparts, 
	    		priceSignals,
	    		powerLimitSignals,
	    		ignoreLoadProfileBefore,
	    		ignoreLoadProfileAfter,
	    		this.randomGenerator,
	    		this.globalLogger,
	    		fitnessFunction);
	    
	    
	    // SHORT CUT IFF NOTHING HAS TO BE OPTIMIZED
 		if (numberOfBits == 0) {
 			Solution solution = new Solution(problem);
 			problem.evaluate(solution);

 			List<BitSet> emptySolution = new ArrayList<>();
 			for (EAProblemPartExchange part : problemparts) {
 				emptySolution.add(new BitSet());
 			}
 			SolutionWithFitness result = new SolutionWithFitness(emptySolution, solution.getFitness());
 			return result;
 		}
	    
	    algorithm = new gGA(problem, false);
	    
	    /* Algorithm parameters */
	    algorithm.setInputParameter("maxEvaluations", gaparameters.numEvaluations);
	    algorithm.setInputParameter("populationSize", gaparameters.popSize); 
	    
	    /* Mutation and Crossover for Real codification */
	    parameters = new HashMap();
	    parameters.put("probability", gaparameters.mutationProbability);
	    mutation = MutationFactory.getMutationOperator(
	    		"BitFlipMutation", 
	    		parameters,
	    		this.randomGenerator);                    
	    
	    //crossover
	    parameters = new HashMap();
	    parameters.put("probability", gaparameters.crossoverProbability);
	    crossover = CrossoverFactory.getCrossoverOperator(
	    		"SinglePointCrossover", parameters, this.randomGenerator); 
	    
	    //selection
	    parameters = null ;
	    selection = SelectionFactory.getSelectionOperator(
	    		"BinaryTournament", parameters, this.randomGenerator); 
	    
	    //add the operators
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation", mutation);
	    algorithm.addOperator("selection",selection);
	    
	    /* Execute the Algorithm */
	    SolutionSet population = algorithm.execute();

		Binary s = (Binary) population.best(new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				double v1 = o1.getObjective(0), v2 = o2.getObjective(0);
				if (v1 < v2) return 1;
				else if (v1 > v2) return -1;
				else return 0;
			}
		}).getDecisionVariables()[0];
		
		int bitpos = 0;
		ArrayList<BitSet> resultBitSet = new ArrayList<BitSet>();
		for (EAProblemPartExchange part : problemparts) {
			resultBitSet.add(s.bits_.get(bitpos, bitpos + part.getBitCount()));
			bitpos += part.getBitCount();
		}
		
		if(true){ // debug
			for (int i = 0; i < population.size(); i++) {
				Solution solution = population.get(i);
				System.out.print(solution.getObjective(0) + " ; ");
				
			}
			System.out.println();
		}
		
		double returnFitness = population.best(new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				double v1 = o1.getObjective(0), v2 = o2.getObjective(0);
				if (v1 < v2) return 1;
				else if (v1 > v2) return -1;
				else return 0;
			}
		}).getObjective(0);
		
		SolutionWithFitness result = new SolutionWithFitness(resultBitSet, returnFitness);
		return result;
	}
	
}

