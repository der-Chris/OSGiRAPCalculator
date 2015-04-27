package calculator.operator.times.impl;

import calculator.operator.IOperator;

public class OperatorTimesImpl implements IOperator {

	public String getOperatorSymbol() {
		return " * ";
	}
	
	public double calculate(double number1, double number2) {
		return number1 * number2;
	}
	
	public int getPriority() {
		return 30;
	}
}
