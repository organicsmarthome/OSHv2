package jmetal.operators.mutation;

import java.util.HashMap;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import osh.core.OSHRandomGenerator;
@SuppressWarnings("rawtypes")
public class MutationFactory {
	
	/**
	   * Gets a crossover operator through its name.
	   * @param name of the operator
	   * @return the operator
	   * @throws JMException 
	   */
	@SuppressWarnings("unchecked")
	public static Mutation getMutationOperator(
			String name, 
			HashMap parameters,
			OSHRandomGenerator randomGenerator) throws JMException{
		
		PseudoRandom pseudoRandom = new PseudoRandom(randomGenerator);
		
		//TODO: Reflections
		if (name.equalsIgnoreCase("BitFlipMutation")) {
			return new BitFlipMutation(parameters, pseudoRandom);	
		}
		else {
			Configuration.logger_.severe("Operator '" + name + "' not found ");
			Class cls = java.lang.String.class;
			String name2 = cls.getName() ;    
			throw new JMException("Exception in " + name2 + ".getMutationOperator()") ;
		}        
	} // getMutationOperator
	
	
}
