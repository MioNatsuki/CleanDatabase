import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal extends JFrame {

	private JPanel contentPane;
	ImageIcon icono = new ImageIcon("depuracion-BBDD.png");
	ImageIcon icono2 = new ImageIcon ("Minería.png");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
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
	public Principal() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("MINER\u00CDA DE DATOS");
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMineraDeDatos = new JLabel("MINER\u00CDA DE DATOS");
		lblMineraDeDatos.setFont(new Font("Yu Gothic Light", Font.BOLD, 32));
		lblMineraDeDatos.setBounds(159, 11, 335, 45);
		contentPane.add(lblMineraDeDatos);
		
		JLabel lblNewLabel = new JLabel(icono);
		lblNewLabel.setBounds(51, 113, 243, 124);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel(icono2);
		label.setBounds(407, 113, 243, 124);
		contentPane.add(label);
		
		JButton btnLimpieza = new JButton("LIMPIEZA");
		btnLimpieza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Ventana frame = new Ventana();
				frame.setVisible(true);
				dispose();
			}
		});
		btnLimpieza.setBounds(126, 267, 89, 23);
		contentPane.add(btnLimpieza);
		
		JButton btnMinera = new JButton("MINER\u00CDA");
		btnMinera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Algoritmos frame = new Algoritmos();
				frame.setVisible(true);
				dispose();
			}
		});
		btnMinera.setBounds(484, 267, 89, 23);
		contentPane.add(btnMinera);
	}
}
