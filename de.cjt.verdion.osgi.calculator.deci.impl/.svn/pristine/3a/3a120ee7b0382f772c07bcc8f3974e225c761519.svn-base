package calculator.deci.impl;


import java.util.ArrayList;
import java.util.List;




import org.osgi.service.component.ComponentContext;

//import calculator.text.IText;
import calculator.calculate.ICalculate;
import calculator.numeralsystem.NumeralSystem;
import calculator.operator.IOperator;

public class DeciImpl implements NumeralSystem {
	
	private ICalculate calc;
	private List<IOperator> operator = new ArrayList<IOperator>();;
	
	private ComponentContext context;
	
	private String[] buttons = {"7", "8", "9", "4", "5", "6", "1", "2", "3", " ", "0", ".", "="};
	private int rows = 3;
	private String name = "Decimal";
	private String backup = "";
	private int priority = 100;
	
	protected void activate(final ComponentContext cxt){
		context = cxt;
	}
	
	
	protected void deactivate(final ComponentContext cxt){
		context = null;
	}
	
	public void bindOperator(IOperator operant) {
		if(operant != null) {
			this.operator.add(operant);
		}
	}
	
	public void unbindOperator(IOperator operant) {
		this.operator.remove(operant);
	}
	
	public void bindCalc(ICalculate calculate) {
		if (calculate != null) {
					this.calc = calculate;
		}
	}
	
	public void unbindCalc(ICalculate calculate) {
		this.calc = null;
	}
	
	public String calculate(String therm) {
		if (therm.endsWith(" ")) {
			return null;
		}
		return calc.calculate(therm);
	}
	
	public String[] getButtons() {
		return buttons;
	}
	
	public int getRows() {
		return rows;
	}
	
	public String getName() {
		return name;
	}
	
	public void setBackup(String backup) {
		this.backup = backup;
	}
	
	public String getBackup() {
		return this.backup;
	}
	
	public int getPriority() {
		return priority;
	}
	
}
