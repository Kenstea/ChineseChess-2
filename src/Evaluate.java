
public class Evaluate {

    private static final int KING_INDEX = 0;
    private static final int ADVISOR_INDEX = 1;
    private static final int BISHOP_INDEX = 2;
    private static final int KNIGHT_INDEX = 3;
    private static final int ROOK_INDEX = 4;
    private static final int CANNON_INDEX = 5;
    private static final int PAWN_INDEX = 6;
    
    private static final int RED_INDEX = 0;
    private static final int BLACK_INDEX = 1;
    
    private static int mobilityWeighting(int p){
    	int mobility[]={0,1,1,13,7,7,15};
    	return mobility[p];
    }
    
    private static int[] strength(Board board){
    	int[] strength = new int[2];
    	int pieceIndex;
    	int colorIndex;
    	Piece piece;
        for (String key : board.pieces.keySet()) {
        	piece = board.pieces.get(key);
            pieceIndex=getPiecePos(piece.character);
            colorIndex = (piece.color == Piece.RED)?RED_INDEX:BLACK_INDEX;
            strength[colorIndex]+=pieceValue(pieceIndex);
        }
        return strength;
    }
    
    private static int[] material(Board board){
    	int[] material = new int[2];
    	int pieceIndex;
    	int colorIndex;
    	int[] movePos;
    	Piece piece;
        for (String key : board.pieces.keySet()) {
        	piece = board.pieces.get(key);
            pieceIndex=getPiecePos(piece.character);
            colorIndex = (piece.color == Piece.RED)?RED_INDEX:BLACK_INDEX;
            movePos = (piece.color == Piece.RED)?piece.position:mirror(piece.position);
            material[colorIndex]+=evaluatePositionValue(pieceIndex,movePos);
        }
    	return material;
    }
    
    private static int[] mobility(Board board){
    	int[] mobility = new int[2];
    	int pieceIndex;
    	int colorIndex;
    	Piece piece;
        for (String key : board.pieces.keySet()) {
        	piece = board.pieces.get(key);
        	if(piece.character==Piece.CANNON_TYPE||piece.character==Piece.PAWN_TYPE||piece.character==Piece.ROOK_TYPE||piece.character==Piece.KNIGHT_TYPE){
        	//Mobility only applies to rook, knight, cannon and pawn
            pieceIndex=getPiecePos(piece.character);
            colorIndex = (piece.color == Piece.RED)?RED_INDEX:BLACK_INDEX;
        	mobility[colorIndex]+=mobilityWeighting(pieceIndex)*Rules.getPossibleMoves(piece.key, piece.position, board).size();
        	}
        }
    	return mobility;
    }
    
    private static int[] control(Board board){
    	int[] control = new int[2];
    	int pieceIndex;
    	int colorIndex;
    	Piece piece;
        for (String key : board.pieces.keySet()) {
        	piece = board.pieces.get(key);
        	if(piece.character==Piece.CANNON_TYPE||piece.character==Piece.KNIGHT_TYPE||piece.character==Piece.ROOK_TYPE){
	            pieceIndex=getPiecePos(piece.character);
	            colorIndex = (piece.color == Piece.RED)?RED_INDEX:BLACK_INDEX;
	            for(int[] pos :Rules.getPossibleMoves(piece.key, piece.position, board)){
	            	if(board.getPiece(pos)!=null&&board.getPiece(pos).color!=piece.color)
	            		control[colorIndex]+=pieceValue(pieceIndex)/10;
	            }
        	}
        }
    	return control;
    }
    
    //Evaluating different king of threats, including rook, pawn, knight and pawn
    //by finding its position
    private static int[] threat(Board board){
    	int[] threat = new int[2];
    	int colorIndex;
    	Piece piece;
        for (String key : board.pieces.keySet()) {
        	piece = board.pieces.get(key);
            colorIndex = (piece.color == Piece.RED)?RED_INDEX:BLACK_INDEX;
            if(piece.equals(Piece.ROOK_TYPE)&&halfwayPiece(piece))
            	threat[colorIndex]+=30;
            if(piece.equals(Piece.CANNON_TYPE)&&bottomCannon(piece))
            	threat[colorIndex]+=35;
            if(piece.equals(Piece.PAWN_TYPE)&&halfwayPiece(piece))
            	threat[colorIndex]+=10;
            if(piece.equals(Piece.KNIGHT_TYPE)&&halfwayPiece(piece))
            	threat[colorIndex]+=40;
        }
    	return threat;
    }
    
    private static boolean bottomCannon(Piece piece) {
		return (piece.position[0]==9)?true:false;
	}

	private static boolean halfwayPiece(Piece p){
    	return (p.position[0]>5)?true:false;
    }
    
    public static int evaluate(Board board, char player) {
    	
    	int[]strength = new int[2];
    	int[]material = new int[2];
    	int[]mobility = new int[2];
    	int[]threat = new int[2];
    	int[]control = new int[2];
        int redValue=0;
        int blackValue=0;
    	
        //Step1: Getting evaluated piece value and position value from tables
        strength=strength(board);
        //Step2: Getting position piece value from tables
        material=material(board);
        //Step3: Getting mobility value from pieces
        mobility=mobility(board);
        //Step4: Getting threat value from pieces
        threat=threat(board);
        //Step5: Getting control value from pieces
        control=control(board);
        //Final step: assign all evaluated values to red and black players
        //Weighting of strength, material, mobility and treats are assigned in 4:3:2:1
        redValue=6*strength[RED_INDEX]+4*material[RED_INDEX]+4*threat[RED_INDEX]+1*mobility[RED_INDEX]+1*control[RED_INDEX];
        blackValue=6*strength[BLACK_INDEX]+4*material[BLACK_INDEX]+4*threat[BLACK_INDEX]+1*mobility[BLACK_INDEX]+1*control[BLACK_INDEX];
        
        return ((player==Piece.RED)?redValue-blackValue:blackValue-redValue);
        
    }
    
