package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ConnectionPanel extends JPanel {

	private static final long serialVersionUID = 8194724955087227091L;
	private JButton btnConnection;
	private JButton btnReset;
	private JTextField mongoURI;
	private JTextField database;
	private JLabel message;
	private JPanel centerPan;
	
	public ConnectionPanel(FrameLogi fl) {
		//btnconnection
		btnConnection = new JButton("Connection");
		btnConnection.setFocusable(false);
		btnConnection.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String uri = mongoURI.getText();
				String db = database.getText();
				if (!uri.isEmpty() && !db.isEmpty()) {
					message.setText(" ");
					fl.tryConnection(uri, db);
				} else
					setMessage("MongoURI or Database is empty !", Color.RED);
			}
		});
		//btnreset
		btnReset = new JButton("Reset");
		btnReset.setFocusable(false);
		btnReset.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mongoURI.setText("");
				database.setText("");
				message.setText(" ");
			}
		});
		//mongoURI
		mongoURI = new JTextField("");
		//database
		database = new JTextField("plugin");
		//message
		message = new JLabel(" ");
		message.setHorizontalAlignment(JLabel.CENTER);
		//centerPan
		centerPan = new JPanel();
		centerPan.setLayout(new GridLayout(6, 1, 0, 5));
		centerPan.setBorder(new TitledBorder("Connection"));
		//add in centerPan
		this.centerPan.add(new Label("MongoURI:"));
		this.centerPan.add(mongoURI);
		this.centerPan.add(new Label("Collection:"));
		this.centerPan.add(database);
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
