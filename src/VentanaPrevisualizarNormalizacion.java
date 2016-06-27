import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaPrevisualizarNormalizacion extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */

	
	public VentanaPrevisualizarNormalizacion(final Limpieza limpieza, final int numAtributo, final ArrayList<String> valores) {
		setTitle("Normalizacion");
		setResizable(false);
		setBounds(100, 100, 236, 245);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza.cambiarValoresAtributo(numAtributo, valores);
				setVisible(false);
			}
		});
		btnModificar.setBounds(20, 182, 96, 23);
		contentPane.add(btnModificar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 208, 152);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		for(String valor: valores)
		{
			textArea.append(valor + "\n");
		}
		scrollPane.setViewportView(textArea);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(126, 182, 89, 23);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		contentPane.add(btnCancelar);
		
		setVisible(true);
	}
}

