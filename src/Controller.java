import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Controller {
	Board board=null;
    private HashMap<String, Piece> initPieces() {
    	HashMap<String, Piece> pieces = new HashMap<String, Piece>();
        pieces.put("bj0", new Piece("bj0", new int[]{0, 0}));
        pieces.put("bm0", new Piece("bm0", new int[]{0, 1}));
        pieces.put("bx0", new Piece("bx0", new int[]{0, 2}));
        pieces.put("bs0", new Piece("bs0", new int[]{0, 3}));
        pieces.put("bb0", new Piece("bb0", new int[]{0, 4}));
        pieces.put("bs1", new Piece("bs1", new int[]{0, 5}));
        pieces.put("bx1", new Piece("bx1", new int[]{0, 6}));
        pieces.put("bm1", new Piece("bm1", new int[]{0, 7}));
        pieces.put("bj1", new Piece("bj1", new int[]{0, 8}));
        pieces.put("bp0", new Piece("bp0", new int[]{2, 1}));
        pieces.put("bp1", new Piece("bp1", new int[]{2, 7}));
        pieces.put("bz0", new Piece("bz0", new int[]{3, 0}));
        pieces.put("bz1", new Piece("bz1", new int[]{3, 2}));
        pieces.put("bz2", new Piece("bz2", new int[]{3, 4}));
        pieces.put("bz3", new Piece("bz3", new int[]{3, 6}));
        pieces.put("bz4", new Piece("bz4", new int[]{3, 8}));

        pieces.put("rj0", new Piece("rj0", new int[]{9, 0}));
        pieces.put("rm0", new Piece("rm0", new int[]{9, 1}));
        pieces.put("rx0", new Piece("rx0", new int[]{9, 2}));
        pieces.put("rs0", new Piece("rs0", new int[]{9, 3}));
        pieces.put("rb0", new Piece("rb0", new int[]{9, 4}));
        pieces.put("rs1", new Piece("rs1", new int[]{9, 5}));
        pieces.put("rx1", new Piece("rx1", new int[]{9, 6}));
        pieces.put("rm1", new Piece("rm1", new int[]{9, 7}));
        pieces.put("rj1", new Piece("rj1", new int[]{9, 8}));
        pieces.put("rp0", new Piece("rp0", new int[]{7, 1}));
        pieces.put("rp1", new Piece("rp1", new int[]{7, 7}));
        pieces.put("rz0", new Piece("rz0", new int[]{6, 0}));
        pieces.put("rz1", new Piece("rz1", new int[]{6, 2}));
        pieces.put("rz2", new Piece("rz2", new int[]{6, 4}));
        pieces.put("rz3", new Piece("rz3", new int[]{6, 6}));
        pieces.put("rz4", new Piece("rz4", new int[]{6, 8}));
        return pieces;
    }
    
    private Board initBoard() {
        board = new Board();
        board.pieces = initPieces();
        for (String key : board.pieces.keySet()) 
        		board.setPiece(board.pieces.get(key));
        return board;
    }
    
    public Board init() {
        initPieces();
        return initBoard();
    }
    
    public int[] from;
    public Piece[] responseMoveChess(Board board, char color) {
        Search searchModel = new Search();
        Node result = searchModel.bestMove(board, color);
        Piece caputredPiece = board.move(result.piece, result.to);
        from = result.from;
        return new Piece[]{board.pieces.get(result.piece), caputredPiece};
    }    
    
    public void printBoard(Board board) {
    	String strBoard[][] = new String[10][9];
    	StringBuffer s = new StringBuffer();
    	for(int i=0;i<strBoard.length;i++){
    		for(int j=0;j<strBoard[i].length;j++){
				if((i==4||i==5)){
					strBoard[i][j]="----";						
				}else if((j==3||j==4||j==5)&&(i==0||i==1||i==2||i==7||i==8||i==9)){
					strBoard[i][j]="x";
				}
				else strBoard[i][j]=".";
    		}
    	}
    	
        for (String key : board.pieces.keySet()) {
            Piece piece = board.pieces.get(key);
            if(piece.color==Piece.RED)
            	strBoard[piece.position[0]][piece.position[1]]="<"+piece.getPieceName()+">";
            else strBoard[piece.position[0]][piece.position[1]]="("+piece.getPieceName()+")";
        }
        
        for(int i=0;i<=strBoard[0].length;i++){
        	if(i==0){
        		s.append("\t");
        	}else{
        		s.append(i+"\t");
        	}
        }s.append("\n");
        
    	for(int i=0;i<strBoard.length;i++){
    		for(int j=0;j<strBoard[i].length;j++){
    			if(j==0){
    				s.append((char) (i+'A')+"\t");
    			}
    			s.append(strBoard[i][j]+"\t");			
    		}s.append("\n");
    	}
    	System.out.println(s);
    }
    
    public boolean isLegal(Piece p, int[]from, int[]to){
    	ArrayList<int[]> legalMoves=Rules.getPossibleMoves(p.key, p.position, board);
    		for(int[] coor : legalMoves){
    			if(Arrays.equals(coor, to)){
    				return true;
    			}
    		}
    	return false;
    }
    //Piece.RED means red win; Piece.BLACK means black win; 'x' means game continue
    public char getState(Board board) {
        boolean isRedWin = board.pieces.get("bb0") == null;
        boolean isBlackWin = board.pieces.get("rb0") == null;
        if (isRedWin) return Piece.RED;
        else if (isBlackWin) return Piece.BLACK;
        else return 'x';
    }
    
}
