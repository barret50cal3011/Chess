import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.Math;

import java.util.ArrayList;

public class Chess extends JFrame implements ActionListener
{
	private static final String RESTART = "restart";

	private JPanel sPanel;
	private JPanel boardPanel;

	private JLabel turn;
	private JPanel nPanel;

	private JButton restart;
	private JButton[][] buttonBoard;

	private boolean pieceSelected;
	private int xMove;
	private int yMove;

	private Board board;

	public Chess() 
	{
		super("Chess");
		board = new Board();

		setSize(new Dimension(700, 750));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		sPanel = new JPanel();

		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(8, 8));

		restart = new JButton("restart game");
		restart.setActionCommand(RESTART);
		restart.addActionListener(this);
		sPanel.add(restart);

		nPanel = new JPanel();
		turn = new JLabel(Piece.WHITE);
		nPanel.add(turn);

		buttonBoard = new JButton[8][8];
		for(int i = 0; i < buttonBoard.length; i++)
		{
			for(int j = 0; j < buttonBoard[i].length; j++)
			{
				String buttonPiece = "";
				if(board.getBoxAt(i,j).getPiece() != null)
				{
					buttonPiece = board.getBoxAt(i, j).getPiece().toString();
				}
				buttonBoard[i][j] = new JButton(buttonPiece);
				buttonBoard[i][j].setActionCommand(i + "-" + j);
				buttonBoard[i][j].addActionListener(this);
				boardPanel.add(buttonBoard[i][j]);

			}
		}

