import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class VentanaCorrelaciones extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */


	public VentanaCorrelaciones(final Limpieza limpieza) {
		setTitle("Correlaciones");
		limpieza.calcularCorrelacion();
		setResizable(false);
		setBounds(100, 100, 326, 304);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		JLabel lblAtributo1 = new JLabel("Atributo 1");
		lblAtributo1.setBounds(10, 11, 69, 14);
		contentPane.add(lblAtributo1);
		
		JLabel lblAtributo2 = new JLabel("Atributo 2");
		lblAtributo2.setBounds(219, 11, 80, 14);
		contentPane.add(lblAtributo2);
		
		final JComboBox<String> comboBoxAtrib2 = new JComboBox<String>();
		limpieza.llenarConEncabezados(comboBoxAtrib2);
		comboBoxAtrib2.setBounds(175, 33, 124, 20);
		contentPane.add(comboBoxAtrib2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 64, 289, 114);
		contentPane.add(scrollPane);
		
		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		final JComboBox<String> comboBoxAtrib1 = new JComboBox<String>();
		comboBoxAtrib1.setBounds(10, 36, 124, 20);
		limpieza.llenarConEncabezados(comboBoxAtrib1);
		contentPane.add(comboBoxAtrib1);
		
		btnAceptar.setBounds(110, 189, 96, 23);
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBoxAtrib1.getSelectedIndex() == comboBoxAtrib2.getSelectedIndex())
				{
					JOptionPane.showMessageDialog(null, "Selecciono los mismos atribtuos");
				}
				else
				{
					limpieza.llenarDatosCorrelacionAtribDados(comboBoxAtrib1.getSelectedItem().toString(),
							comboBoxAtrib2.getSelectedItem().toString(), comboBoxAtrib1.getSelectedIndex(),
							comboBoxAtrib2.getSelectedIndex(), textArea);
				}
			}
		});
		contentPane.add(btnAceptar);
				
		setVisible(true);
	}
}

