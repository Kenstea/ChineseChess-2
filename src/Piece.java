
public class Piece implements Cloneable {
	
	public static final char ROOK_TYPE='j';
	public static final char KNIGHT_TYPE='m';
	public static final char CANNON_TYPE='p';
	public static final char BISHOP_TYPE='x';
	public static final char ADVISOR_TYPE='s';
	public static final char PAWN_TYPE='z';
	public static final char KING_TYPE='b';

	public static final char RED = 'r';
	public static final char BLACK = 'b';
	
    public String key;
    public char color;
    public char character;
    public char index;
    public int[] position = new int[2];

    public Piece(String name, int[] position) {
        this.key = name;
        this.color = name.charAt(0);
        this.character = name.charAt(1);
        this.index = name.charAt(2);
        this.position = position;
    }

    public String getPieceName(){
    	
    	StringBuffer name = new StringBuffer();
    	
    	/*switch(color){
    	case RED: 	name.append("紅");
    				break;
    	case BLACK:	name.append("黑");
    				break;
    	}*/
    	switch(character){
    	case ROOK_TYPE:		name.append("車");
    						break;
    	case KNIGHT_TYPE:	name.append("馬");
							break;
    	case ADVISOR_TYPE:	if (color==BLACK)
    							name.append("士");
    						else name.append("仕");
							break;
    	case BISHOP_TYPE:	if (color==BLACK)
								name.append("象");
    						else name.append("相");
    						break;
    	case KING_TYPE:		if (color==BLACK)
    							name.append("將");
							else name.append("帥");
							break;			
    	case PAWN_TYPE:		if (color==BLACK)
								name.append("卒");
							else name.append("兵");
							break;			
    	case CANNON_TYPE:	if (color==BLACK)
							name.append("炮");
							else name.append("砲");
							break;			
    	}
		return name.toString();
    }
    
    public String toString(){
    	return this.getPieceName();
    }
    
}