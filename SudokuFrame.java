import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame implements ActionListener{
	JPanel panel;					//declare all the components you will be using
	JTextArea textPuzzle;	
	JTextArea textSolution;
	JButton checker;
	JLabel soln;
	JLabel numSoln;
	JLabel time;
	JLabel ms;
	JCheckBox automode;	
	
	public SudokuFrame() {   //constructor for the Frame
		super("Sudoku Solver");   //title
	
		// Could do this:
		// setLocationByPlatform(true);
		panel = new JPanel();
		textPuzzle = new JTextArea(15,20);	
		textSolution = new JTextArea(15,20);		
		checker = new JButton("Check");	
		checker.addActionListener(this);
		
		textPuzzle.setBorder(new TitledBorder("Puzzle"));
		textSolution.setBorder(new TitledBorder("Solution"));
				
		this.setLayout(new BorderLayout(4,4));
		this.add(panel, BorderLayout.SOUTH);
		
		this.add(textPuzzle, BorderLayout.CENTER);
		this.add(textSolution, BorderLayout.EAST);
		
		//set up the panel with the check button and the autocheck option
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(checker);
		automode = new JCheckBox("Auto Check"); 
		automode.setSelected(true);
		panel.add(automode);
		
		Document textbody = textPuzzle.getDocument();  //the Document from the textArea supports DocumentListener
		textbody.addDocumentListener(
				new DocumentListener() {
						public void changedUpdate(DocumentEvent e) {  //any time a change is detected, run the solver
							
							if(automode.isSelected()){				//make sure auto check box is checked though
								String puzzle = textPuzzle.getText();
								try{
									int[][] grid = Sudoku.textToGrid(puzzle);
									Sudoku sudo = new Sudoku(grid);
									int count = sudo.solve();
									String output = sudo.getSolutionText();
									textSolution.setText(output);
									textSolution.append("solutions: " + count + '\n');
									textSolution.append("elapsed: " + sudo.getElapsed() + "ms");
	
								}
								catch(RuntimeException error){
									textSolution.setText("Parsing Error! " + error.getMessage());
								}
							}
							
						}
						public void removeUpdate(DocumentEvent e) {  //do same things as changedUpdate
							changedUpdate(e);
						}
						public void insertUpdate(DocumentEvent e) {  //do same things as changedUpdate
							changedUpdate(e);
						}	
				}
		
		);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	/*
	 * actionPerformed:
	 * Part of the actionListener interface that we are supporting. 
	 * Upon press of the Check button, retrieves the text in the PUZZLE 
	 * textbox and sends it to the Sudoku class
	 * for processing and solving. Outputs the result to the SOLUTION text Area
	 */
	public void actionPerformed(ActionEvent buttonpress){
		String puzzle = textPuzzle.getText();
		try{
			int[][] grid = Sudoku.textToGrid(puzzle);
			Sudoku sudo = new Sudoku(grid);
			int count = sudo.solve();
			String output = sudo.getSolutionText();
			textSolution.setText(output);
			textSolution.append("solutions: " + count + '\n');
			textSolution.append("elapsed: " + sudo.getElapsed() + "ms");

		}
		catch(RuntimeException e){
			textSolution.setText("Parsing Error! " + e.getMessage());
		}
		
		
	 }
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();

	}

}
