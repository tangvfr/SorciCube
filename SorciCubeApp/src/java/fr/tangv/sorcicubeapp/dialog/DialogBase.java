package fr.tangv.sorcicubeapp.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;

public abstract class DialogBase<T extends Component> extends JDialog {

	private static final long serialVersionUID = -5728665688478855133L;
	private Window frameLogi;
	protected JPanel panUp;
	protected JPanel panDown;
	
	public DialogBase(Window owner, String action, String label, T comp) {
		this(owner, action, label, comp, null);
	}
	
	public DialogBase(Window owner, String action, String label, T comp, Dimension minDim) {
		super(owner);
		this.frameLogi = owner;
		owner.setEnabled(false);
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					DialogBase.this.eventOk(comp);
				} catch (IOException | ReponseRequestException | RequestException e1) {
					JOptionPane.showMessageDialog(DialogBase.this, "Error: "+e1.getMessage(), "EvneOK DialogBase", JOptionPane.ERROR_MESSAGE);
				}
				DialogBase.this.processWindowEvent(new WindowEvent(DialogBase.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DialogBase.this.processWindowEvent(new WindowEvent(DialogBase.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle(label+" "+action);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.panUp = new JPanel();
		panUp.setLayout(new BorderLayout());
		panUp.add(new JLabel(label+": "), BorderLayout.WEST);
		try {
			this.initComp(comp);
		} catch (IOException | ReponseRequestException | RequestException e1) {
			JOptionPane.showMessageDialog(DialogBase.this, "Error: "+e1.getMessage(), "Init DialogBase", JOptionPane.ERROR_MESSAGE);
		}
		panUp.add(comp, BorderLayout.CENTER);
		panUp.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.add(panUp);
		this.panDown = new JPanel();
		panDown.setLayout(new BoxLayout(panDown, BoxLayout.X_AXIS));
		panDown.add(btnCancel);
		panDown.add(new CompEmpty(10, 10));
		panDown.add(btnOk);
		panDown.setBorder(new EmptyBorder(0, 10, 10, 10));
		this.add(panDown);
		if (minDim != null)
			this.setMinimumSize(minDim);
		this.pack();
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			frameLogi.setEnabled(true);
		}
		super.processWindowEvent(e);
	}
	
	public abstract void eventOk(T comp) throws IOException, ReponseRequestException, RequestException;
	
	protected void initComp(T comp) throws IOException, ReponseRequestException, RequestException {};
	
}
