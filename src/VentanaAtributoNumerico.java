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

public class VentanaAtributoNumerico extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */

	
	public VentanaAtributoNumerico(final Limpieza limpieza, final String nombreAtributo) {
		final int numAtributo = limpieza.listaEncabezados.indexOf(nombreAtributo);
		final AtributoNumerico atributo = limpieza.listaAtributosNumericos.get(
				limpieza.listaAtributosNumericos.indexOf(
						new AtributoNumerico(numAtributo)));
		limpieza.calcularDatosAtribNum();
		setTitle(nombreAtributo);
		setResizable(false);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		final JComboBox<String> comboErrores = new JComboBox<String>();
		comboErrores.setEditable(false);
		comboErrores.setToolTipText("Atributo");
		comboErrores.setBounds(36, 36, 180, 20);
		limpieza.llenarErroresAtributoComboBox(nombreAtributo, comboErrores);
		contentPane.add(comboErrores);
		
		final JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboErrores.getItemCount() > 0)
				{
					new VentanaErrorNum(limpieza, comboErrores.getSelectedItem().toString(), numAtributo);
				}
			}
		});
		btnModificar.setBounds(238, 11, 89, 23);
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
		lblError.setBounds(36, 11, 46, 14); //entro if
		contentPane.add(lblError);
		
		JLabel lblNormalizacion = new JLabel("Normalizacion");
		lblNormalizacion.setBounds(36, 67, 89, 14);
		contentPane.add(lblNormalizacion);
		
		final JComboBox<String> comboBoxMinMax = new JComboBox<>();
		comboBoxMinMax.setBounds(583, 136, 89, 20);
		comboBoxMinMax.addItem("0 a 1");
		comboBoxMinMax.addItem("-1 a 1");
		contentPane.add(comboBoxMinMax);
		
		final JButton btnEscalamiento = new JButton("Escalamiento decimal");
		btnEscalamiento.setBounds(36, 92, 89, 23);
		contentPane.add(btnEscalamiento);
		
		final JButton btnZscoreDesest = new JButton("Z-Score DesEst");
		btnZscoreDesest.setBounds(36, 121, 89, 23);
		contentPane.add(btnZscoreDesest);
		
		final JButton btnZscoreDesmedabs = new JButton("Z-Score DesMedAbs");
		btnZscoreDesmedabs.setBounds(161, 92, 89, 23);
		contentPane.add(btnZscoreDesmedabs);
		
		final JButton btnMinmax = new JButton("Min-Max");
		btnMinmax.setBounds(161, 121, 89, 23);
		contentPane.add(btnMinmax);
		
		JLabel lblEstadisticas = new JLabel("Estadisticas");
		lblEstadisticas.setBounds(409, 11, 81, 14);
		contentPane.add(lblEstadisticas);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(408, 25, 264, 100);
		contentPane.add(scrollPane_2);
		
		JTextArea textAreaEstadisticas = new JTextArea();
		textAreaEstadisticas.setEditable(false);
		scrollPane_2.setViewportView(textAreaEstadisticas);
		limpieza.llenarEstadisticas(textAreaEstadisticas, numAtributo);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpieza.hacerCalculosTardados();
				dispose();
			}
		});
		btnAceptar.setBounds(238, 39, 89, 23);
		contentPane.add(btnAceptar);
		
		JLabel lblCorrelaciones = new JLabel("Correlaciones");
		lblCorrelaciones.setBounds(36, 276, 123, 14);
		contentPane.add(lblCorrelaciones);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(36, 311, 521, 119);
		contentPane.add(scrollPane_3);
		
		JTextArea textAreaAtributos = new JTextArea();
		scrollPane_3.setViewportView(textAreaAtributos);
		limpieza.llenarDatosCorrelacionesNumericas(textAreaAtributos); 
		textAreaAtributos.setEditable(false);
		
		btnEscalamiento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new VentanaPrevisualizarNormalizacion(limpieza, numAtributo, limpieza.normalizacionEscalamiento(atributo));
				btnZscoreDesest.setEnabled(false);
				btnZscoreDesmedabs.setEnabled(false);
				btnEscalamiento.setEnabled(false);
				btnMinmax.setEnabled(false);
				System.out.println("Ya se termino de hacer esto");
			}
		});
		btnZscoreDesest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new VentanaPrevisualizarNormalizacion(limpieza, numAtributo, limpieza.normalizacionZScoreDesEst(atributo));
				btnZscoreDesest.setEnabled(false);
				btnZscoreDesmedabs.setEnabled(false);
				btnEscalamiento.setEnabled(false);
				btnMinmax.setEnabled(false);
				System.out.println("Ya se termino de hacer esto");
			}
		});
		btnZscoreDesmedabs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new VentanaPrevisualizarNormalizacion(limpieza, numAtributo, limpieza.normalizacionZScoreDesMedAbs(atributo));
				btnZscoreDesest.setEnabled(false);
				btnZscoreDesmedabs.setEnabled(false);
				btnEscalamiento.setEnabled(false);
				btnMinmax.setEnabled(false);
				System.out.println("Ya se termino de hacer esto");
			}
		});
		btnMinmax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBoxMinMax.getSelectedIndex() == 0)
				{
					new VentanaPrevisualizarNormalizacion(limpieza, numAtributo, limpieza.normalizacionMinMax(atributo, 0, 1));
				}
				else
				{
					new VentanaPrevisualizarNormalizacion(limpieza, numAtributo, limpieza.normalizacionMinMax(atributo, -1, 1));
				}
				btnZscoreDesest.setEnabled(false);
				btnZscoreDesmedabs.setEnabled(false);
				btnEscalamiento.setEnabled(false);
				btnMinmax.setEnabled(false);
				System.out.println("Ya se termino de hacer esto");
			}
		});
		
		setVisible(true);
	}
}

