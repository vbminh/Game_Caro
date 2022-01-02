package CaroByMinh;

import java.awt.Color;

public class Value {
	
	//kích thước bàn cờ
	public static final int SIZE = 15;
	
	//Chế độ chơi mặc định: User đi trước
	public static final int DEFAULT_MODE = 0; 
	
	//Độ rộng của mỗi cell
	public static final int CELL_WIDTH = 30; 
	
	//Khoảng cách giữa các panel
	public static final int MARGIN = 10; 
	
	//Cỡ chữ trong mỗi cell
	public static final int TEXT_CELL_SIZE = 20; 
	
	//Màu chữ mặc định của X
	public static final Color USER_TEXT_COLOR = Color.RED; 
	
	//Màu chữ mặc định của O
	public static final Color AI_TEXT_COLOR = Color.GREEN; 
	
	//Màu mặc định của mỗi ô vuông
	public static final Color CELL_COLOR = Color.WHITE; 
	
	//Màu mặc định khi user click vào một ô trong bàn cờ
	public static final Color CLICK_CELL_COLOR = Color.lightGray;
	
	//Giá trị mặc định của user
	public static final int USER_VALUE = 1;
	
	//Giá trị mặc định của AI
	public static final int AI_VALUE = -1;
	
	//Độ sâu tìm kiếm tối đa
	public static final int MAX_DEPTH = 3;
	
	//Số lượng lấy ra tối đa của danh sách các ô được lượng giá cao nhất
	public static final int MAX_NUM_OF_HIGHEST_CELL_LIST = 8;
}
