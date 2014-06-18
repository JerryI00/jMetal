//  XInt.java
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

package org.uma.jmetal.util.wrapper;

import org.uma.jmetal.core.Solution;
import org.uma.jmetal.core.SolutionType;
import org.uma.jmetal.encoding.solution.IntSolutionType;
import org.uma.jmetal.util.JMetalException;

/**
 * Wrapper class for accessing integer-coded solutions
 */
public class XInt {
  private Solution solution_;
  private SolutionType type_;

  public XInt(Solution solution) {
    type_ = solution.getType();
    solution_ = solution;
  }

  public XInt(XInt solution) {
    type_ = solution.type_;
    solution_ = solution.solution_;
  }

  public double getValue(int index) {
    return ((IntSolutionType)type_).getIntValue(solution_, index) ;
  }

  public void setValue(int index, int value) {
    ((IntSolutionType)type_).setIntValue(solution_, index, value);
  }

  public int getNumberOfDecisionVariables() {
    return ((IntSolutionType)type_).getNumberOfIntVariables(solution_) ;
  }

  public double getUpperBound(int index) {
    return ((IntSolutionType)type_).getIntUpperBound(solution_,index) ;
  }

  public double getLowerBound(int index) {
    return ((IntSolutionType)type_).getIntLowerBound(solution_,index) ;
  }

  public int size() {
    return getNumberOfDecisionVariables();
  }

  public Solution getSolution() {
    return solution_;
  }

  /*
   * Static methods
   */
  public static double getValue(Solution solution, int index) {
    if (solution.getType() instanceof IntSolutionType) {
      return ((IntSolutionType) solution.getType()).getIntValue(solution, index);
    } else {
      throw new JMetalException(
        "The solution type of the solution is invalid: " + solution.getType());
    }
  }
  
 /*
  public int getValue(int index) throws JMetalException {
    if (type_.getClass() == IntSolution.class) {
      return (int) solution_.getDecisionVariables()[index].getValue();
    } else if (type_.getClass() == ArrayIntSolution.class) {
      return ((ArrayInt) (solution_.getDecisionVariables()[0])).getArray()[index];
    } else {
      Configuration.logger_.severe("org.uma.jmetal.util.wrapper.XInt.getValue, solution type " +
        type_ + "+ invalid");
    }
    return 0;
  }

  public void setValue(int index, int value) throws JMetalException {
    if (type_.getClass() == IntSolution.class) {
      solution_.getDecisionVariables()[index].setValue(value);
    } else if (type_.getClass() == ArrayIntSolution.class) {
      ((ArrayInt) (solution_.getDecisionVariables()[0])).getArray()[index] = value;
    } else {
      Configuration.logger_.severe("org.uma.jmetal.util.wrapper.XInt.setValue, solution type " +
        type_ + "+ invalid");
    }
  }

 
  public int getLowerBound(int index) throws JMetalException {
    if (type_.getClass() == IntSolution.class) {
      return (int) solution_.getDecisionVariables()[index].getLowerBound();
    } else if (type_.getClass() == ArrayIntSolution.class) {
      return (int) ((ArrayInt) (solution_.getDecisionVariables()[0])).getLowerBound(index);
    } else {
      Configuration.logger_.severe("org.uma.jmetal.util.wrapper.XInt.getLowerBound, solution type " +
        type_ + "+ invalid");
    }
    return 0;
  }

  
  public int getUpperBound(int index) throws JMetalException {
    if (type_.getClass() == IntSolution.class) {
      return (int) solution_.getDecisionVariables()[index].getUpperBound();
    } else if (type_.getClass() == ArrayIntSolution.class) {
      return (int) ((ArrayInt) (solution_.getDecisionVariables()[0])).getUpperBound(index);
    } else {
      Configuration.logger_.severe("org.uma.jmetal.util.wrapper.XInt.getUpperBound, solution type " +
        type_ + "+ invalid");
    }

    return 0;
  }

  
  public int getNumberOfDecisionVariables() {
    if (type_.getClass() == IntSolution.class) {
      return solution_.getDecisionVariables().length;
    } else if (type_.getClass() == ArrayIntSolution.class) {
      return ((ArrayInt) (solution_.getDecisionVariables()[0])).length();
    } else {
      Configuration.logger_.severe("org.uma.jmetal.util.wrapper.XInt.size, solution type " +
        type_ + "+ invalid");
    }
    return 0;
  }
  */
}
