package calculator.operator.plus.impl;

import calculator.operator.IOperator;

public class OperatorPlusImpl implements IOperator {

	public String getOperatorSymbol() {
		return " + ";
	}
	
	public double calculate(double number1, double number2) {
		return number1 + number2;
	}
	
	public int getPriority() {
		return 10;
	}
}
