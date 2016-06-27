import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class VentanaErrorNom extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */


	public VentanaErrorNom(final Limpieza limpieza, final String error, final int numAtributo) {
		setTitle(error);
		setResizable(false);
		setBounds(100, 100, 463, 273);
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
		lblError.setBounds(63, 11, 46, 14);
		contentPane.add(lblError);
		
		JLabel label = new JLabel(error);
		label.setBounds(119, 11, 46, 14);
		contentPane.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 90, 220, 120);
		contentPane.add(scrollPane);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(63, 36, 173, 20);
		contentPane.add(comboBox);
		btnModificar.setBounds(293, 141, 96, 23);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza.reemplazarErrorPorDato(error, comboBox.getSelectedItem().toString(), numAtributo);
				setVisible(false);
			}
		});
		contentPane.add(btnModificar);
		limpieza.llenarDistanciasLeven(numAtributo, textArea, comboBox, error);
		
		setVisible(true);
	}
}


