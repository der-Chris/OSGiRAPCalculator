package calculator.hex.impl;


import java.util.ArrayList;
import java.util.List;





import org.osgi.service.component.ComponentContext;


//import calculator.text.IText;
import calculator.calculate.ICalculate;
import calculator.numeralsystem.NumeralSystem;
import calculator.operator.IOperator;

public class HexImpl implements NumeralSystem {

	private ICalculate calc;
	private List<IOperator> operator = new ArrayList<IOperator>();;

	private ComponentContext context;

	private String[] buttons = {"F","E","D","C","B","A","7", "8", "9", "4", "5", "6", "1", "2", "3", " ", "0", "="};
	private int rows = 3;
	private String name = "Hex";
	private String backup = "";
	private int priority = 60;

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
		String[] thermeArray = therm.split(" ");
		String thermedeci = "";
		for (int i = 0; i < thermeArray.length; i++) {
			if (thermeArray[i].contains("0") || thermeArray[i].contains("1") ||thermeArray[i].contains("2") ||
					thermeArray[i].contains("3") || thermeArray[i].contains("4") || thermeArray[i].contains("5") ||
					thermeArray[i].contains("6") || thermeArray[i].contains("7") || thermeArray[i].contains("8") ||
					thermeArray[i].contains("9") || thermeArray[i].contains("A") || thermeArray[i].contains("B") ||
					thermeArray[i].contains("C") || thermeArray[i].contains("D") || thermeArray[i].contains("E") ||
					thermeArray[i].contains("F")) {
				String temp = String.valueOf(Integer.parseInt(thermeArray[i], 16));
				thermedeci = thermedeci + temp;
			} else {
				thermedeci = thermedeci +  " " + thermeArray[i] + " ";
			}
		}
		if (thermedeci.endsWith(" ")) {
			return null;
		}
		String ergebnis = String.valueOf(calc.calculate(thermedeci));
		if (!ergebnis.equals("Infinity")) {
			if (ergebnis.contains("E")) {
				return "0";
			}
			int value = Integer.parseInt(ergebnis.substring(0, ergebnis.length() - 2));
			ergebnis = "";
			while (value != 0) {
				if (value%16 == 15) {
					ergebnis += "F";
				} else if (value%16 == 14) {
					ergebnis += "E";
				} else if (value%16 == 13) {
					ergebnis += "D";
				} else if (value%16 == 12) {
					ergebnis += "C";
				} else if (value%16 == 11) {
					ergebnis += "B";
				} else if (value%16 == 10) {
					ergebnis += "A";
				} else {
					ergebnis += value%16;
				}
				value = value/16;
			}
			String ergebnisRichtigrum = "";
			for (int i = ergebnis.length() - 1; i >= 0; i--) {
				ergebnisRichtigrum += String.valueOf(ergebnis.charAt(i));
			}
			return ergebnisRichtigrum;
		} else {
			return ergebnis;
		}

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
