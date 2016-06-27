import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JComboBox;

public class Ventana extends JFrame {

	private JPanel contentPane;
	static private Limpieza limpieza;
	
	public Ventana() {
		setTitle("LIMPIEZA");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JButton btnCorrelaciones = new JButton("Correlaciones");
		btnCorrelaciones.setEnabled(false);
		btnCorrelaciones.setBounds(271, 159, 89, 23);
		btnCorrelaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new VentanaCorrelaciones(limpieza);
			}
		});
		contentPane.add(btnCorrelaciones);
		
		JLabel lblClicEnIniciar = new JLabel("CLIC EN INICIAR PARA COMENZAR LIMPIEZA");
		lblClicEnIniciar.setFont(new Font("Yu Gothic", Font.BOLD, 18));
		lblClicEnIniciar.setBounds(107, 11, 485, 46);
		contentPane.add(lblClicEnIniciar);
		
		final JComboBox<String> comboBoxAtributos = new JComboBox<String>();
		comboBoxAtributos.setEditable(false);
		comboBoxAtributos.setToolTipText("Atributo");
		comboBoxAtributos.setBounds(61, 126, 180, 20);
		contentPane.add(comboBoxAtributos);
		
		final JButton btnVer = new JButton("Ver");
		btnVer.setEnabled(false);
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Comprueba que no este seleccionada la palabra Atributo qua va al inicio
				int indiceSeleccionado = comboBoxAtributos.getSelectedIndex();
				if(limpieza.esAtributoNumerico(indiceSeleccionado))
				{
					new VentanaAtributoNumerico(limpieza, limpieza.listaEncabezados.get(indiceSeleccionado));
				}
				else
				{
					new VentanaAtributoNominal(limpieza, limpieza.listaEncabezados.get(indiceSeleccionado ));
				}
			}
		});
		btnVer.setBounds(271, 125, 89, 23);
		contentPane.add(btnVer);
		
		final JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(370, 125, 89, 23);
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza.eliminarAtributo(comboBoxAtributos.getSelectedIndex());
				limpieza.hacerCalculosTardados();
				limpieza.llenarConEncabezados(comboBoxAtributos);
				
			}
		});
		contentPane.add(btnEliminar);
		
		final JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setEnabled(false);
		btnGuardar.setBounds(469, 125, 89, 23);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza.guardarCorrelaciones();
				limpieza.guardarDatosYErrores();
				limpieza.guardarNuevosDatosTxt();
				limpieza.guardarExcel();
				limpieza.guardarExpresionesRegulares();
			}
		});
		contentPane.add(btnGuardar);
		
		final JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpieza = new Limpieza();
				limpieza.hacerCalculosTardados();
				btnVer.setEnabled(true);
				btnIniciar.setEnabled(false);
				btnEliminar.setEnabled(true);
				btnGuardar.setEnabled(true);
				btnCorrelaciones.setEnabled(true);
				limpieza.llenarConEncabezados(comboBoxAtributos);
				//limpieza.OneR();
			}
		});
		btnIniciar.setBounds(271, 63, 89, 23);
		contentPane.add(btnIniciar);
		
		JLabel lblAtributo = new JLabel("Atributo");
		lblAtributo.setBounds(61, 101, 46, 14);
		contentPane.add(lblAtributo);
		
		JButton btnRegresar = new JButton("Regresar");
		btnRegresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Principal frame = new Principal();
				frame.setVisible(true);
			}
		});
		btnRegresar.setBounds(271, 213, 89, 23);
		contentPane.add(btnRegresar);
	}
}
