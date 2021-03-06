package de.cjt.verdion.osgi.calculator.gui.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import calculator.numeralsystem.NumeralSystem;
import calculator.operator.IOperator;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


public class BasicEntryPoint extends AbstractEntryPoint {

	private Text lower_textfield;
	TabFolder tabFolder;

	private Composite operatorArea;
	private boolean ergebnis = false;
	private List<IOperator> operator = new ArrayList<IOperator>();
	private List<Button> operatorButton = new ArrayList<Button>();
	Composite buttonArea;
	private List<Button> numeralSystemsButton = new ArrayList<Button>();
	private List<Composite> numeralSystemsComposite = new ArrayList<Composite>();
	private List<Composite> mainNumeralSystemsComposite = new ArrayList<Composite>();
	private List<TabItem> numeralSystemsTabs = new ArrayList<TabItem>();
	private List<NumeralSystem> numeralSystems = new ArrayList<NumeralSystem>();	
	BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
	BundleContext context2 = FrameworkUtil.getBundle(getClass()).getBundleContext();
	private ServiceTracker serviceTrackerOperator;
	private ServiceTracker serviceTrackerNumeralSystem;
	private Composite parent;
	private int counter = 0;
	private boolean started = false;

	private String symbols = ("0123456789ABCDEFGHIJKLMNUPQRSTUVWXYZ");




