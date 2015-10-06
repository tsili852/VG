package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import javax.swing.Box;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class PlotFrm extends JFrame {

	private JPanel contentPane;
	private ChartPanel pnlPlot;
	private JButton btnCancel;
	private JFreeChart jChart;


	/**
	 * Create the frame.
	 */
	public PlotFrm(JFreeChart chart) {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 562, 507);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		BarRenderer rendered = null;
		CategoryPlot plot = null;
		rendered = new BarRenderer();
		contentPane.setLayout(null);

		pnlPlot = new ChartPanel(jChart);
		pnlPlot.setBounds(10, 4, 526, 420);
		
		contentPane.add(pnlPlot);
		pnlPlot.setLayout(new BoxLayout(pnlPlot, BoxLayout.X_AXIS));
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(240, 435, 65, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		contentPane.add(btnCancel);
	}
}
