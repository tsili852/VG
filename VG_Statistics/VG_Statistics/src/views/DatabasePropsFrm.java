package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLClientInfoException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;

public class DatabasePropsFrm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea txtDatabaseLocation;
	private JButton btnBackup;
	private JButton btnRestore;
	private JButton btnClearDatabase;
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DatabasePropsFrm dialog = new DatabasePropsFrm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DatabasePropsFrm() {
		createComponentsAndEvents();
	}

	public void createComponentsAndEvents() {

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

		setBounds(100, 100, 391, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblDatabaseLocation = new JLabel("Database Location");
		lblDatabaseLocation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDatabaseLocation.setBounds(10, 11, 114, 23);
		contentPanel.add(lblDatabaseLocation);

		txtDatabaseLocation = new JTextArea();
		txtDatabaseLocation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtDatabaseLocation.setEditable(false);
		txtDatabaseLocation.setBounds(134, 11, 231, 23);
		contentPanel.add(txtDatabaseLocation);
		
		txtDatabaseLocation.setText(System.getProperty("user.home") + File.separator + "VG Statistics");

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Backup & Restore", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 62, 158, 108);
		contentPanel.add(panel);
		panel.setLayout(null);

		btnBackup = new JButton("Backup Database");
		btnBackup.setBounds(10, 34, 136, 23);
		panel.add(btnBackup);

		btnRestore = new JButton("Restore Database");
		btnRestore.setBounds(10, 71, 136, 23);
		panel.add(btnRestore);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Actions", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(207, 62, 158, 108);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		btnClearDatabase = new JButton("Clear Database");
		btnClearDatabase.setBackground(Color.RED);
		btnClearDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClearDatabase.setBounds(10, 27, 136, 23);
		panel_1.add(btnClearDatabase);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(event -> {
				dispose();
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);

		}
	}
}