		add(sPanel, BorderLayout.SOUTH);
		add(nPanel, BorderLayout.NORTH);
		add(boardPanel, BorderLayout.CENTER);

		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command.equals(RESTART))
		{
			board = new Board();
			for(int i = 0; i < buttonBoard.length; i++)
			{
				for(int j = 0; j < buttonBoard[i].length; j++)
				{
					String buttonPiece = "";
					if(board.getBoxAt(i,j).getPiece() != null)
					{
						buttonPiece = board.getBoxAt(i, j).getPiece().toString();
					}
					buttonBoard[i][j].setText(buttonPiece);
				}
			}
			turn.setText(Piece.WHITE);

		}else
		{
			if(pieceSelected)
			{
				int moveToX = 0;
				int moveToY = 0;
				pieceSelected = false;

				String strCordinates[] = command.split("-");
				try{
					moveToY = Integer.parseInt(strCordinates[0]);
					moveToX = Integer.parseInt(strCordinates[1]);
				}catch(NumberFormatException e){e.printStackTrace();}

				try{
					board.movePiece(xMove, yMove, moveToX, moveToY);

					String buttonText = board.getBoxAt(moveToY, moveToX).getPiece().toString();

					buttonBoard[moveToY][moveToX].setText(buttonText);
					buttonBoard[yMove][xMove].setText("");

					if(board.castle())
					{
						if(board.getBoxAt(moveToY, moveToX).getPiece().getColor().equals(Piece.BLACK))
						{
							if(moveToX == 6)
							{
								buttonBoard[0][5].setText(new Tower(Piece.BLACK, 5, 0).toString());
								buttonBoard[0][7].setText("");
							}else if(moveToX == 1)
							{
								buttonBoard[0][2].setText(new Tower(Piece.BLACK, 2, 0).toString());
								buttonBoard[0][0].setText("");
							}
						}else if(board.getBoxAt(moveToY, moveToX).getPiece().getColor().equals(Piece.WHITE))
						{
							if(moveToX == 6)
							{
								buttonBoard[7][5].setText(new Tower(Piece.WHITE, 5, 7).toString());
								buttonBoard[7][7].setText("");
							}else if(moveToX == 1)
							{
								buttonBoard[7][2].setText(new Tower(Piece.WHITE, 2, 7).toString());
								buttonBoard[7][0].setText("");
							}
						}
						board.setCastle(false);
					}

					if(board.promote())
					{
						String[] pieces = 
						{
							Tower.TOWER,
							Knight.KNIGHT,
							Bishop.BISHOP,
							Queen.QUEEN
						};

						String selectedPiece = (String)JOptionPane.showInputDialog(
							this,
							"To what piece do you want to promote?",
							"Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							pieces,
							Tower.TOWER
						);

						if(board.getBoxAt(moveToY, moveToX).getPiece().getColor().equals(Piece.BLACK))
						{
							if(selectedPiece.equals(Tower.TOWER))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Tower(Piece.BLACK, moveToX, moveToY));
							}else if(selectedPiece.equals(Knight.KNIGHT))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Knight(Piece.BLACK, moveToX, moveToY));
							}else if(selectedPiece.equals(Bishop.BISHOP))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Bishop(Piece.BLACK, moveToX, moveToY));
							}else if(selectedPiece.equals(Queen.QUEEN))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Queen(Piece.BLACK, moveToX, moveToY));
							}
						}else if(board.getBoxAt(moveToY, moveToX).getPiece().getColor().equals(Piece.WHITE))
						{
							if(selectedPiece.equals(Tower.TOWER))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Tower(Piece.WHITE, moveToX, moveToY));
							}else if(selectedPiece.equals(Knight.KNIGHT))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Knight(Piece.WHITE, moveToX, moveToY));
							}else if(selectedPiece.equals(Bishop.BISHOP))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Bishop(Piece.WHITE, moveToX, moveToY));
							}else if(selectedPiece.equals(Queen.QUEEN))
							{
								board.getBoxAt(moveToY, moveToX).setPiece(new Queen(Piece.WHITE, moveToX, moveToY));
							}
						}

						Piece p = board.getBoxAt(moveToY, moveToX).getPiece();
						buttonBoard[moveToY][moveToX].setText(p.getColor() + " " + p.getType());
						board.setPromote(false);
					}

					if(board.getCheck())
					{
						JOptionPane.showMessageDialog(
							this,
							"Check!",
							"",
							JOptionPane.INFORMATION_MESSAGE
						);
						board.setCheck(false);
					}

					if(turn.getText().equals(Piece.BLACK)){turn.setText(Piece.WHITE);}
					else if(turn.getText().equals(Piece.WHITE)){turn.setText(Piece.BLACK);}
				}catch(MovementException e)
				{
					pieceSelected = true;
					xMove = moveToX;
					yMove = moveToY;
				}catch(IligalMovementException e)
				{
					JOptionPane.showMessageDialog(
						this,
						e.getMessage(),
						"Movement Error!",
						JOptionPane.ERROR_MESSAGE
					);
				}catch(CheckException e)
				{
					JOptionPane.showMessageDialog(
						this,
						e.getMessage(),
						"Check!",
						JOptionPane.INFORMATION_MESSAGE
					);
				}
			}else
			{
				String strCordinates[] = command.split("-");
				try{
					yMove = Integer.parseInt(strCordinates[0]);
					xMove = Integer.parseInt(strCordinates[1]);
				}catch(NumberFormatException e){e.printStackTrace();}
				Piece p = board.getBoxAt(yMove, xMove).getPiece();

				if(p != null && p.getColor().equals(turn.getText()))
				{
					pieceSelected = true;
				}
				else if (p == null) {System.out.println("No piece selected");}
				else 
				{
					JOptionPane.showMessageDialog(
						this,
						"It is not your turn",
						"",
						JOptionPane.INFORMATION_MESSAGE
					);
				}
			}

		}
	}

	public static void main(String[] args)
	{
		new Chess();
	}
}

//***************************************************
//Mundo
//***************************************************
class Board
{
	private Box[][] board;

	private boolean castle;
	private boolean check;

	private boolean promote;

	private int whiteX;
	private int whiteY;
	private int blackX;
	private int blackY;

