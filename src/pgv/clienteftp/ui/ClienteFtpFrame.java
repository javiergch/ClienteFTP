package pgv.clienteftp.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import pgv.clienteftp.ui.model.FicherosTableModel;

@SuppressWarnings("serial")
public class ClienteFtpFrame extends JFrame {
	
	//FTP
	private FTPClient cliente;
	private String server, user, pass;
	private Integer port;
	private Boolean conectado = false;
	private Boolean datos = false;
	
	//Menu herramientas
	private JMenuBar menuBar;
	//menu servidor
	private JMenu servidorMenu;
	private JMenuItem conectarMenuItem;
	private JMenuItem desconectarMenuItem;
	
	//paneles
	private JPanel centralPanel, inferiorPanel;
	
	//tabla
	private JLabel labelDirectorio;
	private FicherosTableModel ficherosTableModel;
	private JTable tableFicheros;
	
	
	public ClienteFtpFrame(String server, Integer port, String user, String pass) {
		this.server = server;
		this.port = port;
		this.user = user;
		this.pass = pass;	
		this.datos = true;
		cliente = new FTPClient();
		
		initFrame();
		initComponents();
		loadData();
	}

	public ClienteFtpFrame() {		
		initFrame();
		initComponents();
	}

	private void initFrame() {
		setTitle("Mi cliente FTP");
		setSize(800,600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				onWindowClosing(e);
			}
		});
	}

	private void initComponents() {
		initMenuBar();
		
		centralPanel = initCentralPanel();
		inferiorPanel = initInferiorPanel();
		
		getContentPane().add(centralPanel, BorderLayout.CENTER);
		getContentPane().add(inferiorPanel, BorderLayout.SOUTH);
	}
	
	private JPanel initCentralPanel() {	
		labelDirectorio = new JLabel("", SwingConstants.CENTER);		
		ficherosTableModel = new FicherosTableModel();
		tableFicheros = new JTable(ficherosTableModel);
		tableFicheros.setFillsViewportHeight(true);
		tableFicheros.setAutoCreateRowSorter(true);
		tableFicheros.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					onDoubleClickMouseEvent(e);
			}
		}) ;
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tableFicheros);
		
		JPanel centralPanel = new JPanel(new BorderLayout());
		centralPanel.add(labelDirectorio, BorderLayout.NORTH);
		centralPanel.add(scrollPane, BorderLayout.CENTER);
		
		return centralPanel;
	}

	protected void onDoubleClickMouseEvent(MouseEvent e) {
		int indiceModelo = tableFicheros.convertRowIndexToModel(tableFicheros.getSelectedRow());	//convertir indice de la tabla al modelo
		FTPFile fichero = ficherosTableModel.getFichero(indiceModelo);
		
		if(fichero.getType() == 1)
			if(!conectado) {
				JOptionPane.showMessageDialog(this, "Debe conectarse primero","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			
			try {
				cliente.changeWorkingDirectory(fichero.getName());
				labelDirectorio.setText(cliente.printWorkingDirectory());
				
				FTPFile [] ficherosArray = cliente.listFiles();
				ficherosTableModel.setFicheros(ficherosArray);
				
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
	}

	private JPanel initInferiorPanel() {
		JPanel inferiorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton buttonDescargar = new JButton("Descargar");
		buttonDescargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonDescargarActionPerformed(e);
			}
		});
		
		inferiorPanel.add(buttonDescargar);
		
		
		return inferiorPanel;
	}

	private void initMenuBar() {
		conectarMenuItem = new JMenuItem("Conectar");
		conectarMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onConectarMenuItemActionPerformed(e);
			}
		});
		desconectarMenuItem = new JMenuItem("Desconectar");
		desconectarMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDesconectarMenuItemActionPerformed(e);
			}
		});
		
		
		servidorMenu = new JMenu("Servidor");
		servidorMenu.add(conectarMenuItem);
		servidorMenu.add(desconectarMenuItem);
		
		menuBar = new JMenuBar();
		menuBar.add(servidorMenu);
		
		setJMenuBar(menuBar);
	}
	
	private void loadData() {
		iniciarSesion();
				
		try {
			labelDirectorio.setText(cliente.printWorkingDirectory());
			
			FTPFile [] ficherosArray = cliente.listFiles();
			ficherosTableModel.setFicheros(ficherosArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void onButtonDescargarActionPerformed(ActionEvent e) {		
		try {
			if (tableFicheros.getSelectedRows().length == 1)	{
				int indiceModelo = tableFicheros.convertRowIndexToModel(tableFicheros.getSelectedRow());	//convertir indice de la tabla al modelo
				FTPFile fichero = ficherosTableModel.getFichero(indiceModelo);
				
				if(fichero.getType() == 0) {
					//descargar un fichero
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setSelectedFile(new File(fichero.getName()));
					fileChooser.setCurrentDirectory(new File("."));
								
					
					int resultado = fileChooser.showSaveDialog(this);
					if(resultado == JFileChooser.APPROVE_OPTION) {
						//DESCARGAR
						File descarga = fileChooser.getSelectedFile();
						FileOutputStream flujo = new FileOutputStream(descarga);
						cliente.retrieveFile(fichero.getName(), flujo);
						flujo.flush();
						flujo.close();	
					}
				}
				else {
					JOptionPane.showMessageDialog(this, "Debe seleccionar un fichero","Mensaje", JOptionPane.ERROR_MESSAGE);
				}

			}
			else {
				JOptionPane.showMessageDialog(this, "Debe seleccionar un fichero","Mensaje", JOptionPane.ERROR_MESSAGE);
			}
		} catch (ArrayIndexOutOfBoundsException | IOException ex) {
			System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog(this, "Debe seleccionar un fichero","Mensaje", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	protected void onConectarMenuItemActionPerformed(ActionEvent e) {
		if(datos == true) {
			if(iniciarSesion())
				JOptionPane.showMessageDialog(this, "Conexión establecida con éxito con: " + server,"Conexión", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "No se pudo conectar: " + server,"Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			dispose();
			new LoginDialog().setVisible(true);
		}
	}

	protected void onDesconectarMenuItemActionPerformed(ActionEvent e) {
		if(desconectar())
			JOptionPane.showMessageDialog(this, "Se ha cerrado la conexión con éxito","Desconexión", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "No se ha podido cerrar la conexión" + server,"Error", JOptionPane.ERROR_MESSAGE);
	}

	protected void onWindowClosing(WindowEvent e) {
		dispose();
	}
	
	private boolean iniciarSesion() {
		if(conectar())
			if(logear())
				return true;
		
		return false;
	}
	
	private boolean conectar() {
		try {
			cliente.connect(server, port);
			conectado = true;
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conectado = false;
		return false;
	}
	private boolean desconectar() {
		try {
			cliente.disconnect();
			conectado = false;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		conectado = true;
		return false;
	}
	private boolean logear() {
		try {
			cliente.login(user, pass);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
