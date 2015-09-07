package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import utilities.FileTypeFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

import javax.swing.JLabel;

import java.awt.Font;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField txtFileName;
	private JButton btnExit;
	private JLabel lblImported;
	private JTextField txtTxtmeasurements;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Main() {
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				}
			}
		});
		btnOpenFile.setBounds(10, 43, 89, 23);
		contentPane.add(btnOpenFile);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int answer = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
		btnExit.setBounds(221, 112, 89, 23);
		contentPane.add(btnExit);
		
		lblImported = new JLabel("Imported measurements");
		lblImported.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImported.setBounds(10, 77, 158, 23);
		contentPane.add(lblImported);
		
		txtTxtmeasurements = new JTextField();
		txtTxtmeasurements.setEnabled(false);
		txtTxtmeasurements.setBounds(157, 79, 64, 20);
		contentPane.add(txtTxtmeasurements);
		txtTxtmeasurements.setColumns(10);
	}
}
