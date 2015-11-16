package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.jdbc.JDBCXYDataset;
import org.jfree.ui.ApplicationFrame;

import utilities.SqlConnector;

public class ComparisonFrm extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7947269096887191557L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnShowGraph;
	private JButton cancelButton;
	private JComboBox<String> cmbBlades;
	private JComboBox<String> cmbCompType;
	private JTable table;
	private JButton btnRemove;
	private JButton btnAdd;
	private JComboBox<String> cmbVGs;
	private int tableRows = 0;

	/**
	 * Create the dialog.
	 */
	public ComparisonFrm() {
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

		SqlConnector connector = new SqlConnector("VG_db");

		setTitle("Comparison Graph");
		setBounds(100, 100, 464, 249);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblComparisonType = new JLabel("Comparison Type");
		lblComparisonType.setBounds(10, 11, 104, 14);
		lblComparisonType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPanel.add(lblComparisonType);

		cmbCompType = new JComboBox<String>();
		cmbCompType.setBounds(124, 9, 142, 21);
		cmbCompType.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Force / Time", "Temperature / Time", "Humidity / Time" }));
		contentPanel.add(cmbCompType);

		JLabel lblBladeId = new JLabel("Blade");
		lblBladeId.setBounds(10, 43, 104, 14);
		lblBladeId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPanel.add(lblBladeId);

		cmbBlades = new JComboBox<String>();

		cmbBlades.setBounds(124, 41, 142, 21);

		String sqlStatement2 = "Select distinct Blade from Measurements";
		ResultSet rsBlades = null;
		try {
			connector.connectToDatabase();
			rsBlades = connector.executeResultSetQuery(sqlStatement2);
			while (rsBlades.next()) {
				cmbBlades.addItem(rsBlades.getString(1));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (connector.isConnectionOpen()) {
					connector.closeConnection();
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		contentPanel.add(cmbBlades);

		JLabel lblVgId = new JLabel("VG");
		lblVgId.setBounds(10, 75, 104, 14);
		lblVgId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPanel.add(lblVgId);

		cmbVGs = new JComboBox<String>();
		cmbVGs.setBounds(124, 73, 142, 21);
		
		String sqlStatement = "Select distinct VGID from Measurements Where Blade = "
				+ cmbBlades.getItemAt(0);
		ResultSet rsVgs = null;
		try {
			connector.connectToDatabase();
			rsVgs = connector.executeResultSetQuery(sqlStatement);
			while (rsVgs.next()) {
				cmbVGs.addItem(rsVgs.getString(1));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(rootPane,
					"Could not connect to database Error code: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (connector.isConnectionOpen()) {
					connector.closeConnection();
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(rootPane,
						"Could not connect to database Error code: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		cmbBlades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ArrayList<String> vgList = new ArrayList<String>();
				int counter = 0;
//				cmbVGs.removeAll();
				String sqlStatement2 = "Select distinct VGID from Measurements Where Blade = "
						+ cmbBlades.getSelectedItem();
				ResultSet rsVgs = null;
				try {
					connector.connectToDatabase();
					rsVgs = connector.executeResultSetQuery(sqlStatement2);
					while (rsVgs.next()) {
//						cmbVGs.addItem(rsVgs.getString(1));
						vgList.add(rsVgs.getString(1));
					}
					String[] VGs = new String[vgList.size()];
					VGs = vgList.toArray(VGs);
					DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(VGs);
					cmbVGs.setModel(model);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(rootPane,
							"Could not connect to database Error code: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					try {
						if (connector.isConnectionOpen()) {
							connector.closeConnection();
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(rootPane,
								"Could not connect to database Error code: " + e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		contentPanel.add(cmbVGs);

		btnAdd = new JButton("Add");

		btnAdd.setBounds(282, 134, 71, 23);
		contentPanel.add(btnAdd);

		btnRemove = new JButton("Remove");

		btnRemove.setBounds(367, 134, 71, 23);
		contentPanel.add(btnRemove);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(282, 11, 156, 112);
		contentPanel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setName("");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Blade", "VG" }) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5580946013648059467L;
			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);
		table.getColumnModel().getColumn(1).setResizable(false);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnShowGraph = new JButton("Show Graph");
		btnShowGraph.addActionListener(new ActionListener() {
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			public void actionPerformed(ActionEvent e) {
				if (model.getRowCount() < 2) {
					JOptionPane.showMessageDialog(rootPane, "Select more than one VGs for comparison ", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int nRow = model.getRowCount(), nCol = model.getColumnCount();
					tableRows = nRow;
					Object[][] tableData = new Object[nRow][nCol];
					for (int i = 0; i < nRow; i++)
						for (int j = 0; j < nCol; j++)
							tableData[i][j] = model.getValueAt(i, j);

					String query = "";
					String axisTitles = "";
					String xTitle = "";
					String yTitle = "";
					String sqlBladeID = (String) tableData[0][0];
					String sqlVGID = (String) tableData[0][1];

					if (cmbCompType.getSelectedIndex() == 0) {
						// query = "Select substr(DateTime, 11 ,9) as Time,
						// Force from Measurements Where Blade = ";
						query = "Select (select count(*) from Measurements b  where a.DateTime >= b.DateTime and Blade = "
								+ sqlBladeID + " and VGID = " + sqlVGID + "),"
								+ "Force from Measurements a Where Blade = ";

						axisTitles = "Force / Time";
						xTitle = "Time";
						yTitle = "Force";
					}
					try {
						if (connector.isConnectionOpen()) {
							connector.closeConnection();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					formGraph(tableData, axisTitles, xTitle, yTitle, query);
					dispose();
				}
				
			}
		});
		btnShowGraph.setActionCommand("OK");
		buttonPane.add(btnShowGraph);
		getRootPane().setDefaultButton(btnShowGraph);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbVGs.getSelectedIndex() >= 0) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					Vector<Object> row = new Vector<>();
					row.add(cmbBlades.getSelectedItem());
					row.add(cmbVGs.getSelectedItem());
					model.addRow(row);
				}
			}
		});

		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int[] rows = table.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					model.removeRow(rows[i] - i);
				}

			}
		});

		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	private void formGraph(Object[][] tableData, String axisTitles, String xTitle, String yTitle, String query) {
		XYPlot plot;
		int datasetIndex = 0;
		@SuppressWarnings("resource")
		SqlConnector connector = new SqlConnector("VG_db");
		try {
			connector.connectToDatabase();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String updatedQuery = query + tableData[0][0] + " and VGID = " + tableData[0][1];

		JDBCXYDataset dataset = null;
		try {
			dataset = new JDBCXYDataset(connector.getConnection(), updatedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JFreeChart jChart = ChartFactory.createTimeSeriesChart(axisTitles, xTitle, yTitle, dataset, false, true, true);

		jChart.setBackgroundPaint(Color.white);
		plot = jChart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);

		final ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);

		final NumberAxis rangeAxis2 = new NumberAxis("Range Axis 2");
		rangeAxis2.setAutoRangeIncludesZero(false);

		final JPanel content = new JPanel(new BorderLayout());

		final ChartPanel chartPanel = new ChartPanel(jChart);
		content.add(chartPanel);

		ChartFrame Frm = new ChartFrame(axisTitles, jChart);
//		ApplicationFrame Frm = new ApplicationFrame(axisTitles);

		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
//		Frm.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Frm.setContentPane(content);
		Frm.setVisible(true);
		
		Frm.setSize(1200, 850);

		for (int i = 1; i < tableRows; i++) {
			datasetIndex++;
			String sqlBladeID = (String) tableData[i][0];
			String sqlVGID = (String) tableData[i][1];
			query = "Select (select count(*) from Measurements b  where a.DateTime >= b.DateTime and Blade = "
					+ sqlBladeID + " and VGID = " + sqlVGID + ")," + "Force from Measurements a Where Blade = ";
			updatedQuery = query + tableData[i][0] + " and VGID = " + tableData[i][1];
			try {
				dataset = new JDBCXYDataset(connector.getConnection(), updatedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			plot.setDataset(datasetIndex, dataset);
			plot.setRenderer(datasetIndex, new StandardXYItemRenderer());
		}
	}
}
