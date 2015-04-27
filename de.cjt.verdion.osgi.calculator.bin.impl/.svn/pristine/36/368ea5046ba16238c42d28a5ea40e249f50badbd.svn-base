package calculator.bin.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import calculator.calculate.ICalculate;
import calculator.numeralsystem.NumeralSystem;
import calculator.operator.IOperator;

public class BinImpl implements NumeralSystem {

	private ICalculate calc;
	private List<IOperator> operator = new ArrayList<IOperator>();
	
	private ComponentContext context;
	
	private String[] buttons = {"0", "1", "="};
	private int rows = 2;
	private String name = "Binary";
	private String backup = "";
	private int priority = 80;
	
	
	protected void activate(final ComponentContext cxt){
		context = cxt;
	}


	protected void deactivate(final ComponentContext cxt){
		context = null;
	}
	
	public void bindOperator(IOperator operant) {
		if(operant == null) {
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
	
	public String calculate(String therme) {
		String[] thermeArray = therme.split(" ");
		String thermedeci = "";
		for (int i = 0; i < thermeArray.length; i++) {
			if (thermeArray[i].contains("0") || thermeArray[i].contains("1")) {
				String temp = String.valueOf(Integer.parseInt(thermeArray[i], 2));
				thermedeci = thermedeci + temp;
			} else {
				thermedeci = thermedeci +  " " + thermeArray[i] + " ";
			}
		}
		if (thermedeci.endsWith(" ")) {
			return null;
		}
		double value = Double.parseDouble(calc.calculate(thermedeci));
		return (Integer.toBinaryString(((int) value)));
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
