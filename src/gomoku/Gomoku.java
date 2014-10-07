package gomoku;
import java.util.Random;

import cs251.lab3.GomokuGUI;
import cs251.lab3.GomokuModel;

/**
 * class that manages and creates Gomoku game
 * @author matthewletter
 *
 */
public class Gomoku implements GomokuModel {
    /**************************************/
	/*         Private variables          */
    /**************************************/
    private GomokuModel.Square[][] board = new GomokuModel.Square[this.getNumRows()][this.getNumCols()];
    private boolean player1Turn = true;
    private boolean computerPlayer = true;

    /**************************************/
	/*          Public variables          */
    /**************************************/

    /**
     * initializes A new game for Gomoku
     */
    public Gomoku() {
        //this.newGame();
    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#doClick(int, int)
     */
    @Override
    public Outcome doClick(int arg0, int arg1) {
        if(this.board[arg0][arg1] == GomokuModel.Square.EMPTY){
            if(computerPlayer){
                this.board[arg0][arg1] = GomokuModel.Square.CROSS;
                if( isGameOver(0) == GomokuModel.Outcome.CROSS_WINS){
                    return GomokuModel.Outcome.CROSS_WINS;
                }else{
                    return computerMove();
                }

            }else{
                if(player1Turn){
                    this.board[arg0][arg1] = GomokuModel.Square.CROSS;
                    player1Turn = false;
                }else{
                    this.board[arg0][arg1] = GomokuModel.Square.RING;
                    player1Turn = true;
                }
                return isGameOver(0);
            }
        }
        return GomokuModel.Outcome.GAME_NOT_OVER;
    }
    /**
     * boss lvl computer player
     * @return computers move
     */
    private Outcome computerMove() {
        Random random = new Random(System.currentTimeMillis());
        //can i win?
        for(int i = 0; i<getNumRows(); i++){
            for(int j =0; j<getNumCols(); j++){
                if(board[i][j] == GomokuModel.Square.EMPTY){

                    board[i][j] = GomokuModel.Square.RING;
                    if (isGameOver(0) == GomokuModel.Outcome.RING_WINS) {

                        return isGameOver(0);
                    }
                    board[i][j] = GomokuModel.Square.EMPTY;
                }
            }
        }
        //can I block a win
        for(int i = 0; i<getNumRows(); i++){
            for(int j =0; j<getNumCols(); j++){
                if(board[i][j] == GomokuModel.Square.EMPTY){

                    board[i][j] = GomokuModel.Square.CROSS;
                    if (isGameOver(0) == GomokuModel.Outcome.CROSS_WINS){

                        board[i][j] = GomokuModel.Square.RING;
                        return isGameOver(0);
                    }
                    board[i][j] = GomokuModel.Square.EMPTY;
                }
            }
        }

        //can i block win-1 in a row
        for(int i = 0; i<getNumRows(); i++){
            for(int j =0; j<getNumCols(); j++){
                if(board[i][j] == GomokuModel.Square.EMPTY){

                    board[i][j] = GomokuModel.Square.CROSS;
                    if (gameMove(1,i,j) == GomokuModel.Outcome.CROSS_WINS){
                        board[i][j] = GomokuModel.Square.RING;
                        return isGameOver(0);
                    }
                    board[i][j] = GomokuModel.Square.EMPTY;
                }
            }
        }

        //can I make win-x in a row?
        for(int k = 0; k < this.getNumInLineForWin();k++){
            for(int i = 0; i<getNumRows(); i++){
                for(int j =0; j<getNumCols(); j++){
                    if(board[i][j] == GomokuModel.Square.EMPTY){

                        board[i][j] = GomokuModel.Square.CROSS;
                        if (gameMove(k,i,j) == GomokuModel.Outcome.CROSS_WINS){

                            board[i][j] = GomokuModel.Square.RING;
                            return isGameOver(0);
                        }
                        board[i][j] = GomokuModel.Square.EMPTY;
                    }
                }
            }
        }

        //last resort
        for(int i = 0; i<getNumRows(); i++){
            for(int j =0; j<getNumCols(); j++){
                int r = random.nextInt(this.getNumRows());
                int c = random.nextInt(this.getNumCols());
                if(board[r][c] == GomokuModel.Square.EMPTY){
                    board[r][c] = GomokuModel.Square.RING;
                    return isGameOver(0);
                }
            }
        }
        return isGameOver(0);
    }
    /**
     * helper class for computer move search
     * @param bias
     * @param row
     * @param col
     * @return outcome of "fake" computer move
     */
    private Outcome gameMove(int bias, int row, int col){
        GomokuModel.Outcome outcome = GomokuModel.Outcome.GAME_NOT_OVER;

        if(checkUp(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkDown(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkLeft(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkRight(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkDagUpRight(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkDagUpLeft(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkDagDownRight(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        else if(checkDagDownLeft(bias, row, col)){
            outcome = squareToOutcome(board[row][col]);
        }
        return outcome;
    }

    /**
     * scans all pieces of the board looking for the amount in a row needed
     * to win in O(n^3)
     * @return outcome of if the game is over or who won
     */
    private Outcome isGameOver(int bias) {
        GomokuModel.Outcome outcome = GomokuModel.Outcome.GAME_NOT_OVER;
        int countEmpty = 0;
        for(int row = 0; row < this.getNumRows();row ++){
            for(int col = 0; col < this.getNumCols(); col++){
                if(board[row][col] != GomokuModel.Square.EMPTY){
                    if(checkUp(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkDown(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkLeft(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkRight(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkDagUpRight(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkDagUpLeft(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkDagDownRight(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                    else if(checkDagDownLeft(bias,row, col)){
                        outcome = squareToOutcome(board[row][col]);
                    }
                }
                else{
                    countEmpty++;
                }
            }
        }
        if(countEmpty == 0 && outcome == GomokuModel.Outcome.GAME_NOT_OVER) outcome = GomokuModel.Outcome.DRAW;
        return outcome;
    }
    /**
     * helper method that takes a square and returns
     * an outcome to reflect the win/loss of the game
     * @param square we want to test
     * @return Outcome value of the @param square
     */
    private Outcome squareToOutcome(Square square){
        switch(square){
            case CROSS: return GomokuModel.Outcome.CROSS_WINS;
            case RING: return GomokuModel.Outcome.RING_WINS;
            default: System.err.println("error in isGameOver");
                break;
        }
        return GomokuModel.Outcome.GAME_NOT_OVER;
    }
    /**
     * @param bias
     * @param col
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkUp(int bias, int row, int col){
        int count = bias;
        boolean dontExit = true;
        for(int i = row; i>=0 && dontExit; i--){
            if(board[i][col] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkDown(int bias, int row, int col){
        int count = bias;
        boolean dontExit = true;
        for(int i = row; i < this.getNumRows() && dontExit; i++){
            if(board[i][col] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkRight(int bias, int row, int col){
        int count = bias;
        boolean dontExit = true;
        for(int i = col; i < this.getNumCols() && dontExit; i++){
            if(board[i][col] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkLeft(int bias, int row, int col){
        int count = bias;
        boolean dontExit = true;
        for(int i = col; i >= 0 && dontExit; i--){
            if(board[row][i] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkDagUpRight(int bias, int row, int col){
        int count = bias;
        int j = col;
        boolean dontExit = true;
        for(int i = row; i >= 0 && dontExit; i--){
            if(board[i][j] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }

            j++;
            if(j >= this.getNumCols()){
                dontExit = false;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkDagUpLeft(int bias, int row, int col){
        int count = bias;
        int j = col;
        boolean dontExit = true;
        for(int i = row; i >= 0 && dontExit; i--){
            if(board[i][j] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }

            j--;
            if(j < 0){
                dontExit = false;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkDagDownRight(int bias, int row, int col){
        int count = bias;
        int j = col;
        boolean dontExit = true;
        for(int i = row; i >= this.getNumRows() && dontExit; i++){
            if(board[i][j] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }

            j++;
            if(j >= this.getNumCols()){
                dontExit = false;
            }
        }
        return false;

    }
    /**
     * @param row
     * @param col
     * @return true if desired number in a row for the win is encountered
     */
    private boolean checkDagDownLeft(int bias, int row, int col){
        int count = bias;
        int j = col;
        boolean dontExit = true;
        for(int i = row; i >= this.getNumRows() && dontExit; i++){
            if(board[i][j] == board[row][col]){
                count++;
            }
            else{
                dontExit = false;
            }
            if(count == this.getNumInLineForWin()){
                return true;
            }

            j--;
            if(j < 0){
                dontExit = false;
            }
        }
        return false;

    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#getBoardString()
     */
    @Override
    public String getBoardString() {
        if(board == null) return null;
        char space = (char)32;

        String myString = "";
        for(GomokuModel.Square[] sa : board){
            for(GomokuModel.Square s : sa){
                if(s == GomokuModel.Square.EMPTY)myString += space;
                else if(s == GomokuModel.Square.CROSS) myString +="x";
                else if(s == GomokuModel.Square.RING) myString +="o";
            }
            myString +="\n";
        }

        return myString;
    }


    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#getNumInLineForWin()
     */
    @Override
    public int getNumInLineForWin() {
        return GomokuModel.SQUARES_IN_LINE_FOR_WIN;
    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#getNumCols()
     */
    @Override
    public int getNumCols() {
        return GomokuModel.DEFAULT_NUM_COLS;
    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#getNumRows()
     */
    @Override
    public int getNumRows() {
        return GomokuModel.DEFAULT_NUM_ROWS;
    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#newGame()
     */
    @Override
    public void newGame() {
        for(int i = 0; i<getNumRows(); i++){
            for(int j =0; j<getNumCols(); j++){
                board[i][j] = GomokuModel.Square.EMPTY;
            }
        }
    }

    /* (non-Javadoc)
     * @see cs251.lab3.GomokuModel#setComputerPlayer(java.lang.String)
     */
    @Override
    public void setComputerPlayer(String arg0) {
        if("COMPUTER".equals(arg0))computerPlayer = true;
    }

    /**
     * Main running function for Gomoku. Sets up the initial game and passes it to the GUI creater
     * @param args
     * 		not used
     */
    public static void main(String[] args) {
        Gomoku game = new Gomoku ();
        if( args.length > 0) {
            game.setComputerPlayer ( args [0]);
        }
        GomokuGUI.showGUI ( game );
    }
}