	public Board()
	{
		board = new Box[8][8];
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				board[i][j] = new Box();
				int ij = i + j;
				if((ij % 2) == 0)
				{
					board[i][j].setColor(Box.WHITE);
				}else 
				{
					board[i][j].setColor(Box.BLACK);
				}
			}
		}

		board[0][7].setPiece(new Tower(Piece.WHITE, 0, 7));
		board[1][7].setPiece(new Knight(Piece.WHITE, 1, 7));
		board[2][7].setPiece(new Bishop(Piece.WHITE, 2, 7));
		board[3][7].setPiece(new Queen(Piece.WHITE, 3, 7));
		board[4][7].setPiece(new King(Piece.WHITE, 4, 7));
		board[5][7].setPiece(new Bishop(Piece.WHITE, 5, 7));
		board[6][7].setPiece(new Knight(Piece.WHITE, 6, 7));
		board[7][7].setPiece(new Tower(Piece.WHITE, 7, 7));

		board[0][6].setPiece(new Pawn(Piece.WHITE, 0, 6));
		board[1][6].setPiece(new Pawn(Piece.WHITE, 1, 6));
		board[2][6].setPiece(new Pawn(Piece.WHITE, 2, 6));
		board[3][6].setPiece(new Pawn(Piece.WHITE, 3, 6));
		board[4][6].setPiece(new Pawn(Piece.WHITE, 4, 6));
		board[5][6].setPiece(new Pawn(Piece.WHITE, 5, 6));
		board[6][6].setPiece(new Pawn(Piece.WHITE, 6, 6));
		board[7][6].setPiece(new Pawn(Piece.WHITE, 7, 6));

		board[0][1].setPiece(new Pawn(Piece.BLACK, 0, 1));
		board[1][1].setPiece(new Pawn(Piece.BLACK, 1, 1));
		board[2][1].setPiece(new Pawn(Piece.BLACK, 2, 1));
		board[3][1].setPiece(new Pawn(Piece.BLACK, 3, 1));
		board[4][1].setPiece(new Pawn(Piece.BLACK, 4, 1));
		board[5][1].setPiece(new Pawn(Piece.BLACK, 5, 1));
		board[6][1].setPiece(new Pawn(Piece.BLACK, 6, 1));
		board[7][1].setPiece(new Pawn(Piece.BLACK, 7, 1));

		board[0][0].setPiece(new Tower(Piece.BLACK, 0, 0));
		board[1][0].setPiece(new Knight(Piece.BLACK, 1, 0));
		board[2][0].setPiece(new Bishop(Piece.BLACK, 2, 0));
		board[3][0].setPiece(new Queen(Piece.BLACK, 3, 0));
		board[4][0].setPiece(new King(Piece.BLACK, 4, 0));
		board[5][0].setPiece(new Bishop(Piece.BLACK, 5, 0));
		board[6][0].setPiece(new Knight(Piece.BLACK, 6, 0));
		board[7][0].setPiece(new Tower(Piece.BLACK, 7, 0));

		whiteX = 4;
		whiteY = 7;
		blackX = 4;
		blackY = 0;

		calculateCheckBoxes();
	}

	public Box getBoxAt(int x, int y)
	{
		return board[y][x];
	}

	public void movePiece(int initialX, int initialY, int finalX, int finalY) 
				throws MovementException, IligalMovementException, CheckException
	{
		Piece p = board[initialX][initialY].getPiece();

		IligalMovementException e = new IligalMovementException("Piece in the way");

		if(board[finalX][finalY].getPiece() != null &&
			board[finalX][finalY].getPiece().getColor().equals(p.getColor()))
		{
			throw new MovementException("You have another piece in that location");
		}
		else if(!p.isLegalMove(initialX, initialY, finalX, finalY))
		{
			throw new IligalMovementException("Illegal movement");
			//TODO: error message
		}
		else
		{
			if(p.getType().equals(Tower.TOWER))
			{
				int i = initialX + 1;
				while(i < finalX)
				{
					if(board[i][initialY].getPiece() != null)
					{
						throw e;
					}
					i++;
				}

				i = initialX - 1;
				while(i > finalX)
				{
					if(board[i][initialY].getPiece() != null)
					{
						throw e;
					}
					i--;
				}

				i = initialY + 1;
				while(i < finalY)
				{
					if(board[initialX][i].getPiece() != null)
					{
						throw e;
					}
					i++;
				}

				i = initialY - 1;
				while(i  > finalY)
				{
					if(board[initialX][i].getPiece() != null)
					{
						throw e;
					}
					i--;
				}
			}else if(p.getType().equals(Bishop.BISHOP))
			{
				int i = initialX + 1;
				int j = initialY + 1;

				while(i < finalX && j < finalY)
				{
					if(board[i][j].getPiece() != null)
					{
						throw e;
					}
					i++;
					j++;
				}

				i = initialX + 1;
				j = initialY - 1;
				while(i < finalX && j  > finalY)
				{
					if(board[i][j].getPiece() != null)
					{
						throw e;
					}
					i++;
					j--;
				}

				i = initialX - 1;
				j = initialY + 1;
				while(i > finalX && j < finalY)
				{
					if(board[i][j].getPiece() != null)
					{
						throw e;
					}
					i--;
					j++;
				}

				i = initialX - 1;
				j = initialY - 1;
				while(i > finalX && j > finalY)
				{
					if(board[i][j].getPiece() != null)
					{
						throw e;
					}
					i--;
					j--;
				}
				
			}else if(p.getType().equals(Queen.QUEEN))
			{
				if((initialX == finalX)||(initialY == finalY))
				{
					int i = initialX + 1;
					while(i < finalX)
					{
						if(board[i][initialY].getPiece() != null)
						{
							throw e;
						}
						i++;
					}

					i = initialX - 1;
					while(i > finalX)
					{
						if(board[i][initialY].getPiece() != null)
						{
							throw e;
						}
						i--;
					}

					i = initialY + 1;
					while(i < finalY)
					{
						if(board[initialX][i].getPiece() != null)
						{
							throw e;
						}
						i++;
					}

					i = initialY - 1;
					while(i  > finalY)
					{
						if(board[initialX][i].getPiece() != null)
						{
							throw e;
						}
						i--;
					}
				}else if(Math.abs(initialX - finalX) == Math.abs(initialY - finalY))
				{
					int i = initialX + 1;
					int j = initialY + 1;

					while(i < finalX && j < finalY)
					{
						if(board[i][j].getPiece() != null)
						{
							throw e;
						}
						i++;
						j++;
					}

					i = initialX + 1;
					j = initialY - 1;
					while(i < finalX && j  > finalY)
					{
						if(board[i][j].getPiece() != null)
						{
							throw e;
						}
						i++;
						j--;
					}

					i = initialX - 1;
					j = initialY + 1;
					while(i > finalX && j < finalY)
					{
						if(board[i][j].getPiece() != null)
						{
							throw e;
						}
						i--;
						j++;
					}

					i = initialX - 1;
					j = initialY - 1;
					while(i > finalX && j > finalY)
					{
						if(board[i][j].getPiece() != null)
						{
							throw e;
						}
						i--;
						j--;
					}
				}
			}else if(p.getType().equals(King.KING))
			{

				if(p.wasMoved() && Math.abs(initialX - finalX) > 1)
				{
					throw new IligalMovementException("Illeagal movement");
				}

				if(p.getColor().equals(Piece.BLACK))
				{
					if(board[finalX][finalY].getCheck(Piece.BLACK))
					{
						throw new CheckException("King can´t move there.");
					}
				}else if(p.getColor().equals(Piece.WHITE))
				{
					if(board[finalX][finalY].getCheck(Piece.WHITE))
					{
						throw new CheckException("King can´t move there");
					}
				}

				
			}else if(p.getType().equals(Pawn.PAWN))
			{
				int dy = 10;
				if(p.getColor().equals(Piece.BLACK)){dy = finalY - initialY;}
				else if(p.getColor().equals(Piece.WHITE)){dy = initialY - finalY;}

				if(p.getColor().equals(Piece.BLACK) && finalY < initialY)
				{
					throw new IligalMovementException("Illeagal movement");
				}else if(p.getColor().equals(Piece.WHITE) && finalY > initialY)
				{
					throw new IligalMovementException("Illeagal movement");
				}

				int dx = Math.abs(initialX - finalX);

				if(board[finalX][finalY].getPiece() == null && ((dx < 2) && (dx != 0) && (dy == 1)))
				{
					throw new IligalMovementException("Illeagal movement");
				}else if(dy == 2){
					if((p.getColor().equals(Piece.BLACK) && initialY != 1) || board[initialX][2].getPiece() != null)
					{
						throw new IligalMovementException("Illeagal movement");
					}else if((p.getColor().equals(Piece.WHITE) && initialY != 6) || board[initialX][5].getPiece() != null)
					{
						throw new IligalMovementException("Illeagal movement");
					}
				}else if(board[finalX][finalY].getPiece() != null && dx == 0)
				{
					throw new IligalMovementException("Illeagal movement");
				}
			}
			if(p.getType().equals(King.KING) && !p.wasMoved())
			{
				if(initialY == finalY){
					if(Math.abs(initialX - finalX) > 1)
					{
						Piece eastTower = board[7][initialY].getPiece();
						Piece westTower = board[0][initialY].getPiece();

						if(finalX == 6 && eastTower != null && eastTower.getType().equals(Tower.TOWER) && !eastTower.wasMoved())
						{
							if(board[5][initialY].getPiece() == null){
								p.setMoved(true);
								board[7][initialY].setPiece(null);
								board[5][initialY].setPiece(eastTower);
								castle = true;
							}
						}else if(finalX == 1 && westTower != null && westTower.getType().equals(Tower.TOWER) && !westTower.wasMoved())
						{
							if(board[2][initialY].getPiece() == null){
								p.setMoved(true);
								board[0][initialY].setPiece(null);
								board[2][initialY].setPiece(westTower);
								castle = true;
							}
						}
					}
				}
				p.setMoved(true);
				
			}
			board[finalX][finalY].setPiece(p);
			board[initialX][initialY].setPiece(null);
			p.setPositionX(finalX);
			p.setPositionY(finalY);
			if(p.getType().equals(Pawn.PAWN))
			{
					if(p.getColor().equals(Piece.BLACK) && finalY == 7)
					{
						promote = true;
					}else if(p.getColor().equals(Piece.WHITE) && finalY == 0)
					{	
						promote = true;
					}
			}
			calculateCheckBoxes();

			if(board[blackX][blackY].getCheck(Piece.BLACK))
			{
				if(p.getColor().equals(Piece.WHITE))
				{
					check = true;
				}else if(p.getColor().equals(Piece.BLACK) && !p.getType().equals(King.KING))
				{
					board[initialX][initialY].setPiece(p);
					board[finalX][finalY].setPiece(null);
					p.setPositionX(initialX);
					p.setPositionY(initialY);
					throw new CheckException("Your king is in check");
				}
			}else if(board[whiteX][whiteY].getCheck(Piece.WHITE))
			{
				if(p.getColor().equals(Piece.WHITE) && !p.getType().equals(King.KING))
				{
					board[initialX][initialY].setPiece(p);
					board[finalX][finalY].setPiece(null);
					p.setPositionX(initialX);
					p.setPositionY(initialY);
					throw new CheckException("Your king is in check");
				}else if(p.getColor().equals(Piece.BLACK))
				{
					check = true;
				}
			}

			if(p.getType().equals(King.KING))
			{
				if(p.getColor().equals(Piece.BLACK))
				{
					blackX = finalX;
					blackY = finalY;
				}else if(p.getColor().equals(Piece.WHITE))
				{
					whiteX = finalX;
					whiteY = finalY;
				}
			}
		}
	}

	public void calculateCheckBoxes()
	{

		ArrayList<Piece> pieces = new ArrayList<Piece>();

		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				pieces.add(board[i][j].getPiece());
				board[i][j].setCheck(false, Piece.WHITE);
				board[i][j].setCheck(false, Piece.BLACK);
			}
		}

		for(int i = 0; i < pieces.size(); i++)
		{
			Piece p = pieces.get(i);
			if(p != null){
				checkOnPiece(p);
			}
		}
	}

	private void checkOnPiece(Piece p)
	{
 		int posX = p.getPositionX();
 		int posY = p.getPositionY();
 		if(p.getType().equals(Tower.TOWER) || p.getType().equals(Queen.QUEEN))
 		{
 			int x = posX + 1;
 			int y = posY;
 			while(x < 8)
 			{
 				
 				if(p.getColor().equals(Piece.BLACK))
 				{
 					board[x][y].setCheck(true, Piece.WHITE);
 				}else if(p.getColor().equals(Piece.WHITE))
 				{
 					board[x][y].setCheck(true,Piece.BLACK);
 				}

 				if(board[x][y].getPiece() != null)
 				{
 					break;
 				}
 				x++;
 			}

 			x = posX - 1;
 			while(x > -1)
 			{
 				if(p.getColor().equals(Piece.BLACK))
 				{
 					board[x][y].setCheck(true, Piece.WHITE);
 				}else if(p.getColor().equals(Piece.WHITE))
 				{
 					board[x][y].setCheck(true, Piece.BLACK);
 				}

 				if(board[x][y].getPiece() != null)
 				{
 					break;
 				}
 				x--;
 			}

 			x = posX;
 			y = posY + 1;
 			while(y < 8)
 			{
 				if(p.getColor().equals(Piece.BLACK))
 				{
 					board[x][y].setCheck(true, Piece.WHITE);
 				}else if(p.getColor().equals(Piece.WHITE))
 				{
 					board[x][y].setCheck(true, Piece.BLACK);
 				}

 				if(board[x][y].getPiece() != null)
 				{
 					break;
 				}
 				y++;
 			}

 			y = posY - 1;
 			while(y > -1)
 			{
 				if(p.getColor().equals(Piece.BLACK))
 				{
 					board[x][y].setCheck(true, Piece.WHITE);
 				}else if(p.getColor().equals(Piece.WHITE))
 				{
 					board[x][y].setCheck(true, Piece.BLACK);
 				}

 				if(board[x][y].getPiece() != null)
 				{
 					break;
 				}
 				y--;
 			}
 		}

 		if(p.getType().equals(Bishop.BISHOP) || p.getType().equals(Queen.QUEEN))
 		{
 			int x = posX + 1;
 			int y = posY + 1;
 			if(p.getColor().equals(Piece.BLACK))
 			{
 				while(x < 8 && y < 8)
 				{
 					board[x][y].setCheck(true, Piece.WHITE);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x++;
 					y++;
 				}

 				x = posX + 1;
 				y = posY - 1;

 				while(x < 8 && y > -1)
 				{
 					board[x][y].setCheck(true, Piece.WHITE);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x++;
 					y--;
 				} 

 				x = posX - 1;
 				y = posY + 1;
 				while(x > -1 && y < 8)
 				{
 					board[x][y].setCheck(true, Piece.WHITE);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x--;
 					y++;
 				}

 				x = posX - 1;
 				y = posY - 1;
 				while(x > -1 && y > -1)
 				{
 					board[x][y].setCheck(true, Piece.WHITE);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x--;
 					y--;
 				}
 			}else if(p.getColor().equals(Piece.WHITE))
 			{
 				while(x < 8 && y < 8)
 				{
 					board[x][y].setCheck(true, Piece.BLACK);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x++;
 					y++;
 				}

 				x = posX + 1;
 				y = posY - 1;

 				while(x < 8 && y > -1)
 				{
 					board[x][y].setCheck(true, Piece.BLACK);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x++;
 					y--;
 				} 

 				x = posX - 1;
 				y = posY + 1;
 				while(x > -1 && y < 8)
 				{
 					board[x][y].setCheck(true, Piece.BLACK);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x--;
 					y++;
 				}

 				x = posX - 1;
 				y = posY - 1;
 				while(x > -1 && y > -1)
 				{
 					board[x][y].setCheck(true, Piece.BLACK);

 					if(board[x][y].getPiece() != null)
 					{
 						break;
 					}
 					x--;
 					y--;
 				}
 			}
 		}else if (p.getType().equals(Knight.KNIGHT))
 		{
 			if(p.getColor().equals(Piece.WHITE))
 			{
 				int x = posX + 2;
 				int y = posY + 1;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 1;
 				y = posY + 2;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 1;
 				y = posY + 2;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 2;
 				y = posY + 1;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 2;
 				y = posY - 1;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 1;
 				y = posX - 2;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 2;
 				y = posY - 1;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 1;
 				y = posY - 2;
 				try{board[x][y].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}
 			}else if(p.getColor().equals(Piece.BLACK))
 			{
 				int x = posX + 2;
 				int y = posY + 1;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 1;
 				y = posY + 2;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 1;
 				y = posY + 2;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 2;
 				y = posY + 1;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 2;
 				y = posY - 1;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX + 1;
 				y = posX - 2;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 2;
 				y = posY - 1;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				x = posX - 1;
 				y = posY - 2;
 				try{board[x][y].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}
 			}
 		}else if(p.getType().equals(King.KING))
 		{
 			if(p.getColor().equals(Piece.BLACK))
 			{
 				try{board[posX - 1][posY - 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX][posY - 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY - 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX - 1][posY].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX - 1][posY + 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX][posY + 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY + 1].setCheck(true, Piece.WHITE);}
 				catch(ArrayIndexOutOfBoundsException e){}

 			}else if(p.getColor().equals(Piece.WHITE))
 			{
 				try{board[posX - 1][posY - 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX][posY - 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY - 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX - 1][posY].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX - 1][posY + 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX][posY + 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}

 				try{board[posX + 1][posY + 1].setCheck(true, Piece.BLACK);}
 				catch(ArrayIndexOutOfBoundsException e){}
 			}
 		}else if(p.getType().equals(Pawn.PAWN))
 		{
 			if(p.getColor().equals(Piece.BLACK))
 			{
 				if(posX < 7 && posY < 7)
 				{
 					board[posX + 1][posY + 1] .setCheck(true, Piece.WHITE);
 				}

 				if(posX > 0 && posY < 7)
 				{
 					board[posX - 1][posY + 1].setCheck(true,Piece.WHITE);
 				}
 			}else if(p.getColor().equals(Piece.WHITE))
 			{
 				if(posX < 7 && posY > 0)
 				{
 					board[posX + 1][posY - 1].setCheck(true, Piece.BLACK);
 				}

 				if(posX > 0 && posY > 0)
 				{
 					board[posX - 1][posY - 1].setCheck(true, Piece.BLACK);
 				}
 			}
 		}
 	}

 	public boolean getCheck()
 	{
 		return check;
 	}

 	public void setCheck(boolean check)
 	{
 		this.check = check;
 	}

	public boolean castle()
	{
		return castle;
	}

	public void setCastle(boolean castle)
	{
		this.castle = castle;
	}

	public boolean promote()
	{
		return promote;
	}

	public void setPromote(boolean promote)
	{
		this.promote = promote;
	}
}

