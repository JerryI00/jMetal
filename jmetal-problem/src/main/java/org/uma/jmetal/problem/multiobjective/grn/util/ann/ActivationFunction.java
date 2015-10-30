package org.uma.jmetal.problem.multiobjective.grn.util.ann;

public abstract class ActivationFunction {

	public abstract double apply(double parameter);
	
	public abstract double applyFirstDerivative(double parameter);
}
