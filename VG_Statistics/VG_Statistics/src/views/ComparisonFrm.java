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

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import utilities.SqlConnector;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class ComparisonFrm extends JDialog {

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
		setBounds(100, 100, 450, 300);
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
		cmbCompType.setModel(new DefaultComboBoxModel<String>(new String[] {"Force / Time", "Temperature / Time", "Humidity / Time"}));
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
				if(connector.isConnectionOpen()) {
					connector.closeConnection();
				}				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		cmbBlades.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent event) {
	            if(event.getStateChange() == ItemEvent.SELECTED){
	            	JOptionPane.showMessageDialog(rootPane, event.getItem(),
							"Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });
		
		
		contentPanel.add(cmbBlades);
		
		JLabel lblVgId = new JLabel("VG");
		lblVgId.setBounds(10, 75, 104, 14);
		lblVgId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPanel.add(lblVgId);
		
		cmbVGs = new JComboBox<String>();
		cmbVGs.setBounds(124, 73, 142, 21);
		contentPanel.add(cmbVGs);
		
		btnAdd = new JButton("Add");
		btnAdd.setBounds(10, 135, 89, 23);
		contentPanel.add(btnAdd);
		
		table = new JTable();
		table.setName("");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			new String[] {
				"Blade", "VG"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
				Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(75);
		
		table.setBounds(124, 105, 142, 113);
		contentPanel.add(table);
		
		btnRemove = new JButton("Remove");
		btnRemove.setBounds(10, 169, 89, 23);
		contentPanel.add(btnRemove);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnShowGraph = new JButton("Show Graph");
				btnShowGraph.setActionCommand("OK");
				buttonPane.add(btnShowGraph);
				getRootPane().setDefaultButton(btnShowGraph);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
