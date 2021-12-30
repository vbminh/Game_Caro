package CaroByMinh;

public class Cell {
	private int x;
	private int y;
	private int selected; //Người đã đánh ô này USER_VALUE: 1, AI_VALUE: -1
	
	public Cell() {
		this.x = -1;
		this.y = -1;
		this.selected = 0;
	}
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.selected = 0; //Chưa ai chọn
	}
	
	public Cell(int x, int y, int selected) {
		this.x = x;
		this.y = y;
		this.selected = selected;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getSelected() {
		return this.selected;
	}
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isClickable() {
		if(this.selected == 0) return true;
		return false;
	}
}
