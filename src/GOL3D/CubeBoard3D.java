package GOL3D;

import javafx.scene.control.Alert;
import model.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * This class creates the boards of boolean values for the cube,
 * to keep track of Game of Life and the logic associated with it.
 * Created by Elise Haram Vannes on 28.03.2017.
 */
public class CubeBoard3D {

    private List<List<Boolean>> board1;
    private List<List<Boolean>> board2;
    private List<List<Boolean>> board3;
    private List<List<Boolean>> board4;
    private List<List<Boolean>> board5;
    private List<List<Boolean>> board6;
    private List<List<Boolean>>[] boardArrays;
    private List<List<Boolean>>[] cloneArrays;

    private int boardSize = 30;
    private Rules rules = new Rules();

    /**
     * Constructor that initializes all arrays for the cubes boolean values.
     */
    public CubeBoard3D(){
        board1 = initStartBoard();
        board2 = initStartBoard();
        board3 = initStartBoard();
        board4 = initStartBoard();
        board5 = initStartBoard();
        board6 = initStartBoard();

        List<List<Boolean>> clone1 = initStartBoard();
        List<List<Boolean>> clone2 = initStartBoard();
        List<List<Boolean>> clone3 = initStartBoard();
        List<List<Boolean>> clone4 = initStartBoard();
        List<List<Boolean>> clone5 = initStartBoard();
        List<List<Boolean>> clone6 = initStartBoard();

        boardArrays = initArrayOfLists(board1,board2,board3,board4,board5,board6);
        cloneArrays = initArrayOfLists(clone1,clone2,clone3,clone4,clone5,clone6);
        defaultStartBoard();
    }

    /**
     * Initializes the board.
     * @return returns the initialized board
     */
    private List<List<Boolean>> initStartBoard(){
        List<List<Boolean>> board = new ArrayList<List<Boolean>>(boardSize);

        for(int i = 0; i < boardSize; i++) {
            board.add(new ArrayList<>(boardSize));

            for(int j = 0; j < boardSize; j++) {
                board.get(i).add(j, false);
            }
        }
        return board;
    }

    /**
     * Creates an initial pattern to be shown on the cube.
     */
    private void defaultStartBoard(){
        board2.get(3).set(15,true);
        board2.get(4).set(15,true);
        board2.get(5).set(15,true);
        board2.get(5).set(14,true);
        board2.get(4).set(13,true);
    }

    /**
     * Sets a simple board used for testing.
     */
    public void setTestBoard(){
        clearBoards();
        board2.get(1).set(0,true);
        board2.get(1).set(1,true);
        board2.get(1).set(2,true);
    }

    /**
     * Returns the size of the board.
     * @return the size of this board
     */
    private int getBoardSize() {
        return boardSize;
    }

    /**
     * Creates clone of the current array.
     * @param indexBoard    index of current board in array of boards
     */
    private void createClone(int indexBoard) {
        cloneArrays[indexBoard] = new ArrayList<List<Boolean>>(getBoardSize());

        for(int i = 0; i < getBoardSize(); i++) {
            cloneArrays[indexBoard].add(new ArrayList<>(getBoardSize()));

            for(int j = 0; j < getBoardSize(); j++) {
                cloneArrays[indexBoard].get(i).add(j, getValue(indexBoard,i, j));
            }
        }
    }

    /**
     * Runs through the nextGeneration-method for all the arrays of the cube.
     */
    void nextGenerations(){
        for(int i = 0; i < 6; i++){
            nextGeneration(i);
        }
    }

    /**
     * Creates the next generation of cells to be drawn or removed.
     * @param indexBoard    index of current board in array of boards
     */
    public void nextGeneration(int indexBoard) {
        createClone(indexBoard);

        for(int i = 0; i < getBoardSize(); i++){
            for(int j = 0; j < getBoardSize(); j++){
                int neighbors = countNeighbor(indexBoard,i, j);
                boolean value = getValue(indexBoard,i, j) ? rules.shouldStayAlive(neighbors) : rules.shouldSpawnActiveCell(neighbors);
                setCloneValue(indexBoard,i, j, value );
            }
        }
    }

    /**
     * Sets values to the clone at the appointed index.
     * @param indexBoard    index of current board in array of boards
     * @param x the first column index
     * @param y the second column index
     * @param value the value to be set
     */
    public void setCloneValue(int indexBoard, int x, int y, boolean value) {
        cloneArrays[indexBoard].get(x).set(y, value);
    }

    /**
     * Makes all the boards equal to their clone.
     */
    void switchBoards(){
        for(int i = 0; i < cloneArrays.length; i++){
            switchBoard(i);
        }
    }

