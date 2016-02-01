
public class Node implements Comparable<Node>{
    public String piece;
    public int[] from;
    public int[] to;
    public int value;
    public Node(String piece, int[] from, int[] to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

	@Override
	public int compareTo(Node o) {
		return Evaluate.getValue(o.piece.charAt(1))>Evaluate.getValue(piece.charAt(1))?1:-1;
	}
	
	@Override
	public String toString(){
		return piece;
	}

}
