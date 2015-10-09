package views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import com.toedter.calendar.JDateChooser;

import utilities.MyTools;
import utilities.SqlConnector;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuItem mntmFromSdCard;
	private JMenu mnImport;
	private JMenu mnImport_1;
	private JMenuItem mntmExit;

	private JPanel panel;
	private JScrollPane scrollPane;
	private JComboBox<String> cmbBlades;
	private JLabel lblBladeId;
	private JLabel lblDateFrom;
	private JLabel lblHumidity;
	private JButton btnImportFromSD;
	private JDateChooser dChooserFrom;
	private JDateChooser dChooserTo;
	private JComboBox<String> cmbVGs;

	private SqlConnector connector = new SqlConnector("VG_db");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setTitle("VG Measurements");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createComponentsAndEvents();
	}

	private void createComponentsAndEvents() {

		try {
			connector.connectToDatabase();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e1.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

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

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				int answer = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit ?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						if (connector.isConnectionOpen()) {
							connector.closeConnection();
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(rootPane,
								"Could not connect to database Error code: " + e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}
			}
		});

		setBounds(100, 100, 628, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 612, 21);
		contentPane.add(menuBar);

		mnImport = new JMenu("File");
		menuBar.add(mnImport);

		mnImport_1 = new JMenu("Import");
		mnImport.add(mnImport_1);

		mntmFromSdCard = new JMenuItem("From SD Card");
		mnImport_1.add(mntmFromSdCard);

		JSeparator separator = new JSeparator();
		mnImport.add(separator);

		mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

		mnImport.add(mntmExit);

		panel = new JPanel();
		panel.setBounds(10, 208, 592, 279);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel);
		panel.setLayout(null);

		final JTable tblMeasurements = new JTable();
		tblMeasurements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblMeasurements.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(tblMeasurements);
		scrollPane.setBounds(0, 0, 592, 279);
		scrollPane.setWheelScrollingEnabled(true);
		panel.add(scrollPane);

		cmbBlades = new JComboBox<String>();
		cmbBlades.setBounds(10, 52, 142, 21);

		String sqlStatement2 = "Select distinct Blade from Measurements";
		ResultSet rsBlades = null;
		try {
			rsBlades = connector.executeResultSetQuery(sqlStatement2);
			cmbBlades.addItem("All Blades");
			while (rsBlades.next()) {
				cmbBlades.addItem(rsBlades.getString(1));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		contentPane.add(cmbBlades);

		lblBladeId = new JLabel("Blade ID");
		lblBladeId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBladeId.setBounds(10, 32, 99, 14);
		contentPane.add(lblBladeId);

		JButton btnExit = new JButton("Exit");

		btnExit.addActionListener(exitAl);
		btnExit.setBounds(329, 164, 89, 23);
		contentPane.add(btnExit);

		dChooserFrom = new JDateChooser();
		dChooserFrom.setBounds(240, 52, 99, 21);
		dChooserFrom.setDateFormatString("dd/MM/yyyy");
		Calendar cal = new GregorianCalendar();
		dChooserFrom.setDate(cal.getTime());
		contentPane.add(dChooserFrom);

		lblDateFrom = new JLabel("Date From");
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateFrom.setBounds(240, 33, 99, 14);
		contentPane.add(lblDateFrom);

		dChooserTo = new JDateChooser();
		dChooserTo.setDateFormatString("dd/MM/yyyy");

		dChooserTo.setDate(cal.getTime());
		dChooserTo.setBounds(348, 52, 99, 21);
		contentPane.add(dChooserTo);

		JLabel lblDateTo = new JLabel("Date To");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateTo.setBounds(349, 33, 99, 14);
		contentPane.add(lblDateTo);

		lblHumidity = new JLabel("Humidity");
		lblHumidity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHumidity.setBounds(62, 109, 47, 18);
		contentPane.add(lblHumidity);
		PlainDocument humMindoc = new PlainDocument();
		humMindoc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
					throws BadLocationException {
				fb.insertString(off, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}

			@Override
			public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
					throws BadLocationException {
				fb.replace(off, len, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}
		});

		JLabel lblFrom = new JLabel("Min");
		lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFrom.setBounds(145, 84, 33, 18);
		contentPane.add(lblFrom);

		JLabel lblMax = new JLabel("Max");
		lblMax.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMax.setBounds(240, 84, 33, 18);
		contentPane.add(lblMax);
		PlainDocument humMaxdoc = new PlainDocument();
		humMaxdoc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
					throws BadLocationException {
				fb.insertString(off, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}

			@Override
			public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
					throws BadLocationException {
				fb.replace(off, len, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}
		});

		JLabel lblTemperature = new JLabel("Temperature");
		lblTemperature.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTemperature.setBounds(37, 138, 72, 18);
		contentPane.add(lblTemperature);
		PlainDocument tempMinDoc = new PlainDocument();
		tempMinDoc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
					throws BadLocationException {
				fb.insertString(off, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}

			@Override
			public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
					throws BadLocationException {
				fb.replace(off, len, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}
		});
		PlainDocument tempMaxDoc = new PlainDocument();
		tempMaxDoc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
					throws BadLocationException {
				fb.insertString(off, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}

			@Override
			public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
					throws BadLocationException {
				fb.replace(off, len, str.replaceAll("\\D++", ""), attr); // remove
																			// non-digits
			}
		});

		JLabel lblForce = new JLabel("Force");
		lblForce.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblForce.setBounds(76, 165, 33, 18);
		contentPane.add(lblForce);

		JButton btnSearch = new JButton("Search");

		btnSearch.setBounds(329, 108, 89, 23);
		contentPane.add(btnSearch);

		spnMinHum = new JSpinner();
		spnMinHum.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinHum.setBounds(119, 109, 86, 20);
		contentPane.add(spnMinHum);

		spnMinTemp = new JSpinner();
		spnMinTemp.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinTemp.setBounds(119, 138, 86, 20);
		contentPane.add(spnMinTemp);

		spnMinForce = new JSpinner();
		spnMinForce.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinForce.setBounds(119, 165, 86, 20);
		contentPane.add(spnMinForce);

		spnMaxForce = new JSpinner();
		spnMaxForce.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxForce.setBounds(215, 165, 86, 20);
		contentPane.add(spnMaxForce);

		spnMaxTemp = new JSpinner();
		spnMaxTemp.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxTemp.setBounds(215, 138, 86, 20);
		contentPane.add(spnMaxTemp);

		spnMaxHum = new JSpinner();
		spnMaxHum.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxHum.setBounds(215, 109, 86, 20);
		contentPane.add(spnMaxHum);

		mntmExit.addActionListener(exitAl);

		spnMinHum.setValue(0);
		spnMaxHum.setValue(100);
		spnMinTemp.setValue(-10);
		spnMaxTemp.setValue(45);
		spnMinForce.setValue(0);
		spnMaxForce.setValue(100);

		btnImportFromSD = new JButton("Import from SD card");
		btnImportFromSD.setBounds(460, 498, 142, 23);

		contentPane.add(btnImportFromSD);

		cmbVGs = new JComboBox<String>();
		cmbVGs.setBounds(162, 52, 72, 21);
		String sqlMaxVG = "Select MAX(VGID) from Measurements";
		ResultSet rsMaxVG = null;
		try {
			rsMaxVG = connector.executeResultSetQuery(sqlMaxVG);
			cmbVGs.addItem("All");
			rsMaxVG.next();
			int maxVG = rsMaxVG.getInt(1);

			for (int i = 1; i <= maxVG; i++) {
				cmbVGs.addItem(String.valueOf(i));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		contentPane.add(cmbVGs);

		JLabel lblVg = new JLabel("VG");
		lblVg.setHorizontalAlignment(SwingConstants.CENTER);
		lblVg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblVg.setBounds(162, 33, 72, 14);
		contentPane.add(lblVg);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Plots", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(457, 32, 142, 121);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JButton btnTimeHumPlot = new JButton("Time/Humidity");

		btnTimeHumPlot.setBounds(10, 21, 123, 23);
		panel_1.add(btnTimeHumPlot);

		JButton btnTimetemperature = new JButton("Time/Temperature");

		btnTimetemperature.setBounds(10, 52, 123, 23);
		panel_1.add(btnTimetemperature);

		JButton btnTimeforce = new JButton("Time/Force");

		btnTimeforce.setBounds(10, 86, 123, 23);
		panel_1.add(btnTimeforce);

		/* All the Event Listeners */

		btnSearch.addActionListener(event -> {
			String sqlSelectStatement = "Select * from Measurements Where ";

			if (cmbBlades.getSelectedIndex() != 0) {
				sqlSelectStatement += " Blade = '" + String.valueOf(cmbBlades.getSelectedItem()) + "' and ";
			}

			if (cmbVGs.getSelectedIndex() != 0) {
				sqlSelectStatement += " VGID = '" + String.valueOf(cmbVGs.getSelectedItem()) + "' and ";
			}

			String sqlDateFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dChooserFrom.getDate());
			String sqlDateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dChooserTo.getDate());
			sqlSelectStatement += " DateTime >= '" + sqlDateFrom + "' and ";
			sqlSelectStatement += " DateTime <= '" + sqlDateTo + "' and ";

			sqlSelectStatement += " Hum >= " + spnMinHum.getValue() + " and ";
			sqlSelectStatement += " Hum <= " + spnMaxHum.getValue() + " and ";

			sqlSelectStatement += " Temp >= " + spnMinTemp.getValue() + " and ";
			sqlSelectStatement += " Temp <= " + spnMaxTemp.getValue() + " and ";

			sqlSelectStatement += " Force >= " + spnMinForce.getValue() + " and ";
			sqlSelectStatement += " Force <= " + spnMaxForce.getValue();

			try {
				ResultSet result = connector.executeResultSetQuery(sqlSelectStatement);
				tblMeasurements.setModel(MyTools.resultSetToTableModel(result));

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}

			// System.out.println(sqlSelectStatement);
		});

		btnTimeHumPlot.addActionListener(event -> {
			String query = "Select DateTime, Hum from Measurements";
			String axisTitles = "Time/Humidity";
			String xTitle = "Time";
			String yTitle = "Humidity";

			showGraph(query, axisTitles, xTitle, yTitle);
		});

		btnTimetemperature.addActionListener(event -> {
			String query = "Select DateTime, Temp from Measurements";
			String axisTitles = "Time/Temperature";
			String xTitle = "Time";
			String yTitle = "Temperature";

			showGraph(query, axisTitles, xTitle, yTitle);
		});

		btnTimeforce.addActionListener(event -> {
			String query = "Select DateTime, Force from Measurements";
			String axisTitles = "Time/Force";
			String xTitle = "Time";
			String yTitle = "Force";

			showGraph(query, axisTitles, xTitle, yTitle);
		});

		mntmFromSdCard.addActionListener(event -> {
			Import frmImport = new Import();
			frmImport.setModal(true);
			frmImport.setVisible(true);
		});

		btnImportFromSD.addActionListener(event -> {
			Import frmImport = new Import();
			frmImport.setModal(true);
			frmImport.setVisible(true);
		});

	}

	ActionListener exitAl = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			int answer = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit ?", "Quit",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					if (connector.isConnectionOpen()) {
						connector.closeConnection();
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(rootPane,
							"Could not connect to database Error code: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		}
	};
	private JSpinner spnMinHum;
	private JSpinner spnMinTemp;
	private JSpinner spnMinForce;
	private JSpinner spnMaxForce;
	private JSpinner spnMaxTemp;
	private JSpinner spnMaxHum;

	/* Other methods */

	private void showGraph(String sqlStatement, String axisTitles, String xTitle, String yTitle) {
		JDBCCategoryDataset dataset = null;
		try {
			dataset = new JDBCCategoryDataset(connector.getConnection(), sqlStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JFreeChart jChart = ChartFactory.createLineChart(axisTitles, xTitle, yTitle, dataset, PlotOrientation.VERTICAL,
				false, true, true);

		ChartFrame cFrame = new ChartFrame(axisTitles, jChart);
		cFrame.setVisible(true);
		cFrame.setSize(800, 450);

		PlotFrm pFrm = new PlotFrm(jChart);
		pFrm.setVisible(true);
	}

	private String getSQLFromFrame() {
		String sqlStatement = null;
		
		if (cmbBlades.getSelectedIndex() != 0) {
			sqlStatement += " Blade = '" + String.valueOf(cmbBlades.getSelectedItem()) + "' and ";
		}

		if (cmbVGs.getSelectedIndex() != 0) {
			sqlStatement += " VGID = '" + String.valueOf(cmbVGs.getSelectedItem()) + "' and ";
		}

		String sqlDateFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dChooserFrom.getDate());
		String sqlDateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dChooserTo.getDate());
		sqlStatement += " DateTime >= '" + sqlDateFrom + "' and ";
		sqlStatement += " DateTime <= '" + sqlDateTo + "' and ";

		sqlStatement += " Hum >= " + spnMinHum.getValue() + " and ";
		sqlStatement += " Hum <= " + spnMaxHum.getValue() + " and ";

		sqlStatement += " Temp >= " + spnMinTemp.getValue() + " and ";
		sqlStatement += " Temp <= " + spnMaxTemp.getValue() + " and ";

		sqlStatement += " Force >= " + spnMinForce.getValue() + " and ";
		sqlStatement += " Force <= " + spnMaxForce.getValue();
		
		return sqlStatement;	
	}

}
