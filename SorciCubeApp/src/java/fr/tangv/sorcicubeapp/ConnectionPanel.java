package fr.tangv.sorcicubeapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class ConnectionPanel extends JPanel {

	private static final long serialVersionUID = 8194724955087227091L;
	private JButton btnConnection;
	private JButton btnReset;
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
		//btnreset
		btnReset = new JButton("Reset");
		btnReset.setFocusable(false);
		btnReset.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				scURI.setText("");
				message.setText(" ");
			}
		});
		//scURI
		scURI = new JTextField("");
		//message
		message = new JLabel(" ");
		message.setHorizontalAlignment(JLabel.CENTER);
		//centerPan
		centerPan = new JPanel();
		centerPan.setLayout(new GridLayout(6, 1, 0, 5));
		centerPan.setBorder(new TitledBorder("Connection"));
		//add in centerPan
		this.centerPan.add(new Label("SorciClientURI:"));
		this.centerPan.add(scURI);
		this.centerPan.add(btnConnection);
		this.centerPan.add(btnReset);
		//frame
		JPanel inPanel = new JPanel();
		inPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
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
