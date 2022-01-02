package CaroByMinh;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingConstants;
import java.awt.Font;


@SuppressWarnings("serial")
public class RunGame extends JFrame implements MouseListener{

	private JPanel contentPane;
	private JPanel TableCells; //Panel chứa ma trận cells
	private Border cellBorder; //Tạo đường viền của mỗi cell
	private JLabel[][] cell; //Ma trận cells
	private JLabel userClickedCell; //cell được user click chọn
	private JLabel aiClickedCell; //cell được AI click chọn
	
	private CaroAI caro;
	
	public static final int TEXT_CELL_SIZE = Value.TEXT_CELL_SIZE; //Cỡ chữ trong mỗi cell

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunGame frame = new RunGame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void newGame() {
		caro = new CaroAI();
		userClickedCell = null;
		aiClickedCell = null;
		for (int i = 0; i < Value.SIZE; i++) {
			for (int j = 0; j < Value.SIZE; j++) {
				cell[i][j].setBackground(Color.WHITE);
				cell[i][j].setText("");
			}
		}
	}
	
	public void updateTableCells(int x, int y, int player) {
		if(player == Value.AI_VALUE) {
			if(aiClickedCell != null) {
				aiClickedCell.setBackground(Color.WHITE); //đặt lại màu clickedCell cũ
			}
			aiClickedCell = cell[x][y];
			aiClickedCell.setForeground(Color.GREEN);
			aiClickedCell.setText("O");
			aiClickedCell.setBackground(Value.CLICK_CELL_COLOR); //làm nổi bật cell được AI chọn
		}
		else {
			cell[x][y].setBackground(Color.WHITE); //đặt lại màu clickedCell cũ
			cell[x][y].setForeground(Color.RED);;
			cell[x][y].setText("X");
		}
	}
	
	/**
	 * Create the frame.
	 */
	public RunGame() {
		/*--------------Set các giá trị mặc định--------------*/
		setResizable(false);
		setTitle("Cờ Caro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Value.SIZE*Value.CELL_WIDTH+3*Value.MARGIN+5, Value.SIZE*Value.CELL_WIDTH+60);//kích thước của mỗi phần thay đổi tự động khi thay đổi SIZE(số hàng/số cột)
		setLocationRelativeTo(null);
		

		caro = new CaroAI(); //Khởi tạo CaroAI
		cellBorder = new LineBorder(Color.black, 1); //Tạo border cho mỗi cell trong ma trận
		
		/*------------------Tạo các đối tượng------------------*/
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TableCells = new JPanel();
		TableCells.setBackground(Value.CELL_COLOR);
		TableCells.setLayout(new GridLayout(Value.SIZE, Value.SIZE, 0, 0));
		TableCells.setBounds(Value.MARGIN, Value.MARGIN, Value.SIZE*Value.CELL_WIDTH, Value.SIZE*Value.CELL_WIDTH);
		contentPane.add(TableCells);
		
		//Tạo ma trận và add vào TableCells
		cell = new JLabel[Value.SIZE][Value.SIZE];
		for (int i = 0; i < Value.SIZE; i++) {
			for (int j = 0; j < Value.SIZE; j++) {
				cell[i][j] = new JLabel();
				cell[i][j].setBackground(Value.CELL_COLOR);
				cell[i][j].setSize(Value.CELL_WIDTH, Value.CELL_WIDTH); //Kích cỡ mỗi cell
				cell[i][j].setOpaque(true);
				cell[i][j].setBorder(cellBorder);
				cell[i][j].setFont(new Font("Comic Sans MS", Font.BOLD, TEXT_CELL_SIZE));
				cell[i][j].setHorizontalAlignment(SwingConstants.CENTER); //căn giữa chữ
				cell[i][j].addMouseListener(this); //Add hàm bắt sự kiện click chuột
				TableCells.add(cell[i][j]); //Add cell vào TableCells
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = -1, y = -1; //Lưu tọa độ
		//Lấy tọa độ user click
		for (int i = 0; i < Value.SIZE; i++) {
			boolean fl = false;
			for (int j = 0; j < Value.SIZE; j++) {
				if(cell[i][j] == e.getSource()) { //Nếu người dùng click vào ô này
					x = i;
					y = j;
					fl = true;
					break;
				}
			}
			if(fl) break; //Dừng kiểm tra
		}
		
		//kiểm tra số lần click của user	
		if(e.getClickCount() == 1) { //Người dùng click dạo(click 1 lần)
			if(userClickedCell != null && userClickedCell != aiClickedCell) {
				userClickedCell.setBackground(Color.WHITE); //Đặt lại màu clickedCell cũ
			}
			userClickedCell = cell[x][y]; //Cập nhật clickedCell
			userClickedCell.setBackground(Value.CLICK_CELL_COLOR); //Làm nổi bật ô được click
		}
		
		else if(e.getClickCount() == 2) { // Người dùng chọn đánh ô này
			if(caro.isClickable(x, y)) {//Nếu ô này chưa được đánh
				caro.update(x, y, Value.USER_VALUE); // update matrix
		        System.out.println("\n----------------------------------------------------------------------");
				System.out.println("Nước đi của User:" + x + " " + y);
				
				// Cập nhật bước đi của User
				updateTableCells(x, y, Value.USER_VALUE);				
				// Kiểm tra trạng thái của bàn cờ sau khi User đánh
				if(checkResult(Value.USER_VALUE)) return;				
				// Nếu user không thắng và bàn cờ chưa full thì đến lượt AI đánh
				caro.nextStep();
				
				// Cập nhật bước đi của AI
				x = caro.getNextX();
				y = caro.getNextY();
				updateTableCells(x, y, Value.AI_VALUE);
				
				// Kiểm tra trạng thái của bàn cờ sau khi AI đánh
				if(checkResult(Value.AI_VALUE)) return;
			}
		}
	}

	private boolean checkResult(int player) {
		if(player == Value.USER_VALUE) {
			boolean result = caro.checkWinner(Value.USER_VALUE);
			if(result == true) {
				System.out.println("User thắng!");
				int n = JOptionPane.showConfirmDialog(null, "You won. Do you want to play again?", null, JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.YES_OPTION)
					newGame();
				return true; //Kết thúc màn chơi
			}
		}
		else {
			boolean result = caro.checkWinner(Value.AI_VALUE);
			if(result == true) {
				System.out.println("AI thắng!");
				int n = JOptionPane.showConfirmDialog(null, "AI won. Do you want to play again?", null, JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.YES_OPTION)
					newGame();
				return true;
			}
		}
		if(caro.isOver()) {
			System.out.println("Hòa!");
			int n = JOptionPane.showConfirmDialog(null, "Draw. Do you want to play again?", null, JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.YES_OPTION)
				newGame();
			return true;
		}
		return false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
