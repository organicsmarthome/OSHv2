//  ElitistES.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.core.*;

import java.util.Comparator;

import jmetal.util.comparators.*;
import jmetal.util.*;

/** 
 * Class implementing a (mu + lambda) ES. Lambda must be divisible by mu
 */
@SuppressWarnings("rawtypes")
public class ElitistES extends Algorithm {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int     mu_     ;
  private int     lambda_ ;
  private boolean showDebugMessages;
  
 /**
  * Constructor
  * Create a new ElitistES instance.
  * @param problem Problem to solve.
  * @mu Mu
  * @lambda Lambda
  */
  public ElitistES(Problem problem, int mu, int lambda, boolean showDebugMessages){
    super(problem) ;
    mu_      = mu     ;
    lambda_  = lambda ;
    this.showDebugMessages = showDebugMessages;
  } // ElitistES
  
 /**
  * Execute the ElitistES algorithm
 * @throws JMException 
  */
  @Override
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int maxEvaluations ;
    int evaluations    ;

    SolutionSet population          ;
    SolutionSet offspringPopulation ;  

    Operator   mutationOperator ;
    Comparator comparator       ;
    
    comparator = new ObjectiveComparator(0) ; // Single objective comparator
    
    // Read the params
    maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();                
   
    // Initialize the variables
    population          = new SolutionSet(mu_) ;   
    offspringPopulation = new SolutionSet(mu_ + lambda_) ;
    
    evaluations  = 0;                

    // Read the operators
    mutationOperator  = this.operators_.get("mutation");

    //System.out.println("(" + mu_ + " + " + lambda_+")ES") ;
     
    // Create the parent population of mu solutions
    Solution newIndividual;
    for (int i = 0; i < mu_; i++) {
      newIndividual = new Solution(problem_);                    
      problem_.evaluate(newIndividual);            
      evaluations++;
      population.add(newIndividual);
    } //for       
     
    // Main loop
    int offsprings ;
    offsprings = lambda_ / mu_ ; 
    
    double lastFitness = Double.MAX_VALUE;
    
    while (evaluations < maxEvaluations) {
      // STEP 1. Generate the mu+lambda population
      for (int i = 0; i < mu_; i++) {
        for (int j = 0; j < offsprings; j++) {
          Solution offspring = new Solution(population.get(i)) ;
          mutationOperator.execute(offspring);
          problem_.evaluate(offspring) ;
          offspringPopulation.add(offspring) ;
          evaluations ++ ;
        } // for
      } // for
      
      // STEP 2. Add the mu individuals to the offspring population
      for (int i = 0 ; i < mu_; i++) {
        offspringPopulation.add(population.get(i)) ;
      } // for
      population.clear() ;

      // STEP 3. Sort the mu+lambda population
      offspringPopulation.sort(comparator) ;
            
      // STEP 4. Create the new mu population
      for (int i = 0; i < mu_; i++)
        population.add(offspringPopulation.get(i)) ;

      if (showDebugMessages) {
    	  double newFitness = population.get(0).getObjective(0);
    	  if (newFitness < lastFitness) {
    		  lastFitness = newFitness;
    		  System.out.println("Evaluation: " + evaluations + " Better Fitness: " + newFitness ) ;
    	  }
      }
      

      // STEP 6. Delete the mu+lambda population
      offspringPopulation.clear() ;
    } // while
    
    // Return a population with the best individual
    SolutionSet resultPopulation = new SolutionSet(1) ;
    resultPopulation.add(population.get(0)) ;
    
    if (showDebugMessages) {
    	System.out.println("Number of Evaluations: " + evaluations);
    }
    
    return resultPopulation ;
  } // execute
} // ElitistES