class Box
{
	public static final String BLACK = "black";
	public static final String WHITE = "white";
	private Piece piece;
	
	private String color; 

	private boolean whiteCheck;
	private boolean blackCheck;

	public Box()
	{

	}

	public Piece getPiece()
	{
		return piece;
	}

	public void setPiece(Piece p)
	{
		piece = p;
	}

	public boolean getCheck(String color)
	{
		boolean check = whiteCheck;
		if(color.equals(Piece.BLACK))
		{
			check = blackCheck;
		}
		return check;
	}

	public void setCheck(boolean check, String color)
	{
		if(color.equals(Piece.WHITE))
		{
			whiteCheck = check;
		}else if(color.equals(Piece.BLACK))
		{
			blackCheck = check;
		}
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

}

abstract class Piece
{
	public static final String WHITE = "White";
	public static final String BLACK = "Black";

	protected String color;

	protected String type;

	protected int positionX;
	protected int positionY;

	public Piece(String type, String color, int posX, int posY)
	{
		this.type = type;
		this.color = color;
		positionX = posX;
		positionY = posY;
	}

	public String getType()
	{
		return type;
	}

	public String getColor()
	{
		return color;
	}

	public int getPositionX()
	{
		return positionX;
	}

	public int getPositionY()
	{
		return positionY;
	}

