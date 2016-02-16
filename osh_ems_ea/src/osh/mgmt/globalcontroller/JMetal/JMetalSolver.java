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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.singleObjective.evolutionStrategy.ElitistES;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import osh.configuration.system.DeviceTypes;
import osh.core.OSHGlobalLogger;
import osh.core.OSHRandomGenerator;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.ea.Schedule;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.observer.EAProblemPartExchange;

/**
 * 
 * @author Till Schuberth, Ingo Mauser
 *
 */
@Deprecated
public class JMetalSolver {
	
	protected OSHRandomGenerator randomGenerator;
	protected OSHGlobalLogger globalLogger;
	private boolean showDebugMessages;
	
	
	/**
	 * CONSTRUCTOR
	 * @param globalLogger
	 * @param randomGenerator
	 * @param showDebugMessages
	 */
	public JMetalSolver(
			OSHGlobalLogger globalLogger,
			OSHRandomGenerator randomGenerator, 
			boolean showDebugMessages) {
		this.globalLogger = globalLogger;
		this.randomGenerator = randomGenerator;
		this.showDebugMessages = showDebugMessages;
	}
	
	
	public List<BitSet> getSolution(
			List<EAProblemPartExchange<?>> problemparts, 
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals, 
			long ignoreLoadProfileBefore,
			IFitness fitnessFunction) throws Exception {
		
		SolutionWithFitness result = getSolutionAndFitness(
				problemparts, 
				priceSignals,
				powerLimitSignals,
				ignoreLoadProfileBefore, 
				fitnessFunction);
		
		return result.getBitSet();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SolutionWithFitness getSolutionAndFitness(
			List<EAProblemPartExchange<?>> problemparts, 
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals, 
			long ignoreLoadProfileBefore,
			IFitness fitnessFunction) throws Exception {
		
		int mu = 1; // Requirement: lambda must be divisible by mu
	    int lambda = 10; // Population size
	    
	    int evaluations = 40 * lambda; // Generations = evaluations / lambda
		
		int numberOfBits = 0;
		for (EAProblemPartExchange i : problemparts) {
			numberOfBits = numberOfBits + i.getBitCount();
		}
		
		// DECLARATION
	    Problem problem;			// The problem to solve
	    Algorithm algorithm;		// The algorithm to use
	    Operator mutation;			// Mutation operator
	            
		HashMap parameters;			// Operator parameters

	    // calculate ignoreLoadProfileAfter (Optimization Horizon)
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

			SolutionWithFitness result = new SolutionWithFitness(Collections.<BitSet>emptyList(), solution.getFitness());
			return result;
		}
	    
	    algorithm = new ElitistES(problem, mu, lambda, showDebugMessages);
	    //algorithm = new NonElitistES(problem, mu, lambda);
	    
	    /* Algorithm parameters */
	    algorithm.setInputParameter("maxEvaluations", evaluations);
	    
	    /* Mutation and Crossover for Real codification */
	    parameters = new HashMap() ;
	    parameters.put("probability", 1.0/30) ;
	    mutation = MutationFactory.getMutationOperator(
	    		"BitFlipMutation", 
	    		parameters,
	    		this.randomGenerator);                    
	    
	    algorithm.addOperator("mutation", mutation);
	 
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

@SuppressWarnings("serial")
class MyProblem extends Problem {
	

	private IFitness fitnessfunction;
	
	private List<EAProblemPartExchange<?>> problemparts;
	private List<EAProblemPartExchange<?>> staticparts;
	
	private HashMap<DeviceTypes,Schedule> staticpartsschedule;
	
	private HashMap<VirtualCommodity,PriceSignal> priceSignals;
	private HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals;
	
	private long ignoreLoadProfileBefore;
	private long ignoreLoadProfileAfter;
	
	/**
	 * 
	 * @param problemparts
	 * @param priceSignal
	 * @param powerSignal
	 * @param ignoreLoadProfileBefore don't use power values before this timestamp (in seconds)
	 * @param randomGenerator
	 * @param optimizationObjective
	 * @throws ClassNotFoundException
	 */
	public MyProblem(
				Collection<EAProblemPartExchange<?>> problemparts, 
				HashMap<VirtualCommodity,PriceSignal> priceSignals,
				HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals,
				
				long ignoreLoadProfileBefore,
				long ignoreLoadProfileAfter,
				
				OSHRandomGenerator randomGenerator,
				OSHGlobalLogger globalLogger,
				
				IFitness fitnessFunction) 
			throws ClassNotFoundException {
		super(new PseudoRandom(randomGenerator));
		
		this.problemparts = new ArrayList<EAProblemPartExchange<?>>();
		
		this.priceSignals = priceSignals;
		this.powerLimitSignals = powerLimitSignals;
		
		this.ignoreLoadProfileBefore = ignoreLoadProfileBefore;
		this.ignoreLoadProfileAfter = ignoreLoadProfileAfter;
		
		this.staticparts = new ArrayList<EAProblemPartExchange<?>>();
		this.fitnessfunction = fitnessFunction;
		
		for (EAProblemPartExchange<?> i : problemparts) {
			if (i.getBitCount() > 0) {
				this.problemparts.add(i);
			} else {
				this.staticparts.add(i);
			}
		}
		
		staticpartsschedule = new HashMap<>();
		
		for (EAProblemPartExchange<?> i : staticparts) {
			
			DeviceTypes currentType = i.getDeviceType();
			
			Schedule currentSchedule = staticpartsschedule.get(currentType);
			if (currentSchedule == null) {
				currentSchedule = new Schedule(new SparseLoadProfile(), 0.0);
			}
					
			
			currentSchedule = currentSchedule.merge(i.getSchedule(new BitSet()));
			staticpartsschedule.put(currentType, currentSchedule);
			
		}
		
		int numberOfBits = 0;
		for (EAProblemPartExchange<?> part : problemparts) numberOfBits += part.getBitCount();
		
	    numberOfVariables_  = 1;
	    numberOfObjectives_ = 1;
	    numberOfConstraints_= 0;
	    problemName_        = "ControllerBox";
	             
	    solutionType_	= new BinarySolutionType(this) ;
	    	    
	    length_			= new int[numberOfVariables_];
	    length_[0] 		= numberOfBits ;
		
	}
	
	
	@Override
	public void evaluate(Solution solution) throws JMException {
		
		try {
		int bitpos = 0;
		double fitness = 0;
		
		Binary variable = (Binary) solution.getDecisionVariables()[0];
		
		HashMap<DeviceTypes,LinkedList<Schedule>> mapToMerge = new HashMap<>();
		
		// add all variable EA Parts to list (parts with bits...)
		for (EAProblemPartExchange<?> part : problemparts) {
			
			DeviceTypes currentDeviceType = part.getDeviceType();
			
			LinkedList<Schedule> currentScheduleList = mapToMerge.get(currentDeviceType);
			
			if (currentScheduleList == null) {
				currentScheduleList = new LinkedList<>();
				mapToMerge.put(currentDeviceType, currentScheduleList);
			}

			currentScheduleList.add(part.getSchedule(variable.bits_.get(bitpos, bitpos + part.getBitCount())));
			
			bitpos += part.getBitCount();
		}
		
		// add all static parts to list (parts with 0 bits)
		for (Entry<DeviceTypes,Schedule> e : staticpartsschedule.entrySet()) {
			
			LinkedList<Schedule> currentScheduleList = mapToMerge.get(e.getKey());
			
			if (currentScheduleList == null) {
				currentScheduleList = new LinkedList<>();
				mapToMerge.put(e.getKey(), currentScheduleList);
			}
			
			currentScheduleList.add(e.getValue());
		}
		
		// Merge plans/schedules per DeviceType
		HashMap<DeviceTypes,LoadProfile> planMerged = new HashMap<>();
		
		for ( Entry<DeviceTypes,LinkedList<Schedule>> e : mapToMerge.entrySet() ) {
			LinkedList<Schedule> toMerge = e.getValue();
			DeviceTypes currentType = e.getKey();
			
			if ( toMerge != null ) {
				while (toMerge.size() > 1) {
					ListIterator<Schedule> it = toMerge.listIterator();
					while (it.hasNext()) {
						Schedule s1 = it.next();
						if (!it.hasNext()) break;
						it.remove();
						Schedule s2 = it.next();
						it.remove();
						it.add(s1.merge(s2));
					}		
				}
				
				Schedule merged;
				if (toMerge.size() == 0) {
					// no other profiles to merge, use empty profile
					merged = new Schedule(new SparseLoadProfile(), 0);
				} 
				else {
					// get merged profile
					merged = toMerge.get(0);
				}
				
				// add merged profile to planMerged
				planMerged.put(currentType, merged.getProfile());
			}
		}
		
		// calculate variable fitness depending on price signals...
		fitness = this.fitnessfunction.getFitnessValue(
				ignoreLoadProfileBefore, 
				ignoreLoadProfileAfter,
				planMerged, 
				priceSignals,
				powerLimitSignals
				);
		
		// add luke warm cervisia (additional fixed costs...)
		for ( Entry<DeviceTypes,LinkedList<Schedule>> e : mapToMerge.entrySet() ) {
			LinkedList<Schedule> list = e.getValue();
			for (int i = 0; i < list.size(); i++) {
				Schedule s = list.get(i);
				fitness += s.getLukewarmCervisia();
			}
		}
		
		solution.setObjective(0, fitness); //small value is good value
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//TODO: write tests for this function
	//used for class fitnessFactor
	@SuppressWarnings("unused")
	private int[] schedule2powerarrayfor24h(LoadProfile schedule, int parts) {
		int[] power = new int[parts];
		double partseconds = 86400.0 / parts;
		double currentPower = 0.0;
		double powersum = 0.0;
		long lastChange = 0;
		long nextChange = 0;
		
		for (int pos = 0; pos < parts; pos++) {
			powersum = 0.0;
			powersum += currentPower / partseconds * (Math.min(nextChange, partseconds * (pos + 1)) - partseconds * pos);		

			while (nextChange < partseconds * (pos + 1)) {
				lastChange = nextChange;
				Long tmp = schedule.getNextLoadChange(Commodity.ACTIVEPOWER, nextChange);
				if (tmp == null) {
					if (lastChange >= schedule.getEndingTimeOfProfile()) {
						nextChange += 86400;
						currentPower = 0;
					} else {
						nextChange = schedule.getEndingTimeOfProfile();
						currentPower = schedule.getLoadAt(Commodity.ACTIVEPOWER, nextChange - 1);
					}
				} else {
					nextChange = tmp;
				}
				if (schedule.getEndingTimeOfProfile() <= lastChange) {
					currentPower = 0;
				} else {
					currentPower = schedule.getLoadAt(Commodity.ACTIVEPOWER, nextChange - 1);
				}

				powersum += currentPower / partseconds * (Math.min(nextChange, partseconds * (pos + 1)) - (lastChange));		
			}
			
			power[pos] = (int) powersum;
		}
		
		return power;
	}
		
}