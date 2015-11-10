package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import utilities.SqlConnector;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Component;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ComparisonFrm dialog = new ComparisonFrm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

		@SuppressWarnings("resource")
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
		cmbBlades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmbVGs.removeAll();
				String sqlStatement2 = "Select distinct VGID from Measurements Where Blade = "
						+ cmbBlades.getSelectedItem();
				ResultSet rsVgs = null;
				try {
					connector.connectToDatabase();
					rsVgs = connector.executeResultSetQuery(sqlStatement2);
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
					Object[][] tableData = new Object[nRow][nCol];
					for (int i = 0; i < nRow; i++)
						for (int j = 0; j < nCol; j++)
							tableData[i][j] = model.getValueAt(i, j);

					JFreeChart jChart = null;
					String sqlStatement = null;
					String axisTitles = null;
					String xTitle = null;
					String yTitle = null;
					if (cmbCompType.getSelectedIndex() == 1) {
						sqlStatement = "Select substr(DateTime, 11 ,9), Force from Measurements Where ";
						axisTitles = "Force / Time";
						xTitle = "Time";
						yTitle = "Force";
					}
					if (cmbCompType.getSelectedIndex() == 2) {
						sqlStatement = "Select substr(DateTime, 11 ,9), Hum from Measurements Where ";
						axisTitles = "Humidity / Time";
						xTitle = "Time";
						yTitle = "Humidity";
					}
					if (cmbCompType.getSelectedIndex() == 3) {
						sqlStatement = "Select substr(DateTime, 11 ,9), Temp from Measurements Where ";
						axisTitles = "Temperature / Time";
						xTitle = "Time";
						yTitle = "Temperature";
					}
					jChart = ChartFactory.createLineChart(axisTitles, xTitle, yTitle, null, PlotOrientation.VERTICAL,
							false, true, true);
					int datasetIndex = 0;
					for (int i = 0; i < nRow; i++) {
						String updatedSqlStatement = sqlStatement + "Blade = " + tableData[i][0] + " and VGID = "
								+ tableData[i][1];
						JDBCCategoryDataset dataset = null;
						try {
							dataset = new JDBCCategoryDataset(connector.getConnection(), updatedSqlStatement);
						} catch (SQLException ex) {
							ex.printStackTrace();
						}

						jChart.setd
					}
					ChartFrame cFrame = new ChartFrame(axisTitles, jChart);
					cFrame.setVisible(true);
					cFrame.setSize(1200, 850);
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
}
