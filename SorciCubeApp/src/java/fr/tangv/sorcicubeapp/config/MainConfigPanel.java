package fr.tangv.sorcicubeapp.config;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;

public class MainConfigPanel extends JPanel {

	private static final long serialVersionUID = 2368005438019853886L;
	private final TitledBorder title;
	private final JScrollPane scroll;
	private final JButton back;
	private ConfigPanel selection;
	
	public MainConfigPanel() {
		setLayout(new BorderLayout(5, 5));
		this.title = new TitledBorder("");
		setBorder(title);
		//scroll
		this.scroll = new JScrollPane(10, 10);
		//init buttons
		this.back = new JButton("Back");
		back.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (selection.parent != null)
					setView(selection.parent);
			}
		});
		JButton save = new JButton("Save");
		JButton cancel = new JButton("Cancel");
		//buttons display
		JPanel split2 = new JPanel(new GridLayout(1, 2, 0, 5));
		split2.add(save);
		split2.add(cancel);
		JPanel split1 = new JPanel(new GridLayout(1, 2, 0, 10));
		split1.add(this.back);
		split1.add(split2);
		//diplay
		this.add(scroll, BorderLayout.CENTER);
		this.add(split1, BorderLayout.SOUTH);
	}
	
	public MainConfigPanel(ConfigPanel panel) {
		this();
		setView(panel);
	}
	
	public void setView(ConfigPanel panel) {
		this.selection = panel;
		this.back.setEnabled(panel.parent != null);
		this.title.setTitle(panel.getPath());
		panel.update();
		this.scroll.setViewportView(panel);
	}
		
}

