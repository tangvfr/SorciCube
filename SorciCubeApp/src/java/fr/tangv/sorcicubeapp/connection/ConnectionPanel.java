package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class ConnectionPanel extends JPanel {

	private static final long serialVersionUID = 8194724955087227091L;
	private JButton btnConnection;
	private JButton btnCreate;
	private JTextField scURI;
	private JLabel message;
	private JPanel centerPan;
	
	public ConnectionPanel(FrameLogi fl) {
		//btnconnection
		btnConnection = new JButton("Connection");
		btnConnection.setFocusable(false);
		btnConnection.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					SorciClientURI uri = new SorciClientURI(scURI.getText());
					message.setText(" ");
					fl.tryConnection(uri);
				} catch (NumberFormatException | UnknownHostException | URISyntaxException e1) {
					message.setText("Error: "+e1.getMessage());
					message.setForeground(Color.MAGENTA);
				}
			}
		});
		//btnCreate
		btnCreate = new JButton("Create URI");
		btnCreate.setFocusable(false);
		btnCreate.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog dialog = new JDialog(fl);
				dialog.setTitle("Create URI");
				dialog.addWindowListener(new WindowListener() {
					@Override public void windowOpened(WindowEvent e) {}
					@Override public void windowIconified(WindowEvent e) {}
					@Override public void windowDeiconified(WindowEvent e) {}
					@Override public void windowDeactivated(WindowEvent e) {}
					@Override public void windowClosing(WindowEvent e) {}
					@Override public void windowClosed(WindowEvent e) {
						fl.setEnabled(true);
						fl.setAlwaysOnTop(true);
						fl.setAlwaysOnTop(false);
					} 
					@Override public void windowActivated(WindowEvent e) {}
				});
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(fl);
				dialog.setContentPane(new PanelURI(dialog , scURI));
				dialog.setSize(300, 300);
				dialog.setResizable(false);
				dialog.setVisible(true);
				fl.setEnabled(false);
			}
		});
		//scURI
		scURI = new JTextField("");
		//message
		message = new JLabel(" ");
		message.setHorizontalAlignment(JLabel.CENTER);
		//centerPan
		centerPan = new JPanel();
		centerPan.setLayout(new GridLayout(4, 1, 0, 5));
		centerPan.setBorder(new TitledBorder("Connection"));
		//add in centerPan
		this.centerPan.add(new Label("SorciClientURI:"));
		this.centerPan.add(scURI);
		this.centerPan.add(btnConnection);
		this.centerPan.add(btnCreate);
		//frame
		JPanel inPanel = new JPanel();
		int marge = 50;
		inPanel.setBorder(new EmptyBorder(marge, marge, marge, marge));
		inPanel.setLayout(new BorderLayout());
		inPanel.add(centerPan, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(inPanel, BorderLayout.CENTER);
		this.add(message, BorderLayout.SOUTH);
	}
	
	public void setMessage(String message, Color color) {
		this.message.setText(message);
		this.message.setForeground(color);
	}
	
}
