package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;

import utilities.MyTools;
import utilities.SqlConnector;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.Console;

import javax.swing.JButton;

import com.toedter.calendar.JDateChooser;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuItem mntmFromSdCard;
	private JMenu mnImport;
	private JMenu mnImport_1;
	private JMenuItem mntmExit;
	
    private JPanel panel;
    private JScrollPane scrollPane;
    private JComboBox<String> cmbWT;
    private JLabel lblWindTurbineId;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createComponentsAndEvents();
	}
	
	private void createComponentsAndEvents(){
		
		try {
			connector.connectToDatabase();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e1.getMessage(),"Error", 
					JOptionPane.ERROR_MESSAGE);
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
		

		
//		String sqlStatement = "Select * from Measurements";
//		ResultSet result = connector.executeResultSetQuery(sqlStatement);
			
		final JTable tblMeasurements = new JTable();
//		tblMeasurements.setModel(MyTools.resultSetToTableModel(result));		
		tblMeasurements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblMeasurements.setFillsViewportHeight(true);
		
		scrollPane = new JScrollPane(tblMeasurements);
		scrollPane.setBounds(0, 0, 592, 279);
		scrollPane.setWheelScrollingEnabled(true);
		panel.add(scrollPane);
		
		cmbWT = new JComboBox<String>();
		cmbWT.setBounds(10, 52, 142, 21);
		
		String sqlStatement2 = "Select distinct WTID from Measurements";
		ResultSet rsWTIDs = null;
		try {
			rsWTIDs = connector.executeResultSetQuery(sqlStatement2);
			cmbWT.addItem("All IDs");
			while (rsWTIDs.next()) {
				cmbWT.addItem(rsWTIDs.getString(1));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
		
		contentPane.add(cmbWT);
		
		lblWindTurbineId = new JLabel("Wind Turbine ID");
		lblWindTurbineId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWindTurbineId.setBounds(10, 32, 99, 14);
		contentPane.add(lblWindTurbineId);
		
		JButton btnExit = new JButton("Exit");
		
		btnExit.addActionListener(exitAl);
		btnExit.setBounds(329, 164, 89, 23);
		contentPane.add(btnExit);
		
		dChooserFrom = new JDateChooser();
		dChooserFrom.setBounds(329, 52, 99, 21);
		dChooserFrom.setDateFormatString("dd/MM/yyyy");
		Calendar cal = new GregorianCalendar();
		dChooserFrom.setDate(cal.getTime());
		contentPane.add(dChooserFrom);
		
		lblDateFrom = new JLabel("Date From");
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateFrom.setBounds(329, 33, 99, 14);
		contentPane.add(lblDateFrom);
		
		dChooserTo = new JDateChooser();
		dChooserTo.setDateFormatString("dd/MM/yyyy");
		
		dChooserTo.setDate(cal.getTime());
		dChooserTo.setBounds(437, 52, 99, 21);
		contentPane.add(dChooserTo);
		
		JLabel lblDateTo = new JLabel("Date To");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateTo.setBounds(438, 33, 99, 14);
		contentPane.add(lblDateTo);
		
		lblHumidity = new JLabel("Humidity");
		lblHumidity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHumidity.setBounds(62, 109, 47, 18);
		contentPane.add(lblHumidity);
		PlainDocument humMindoc = new PlainDocument();
		humMindoc.setDocumentFilter(new DocumentFilter() {
		    @Override
		    public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    } 
		    @Override
		    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
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
		        throws BadLocationException 
		    {
		        fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    } 
		    @Override
		    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
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
		        throws BadLocationException 
		    {
		        fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    } 
		    @Override
		    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    }
		});
		PlainDocument tempMaxDoc = new PlainDocument();
		tempMaxDoc.setDocumentFilter(new DocumentFilter() {
		    @Override
		    public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    } 
		    @Override
		    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
		        throws BadLocationException 
		    {
		        fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
		    }
		});
		
		JLabel lblPressure = new JLabel("Pressure");
		lblPressure.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPressure.setBounds(63, 165, 46, 18);
		contentPane.add(lblPressure);
		
		JButton btnSearch = new JButton("Search");

		btnSearch.setBounds(329, 108, 89, 23);
		contentPane.add(btnSearch);
		
		final JSpinner spnMinHum = new JSpinner();
		spnMinHum.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinHum.setBounds(119, 109, 86, 20);
		contentPane.add(spnMinHum);
		
		final JSpinner spnMinTemp = new JSpinner();
		spnMinTemp.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinTemp.setBounds(119, 138, 86, 20);
		contentPane.add(spnMinTemp);
		
		final JSpinner spnMinPres = new JSpinner();
		spnMinPres.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMinPres.setBounds(119, 165, 86, 20);
		contentPane.add(spnMinPres);
		
		final JSpinner spnMaxPres = new JSpinner();
		spnMaxPres.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxPres.setBounds(215, 165, 86, 20);
		contentPane.add(spnMaxPres);
		
		final JSpinner spnMaxTemp = new JSpinner();
		spnMaxTemp.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxTemp.setBounds(215, 138, 86, 20);
		contentPane.add(spnMaxTemp);
		
		final JSpinner spnMaxHum = new JSpinner();
		spnMaxHum.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(0.1)));
		spnMaxHum.setBounds(215, 109, 86, 20);
		contentPane.add(spnMaxHum);

		mntmExit.addActionListener(exitAl);
		mntmFromSdCard.addActionListener(importFromSDAl);
		
		
		spnMinHum.setValue(0);
		spnMaxHum.setValue(100);
		spnMinTemp.setValue(-10);
		spnMaxTemp.setValue(45);
		spnMinPres.setValue(0);
		spnMaxPres.setValue(100);
		
		btnImportFromSD = new JButton("Import from SD card");
		btnImportFromSD.setBounds(460, 164, 142, 23);
		btnImportFromSD.addActionListener(importFromSDAl);
		contentPane.add(btnImportFromSD);
		
		cmbBlades = new JComboBox<String>();
		cmbBlades.setBounds(162, 52, 72, 21);
		cmbBlades.addItem("All");
		cmbBlades.addItem("1");
		cmbBlades.addItem("2");
		cmbBlades.addItem("3");
		contentPane.add(cmbBlades);
		
		JLabel lblBlade = new JLabel("Blade");
		lblBlade.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlade.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBlade.setBounds(162, 32, 72, 14);
		contentPane.add(lblBlade);
		
		cmbVGs = new JComboBox<String>();
		cmbVGs.setBounds(240, 52, 72, 21);
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
			JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
		contentPane.add(cmbVGs);
		
		JLabel lblVg = new JLabel("VG");
		lblVg.setHorizontalAlignment(SwingConstants.CENTER);
		lblVg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblVg.setBounds(240, 33, 72, 14);
		contentPane.add(lblVg);
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sqlSelectStatement = "Select * from Measurements Where ";
				if (cmbWT.getSelectedIndex() != 0) {
					sqlSelectStatement += " WTID = '" + String.valueOf(cmbWT.getSelectedItem()) + "' and ";
				}
				
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
				
				sqlSelectStatement += " Pres >= " + spnMinPres.getValue() + " and ";
				sqlSelectStatement += " Pres <= " + spnMaxPres.getValue();
				
				try {
					ResultSet result = connector.executeResultSetQuery(sqlSelectStatement);
					tblMeasurements.setModel(MyTools.resultSetToTableModel(result));
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
				
//				JOptionPane.showMessageDialog(null, sqlSelectStatement);
//				System.out.println(sqlSelectStatement);
			}
		});
	}
	
	ActionListener exitAl = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			int answer = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					if (connector.isConnectionOpen()) {
						connector.closeConnection();
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		}
	};
	
	ActionListener importFromSDAl = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			Import frmImport = new Import();
			frmImport.setModal(true);
			frmImport.setVisible(true);
		}
	};
	private JLabel lblDateFrom;
	private JLabel lblHumidity;
	private JButton btnImportFromSD;
	private JDateChooser dChooserFrom;
	private JDateChooser dChooserTo;
	private JComboBox<String> cmbBlades;
	private JComboBox<String> cmbVGs;
}
