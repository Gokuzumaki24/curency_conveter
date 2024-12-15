package currencyConverter;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("localization.translation"); //$NON-NLS-1$
	private JPanel contentPane;
	private JTextField fieldAmount;
	private ArrayList<Currency> currencies = Currency.init();

	/**
	 * Create the mainWindow frame
	 */
	public MainWindow() {
		setTitle(BUNDLE.getString("MainWindow.this.title")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		setLocationRelativeTo(null);
		setResizable(false);

		// Create menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// "File" menu
		JMenu mnFile = new JMenu(BUNDLE.getString("MainWindow.mnFile.text")); //$NON-NLS-1$
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem mntmQuit = new JMenuItem(BUNDLE.getString("MainWindow.mntmQuit.text")); //$NON-NLS-1$
		mntmQuit.setMnemonic(KeyEvent.VK_Q);
		mntmQuit.addActionListener(arg0 -> System.exit(0));
		mnFile.add(mntmQuit);

		// "Help" menu
		JMenu mnHelp = new JMenu(BUNDLE.getString("MainWindow.mnHelp.text")); //$NON-NLS-1$
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		// Window components
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Label "Convert"
		JLabel lblConvert = new JLabel("Convert From:");
		lblConvert.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvert.setBounds(20, 20, 100, 30);
		contentPane.add(lblConvert);

		// ComboBox for source currency
		final JComboBox<String> comboBoxCountry1 = new JComboBox<>();
		comboBoxCountry1.setBounds(140, 20, 200, 30);
		populate(comboBoxCountry1, currencies);
		contentPane.add(comboBoxCountry1);

		// Label "To"
		JLabel lblTo = new JLabel("To:");
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTo.setBounds(60, 70, 60, 30);
		contentPane.add(lblTo);

		// ComboBox for target currency
		final JComboBox<String> comboBoxCountry2 = new JComboBox<>();
		comboBoxCountry2.setBounds(140, 70, 200, 30);
		populate(comboBoxCountry2, currencies);
		contentPane.add(comboBoxCountry2);

		// Label "Amount"
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(40, 120, 80, 30);
		contentPane.add(lblAmount);

		// TextField for amount input
		fieldAmount = new JTextField("0.00");
		fieldAmount.setBounds(140, 120, 200, 30);
		fieldAmount.setColumns(10);
		fieldAmount.setDocument(new JTextFieldLimit(10)); // Limit to 10 characters
		contentPane.add(fieldAmount);

		// Label for conversion result
		final JLabel lblResult = new JLabel("");
		lblResult.setHorizontalAlignment(SwingConstants.LEFT);
		lblResult.setBounds(140, 200, 400, 30);
		lblResult.setFont(lblResult.getFont().deriveFont(14f)); // Larger font for better readability
		contentPane.add(lblResult);

		// Button "Convert"
		JButton btnConvert = new JButton("Convert");
		btnConvert.setBounds(140, 160, 120, 30);
		btnConvert.addActionListener(arg0 -> {
			try {
				String currency1 = comboBoxCountry1.getSelectedItem().toString();
				String currency2 = comboBoxCountry2.getSelectedItem().toString();
				double amount = Double.parseDouble(fieldAmount.getText());
				double convertedAmount = convert(currency1, currency2, currencies, amount);

				DecimalFormat format = new DecimalFormat("#0.00");
				String result = format.format(amount) + " " + currency1 + " = " + format.format(convertedAmount) + " " + currency2;
				lblResult.setText(result);
			} catch (NumberFormatException e) {
				lblResult.setText("Invalid input! Please enter a valid number.");
			} catch (Exception e) {
				lblResult.setText("Conversion failed! Please try again.");
			}
		});
		contentPane.add(btnConvert);
	}

	// Populate comboBox with currency names
	public static void populate(JComboBox<String> comboBox, ArrayList<Currency> currencies) {
		for (Currency currency : currencies) {
			comboBox.addItem(currency.getName());
		}
	}

	// Find the exchange value for conversion
	public static double convert(String currency1, String currency2, ArrayList<Currency> currencies, double amount) {
		String shortNameCurrency2 = null;
		double exchangeValue = 0.0;

		for (Currency currency : currencies) {
			if (currency.getName().equals(currency2)) {
				shortNameCurrency2 = currency.getShortName();
				break;
			}
		}

		if (shortNameCurrency2 != null) {
			for (Currency currency : currencies) {
				if (currency.getName().equals(currency1)) {
					exchangeValue = currency.getExchangeValues().get(shortNameCurrency2);
					break;
				}
			}
		}

		return Currency.convert(amount, exchangeValue);
	}
}
