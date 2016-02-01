import java.util.LinkedList;


public class Record {
	private LinkedList<Piece> history;
	private LinkedList<Piece> capturePiece;
	private LinkedList<int[]> orgPosition;
	public Record(){
		this.history = new LinkedList<Piece>();
		this.capturePiece=new LinkedList<Piece>();
		this.orgPosition = new LinkedList<int[]>();
	}
	
	public void recordAll(Piece piece, int from[], Piece capturedPiece){
		history.add(piece);
        orgPosition.add(from);
        if(capturedPiece!=null) capturePiece.add(capturedPiece);
	}

	public String viewCapturePieces() {
		return this.capturePiece.toString();
	}

	public LinkedList<Piece> getHistory() {
		return this.history;
	}

	public LinkedList<Piece> getCapturePieces() {
		return this.capturePiece;
	}
	
	public LinkedList<int[]> getOrgPosition() {
		return this.orgPosition;
	}
	
}
