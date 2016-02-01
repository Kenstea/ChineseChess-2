import java.util.HashMap;


public class Board{
    public static final int BOARD_WIDTH = 9, BOARD_HEIGHT = 10;
    public HashMap<String, Piece> pieces;
    public HashMap<String, Piece> capturedPieces = new HashMap<String, Piece>();
    public char player = Piece.RED;
    private Piece[][] cells = new Piece[BOARD_HEIGHT][BOARD_WIDTH];
    public boolean isInside(int[] position) {
        return isInside(position[0], position[1]);
    }

    public boolean isInside(int x, int y) {
        return !(x < 0 || x >= BOARD_HEIGHT
                || y < 0 || y >= BOARD_WIDTH);
    }
    
    public boolean isEmpty(int[] position) {
        return isEmpty(position[0], position[1]);
    }

    public boolean isEmpty(int x, int y) {
        return isInside(x, y) && cells[x][y] == null;
    }
    
    public void setPiece(Piece piece) {
        int[] pos = piece.position;
        cells[pos[0]][pos[1]] = piece;
        pieces.put(piece.key, piece);
        return;
    }
    
    public Piece move(String key, int[] newPos) {
        Piece orig = pieces.get(key);
        Piece inNewPos = getPiece(newPos);
        /* If the new slot has been taken by another piece, then it will be killed.*/
        if (inNewPos != null){
            pieces.remove(inNewPos.key);
            capturedPieces.get(inNewPos);
        }
        /* Clear original slot and updatePiece new slot.*/
        int[] origPos = orig.position;
        cells[origPos[0]][origPos[1]] = null;
        cells[newPos[0]][newPos[1]] = orig;
        orig.position = newPos;
        player = (player == Piece.RED) ? Piece.BLACK : Piece.RED;
        
        return inNewPos;
    }
    
    public void undo(Piece piece, int[] orgi, Piece capturedPiece){
    	move(piece.key,orgi);
    	if(capturedPiece!=null){
    		pieces.put(capturedPiece.key, capturedPiece);
            int[] origPos = pieces.get(capturedPiece.key).position;
            cells[origPos[0]][origPos[1]] = pieces.get(capturedPiece.key);    	
            }
    }

    public Piece getPiece(int[] pos) {
        return getPiece(pos[0], pos[1]);
    }

    public Piece getPiece(int x, int y) {
        return cells[x][y];
    }
    
}
