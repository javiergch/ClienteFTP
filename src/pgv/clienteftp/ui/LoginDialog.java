package pgv.clienteftp.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog {
	
	private JPanel superiorPanel, inferiorPanel;
	private JTextField textServidor, textPuerto, textUsuario, textPassword;
	
	public LoginDialog() {
		initDialog();
		initComponents();
	}

	private void initDialog() {
		setTitle("Iniciar conexión");
		setSize(480, 240);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setModal(true);
	}

	private void initComponents() {
		superiorPanel = initCentralPanel();
		inferiorPanel = initInferiorPanel();
		
		getContentPane().add(superiorPanel, BorderLayout.NORTH);
		getContentPane().add(inferiorPanel, BorderLayout.SOUTH);
	}

	private JPanel initCentralPanel() {
		JPanel superiorPanel = new JPanel();
		superiorPanel.setLayout(new GridBagLayout());
		
		textServidor = new JTextField();
		textPuerto = new JTextField(5);
		textUsuario = new JTextField(20);
		textPassword = new JTextField(20);
		textServidor.setText("ftp.rediris.es");	//datos por defecto
		textPuerto.setText("21");
		textUsuario.setText("anonymous");
		textPassword.setText("");
		
		
		Insets margen = new Insets(5, 5, 5, 5);
		
		//superiorPanel.add(new JLabel("componente"), new GridBagConstraints(0 (columna), 0 (fila), 1 (columnas que ocupa), 1 (filas que ocupa), 0.0 (% horizontal ocupado), 0.0 (% vertical ocupado), GridBagConstraints.NORTH (anclaje del componente), GridBagConstraints.HORIZONTAL (espacio que ocupa el componente), margen (margen externo alrededor), 0 (margen interno horizontal), 0 (margen interno vertical)));
		superiorPanel.add(new JLabel("Servidor:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		superiorPanel.add(textServidor, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		
		superiorPanel.add(new JLabel("Puerto:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		superiorPanel.add(textPuerto, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		
		superiorPanel.add(new JLabel("Usuario:"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		superiorPanel.add(textUsuario, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		
		superiorPanel.add(new JLabel("Contraseña:"), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		superiorPanel.add(textPassword, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, margen, 0, 0));
		
		
		return superiorPanel;
	}

	private JPanel initInferiorPanel() {
		JPanel inferiorPanel = new JPanel();
		inferiorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton buttonConectar = new JButton("Conectar");
		buttonConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonConectarActionPerformed(e);
			}
		});
		JButton buttonCancelar = new JButton("Cancelar");
		buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonCancelarActionPerformed(e);
			}
		});
		
		getRootPane().setDefaultButton(buttonConectar);	//establecer boton por defecto
		inferiorPanel.add(buttonConectar);
		inferiorPanel.add(buttonCancelar);
				
		
		return inferiorPanel;
	}

	protected void onButtonConectarActionPerformed(ActionEvent e) {
		dispose();
		new ClienteFtpFrame(textServidor.getText(), Integer.parseInt(textPuerto.getText()), textUsuario.getText(), textPassword.getText()).setVisible(true);
		
	}

	protected void onButtonCancelarActionPerformed(ActionEvent e) {
		dispose();
		new ClienteFtpFrame().setVisible(true);
	}
}