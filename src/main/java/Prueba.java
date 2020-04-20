import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Prueba {

	private int textPaneWidth = 500;
	private int textPaneHeigth = 200;
	private int scrollPaneWidth = 100;
	private int scrollPaneHeigth = 100;
	private JTextPane textPane;
	private JButton button;
	private JFrame frame;
	private JScrollPane scrollPane;

	public static void main(String[] args) {

		Prueba gui = new Prueba();
		gui.go();

	}

	private void go() {

		frame = new JFrame("Test");
		button = new JButton("button");
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		button.addActionListener(new ButtonListener());
		textPane.setFont(new Font("Courier", Font.PLAIN, 12));

		// Sizes:
//        textPane.setSize(textPaneWidth, textPaneHeigth);                             // ???
//        textPane.setPreferredSize(new Dimension(textPaneWidth, textPaneHeigth));     // ???
		scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeigth));

		frame.add(button);
		frame.add(scrollPane, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			textPane.setText("==================================================================== \n"
					+ "==================================================================== \n"
					+ "==================================================================== \n"
					+ "==================================================================== \n"
					+ "==================================================================== \n");
		}

	}
}