import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class VentanaErrorNum extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */

	
	public VentanaErrorNum(final Limpieza limpieza, final String error, final int numAtributo) {
		setTitle(error);
		setResizable(false);
		setBounds(100, 100, 396, 203);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JButton btnModificar = new JButton("Aceptar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		JLabel lblError = new JLabel("Error");
		lblError.setBounds(70, 52, 46, 14);
		contentPane.add(lblError);
		
		JLabel label = new JLabel(error);
		label.setBounds(126, 52, 46, 14);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(70, 77, 123, 55);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnModificar.setBounds(223, 93, 96, 23);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Double.parseDouble(textField.getText());
					limpieza.reemplazarErrorPorDato(error, textField.getText(), numAtributo);
					setVisible(false);
				}
				catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(null, "No es un numero", "Error", JOptionPane.ERROR_MESSAGE, null);
				}
			}
		});
		contentPane.add(btnModificar);
		
		setVisible(true);
	}
}

