
import java.util.ArrayList;

public class Search{
	private static final int DEPTH_LIMIT=9;
    private Board board;
    private Controller controller = new Controller();
    private static int count=0;
    //Search depth varies accordingly to number of pieces remained in chess board
    //Upper limit of search depth would be 9
    private int searchDepth(Board board){
    	int depth = (int)(-0.11*board.pieces.size()+7.3);
    	return Math.min(DEPTH_LIMIT,depth);
    }
    
    public Node bestMove(Board board, char rootColor) {
    	count=0;
    	int depth = searchDepth(board);
    	int alpha = Integer.MIN_VALUE;
    	int beta = Integer.MAX_VALUE;
        this.board = board;
        Node best = null;
        ArrayList<Node> moves = generateAllMoves(rootColor);
        
        for (Node node : moves) {
            /* Move*/
            Piece capturedPiece = board.move(node.piece, node.to);
            node.value = minimax(depth-1,swapColor(rootColor), rootColor, alpha, beta);
            /* Select a best move during searching to save time*/
            if (best == null || node.value >= best.value){
                best = node;
            }
            
            /* Undo move*/
            board.undo(board.pieces.get(node.piece), node.from, capturedPiece);
            count++;
        }
        System.out.println("Explored states: "+count);
        return best;
    }

    //minimax procedure with alpha-beta pruning
    private int minimax(int depth,char currentColor, final char rootColor, int alpha, int beta) {
        /* Return evaluation if reaching leaf node or any side won.*/
        if (depth == 0 || controller.getState(board) != 'x'){            
        	count++;
            return Evaluate.evaluate(board, ((rootColor==Piece.BLACK)?Piece.BLACK:Piece.RED));
        }
        ArrayList<Node> moves = generateAllMoves(currentColor);
        
        	for (Node n : moves) {
        		//Update state
                Piece capturedPiece = board.move(n.piece, n.to);
                if (rootColor==currentColor) alpha = Math.max(alpha, minimax(depth - 1,swapColor(currentColor),rootColor , alpha, beta));
                else beta = Math.min(beta, minimax(depth - 1,swapColor(currentColor),rootColor , alpha, beta));
                
                //Restore state
                board.undo(board.pieces.get(n.piece), n.from, capturedPiece);
                count++;

                /* Alpha-beta Pruning */
                if (alpha>=beta) break;
            }
        return rootColor==currentColor ? alpha : beta;
    }

    //Generating nodes for minimax search.
    //By applying heuristics of piece values, 
    //we sort the more valuable pieces to the front,
    //such that move nodes can be cut-off by alpha-beta pruning
    private ArrayList<Node> generateAllMoves(char color) {
        ArrayList<Node> moves = new ArrayList<Node>();
        for (String key : board.pieces.keySet()) {
            Piece piece = board.pieces.get(key);
            if (color==Piece.BLACK && piece.color == Piece.RED) continue;
            if (color==Piece.RED && piece.color == Piece.BLACK) continue;
            for (int[] nxt : Rules.getPossibleMoves(piece.key, piece.position, board))
                moves.add(new Node(piece.key, piece.position, nxt));
        }
        shellsort(moves);
        return moves;
    }
    private char swapColor(char color){
    	return (color==Piece.BLACK)?Piece.RED:Piece.BLACK;
    }
		
	private static void shellsort( ArrayList<Node> a ){
	       for( int gap = a.size() / 2; gap > 0;
	                    gap = gap == 2 ? 1 : (int) ( gap / 2.2 ) )
	           for( int i = gap; i < a.size(); i++ ){
	               Node tmp = a.get(i);
	               int j = i;
	               for( ; j >= gap && tmp.compareTo( a.get(j-gap)) < 0; j -= gap )
	                   a.set(j, a.get(j - gap));
	               a.set(j,tmp);
	           }
	   }

}