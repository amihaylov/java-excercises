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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class WorkingDaysGUI extends JApplet {
	private JButton btnCalc = new JButton("Calculate");

	static private JTextField jtaStartDate = new JTextField(20);
	static private JTextField jtaEndDate = new JTextField(20);
	static private JTextField fileName = new JTextField(20);
	static private JTextArea jtaOutput = new JTextArea(40, 20);
	static private JButton open = new JButton("Open");

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
		// open.addActionListener(new OpenL());
		cp.add(open);
		fileName.setEditable(false);
		cp.add(fileName);

		// e is automatically converted to ActionEvent type
		open.addActionListener(e -> {
		      JFileChooser c = new JFileChooser();
		      // Demonstrate "Open" dialog:
		      int rVal = c.showOpenDialog(WorkingDaysGUI.this);
		      if(rVal == JFileChooser.APPROVE_OPTION) {
		        fileName.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());

		      }
		      if(rVal == JFileChooser.CANCEL_OPTION) {
		        fileName.setText("You pressed cancel");
		      }
		});
		
		btnCalc.addActionListener(e -> {
			startDateStr = jtaStartDate.getText();
			endDateStr = jtaEndDate.getText();

			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("dd.MM.yyyy");
			// Could put some try catch for wrong dates
			LocalDate startDate = LocalDate.parse(startDateStr, formatter);
			LocalDate endDate = LocalDate.parse(endDateStr, formatter);
			List<String> strList = new ArrayList<String>();
			file = fileName.getText();
			if (endDate == null || startDate == null)
				JOptionPane.showMessageDialog(null,
						"You must type dates first!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			else {
				if (endDate.isBefore(startDate)) {
					jtaOutput.append("End Date should be after Start Date");
				} else {
					if (file == null)
						JOptionPane
								.showMessageDialog(
										null,
										"You must select a file for working days first!",
										"Error!", JOptionPane.ERROR_MESSAGE);
					else
						strList = WorkingDaysOutput.listIfWorkingRange(
								startDate, endDate, file);
					for (int i = 0; i < strList.size(); i++)
						jtaOutput.append(strList.get(i) + " \n");
				}
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