    /**
     * Makes the board equal to the clone.
     * @param indexBoard    index of current board in array of boards
     */
    public void switchBoard(int indexBoard) {
        for(int i = 0; i < getBoardSize(); i++) {
            for(int j = 0; j < getBoardSize(); j++) {
                setValue(indexBoard,i, j, cloneArrays[indexBoard].get(i).get(j));
            }
        }
    }

    /**
     * The method counting the alive cells surrounding the appointed cell.
     * @param indexBoard index of current board in array of boards
     * @param x     the first column index of the array
     * @param y     the second column index of the array
     * @return      the number of alive neighboring cells
     */
    public int countNeighbor(int indexBoard,int x,int y){
        int count = 0;
         for (int i = x - 1; i < x + 2; i++) {
             for (int j = y - 1; j < y + 2; j++) {
                 if (i == x && j == y)
                    continue;

                 if (isInCurrentBoard(indexBoard,i, j))
                    count++;
             }
         }
         return count;
    }

    /**
     * The method checking if the appointed position is within the board array.
     * @param indexBoard index of current board in array of boards
     * @param i         the first column index of the array
     * @param j         the second column index of the array
     * @return          <code>false</code> if the position is exceeding the board array
     */
    private boolean isInCurrentBoard(int indexBoard, int i, int j){
        if(i == -1 && j == -1){
            return false;
        }
        else if(i >= boardSize && j >= boardSize){
            return false;
        }
        else if(i == -1 && j >= boardSize){
            return false;
        }
        else if(i >= boardSize && j == -1){
            return false;
        }
        else if(inBounds(i) && inBounds(j)){
            if(boardArrays[indexBoard].get(i).get(j)){
                return true;
            } else {
                return false;
            }
        }
        else{
            return countBoardNeighbors(indexBoard,i,j);
        }
    }

