//  BinaryReal.java
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

package jmetal.encodings.variable;

import jmetal.core.Variable;
import jmetal.util.PseudoRandom;

/** This class extends the Binary class to represent a Real variable encoded by
 * a binary string
 */
public class BinaryReal extends Binary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
   * Defines the default number of bits used for binary coded variables.
   */	
	public static final int DEFAULT_PRECISION = 30; 
	
  /**
   * Stores the real value of the variable
   */
  private double value_;
  
  /**
   * Stores the lower limit for the variable
   */
  private double lowerBound_;
  
  /**
   * Stores the upper limit for the variable
   */
  private double upperBound_;  
     
  /**
   * Constructor.
   */
  public BinaryReal() {    
    super();
  } //BinaryReal

  /**
   * Constructor
   * @param numberOfBits Length of the binary string.
   * @param lowerBound The lower limit for the variable
   * @param upperBound The upper limit for the variable.
   */
  public BinaryReal(
		  int numberOfBits, 
		  double lowerBound, 
		  double upperBound,
		  PseudoRandom pseudoRandom){
    super(numberOfBits, pseudoRandom);
    lowerBound_     = lowerBound;
    upperBound_     = upperBound;
    
    decode();                              
  } //BinaryReal
    
  /**
   * Copy constructor
   * @param variable The variable to copy
   */
  public BinaryReal(BinaryReal variable){
    super(variable);

    lowerBound_   = variable.lowerBound_;
    upperBound_   = variable.upperBound_;
    /*
    numberOfBits_ = variable.numberOfBits_;
     
    bits_ = new BitSet(numberOfBits_);
    for (int i = 0; i < numberOfBits_; i++)
      bits_.set(i,variable.bits_.get(i));
    */  
    value_ = variable.value_;
  } //BinaryReal
    
  
  /**
   * Decodes the real value encoded in the binary string represented
   * by the <code>BinaryReal</code> object. The decoded value is stores in the 
   * <code>value_</code> field and can be accessed by the method
   * <code>getValue</code>.
   */
  @Override
  public void decode(){
    double value = 0.0;        
    for (int i = 0; i < numberOfBits_; i++) {
      if (bits_.get(i)) {
        value += Math.pow(2.0,i);
      }
    }
        
    value_ = value * (upperBound_ - lowerBound_) /
                                              (Math.pow(2.0,numberOfBits_)-1.0);        
    value_ += lowerBound_;    
  } //decode
 
  /**
   * Returns the double value of the variable.
   * @return the double value.
   */
  @Override
  public double getValue() {
    return value_;
  } //getValue

  /**
   * Creates an exact copy of a <code>BinaryReal</code> object.
   * @return The copy of the object
   */
  @Override
  public Variable deepCopy() {
    return new BinaryReal(this);
  } //deepCopy

  /**
   * Returns the lower bound of the variable.
   * @return the lower bound.
   */
  @Override
  public double getLowerBound() {
    return lowerBound_;
  } // getLowerBound

  /**
   * Returns the upper bound of the variable.
   * @return the upper bound.
   */
  @Override
  public double getUpperBound() {
    return upperBound_;
  } // getUpperBound

  /**
   * Sets the lower bound of the variable.
   * @param lowerBound the lower bound.
   */
  @Override
  public void setLowerBound(double lowerBound) {
    lowerBound_ = lowerBound;
  } // setLowerBound

  /**
   * Sets the upper bound of the variable.
   * @param upperBound the upper bound.
   */
  @Override
  public void setUpperBound(double upperBound) {
    upperBound_ = upperBound;
  } // setUpperBound
  
  /**
   * Returns a string representing the object.
   * @return the string.
   */
  @Override
  public String toString() {
    return value_+"";
  } // toString
} // BinaryReal
