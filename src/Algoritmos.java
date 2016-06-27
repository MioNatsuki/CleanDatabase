import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Algoritmos extends JFrame {

	private JPanel contentPane;


	public Algoritmos() {
		setTitle("ALGORITMOS");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSeleccionaElAlgoritmo = new JLabel("SELECCIONA EL ALGORITMO QUE DESEAS USAR PARA ENTRENAR");
		lblSeleccionaElAlgoritmo.setFont(new Font("Yu Gothic", Font.BOLD, 15));
		lblSeleccionaElAlgoritmo.setBounds(107, 11, 567, 31);
		contentPane.add(lblSeleccionaElAlgoritmo);
		
		JButton btnZeror = new JButton("ZeroR");
		btnZeror.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Clasificacion clasificacion = new Clasificacion();
				clasificacion.ZeroR();
			}
		});
		btnZeror.setBounds(107, 156, 89, 23);
		contentPane.add(btnZeror);
		
		JButton btnOner = new JButton("OneR");
		btnOner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clasificacion clasificacion = new Clasificacion();
				clasificacion.OneR();
			}
		});
		btnOner.setBounds(232, 156, 89, 23);
		contentPane.add(btnOner);
		
		JButton btnNaiveBayes = new JButton("Naive Bayes");
		btnNaiveBayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Limpieza limpieza = new Limpieza();
				limpieza.naiveBayes(null);
			}
		});
		btnNaiveBayes.setBounds(355, 156, 89, 23);
		contentPane.add(btnNaiveBayes);
		
		JButton btnRegresar = new JButton("Regresar");
		btnRegresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Principal frame = new Principal();
				frame.setVisible(true);
			}
		});
		btnRegresar.setBounds(474, 156, 89, 23);
		contentPane.add(btnRegresar);
	}

}
