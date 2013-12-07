package sagma.games.rtsServer;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;

public class Report extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField txtReport;
	private JTextField txtAnswer;
	private JMenuBar menuBar;
	private JMenu mnuReport;
	
	public Report(final ArrayList<String> lines) {
		this(compact(lines));
	}
	
	public Report(final String lines) {
		//layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		//setLayout(layout);
		
		txtReport = new JTextField(lines);
		add(txtReport);
		
		txtAnswer = new JTextField();
		add(txtAnswer);
		
		menuBar = new JMenuBar();
		mnuReport = new JMenu("Report");
		mnuReport.setFont(new Font("Arial", Font.BOLD, 12));
		
		menuBar.add(mnuReport);
		add(menuBar);
		
		setVisible(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		//pack();
		
	}
	
	private static String compact(ArrayList<String> lines) {
		String s = "";
		for (String line : lines) {
			s += line + "\n";
		}
		return s;
	}
}
