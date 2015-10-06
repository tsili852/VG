package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class PlotFrm extends JFrame {

	private static final long serialVersionUID = -2665092327105426049L;
	private JPanel contentPane;
	private ChartPanel pnlPlot;
	private JButton btnCancel;
	private JFreeChart jChart;

	/**
	 * Create the frame.
	 */
	public PlotFrm(JFreeChart chart) {
		setTitle("Plot");
		jChart = chart;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				dispose();
			}
		});
		setBounds(100, 100, 721, 581);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		// BarRenderer rendered = null;
		// CategoryPlot plot = null;
		// rendered = new BarRenderer();
		contentPane.setLayout(null);

		pnlPlot = new ChartPanel(jChart);
		pnlPlot.setBounds(10, 4, 685, 494);

		contentPane.add(pnlPlot);
		pnlPlot.setLayout(new BoxLayout(pnlPlot, BoxLayout.X_AXIS));

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(630, 509, 65, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		contentPane.add(btnCancel);
	}

}
