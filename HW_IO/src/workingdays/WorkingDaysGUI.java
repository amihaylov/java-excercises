package workingdays;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class WorkingDaysGUI extends JApplet {
	private JButton btnCalc = new JButton("Calculate");

	static private JTextField jtaStartDate = new JTextField(20);
	static private JTextField jtaEndDate = new JTextField(20);
	static private JTextArea jtaOutput = new JTextArea(40, 20);

	// Add one more text area + scrolling pane for the working dates to be
	// shown, implement in main() as well

	static String startDateStr, endDateStr, file;

	@Override
	public void init() {
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(btnCalc);
		add(new JScrollPane(jtaOutput));
		cp.add(jtaStartDate);
		cp.add(jtaEndDate);
		cp.add(jtaOutput);

		// e is automatically converted to ActionEvent type
		btnCalc.addActionListener(e -> {
			startDateStr = jtaStartDate.getText();
			endDateStr = jtaEndDate.getText();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			//Could put some try catch for wrong dates
			LocalDate startDate = LocalDate.parse(startDateStr, formatter);
			LocalDate endDate = LocalDate.parse(endDateStr, formatter);
			List<String> strList = new ArrayList<String>();

			if (endDate.isBefore(startDate)) {
				jtaOutput.append("End Date should be after Start Date");
			} else {
				strList = WorkingDaysOutput.listIfWorkingRange(startDate,
						endDate);
				for (int i = 0; i < strList.size(); i++)
					jtaOutput.append(strList.get(i) + " \n");
			}
			
		});

	}

	public static void main(String[] args) throws InvocationTargetException,
			InterruptedException {
		JFrame main = new JFrame("Working Days applet");

		WorkingDaysGUI applet = new WorkingDaysGUI();
		SwingUtilities.invokeAndWait(() -> {
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			main.setSize(800, 600);
			main.setLocationRelativeTo(null);
			main.getContentPane().add(applet);
			applet.init();
			applet.start();
			main.setVisible(true);

		});

	}

}
