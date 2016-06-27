import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaAtributoNominal extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */

	
	public VentanaAtributoNominal(final Limpieza limpieza, String nombreAtributo) {
		final int numAtributo = limpieza.listaEncabezados.indexOf(nombreAtributo);
		limpieza.calcularSugerenciasErroresNominales();
		setTitle(nombreAtributo);
		setResizable(false);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		final JComboBox<String> errores = new JComboBox<String>();
		errores.setBounds(36, 78, 180, 20);
		errores.setEditable(false);
		errores.setToolTipText("Atributo");
		limpieza.llenarErroresAtributoComboBox(nombreAtributo, errores);
		contentPane.add(errores);
		
		final JButton btnModificar = new JButton("Modificar");
		btnModificar.setBounds(321, 77, 89, 23);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(errores.getItemCount() > 0)
				{
					new VentanaErrorNom(limpieza, errores.getSelectedItem().toString(), numAtributo);
				}
			}
		});
		contentPane.add(btnModificar);
	
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(321, 174, 236, 91);
		contentPane.add(scrollPane);
		
		final JTextArea textAreaErrores = new JTextArea();
		scrollPane.setViewportView(textAreaErrores);
		limpieza.llenarErroresAtributoTextArea(numAtributo, textAreaErrores);
		textAreaErrores.setEditable(false);
		
		JLabel lblErrores = new JLabel("Error - Frecuencia");
		lblErrores.setBounds(321, 155, 123, 14);
		contentPane.add(lblErrores);
		
		JLabel lblDatoFrecuencia = new JLabel("Dato - Frecuencia");
		lblDatoFrecuencia.setBounds(36, 155, 123, 14);
		contentPane.add(lblDatoFrecuencia);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(36, 174, 264, 91);
		contentPane.add(scrollPane_1);
		
		JTextArea textAreaDatos = new JTextArea();
		scrollPane_1.setViewportView(textAreaDatos);
		limpieza.llenarDatosAtributo(numAtributo, textAreaDatos);
		textAreaDatos.setEditable(false);
		
		JLabel lblError = new JLabel("Error");
		lblError.setBounds(36, 53, 46, 14);
		contentPane.add(lblError);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(420, 77, 89, 23);
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza.hacerCalculosTardados();
				dispose();
			}
		});
		contentPane.add(btnAceptar);
		
		JLabel lblCorrelaciones = new JLabel("Correlaciones");
		lblCorrelaciones.setBounds(36, 276, 123, 14);
		contentPane.add(lblCorrelaciones);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(36, 309, 521, 91);
		contentPane.add(scrollPane_2);
		
		JTextArea textAreaAtributos = new JTextArea();
		scrollPane_2.setViewportView(textAreaAtributos);
		limpieza.llenarDatosCorrelacionesNominales(textAreaAtributos); 
		textAreaAtributos.setEditable(false);
		
		setVisible(true);
	}
}
