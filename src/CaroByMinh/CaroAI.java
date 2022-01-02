package CaroByMinh;

import java.util.Random;
import java.util.ArrayList;

public class CaroAI {
	 	Random rand;
	    private int nextX;
	    private int nextY;
	    private State root; //Trạng thái hiện tại của trò chơi
	    private Heuristic heuristic;

	    
	    public CaroAI() {
	        rand = new Random();	
			root = new State(); 
			heuristic = new Heuristic();
	    }

	    public int getNextX() {
	    	return nextX;
	    }
	    
	    public int getNextY() {
	    	return nextY;
	    }
	    
		//Kiểm tra người thắng cuộc
		//false: nếu không ai thắng, true: nếu player(AI: 2, USER: 1) thắng.
		public boolean checkWinner(int player) {
			return root.checkWinner(player);
		}
		
		//Cập nhật ma trận
		//x vị trí hàng
		//y vị trí cột
		//value USER_VALUE: 1, AI_VALUE: 2
		public void update(int x, int y, int value) {
			root.update(x, y, value);
		}
		
		//Kiểm tra người chơi có thể chọn ô này hay không
		//x vị trí hàng
		//y vị trí cột
		//true: nếu cell[x][y] = 0, false: ngược lại
		public boolean isClickable(int x, int y) {
			return root.isClickable(x, y);
		}
	    
		//Kiểm tra bàn cờ full chưa
		//true: full, ngược lại
		public boolean isOver() {
			return root.isOver();
		}
	    
		//Tìm nước đi tốt nhất
	    public void nextStep() {
	    	Cell choice = alphaBeta(root); //Tìm kiếm bước đi tối ưu nhất
	    	if(choice == null) System.out.println("~ Lỗi! Không tìm thấy nước đi!");
	    	else {
	    		this.nextX = choice.getX(); //Cập nhật nextX, nextY
	        	this.nextY = choice.getY();
	    		System.out.println("=> Nước đi của AI: " + this.nextX + " " + this.nextY);
	        	if(!isClickable(this.nextX, this.nextY)) System.out.println("~ Lỗi! nước đi bị trùng!");
	        	else update(nextX, nextY, Value.AI_VALUE); //Cập nhật root
	    	}
	    }
	    
	    public Cell alphaBeta(State state) {
	        State remState = new State(state.getState()); //Copy lại giá trị của state
	        heuristic.evaluateEachCell(remState, Value.AI_VALUE);
	        System.out.println("Bảng lượng giá các ô:");
	        heuristic.printEvalState(); //Hiển thị bảng lượng giá
	        ArrayList<EvalCell> list = heuristic.getOptimalList(); //Lấy danh sách những ô tối ưu để xét
	        System.out.println("List bước đi tối ưu:(Max:"+Value.MAX_NUM_OF_HIGHEST_CELL_LIST+")");
	        for (EvalCell evalCell : list) {
				System.out.println(evalCell.getX() + " " + evalCell.getY() + ":" + evalCell.getValue());
			}
	        int maxValue = Integer.MIN_VALUE;
	        int n = list.size();
	        ArrayList<EvalCell> ListChoose = new ArrayList<EvalCell>();
	        for (int i = 0; i < n; i++) { // giả sử chọn một trong các phần tử trong list để tìm kiếm bước đi tối ưu
	            // Giả sử AI đi ô này 
	        	remState.getState()[list.get(i).getX()][list.get(i).getY()] = Value.AI_VALUE;
	        	System.out.println("Với nước đi " + list.get(i).getX() + " " + list.get(i).getY() + ":");
	            int value = minValue(remState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	            System.out.println("Lượng giá trạng thái của bước đi này:" + value);
	            if (maxValue < value) {
	                maxValue = value;
	                ListChoose.clear();
	                ListChoose.add(list.get(i));
	            } else if (maxValue == value) ListChoose.add(list.get(i));
	            // reset lại trạng thái của ô này
	            remState.getState()[list.get(i).getX()][list.get(i).getY()] = 0;
	        }
	        n = ListChoose.size(); // lấy số phần tử có điểm cao nhất
	        int x = rand.nextInt(n); // chọn ra một
	        return ListChoose.get(x).getCell();
	    }

	    //Lượng giá cho Max(AI)
	    private int maxValue(State state, int alpha, int beta, int depth) {
	        if (depth >= Value.MAX_DEPTH || state.checkWinner(Value.AI_VALUE) || state.isOver()) {
	        	return heuristic.evaluateState(state);
	            
	        }
	        heuristic.evaluateEachCell(state, Value.AI_VALUE);
	        ArrayList<EvalCell> list = heuristic.getOptimalList();
	        for (EvalCell item : list) {
	            state.getState()[item.getCell().getX()][item.getCell().getY()] = Value.AI_VALUE;
	            alpha = Math.max(alpha, minValue(state, alpha, beta, depth + 1));
	            state.getState()[item.getCell().getX()][item.getCell().getY()] = 0;
	            if (alpha >= beta) {
	                break;
	            }
	        }
	        return alpha;
	    }
 
	    //Lượng giá cho Min(User)
	    private int minValue(State state, int alpha, int beta, int depth) {
	        if (depth >= Value.MAX_DEPTH || state.checkWinner(Value.USER_VALUE) || state.isOver()) {
	        	return heuristic.evaluateState(state);
	        }
	        heuristic.evaluateEachCell(state, Value.USER_VALUE);
	        ArrayList<EvalCell> list = heuristic.getOptimalList();
	        for (EvalCell item : list) {
	            state.getState()[item.getCell().getX()][item.getCell().getY()] = Value.USER_VALUE;
	            beta = Math.min(beta, maxValue(state, alpha, beta, depth + 1));
	            state.getState()[item.getCell().getX()][item.getCell().getY()] = 0;
	            if (alpha >= beta) {
	                break;
	            }
	        }
	        return beta;
	    }
	    
}
