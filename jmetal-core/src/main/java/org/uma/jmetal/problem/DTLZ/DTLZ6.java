//  DTLZ6.java
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

package org.uma.jmetal.problem.DTLZ;

import org.uma.jmetal.core.Problem;
import org.uma.jmetal.core.Solution;
import org.uma.jmetal.core.Variable;
import org.uma.jmetal.encoding.solutiontype.BinaryRealSolutionType;
import org.uma.jmetal.encoding.solutiontype.RealSolutionType;
import org.uma.jmetal.util.JMetalException;

/**
 * Class representing problem DTLZ6
 */
public class DTLZ6 extends Problem {

  /**
   *
   */
  private static final long serialVersionUID = -6129675039815081872L;

  /**
   * Creates a default DTLZ6 problem instance (12 variables and 3 objectives)
   *
   * @param solutionType The solutiontype type must "Real" or "BinaryReal".
   */
  public DTLZ6(String solutionType) throws ClassNotFoundException, JMetalException {
    this(solutionType, 12, 3);
  } // DTLZ6

  /**
   * Creates a new DTLZ6 problem instance
   *
   * @param numberOfVariables  Number of variables
   * @param numberOfObjectives Number of objective functions
   * @param solutionType       The solutiontype type must "Real" or "BinaryReal".
   */
  public DTLZ6(String solutionType,
    Integer numberOfVariables,
    Integer numberOfObjectives) throws JMetalException {
    numberOfVariables_ = numberOfVariables;
    numberOfObjectives_ = numberOfObjectives;
    numberOfConstraints_ = 0;
    problemName_ = "DTLZ6";

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++) {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } //for

    if (solutionType.compareTo("BinaryReal") == 0) {
      solutionType_ = new BinaryRealSolutionType(this);
    } else if (solutionType.compareTo("Real") == 0) {
      solutionType_ = new RealSolutionType(this);
    } else {
      throw new JMetalException("Error: solutiontype type " + solutionType + " invalid");
    }
  }

  /**
   * Evaluates a solutiontype
   *
   * @param solution The solutiontype to evaluate
   * @throws org.uma.jmetal.util.JMetalException
   */
  public void evaluate(Solution solution) throws JMetalException {
    Variable[] gen = solution.getDecisionVariables();

    double[] x = new double[numberOfVariables_];
    double[] f = new double[numberOfObjectives_];
    double[] theta = new double[numberOfObjectives_ - 1];
    int k = numberOfVariables_ - numberOfObjectives_ + 1;

    for (int i = 0; i < numberOfVariables_; i++) {
      x[i] = gen[i].getValue();
    }

    double g = 0.0;
    for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++) {
      g += java.lang.Math.pow(x[i], 0.1);
    }

    double t = java.lang.Math.PI / (4.0 * (1.0 + g));
    theta[0] = x[0] * java.lang.Math.PI / 2;
    for (int i = 1; i < (numberOfObjectives_ - 1); i++) {
      theta[i] = t * (1.0 + 2.0 * g * x[i]);
    }

    for (int i = 0; i < numberOfObjectives_; i++) {
      f[i] = 1.0 + g;
    }

    for (int i = 0; i < numberOfObjectives_; i++) {
      for (int j = 0; j < numberOfObjectives_ - (i + 1); j++) {
        f[i] *= java.lang.Math.cos(theta[j]);
      }
      if (i != 0) {
        int aux = numberOfObjectives_ - (i + 1);
        f[i] *= java.lang.Math.sin(theta[aux]);
      }
    }

    for (int i = 0; i < numberOfObjectives_; i++) {
      solution.setObjective(i, f[i]);
    }
  }
}
