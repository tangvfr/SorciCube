package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class ConnectionPanel extends JPanel {

	private static final long serialVersionUID = 8194724955087227091L;
	private final JButton btnConnection;
	private final JButton btnCreate;
	private final JTextField scURI;
	private final JMenuItem clearURI;
	private final JLabel message;
	private final JPanel centerPan;
	private final Vector<String> uris;
	private final File file;
	
	public ConnectionPanel(FrameLogi fl, String defaultURI) {
		//btnconnection
		btnConnection = new JButton("Connection");
		btnConnection.setFocusable(false);
		btnConnection.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String text = scURI.getText();
					if (!uris.contains(text)) {
						uris.add(text);
						saveURIS();
					}
					SorciClientURI uri = new SorciClientURI(text);
					message.setText(" ");
					fl.tryConnection(uri);
				} catch (NumberFormatException | URISyntaxException | IOException e1) {
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
		//message
		message = new JLabel(" ");
		message.setHorizontalAlignment(JLabel.CENTER);
		//files
		this.file = new File(System.getenv("appdata")+"/SorciCubeApp/uris");
		this.uris = new Vector<String>();
		try {
			if (!file.exists()) {
				if (!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				file.createNewFile();
			} else
				loadURIS();
		} catch (IOException e) {
			message.setText("Error: "+e.getMessage());
			message.setForeground(Color.MAGENTA);
		}
		//scURI
		if (defaultURI.isEmpty() && !uris.isEmpty())
			defaultURI = uris.lastElement();
		scURI = new JTextField(defaultURI);
		//clearURI
		this.clearURI = new JMenuItem("Clear");
		clearURI.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				uris.clear();
				try {
					saveURIS();
				} catch (IOException e) {
					message.setText("Error: "+e.getMessage());
					message.setForeground(Color.MAGENTA);
				}
			}
		});
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
	
	private void updatePopMenu() {
		JPopupMenu pop = new JPopupMenu();
		for (String uri : uris) {
			JMenuItem item = new JMenuItem(uri);
			item.addMouseListener(new ClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					scURI.setText(uri);
					scURI.repaint();
				}
			});
		}
		pop.addSeparator();
		pop.add(clearURI);
		scURI.setComponentPopupMenu(pop);
	}
	
	private void saveURIS() throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
		Iterator<String> it = uris.iterator();
		while (true) {
			out.append(it.next());
			if (it.hasNext())
				out.newLine();
			else
				break;
		}
		out.close();
		updatePopMenu();
	}
	
	private void loadURIS() throws IOException {
		uris.clear();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		String uri;
		while ((uri = in.readLine()) != null)
			uris.add(uri);
		in.close();
		updatePopMenu();
	}
	
}