    /**
     * Checks if the cell exists in the cube.
     * @param cell  cell in cube
     * @return <code>true</code> if the cell exists in the cube
     */
    private boolean inBounds(int cell){
        if(cell == -1 || cell >= boardSize){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Chooses the method for counting neighbors at neighboring
     * boards when the cell is at the end of a board, according
     * to which board is the current board.
     * The switch-statement will not reach the default option,
     * since it always gets a valid index for the current board.
     * @param indexBoard index of current board in array of boards
     * @param i          the first column index
     * @param j          the second column index
     * @return       <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoardNeighbors(int indexBoard,int i, int j){
        switch(indexBoard){
            case 0:
                return countBoard1Neighbors(i,j);
            case 1:
                return countBoard2Neighbors(i,j);
            case 2:
                return countBoard3Neighbors(i,j);
            case 3:
                return countBoard4Neighbors(i,j);
            case 4:
                return countBoard5Neighbors(i,j);
            case 5:
                return countBoard6Neighbors(i,j);
            default:
                return false;
        }
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board1
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard1Neighbors(int i, int j){
        if (i == -1 && inBounds(j)) {
            return checkValue(board2,j,0);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board6,0,i);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board4,j,0);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board5,0,i);
        }
        return false;
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board2
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard2Neighbors(int i, int j){

        if (i == -1 && inBounds(j)) {
            return checkValue(board6,j,0);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board1,0,i);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board5,j,0);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board3,0,i);
        }
        return false;
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board3
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard3Neighbors(int i, int j){
        if (i == -1 && inBounds(j)) {
            return checkValue(board2, j,boardSize-1);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board6,i,boardSize-1);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board4,j,boardSize-1);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board5,i,boardSize-1);
        }
        return false;
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board4
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard4Neighbors(int i, int j){
        if (i == -1 && inBounds(j)) {
            return checkValue(board6,boardSize-1,j);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board1,boardSize-1,i);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board5,boardSize-1,j);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board3,boardSize-1,i);
        }
        return false;
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board5
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard5Neighbors(int i, int j){
        if (i == -1 && inBounds(j)) {
            return checkValue(board2,boardSize-1,j);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board1,i,boardSize-1);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board4,boardSize-1,j);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board3,i,boardSize-1);
        }
        return false;
    }

    /**
     * Checks if the neighbor at neighboring boards is alive for board6
     * @param i  the first column index
     * @param j  the second column index
     * @return   <code>true</code> if the neighboring cell is alive
     */
    private boolean countBoard6Neighbors(int i, int j){
        if (i == -1 && inBounds(j)) {
            return checkValue(board1,j,0);
        }

        if (inBounds(i) && j == -1) {
            return checkValue(board2,0,i);
        }

        if (i >= boardSize && inBounds(j)) {
            return checkValue(board3,j,0);
        }

        if (inBounds(i) && j >= boardSize) {
            return checkValue(board4,0,i);
        }
        return false;
    }

    /**
     * Checks the value for an index in a board.
     * @param board board to be checked
     * @param i     the first column index
     * @param j     the second column index
     * @return      <code>true</code> if the boolean value stored at this index is true
     */
    private boolean checkValue(List<List<Boolean>> board,int i,int j){
        return board.get(i).get(j);
    }

    /**
     * Sets <code>boolean</code> values to the board.
     * @param indexBoard the index of the current board in array of boards
     * @param x     the first column index
     * @param y     the second column index
     * @param value the <code>boolean</code> value to be set
     */
    public void setValue(int indexBoard,int x, int y, boolean value) {
        boardArrays[indexBoard].get(x).set(y, value);
    }

    /**
     * Returns the <code>boolean</code> value of the appointed position
     * @param indexBoard the index of the current board in array of boards
     * @param x the first column index
     * @param y the second column index
     * @return  the <code>boolean</code> value in this index
     */
    public boolean getValue(int indexBoard,int x, int y) {
        return boardArrays[indexBoard].get(x).get(y);
    }

    /**
     * Toggles the <code>boolean</code> value at the appointed index.
     * @param indexBoard the index of the current board in array of boards
     * @param x the first column index
     * @param y the second column index
     */
    public void toggleValue(int indexBoard, int x, int y) {
        boardArrays[indexBoard].get(x).set(y, !board2.get(x).get(y));
    }

    /**
     * Resets all values of the boards to false
     */
    public void clearBoards() {
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(0, i, j, false)));
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(1, i, j, false)));
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(2, i, j, false)));
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(3, i, j, false)));
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(4, i, j, false)));
        IntStream.range(0, getBoardSize()).forEach(i -> IntStream.range(0, getBoardSize()).forEach(j -> setValue(5, i, j, false)));
    }

    /**
     * Creates a two-dimensional ArrayList our of a boolean two-dimensional array.
     * @param array     the two-dimensional array to be converted
     * @return          a two-dimensional ArrayList with the same content as the input array
     */
    public List<List<Boolean>> createArrayListFromArray(boolean[][] array) {
        List<List<Boolean>> listArray = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            listArray.add(new ArrayList<>());
            for(int j = 0; j < array[0].length; j++){
                Boolean b = array[i][j];
                listArray.get(i).add(b);
            }
        }
        return listArray;
    }

    /**
     * Places the input array from filehandler into the board.
     * @param inputArray    the array loaded from file or URL
     * @param indexBoard the index of the board which will contain the new pattern
     */
    public void setInputInBoard(List<List<Boolean>> inputArray,int indexBoard) {
        // check if the input array is too large
        if(inputArray.size() > boardSize || inputArray.get(0).size() > boardSize) {
            Alert sizeErrorAlert = new Alert(Alert.AlertType.INFORMATION);
            sizeErrorAlert.setTitle("Error with size of pattern");
            sizeErrorAlert.setHeaderText("The pattern is too large for the board");
            sizeErrorAlert.showAndWait();
        } else {
            clearBoards();

            //find the corner to start placing the pattern
            int xStartIndex = (boardSize - inputArray.size()) / 2;
            int yStartIndex = (boardSize - inputArray.get(0).size()) / 2;

            // set new pattern in middle of board
            for (int i = 0; i < inputArray.size(); i++) {
                for (int j = 0; j < inputArray.get(0).size(); j++) {
                    Boolean value = inputArray.get(i).get(j);
                    setValue(indexBoard,i + xStartIndex, j + yStartIndex, value);
                }
            }
        }
    }

    /**
     * Initializes array containing lists.
     * @param board1    the first board to be put in the list
     * @param board2    the second board to be put in the list
     * @param board3    the third board to be put in the list
     * @param board4    the fourth board to be put in the list
     * @param board5    the fifth board to be put in the list
     * @param board6    the sixth board to be put in the list
     * @return          an array containing lists
     */
    private List<List<Boolean>>[] initArrayOfLists(List<List<Boolean>> board1,
                                  List<List<Boolean>>board2, List<List<Boolean>> board3, List<List<Boolean>> board4,
                                  List<List<Boolean>>board5, List<List<Boolean>> board6){
        List<List<Boolean>>[] arrayOfLists = new ArrayList[6];
        arrayOfLists[0] = board1;
        arrayOfLists[1] = board2;
        arrayOfLists[2] = board3;
        arrayOfLists[3] = board4;
        arrayOfLists[4] = board5;
        arrayOfLists[5] = board6;
        return arrayOfLists;
    }

    /**
     * Returns the array containing all the arrays with boolean values of the cube.
     * @return the array of arrays
     */
    List<List<Boolean>>[] getBoardArrays(){
        return boardArrays;
    }
}