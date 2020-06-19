package fr.tangv.sorcicubespell.logi.dialog;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubespell.logi.ClickListener;

public abstract class DialogBase<T extends JComponent> extends JDialog {

	private static final long serialVersionUID = -5728665688478855133L;
	private Window frameLogi;
	
	public DialogBase(Window owner, String label, T comp) {
		super(owner);
		this.frameLogi = owner;
		owner.setEnabled(false);
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DialogBase.this.eventOk(comp);
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
		this.setLocationRelativeTo(owner);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Edit Cart "+label);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		JPanel panUp = new JPanel();
		panUp.setLayout(new BorderLayout());
		panUp.add(new JLabel(label+": "), BorderLayout.WEST);
		panUp.add(comp, BorderLayout.CENTER);
		panUp.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.add(panUp);
		JPanel panDown = new JPanel();
		panDown.setLayout(new BoxLayout(panDown, BoxLayout.X_AXIS));
		panDown.add(btnCancel);
		panDown.add(new CompEmpty(10, 10));
		panDown.add(btnOk);
		panDown.setBorder(new EmptyBorder(0, 10, 10, 10));
		this.add(panDown);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			frameLogi.setEnabled(true);
		}
		super.processWindowEvent(e);
	}
	
	public abstract void eventOk(T comp);
	
}
