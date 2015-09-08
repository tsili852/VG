package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.*;

import utilities.SqlConnector;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import com.toedter.calendar.JDateChooser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;

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
			
		JTable tblMeasurements = new JTable();
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
		
		btnExit.addActionListener(exitAL);
		btnExit.setBounds(329, 164, 89, 23);
		contentPane.add(btnExit);
		
		JDateChooser dChooserFrom = new JDateChooser();
		dChooserFrom.setBounds(161, 52, 99, 21);
		dChooserFrom.setDateFormatString("dd/MM/yyyy");
		Calendar cal = new GregorianCalendar();
		dChooserFrom.setDate(cal.getTime());
		contentPane.add(dChooserFrom);
		
		lblDateFrom = new JLabel("Date From");
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateFrom.setBounds(161, 33, 99, 14);
		contentPane.add(lblDateFrom);
		
		JDateChooser dChooserTo = new JDateChooser();
		dChooserTo.setDateFormatString("dd/MM/yyyy");
		
		dChooserTo.setDate(cal.getTime());
		dChooserTo.setBounds(269, 52, 99, 21);
		contentPane.add(dChooserTo);
		
		JLabel lblDateTo = new JLabel("Date To");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateTo.setBounds(270, 33, 99, 14);
		contentPane.add(lblDateTo);
		
		lblHumidity = new JLabel("Humidity");
		lblHumidity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHumidity.setBounds(62, 109, 47, 18);
		contentPane.add(lblHumidity);
		
		txtHumMin = new JTextField();
		txtHumMin.setBounds(119, 109, 86, 20);
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
		txtHumMin.setDocument(humMindoc);
		contentPane.add(txtHumMin);
		txtHumMin.setColumns(10);
		
		JLabel lblFrom = new JLabel("Min");
		lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFrom.setBounds(145, 84, 33, 18);
		contentPane.add(lblFrom);
		
		JLabel lblMax = new JLabel("Max");
		lblMax.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMax.setBounds(240, 84, 33, 18);
		contentPane.add(lblMax);
		
		txtHumMax = new JTextField();
		txtHumMax.setColumns(10);
		txtHumMax.setBounds(215, 109, 86, 20);
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
		txtHumMax.setDocument(humMaxdoc);
		contentPane.add(txtHumMax);
		
		JLabel lblTemperature = new JLabel("Temperature");
		lblTemperature.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTemperature.setBounds(37, 138, 72, 18);
		contentPane.add(lblTemperature);
		
		txtTempMin = new JTextField();
		txtTempMin.setColumns(10);
		txtTempMin.setBounds(119, 138, 86, 20);
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
		txtTempMin.setDocument(tempMinDoc);
		contentPane.add(txtTempMin);
		
		txtTempMax = new JTextField();
		txtTempMax.setColumns(10);
		txtTempMax.setBounds(215, 138, 86, 20);
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
		txtTempMax.setDocument(tempMaxDoc);
		contentPane.add(txtTempMax);
		
		JLabel lblPressure = new JLabel("Pressure");
		lblPressure.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPressure.setBounds(63, 165, 46, 18);
		contentPane.add(lblPressure);
		
		txtPresMin = new JTextField();
		txtPresMin.setColumns(10);
		txtPresMin.setBounds(119, 165, 86, 20);
		PlainDocument preMinDoc = new PlainDocument();
		preMinDoc.setDocumentFilter(new DocumentFilter() {
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
		txtPresMin.setDocument(preMinDoc);
		contentPane.add(txtPresMin);
		
		txtPresMax = new JTextField();
		txtPresMax.setColumns(10);
		txtPresMax.setBounds(215, 165, 86, 20);
		PlainDocument preMaxDoc = new PlainDocument();
		preMaxDoc.setDocumentFilter(new DocumentFilter() {
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
		txtPresMax.setDocument(preMaxDoc);
		contentPane.add(txtPresMax);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(329, 108, 89, 23);
		contentPane.add(btnSearch);

		mntmExit.addActionListener(exitAL);

	}
	
	ActionListener exitAL = new ActionListener() {
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
	private JLabel lblDateFrom;
	private JLabel lblHumidity;
	private JTextField txtHumMin;
	private JTextField txtHumMax;
	private JTextField txtTempMin;
	private JTextField txtTempMax;
	private JTextField txtPresMin;
	private JTextField txtPresMax;
}
