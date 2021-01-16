package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubecore.clients.ClientType;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class PanelURI extends JPanel {

	private static final long serialVersionUID = 4632617023164843634L;

	private final InputText address;
	private final InputNumber port;
	private final InputText name;
	private final InputText token;
	private final InputNumber types;
	private final JButton create;
	private final JButton cancel;
	
	public PanelURI(JDialog dialog , JTextField scURI) {
		String addressD = "";
		int portD = 8367;
		String nameD = "SorciCubeClientApp";
		String tokenD = "";
		byte typesD = ClientType.APPLICATION.mask;
		try {
			SorciClientURI uri = new SorciClientURI(scURI.getText());
			addressD = uri.getAddr().getHostName();
			portD = uri.getPort();
			nameD = uri.getClientID().name;
			tokenD = uri.getClientID().token;
			typesD = uri.getClientID().types;
		} catch (Exception e) {}
		address = new InputText("Address", addressD);
		port = new InputNumber("Port", portD, 65_535);
		name = new InputText("Name", nameD);
		token = new InputText("Token", tokenD);
		types = new InputNumber("Types", typesD, 255);
		//btn
		create = new JButtonAction("Create", () -> {
			try {
				scURI.setText(new SorciClientURI(InetAddress.getByName(address.getInput()), port.getInput(), (byte) types.getInput(), name.getInput(), token.getInput()).toURI());
				dialog.dispose();
			} catch (NumberFormatException | UnknownHostException | URISyntaxException e) {
				JOptionPane.showMessageDialog(dialog, "Error: "+e.getMessage(), "Error Create URI", JOptionPane.ERROR_MESSAGE);
			}
		});
		cancel = new JButtonAction("Cancel", () -> {
			dialog.dispose();
		});
		//style
		int marge = 15;
		this.setBorder(new EmptyBorder(marge, marge, marge, marge));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(address);
		this.add(port);
		this.add(name);
		this.add(token);
		this.add(types);
		JPanel btns = new JPanel(new BorderLayout(0, 10));
		btns.add(create, BorderLayout.WEST);
		btns.add(cancel, BorderLayout.EAST);
		this.add(btns);
	}
	
}