	public void setPositionX(int posX)
	{
		positionX = posX;
	}

	public void setPositionY(int posY)
	{
		positionY = posY;
	}

	public abstract boolean isLegalMove(int initialX, int initilY, int finalX, int finalY);

	public abstract boolean wasMoved();

	public abstract void setMoved(boolean moved);

	public String toString()
	{
		return color + " " + type;
	}

}

class Tower extends Piece
{
	public static final String TOWER = "tower"; 

	private boolean moved;

	public Tower(String color, int posX, int posY)
	{
		super(TOWER, color, posX, posY);
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		return initialY == finalY || initialX == finalX;
	}

	@Override
	public boolean wasMoved()
	{
		return moved;
	}

	@Override
	public void setMoved(boolean moved)
	{
		this.moved = moved;
	}
}

class Knight extends Piece
{
	public static final String KNIGHT = "knight";

	public Knight(String color, int posX, int posY)
	{
		super(KNIGHT, color, posX, posY);
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		int dx = finalX - initialX;
		int dy = finalY - initialY;

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		return ((dy == 1 && dx == 2)||(dy == 2 && dx == 1));
	}

	@Override public boolean wasMoved(){return true;}
	@Override public void setMoved(boolean moved){;}
}

class Bishop extends Piece
{
	public static final String BISHOP = "bishop";

