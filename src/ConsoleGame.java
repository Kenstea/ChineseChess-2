import java.util.Arrays;
import java.util.Scanner;

public class ConsoleGame {
    private Controller controller;
    private Board board;
    private char currentColor;
    private char playerColor, computerColor;
	private Scanner scanner = new Scanner(System.in);
	private String consoleInput;
	private Record record;
    public ConsoleGame() {
        this.controller = new Controller();
        this.board=controller.init();
        this.currentColor=Piece.RED;
        this.playerColor=Piece.RED;
        this.computerColor=Piece.BLACK;
        this.start();
    }
	
	private void start() {
		System.out.println("Artificial Intelligence Mini-project: Chinese Chess Program");
		boolean end=false;
		while(!end){
			this.record=new Record();
			System.out.println("Game start, enter \"help\" for more information");
			System.out.println("For switching the color you are playing, enter command \"swap\" in the console.");
			controller.printBoard(board);
			while(controller.getState(board)=='x'){
				if(playerColor == currentColor){
					System.out.print(((Piece.RED==currentColor)?"red":"black")+"("+(record.getHistory().size()+1)+"): ");
					//Waiting user input
					consoleInput = scanner.nextLine();
					switch(consoleInput){
					case "help":	{
						System.out.println("Instruction: Please input starting and ending coordinate in each move, ");
						System.out.println("defined by an alphabet and number in \"START,END\".");
						System.out.println("For example, \"A1,C1\" means moving a piece from A1 to C1.");
						System.out.println("Commands:\t");
						System.out.println("\tcapture: Display captured pieces");
						System.out.println("\tdisplay: Display chess board");
						System.out.println("\thistory: Display previous moves");
						System.out.println("\thelp:    Display avaliable options");
						System.out.println("\tquit:    Quit game");
						System.out.println("\trestart: Restart game");
						System.out.println("\tswap:    Swap player pieces");
						System.out.println("\tundo:    Undo move");
						System.out.println("\t<move>:  A move with the format <letter><number>,<letter><number>");
						continue;
					}
					case "capture": {
						System.out.println(record.viewCapturePieces());
						continue;
					}
					case "history": {
						StringBuffer s = new StringBuffer();
						int[] from;
						int[] to ;
						Piece piece;
						for(int i=0;i<record.getOrgPosition().size();i++){
							piece = record.getHistory().get(i);
							from = record.getOrgPosition().get(i);
							to = record.getHistory().get(i).position;
							s.append("Move "+(i+1)+": "+piece.getPieceName()+"("+ptToCoor(from)+","+ptToCoor(to)+"), ");
							if(i%4==3) s.append("\n");
						}
						System.out.println(s.toString());
						continue;
					}
					case "display":	{
						controller.printBoard(board);
						continue;
					}	
					case "restart": {
						this.board=controller.init();
						this.currentColor=Piece.RED;
						this.record=new Record();
						System.out.println("Game restart");
						controller.printBoard(board);
						continue;
					}
					case "swap":{
						playerColor=computerColor;
						computerColor=currentColor;
						continue;
					}
					case "quit":{
						System.out.println("Thank you for playing, see you again!");
						System.exit(0);
					}
					case "undo":	{
						Piece piece = null,capturedPiece = null;
						int[] orgi = null;
						if(playerColor==Piece.BLACK&&record.getHistory().size()==1) continue;
						for(int i=0;i<2;i++){
							if(record.getHistory().size()>0){
								piece =record.getHistory().pollLast();
								orgi = record.getOrgPosition().pollLast();
							}
							if(record.getCapturePieces().size()>0){
								if(Arrays.equals(piece.position, record.getCapturePieces().peekLast().position)){
									capturedPiece = record.getCapturePieces().pollLast();
								}
							}
							board.undo(piece, orgi, capturedPiece);

						}
						controller.printBoard(board);
						continue;
					}	
					default:	{
						if(consoleInput.matches("([a-j]|[A-J])([1-9]),([a-j]|[A-J])([1-9])")){
							int[] from = coorToPt(consoleInput.split(",")[0]);
							int[] to = coorToPt(consoleInput.split(",")[1]);
					        Piece piece = board.getPiece(from[0],from[1]);
					        if(piece!=null&&currentColor==piece.color&&controller.isLegal(piece,from,to)){
						        Piece capturedPiece = board.move(piece.key, to);			       
						        record.recordAll(piece, from, capturedPiece);
					        }else{
					        	System.out.println("Invalid move");
					        	continue;
					        }
						}else{
							System.out.println("Incorrect argument!");
							continue;
						}
					}
				}//end of switch
			}// end of if options from human
			else{ //Robot moves
				System.out.println("Computer is thinking...");
		        Piece[] pieces = controller.responseMoveChess(board,computerColor);
		        record.recordAll(pieces[0], controller.from, pieces[1]);
				System.out.print(((Piece.RED==currentColor)?"red":"black")+ "("+(record.getHistory().size())+"): ");
		        System.out.println(pieces[0]+"("+ptToCoor(controller.from)+","+ptToCoor(pieces[0].position)+")");
			}
			controller.printBoard(board);

			currentColor=(currentColor==Piece.RED)?Piece.BLACK:Piece.RED;
			}//end of while loop of a move
			
			if(controller.getState(board)==Piece.RED){
				System.out.println("Red wins, game ended");
			}else{
				System.out.println("Black wins, game ended");			
			}
			
			System.out.println("Would you like to restrat the game? (y/n)");
			System.out.print("Options: ");
			consoleInput = scanner.nextLine();
			switch(consoleInput){
			case "y": 	{
				this.board=controller.init();
				playerColor=Piece.RED;
				System.out.println("Game restart");
				controller.printBoard(board);
				break;
			}
			case "n":	{
				end=true;
				break;
			}
			}
		}// end of game loop;
		scanner=null;
		System.out.println("Thank you for playing, see you again!");
		return;
	}// end of start function

	public static void main(String[] args) {
		new ConsoleGame();
	}

	public int[] coorToPt(String coordinate){
		char upperChar = Character.toUpperCase(coordinate.charAt(0));
		int x = (int)(upperChar)-64;
		int y = Integer.parseInt(coordinate.substring(1));
		return new int[]{x-1,y-1};
	}
	
	public String ptToCoor(int[] point){
		
		int x=point[0];
		int y=point[1];
		char c = 0;
	    switch(x){
	       case 0 : c='A'; break;
	       case 1 : c='B'; break;
	       case 2 : c='C'; break;
	       case 3 : c='D'; break;
	       case 4 : c='E'; break;
	       case 5 : c='F'; break;
	       case 6 : c='G'; break;
	       case 7 : c='H'; break;
	       case 8 : c='I'; break;
	       case 9 : c='J'; break;
	     }
		String coor = String.valueOf(c)+(y+1);
		
		return coor;
	}
}