	protected void createContents(final Composite parent) {
		this.parent = parent;
		final ServerPushSession pushSession = new ServerPushSession();
		pushSession.start();

		parent.setLayout(new GridLayout(1,false));

		Composite textArea = new Composite(parent, SWT.NONE);

		lower_textfield = new Text(textArea, SWT.BORDER);
		lower_textfield.setEditable(false);
		lower_textfield.setTouchEnabled(true);
		lower_textfield.setBounds(10, 0, 318, 60);

		buttonArea = new Composite(parent, SWT.NONE);
		buttonArea.setLayout(new GridLayout(2, true));
		tabFolder = new TabFolder(buttonArea, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i = tabFolder.getSelectionIndex();
				lower_textfield.setText(numeralSystems.get(i).getBackup());
				ergebnis = false;
			}
		});
		operatorArea = new Composite(buttonArea, SWT.NONE);
		operatorArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		operatorArea.setLayout(new GridLayout());

		Button deleteButton = new Button(operatorArea, SWT.NONE);
		deleteButton.setLayoutData(new GridData(GridData.FILL_BOTH));
		deleteButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lower_textfield.setText("");
				//				lower_textfield.setText(String.valueOf(operatorButton.size() == counter));
				//				stopNumeralSystem();

			}
		});
		deleteButton.setText("delete");

		Button revertButton = new Button(operatorArea, SWT.NONE);
		revertButton.setLayoutData(new GridData(GridData.FILL_BOTH));
		revertButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (lower_textfield.getText().endsWith(" ")) {
					lower_textfield.setText(lower_textfield.getText().substring(0, lower_textfield.getText().length() - 3));
				} else if (lower_textfield.getText().length() < 0){
					lower_textfield.setText(lower_textfield.getText().substring(0, lower_textfield.getText().length() - 1));
				}
			}
		});
		revertButton.setText("<--");

		startOperator();
		startNumeralSystem();
	}

	public void bindNumeralSystems(final NumeralSystem numeralSystem) {
		if (numeralSystem != null && !(numeralSystems.contains(numeralSystem))) {
			updateContent(new Runnable() {
				@Override
				public void run() {					
					numeralSystems.add(numeralSystem);
					//General Composite for the Tab and the NumberArea
					Composite mainTempComp = new Composite(tabFolder, SWT.NONE);
					mainNumeralSystemsComposite.add(mainTempComp);		
					RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
					rowLayout.fill = true;
					mainTempComp.setLayout(rowLayout);
					//The New Tab for the Composite
					TabItem tempTab = new TabItem(tabFolder, SWT.NONE);
					numeralSystemsTabs.add(tempTab);
					tempTab.setText(numeralSystem.getName());
					tempTab.setControl(mainTempComp);
					//Composite NumberArea for the Numbers
					Composite tempComp = new Composite(mainTempComp, SWT.NONE);
					numeralSystemsComposite.add(tempComp);
					tempComp.setLayout(new GridLayout(3, true));
					//create and add NumberButtons
					String tempButtons[] = numeralSystem.getButtons();
					for (int i = 0; i < tempButtons.length - 1 ; i ++) {
						numeralSystemsButton.add(createNumberButtons(tempButtons[i], tempComp, numeralSystem));
						if (numeralSystemsButton.get(numeralSystemsButton.size() - 1).getText().equals(" ")) {
							numeralSystemsButton.get(numeralSystemsButton.size() - 1).setVisible(false);
						}
					}
					//Composite EqualsArea for the Equalssign
					Composite tempComp2 = new Composite(mainTempComp, SWT.NONE);
					numeralSystemsComposite.add(tempComp2);
					tempComp2.setLayout(new GridLayout(1, true));
					//create and add EqualsSign
					numeralSystemsButton.add(createEqualsButtons(tempComp2, numeralSystem));		
					int max = 0;
					if (numeralSystems.size() > 1 && !started) {
						for ( int i = 1;  i < numeralSystems.size();  i = i + 1){
							if (numeralSystems.get(max).getPriority() < numeralSystems.get(i).getPriority()){
									max = i;
							
							}
						}
						tabFolder.setSelection(max);
					}
					buttonArea.layout();

				}
			});
		} 
	}

	public void unbindNumeralSystems(final NumeralSystem numeralSystem) {
		if (numeralSystem != null) {
			updateContent(new Runnable() {
				@Override
				public void run() {					
					final int x = numeralSystems.indexOf(numeralSystem);
					if (x > -1) {
						numeralSystemsTabs.remove(tabFolder.getItem(x));
						tabFolder.getItem(x).dispose();
						mainNumeralSystemsComposite.get(x).dispose();
						mainNumeralSystemsComposite.remove(x);
						numeralSystems.remove(numeralSystem);
					}
					buttonArea.layout();
				}
			});
		}
	}

	public void bindOperator(final IOperator oper) {
		if (oper != null && !(operator.contains(oper))) {
			updateContent(new Runnable() {
				@Override
				public void run() {					
					operator.add(oper);			
					counter++;
					operatorButton.add(createOperatorButtons(oper));
					operatorArea.layout();
				}
			});				
		}
	}

	public void unbindOperator(final IOperator oper) {
		if (oper != null) {	
			updateContent(new Runnable() {
				@Override
				public void run() {					
					int x = operator.indexOf(oper);
					if (x > -1) {
						operatorButton.get(x).dispose();
						operatorButton.remove(operatorButton.get(x));
						operator.remove(oper);
					}
					operatorArea.layout();
				}
			});					
		}
	}

	public Button createNumberButtons(final String name, Composite composite, final NumeralSystem numeralSystem) {
		Button buttonName = new Button(composite, SWT.NONE);
		buttonName.setLayoutData(new GridData(GridData.FILL_BOTH));
		buttonName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!ergebnis && !(lower_textfield.getText().equals("0"))) {
					lower_textfield.setText(lower_textfield.getText() + name);
				} else {
					lower_textfield.setText(name);
					ergebnis = false;
				}
				numeralSystem.setBackup(lower_textfield.getText());
				started = true;
			}
		});
		buttonName.setText(name);
		return buttonName;
	}

	public Button createOperatorButtons(final IOperator oper) {
		Button buttonName = new Button(operatorArea, SWT.NONE);
		buttonName.setLayoutData(new GridData(GridData.FILL_BOTH));
		buttonName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				printOperatorSign(oper.getOperatorSymbol());
				ergebnis = false;
			}
		});
		buttonName.setText(oper.getOperatorSymbol());
		return buttonName;
	}

	public Button createEqualsButtons(Composite composite, final NumeralSystem numeralSystem) {
		Button buttonName = new Button(composite, SWT.NONE);
		buttonName.setLayoutData(new GridData(GridData.FILL_BOTH));
		buttonName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String value = lower_textfield.getText();
				if (!(value.endsWith(" "))) {
					value = numeralSystem.calculate(value);
					if (value.endsWith(".0")) {
						value = value.substring(0, value.length() - 2);
					}
					lower_textfield.setText(value);
					numeralSystem.setBackup(value);
					ergebnis = true;
				}
			}
		});
		buttonName.setText("=");
		return buttonName;
	}

	public void printOperatorSign(String rechenzeichen) {
		String textfeld = lower_textfield.getText();
		//Wurde nur 1 Zeichen eingegeben und es ist kein minusvorzeichen, kein Punkt oder Hochzeichen, so ist das Rechenzeichen zu setzten
		if (textfeld.length() == 0 && rechenzeichen.equals(" - ")) {
			lower_textfield.setText(rechenzeichen);
		} else if(textfeld.length() == 1  && textfeld.charAt(0) != '-' && textfeld.charAt(0) != '.' && textfeld.charAt(0) != '^') { 
			lower_textfield.setText(textfeld + rechenzeichen);
			//Ist es 2 zeichen lang, und es ist kein Hochzeichen, so darf ein Rechenzeichen folgen
		} else if (textfeld.length() == 2 && textfeld.charAt(1) != '^') {
			lower_textfield.setText(textfeld + rechenzeichen);
		} else if (textfeld.length() > 2) {
			//Wenn es schon ein Rechenzeichen gibt und darauf keine Zahl folgt, so kann man das alte gegen das neue Rechenzeichen ersetzten
			if(isOperant(textfeld.charAt(textfeld.length() - 2)) && !(symbols.contains(String.valueOf(textfeld.charAt(textfeld.length()-1))))) {
				if (textfeld.charAt(textfeld.length() - 2) == '-' && rechenzeichen.equals(" - ")) {
					lower_textfield.setText(textfeld + "-");
				} else {
					lower_textfield.setText(textfeld.substring(0, textfeld.length() - 3) + rechenzeichen);
				}
				//wenn es mit einem Minus endet und davor ein Leerzeichen ist, so muss es ein Vorzeichen sein und das Rechenzeichen davor wird ersetzt
			} else if (textfeld.endsWith(" -")) {
				if (textfeld.length() > 4 && isOperant(textfeld.substring(textfeld.length() - 3, textfeld.length() - 2).charAt(0))) {
					lower_textfield.setText(textfeld.substring(0, textfeld.length() - 4) + rechenzeichen + "-");
				}
				//wenn das letzte Zeichen kein Punkt ist, so kann man es einfach setzten
			} else if (textfeld.charAt(textfeld.length()-1) != '.') {
				lower_textfield.setText(textfeld + rechenzeichen);
			}
		}
		int i = tabFolder.getSelectionIndex();
		numeralSystems.get(i).setBackup(lower_textfield.getText());
	}

	public boolean isOperant(char operant) {
		boolean flag = false;
		int x = operator.size();
		for (int i = 0; i < x; i++) {
			char check = operator.get(i).getOperatorSymbol().charAt(1);
			if (check == operant) {
				flag = true;
			}
		}
		return flag;
	}




	public void startOperator()
	{
		serviceTrackerOperator = new ServiceTracker(context, IOperator.class,
				new ServiceTrackerCustomizer()
		{
			@Override
			public Object addingService( final ServiceReference reference )
			{
				final IOperator operator = (IOperator) context.getService(reference);
				if( operator != null )
					bindOperator(operator);
				return operator;
			}

			@Override
			public void modifiedService( final ServiceReference reference, final Object service) {/*ok*/}
			@Override
			public void removedService(  final ServiceReference reference, final Object service) {
				final IOperator operator = (IOperator) context.getService( reference );
				if( operator != null ) {
					unbindOperator(operator);
				}
			}
		});
		Object[] services = serviceTrackerOperator.getServices();
		if (services != null) {
			for (int i = 0; i < services.length; i++) {
				bindOperator((IOperator) services[i]);
			}
		}
		serviceTrackerOperator.open();
	}

	public void stopOperator() {
		if(serviceTrackerOperator != null)
			serviceTrackerOperator.close();
	}

	public void startNumeralSystem() {
		serviceTrackerNumeralSystem = new ServiceTracker(context, NumeralSystem.class,
				new ServiceTrackerCustomizer() {

			public Object addingService(final ServiceReference reference) {
				final NumeralSystem numeralSystem = (NumeralSystem) context.getService(reference);
				if(numeralSystem != null)
					bindNumeralSystems(numeralSystem);
				return numeralSystem;
			}

			public void modifiedService(final ServiceReference reference, final Object service ) {/*ok*/}

			public void removedService(final ServiceReference reference, final Object service) {
				final NumeralSystem numeralSystem = (NumeralSystem) context.getService(reference);
				if(numeralSystem != null) {
					unbindNumeralSystems(numeralSystem);
				}
			}
		});
		Object[] services = serviceTrackerNumeralSystem.getServices();
		if (services != null) {
			for (int i = 0; i < services.length; i++) {
				bindNumeralSystems((NumeralSystem) services[i]);
			}
		}		
		serviceTrackerNumeralSystem.open();

	}

	public void stopNumeralSystem() {
		if(serviceTrackerNumeralSystem != null)
			serviceTrackerNumeralSystem.close();
	}

	public void updateContent(final Runnable runnable) {
		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {
				parent.getDisplay().asyncExec(runnable);
			};
		};
		Thread bgThread = new Thread(bgRunnable);
		bgThread.setDaemon(true);
		bgThread.start();
	}


}