	public Bishop(String color, int posX, int posY)
	{
		super(BISHOP, color, posX, posY);
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		int dx = finalX - initialX;
		int dy = finalY - initialY;

		dx = Math.abs(dx);
		dy = Math.abs(dy);
		return dx == dy;
	}

	@Override public boolean wasMoved(){return true;}
	@Override public void setMoved(boolean moved){;}
}

class Queen extends Piece
{
	public static final String QUEEN = "queen";

	public Queen(String color, int posX, int posY)
	{
		super(QUEEN, color, posX, posY);
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		Bishop b = new Bishop("", -1, -1);
		Tower t = new Tower("", -1, -1);


		return b.isLegalMove(initialX, initialY, finalX, finalY) || 
		t.isLegalMove(initialX, initialY, finalX, finalY);
	}

	@Override 
	public boolean wasMoved(){return true;}

	@Override 
	public void setMoved(boolean moved){;}
}

class King extends Piece
{
	public static final String KING = "king";

	private boolean moved;

	public King(String color, int posX, int posY)
	{
		super(KING, color, posX, posY);
		moved = false;
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		Queen q = new Queen("", -1, -1);


		boolean normalMove = q.isLegalMove(initialX, initialY, finalX, finalY) &&
		((Math.abs(initialX - finalX) < 2) && (Math.abs(initialY - finalY) < 2));
		boolean castle = (initialY == finalY) && ((finalX == 1) || (finalX == 6));

		return normalMove || castle;
	}

	@Override
	public boolean wasMoved()
	{
		return moved;
	}

	@Override
	public void setMoved(boolean moved)
	{
		this.moved = moved;
	}
}

class Pawn extends Piece 
{
	public static final String PAWN = "pawn";

	public Pawn(String color, int posX, int posY)
	{
		super(PAWN, color, posX, posY);
	}

	@Override
	public boolean isLegalMove(int initialX, int initialY, int finalX, int finalY)
	{
		int dy = 10;
		if(color.equals(Piece.BLACK)){dy = finalY - initialY;}
		else if(color.equals(Piece.WHITE)){dy = initialY - finalY;}

		boolean comer = (Math.abs(initialX - finalX) < 2) && (dy == 1);
		boolean mover = (initialX == finalX) && (dy < 3);
		return comer || mover;
		//TODO
	}

	@Override 
	public boolean wasMoved(){return true;}

	@Override 
	public void setMoved(boolean moved){;}
}

class MovementException extends Exception
{
	public MovementException(String message)
	{
		super(message);
	}
}

class IligalMovementException extends Exception
{
	public IligalMovementException(String message)
	{
		super(message);
	}
}

class CheckException extends Exception
{
	public CheckException(String messeage)
	{
		super(messeage);
	}
}