    private static int getPiecePos(char p){
    	switch(p){
    	case Piece.KING_TYPE: return KING_INDEX;
    	case Piece.ADVISOR_TYPE: return ADVISOR_INDEX;
    	case Piece.BISHOP_TYPE: return BISHOP_INDEX;
    	case Piece.KNIGHT_TYPE: return KNIGHT_INDEX;
    	case Piece.ROOK_TYPE: return ROOK_INDEX;
    	case Piece.CANNON_TYPE: return CANNON_INDEX;
    	case Piece.PAWN_TYPE: return PAWN_INDEX;
    	default: return -1;
    	}
    }

    private static int[] mirror(int[] position){
    	return new int[]{Board.BOARD_HEIGHT - 1 - position[0], position[1]};
    }
    
    /* king | advisor | bishop | knight | rook | cannon | pawn*/
    private static int pieceValue(int p) {
        int[] pieceValue = new int[]{1000000, 110, 110, 300, 600, 500, 70};
        return pieceValue[p];
    }
    
    public static int getValue(char p){
    	return pieceValue(getPiecePos(p));
    }
    
    private static int evaluatePositionValue(int p,int[] pos){
	 int cucvlKingPawnMidgameAttackless[][] = {
	  { 9,  9,  9, 11, 13, 11,  9,  9,  9},
	  {19, 24, 34, 42, 44, 42, 34, 24, 19},
	  {19, 24, 32, 37, 37, 37, 32, 24, 19},
	  {19, 23, 27, 29, 30, 29, 27, 23, 19},
	  {14, 18, 20, 27, 29, 27, 20, 18, 14},
	  { 7,  0, 13,  0, 16,  0, 13,  0,  7},
	  { 7,  0,  7,  0, 15,  0,  7,  0,  7},
	  { 0,  0,  0,  1,  1,  1,  0,  0,  0},
	  { 0,  0,  0,  2,  2,  2,  0,  0,  0},
	  { 0,  0,  0, 11, 15, 11,  0,  0,  0}
	};

	  int cucvlAdvisorBishopThreatless[][] = {
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  { 0,  0, 20,  0,  0,  0, 20,  0,  0},
	  { 0,  0,  0,  0,  0,  0,  0,  0,  0},
	  {18,  0,  0, 20, 23, 20,  0,  0, 18},
	  { 0,  0,  0,  0, 23,  0,  0,  0,  0},
	  { 0,  0, 20, 20,  0, 20, 20,  0,  0}
	};

	  int cucvlKnightMidgame[][] = {
	  {90, 90, 90, 96, 90, 96, 90, 90, 90},
	  {90, 96,103, 97, 94, 97,103, 96, 90},
	  {92, 98, 99,103, 99,103, 99, 98, 92},
	  {93,108,100,107,100,107,100,108, 93},
	  {90,100, 99,103,104,103, 99,100, 90},
	  {90, 98,101,102,103,102,101, 98, 90},
	  {92, 94, 98, 95, 98, 95, 98, 94, 92},
	  {93, 92, 94, 95, 92, 95, 94, 92, 93},
	  {85, 90, 92, 93, 78, 93, 92, 90, 85},
	  {88, 85, 90, 88, 90, 88, 90, 85, 88}
	};

	 int cucvlRookMidgame[][] = {
	  {206,208,207,213,214,213,207,208,206},
	  {206,212,209,216,233,216,209,212,206},
	  {206,208,207,214,216,214,207,208,206},
	  {206,213,213,216,216,216,213,213,206},
	  {208,211,211,214,215,214,211,211,208},
	  {208,212,212,214,215,214,212,212,208},
	  {204,209,204,212,214,212,204,209,204},
	  {198,208,204,212,212,212,204,208,198},
	  {200,208,206,212,200,212,206,208,200},
	  {194,206,204,212,200,212,204,206,194}
	};

	int cucvlCannonMidgame[][] = {
	  {100,100, 96, 91, 90, 91, 96,100,100},
	  { 98, 98, 96, 92, 89, 92, 96, 98, 98},
	  { 97, 97, 96, 91, 92, 91, 96, 97, 97},
	  { 96, 99, 99, 98,100, 98, 99, 99, 96},
	  { 96, 96, 96, 96,100, 96, 96, 96, 96},
	  { 95, 96, 99, 96,100, 96, 99, 96, 95},
	  { 96, 96, 96, 96, 96, 96, 96, 96, 96},
	  { 97, 96,100, 99,101, 99,100, 96, 97},
	  { 96, 97, 98, 98, 98, 98, 98, 97, 96},
	  { 96, 96, 97, 99, 99, 99, 97, 96, 96}
	};
	
	switch(p){
	case PAWN_INDEX: return cucvlKingPawnMidgameAttackless[pos[0]][pos[1]];
	case CANNON_INDEX: return cucvlCannonMidgame[pos[0]][pos[1]];
	case ROOK_INDEX: return cucvlRookMidgame[pos[0]][pos[1]];
	case KNIGHT_INDEX: return cucvlKnightMidgame[pos[0]][pos[1]];
	case BISHOP_INDEX: return cucvlAdvisorBishopThreatless[pos[0]][pos[1]];
	case ADVISOR_INDEX: return cucvlAdvisorBishopThreatless[pos[0]][pos[1]];
	case KING_INDEX: return cucvlKingPawnMidgameAttackless[pos[0]][pos[1]];
		default: return -1;
	}

    }
}