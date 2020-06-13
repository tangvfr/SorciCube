package fr.tangv.sorcicubespell.logi;

public class ManagerLogi {

	public static void main(String[] args) {
		new ManagerLogi();
	}
	
	private FrameLogi frameLogi;
	
	public ManagerLogi() {
		this.frameLogi = new FrameLogi(this);
		this.frameLogi.setVisible(true);
	}
	
}
