package calculator.calculate.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import calculator.operator.IOperator;
import calculator.calculate.ICalculate;

public class CalculateImpl implements ICalculate{
	private ComponentContext context;
	private List<IOperator> operators = new ArrayList<IOperator>();;

	public void bindOperator(IOperator operator) {
		if (operator != null) {
			this.operators.add(operator);
			if(operators.size() > 1) {
				this.sortOperators();
			}
		} 
	}

	public void unbindOperator(IOperator operator) {
		this.operators.remove(operator);
	}

	public void sortOperators() {
		for (int n = operators.size() ; n > 1; n = n - 1) {
			for (int i = 0; i < operators.size()-1; i++) {
				if (operators.get(i).getPriority() > operators.get(i + 1).getPriority()) {
					IOperator tempOperator = operators.get(i);
					operators.set(i, operators.get(i + 1));
					operators.set(i + 1, tempOperator);
				}
			}

		}
	}
	
	protected void activate(final ComponentContext cxt){
		context = cxt;
	}
	
	protected void deactivate(final ComponentContext cxt){
		context = null;
	}

	public String calculate (String x1) {
		for (IOperator operator : operators) {
			if (x1.contains(operator.getOperatorSymbol())) {
				String x2 = x1.substring(x1.indexOf(operator.getOperatorSymbol()) + 2);
				x1 = x1.substring(0, x1.indexOf(operator.getOperatorSymbol()));
				x1 = String.valueOf(operator.calculate(Double.parseDouble(calculate(x1)), Double.parseDouble(calculate(x2))));
			}
		}
		return x1;
	}
}
