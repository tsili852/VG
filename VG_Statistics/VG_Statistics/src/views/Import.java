package views;

import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import utilities.FileTypeFilter;
import utilities.Uploader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JLabel;

import java.awt.Font;

public class Import extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFileName;
	private JButton btnExit;
	private JLabel lblImported;
	private JTextField txtMeasurements;
	private File selectedFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Import frame = new Import();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Import() {
		setTitle("VG Measurements");
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
		
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 339, 185);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		txtFileName = new JTextField();
		txtFileName.setBounds(10, 12, 300, 20);
		contentPane.add(txtFileName);
		txtFileName.setColumns(10);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Uploader fileUploader = new Uploader(selectedFile);
				int mesCounter = 0;
				try {
					mesCounter = fileUploader.uploadToSQLLite();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(rootPane, "Could not connect to database Error code: " + e.getMessage(),"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
				txtMeasurements.setText(Integer.toString(mesCounter));
			}
		});
		btnImport.setBounds(221, 43, 89, 23);
		contentPane.add(btnImport);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser =  new JFileChooser(new File("\\"));
				fChooser.setDialogTitle("Open a File");
				fChooser.setFileFilter(new FileTypeFilter("txt","Text File"));
				int result = fChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					txtFileName.setText(fChooser.getSelectedFile().getPath());
					selectedFile = fChooser.getSelectedFile();
				}
			}
		});
		btnOpenFile.setBounds(10, 43, 89, 23);
		contentPane.add(btnOpenFile);
		
		btnExit = new JButton("Cancel");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnExit.setBounds(221, 112, 89, 23);
		contentPane.add(btnExit);
		
		lblImported = new JLabel("Imported measurements");
		lblImported.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImported.setBounds(10, 77, 158, 23);
		contentPane.add(lblImported);
		
		txtMeasurements = new JTextField();
		txtMeasurements.setEnabled(false);
		txtMeasurements.setBounds(157, 79, 64, 20);
		contentPane.add(txtMeasurements);
		txtMeasurements.setColumns(10);
	}
}
