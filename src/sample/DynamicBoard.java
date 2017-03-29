package sample;


import javafx.animation.AnimationTimer;
import sun.plugin.javascript.navig.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Miina Lervik
 * @author Elise Vannes
 * @author Alexander Kingdon
 */

public class DynamicBoard extends Board {
    private int original_x_size;
    private int original_y_size;
    public List<List<Boolean>> dynamicBoardArray = new ArrayList<List<Boolean>>(160);
    public List<List<Boolean>> clone;

    public DynamicBoard() {
    }

    /**
     * Constructs and initiates the playing board used for unit testing.
     * @param board     the board used instead of the default board
     */
    public DynamicBoard(List<List<Boolean>> board) {
        this.dynamicBoardArray = board;
    }

    /**
     * The method initializing the board with all values set to false
     */
    @Override
    public void initStartBoard(){
        for(int i = 0; i < x; i++) {
            dynamicBoardArray.add(i, new ArrayList<Boolean>(y));
        }

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                dynamicBoardArray.get(i).add(j,false);
            }
        }
    }

    @Override
    public void defaultStartBoard(){
        dynamicBoardArray.get(0).set(2,true);
        dynamicBoardArray.get(1).set(2,true);
        dynamicBoardArray.get(2).set(2,true);
        dynamicBoardArray.get(2).set(1,true);
        dynamicBoardArray.get(1).set(0,true);
    }

    @Override
    public void setValue(int x, int y, boolean value) {
        dynamicBoardArray.get(x).set(y, value);
    }

    @Override
    public boolean getValue(int x, int y) {
        return dynamicBoardArray.get(x).get(y);
    }

    @Override
    public void toggleValue(int x, int y) {
        dynamicBoardArray.get(x).set(y, !dynamicBoardArray.get(x).get(y));
    }

    @Override
    public int getWidth() {
        return dynamicBoardArray.size();
    }

    @Override
    public int getHeight() {
        return dynamicBoardArray.get(0).size();
    }

    @Override
    public void createClone() {
        clone = new ArrayList<List<Boolean>>(getWidth());

        for(int i = 0; i < getWidth(); i++) {
            clone.add(new ArrayList<>(getHeight()));

            for(int j = 0; j < getHeight(); j++) {
                clone.get(i).add(j, getValue(i, j));
            }
        }
    }

    @Override
    public void setCloneValue(int x, int y, boolean value) {
        clone.get(x).set(y, value);
    }

    @Override
    public void toggleBoards() {
        for(int i = 0; i < getWidth(); i++) {
            for(int j = 0; j < getHeight(); j++) {
                setValue(i, j, clone.get(i).get(j));
            }
        }
    }

    public void resetBoard() {

        IntStream.range(0, getWidth()).forEach(i -> IntStream.range(0, getHeight()).forEach(j -> setValue(i, j, false)));
    }


    @Override
    public String toString(){
        String boardStringOutput = "";
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                if (dynamicBoardArray.get(i).get(j)) {
                    boardStringOutput += "1";
                } else {
                    boardStringOutput += "0";
                }
            }
        }
        return boardStringOutput;
    }

    /**
     * The method creating an ArrayList our of an array.
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



    public void set_input_in_board(List<List<Boolean>> inputArray) {
        int inputX = inputArray.size();
        int inputY = inputArray.get(0).size();
        original_x_size = dynamicBoardArray.size();
        original_y_size = dynamicBoardArray.get(0).size();
        int diffX = inputX - original_x_size;
        int diffY = inputY - original_y_size;
        int width_loop_count;
        int height_loop_count;
        double factor;

        System.out.println(inputY);
        System.out.println(original_y_size);
        System.out.println(inputX);
        System.out.println(original_x_size);

        //check is the input array is bigger than dynamicBoardArray on both the x and y coordinates
        if( inputX > original_x_size && inputY > original_y_size){
            //check which of the factors is the largest
            if(inputX/x > inputY/original_y_size ){
                factor = (double)inputX/original_x_size;
                height_loop_count = (int)(factor*original_y_size)-original_y_size ;
                System.out.println("height loop: "+ height_loop_count);
                System.out.println("diffX: " + diffX);
                enlarge(diffX, height_loop_count);
            } else {
                factor = (double)inputY/original_y_size ;
                System.out.println(factor);
                width_loop_count = (int)(factor*original_x_size)-original_x_size;
                System.out.println("width loop: "+ width_loop_count);
                System.out.println("y = " + original_y_size);
                System.out.println("diffY: " + diffY);
                enlarge(width_loop_count, diffY);
            }
        //check if the input array is larger on the y coordinate
        } else if(inputY > original_y_size) {
            factor = (double)inputY/y;
            width_loop_count = (int)(factor*original_x_size)-original_x_size;
            enlarge(width_loop_count, diffY);
        //check if the input array is larger on the x coordinate
        } else if(inputX > original_x_size) {
            factor = (double)inputX/original_x_size;
            height_loop_count = (int)(factor*original_y_size)-original_y_size ;
            enlarge(diffX, height_loop_count);

        }

        //place the input array in the middle of dynamicBoardArray and transfer all values
        System.out.println("dynamic size: "+ dynamicBoardArray.size() +"  " +inputArray.size());
        System.out.println("dynamic get0: "+ dynamicBoardArray.get(0).size() +"  "+ inputArray.get(0).size());
        int xStartIndex = (dynamicBoardArray.size() - inputArray.size())/2;
        System.out.println("startindex X: " + xStartIndex);
        int yStartIndex = (dynamicBoardArray.get(0).size() - inputArray.get(0).size())/2;
        System.out.println("startindex y: " + yStartIndex);
        int xIndex = 0;

        /* ///INTSTREAM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        IntStream.range(0,10).forEach(e -> System.out.println(e));
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
        dynamicBoardArray.forEach(e-> e.forEach()));

        */

        for (int i = xStartIndex; i < inputArray.size()+xStartIndex; i++){
            int yIndex = 0;
            for(int j = yStartIndex; j < inputArray.get(0).size()+yStartIndex; j++){
                Boolean value = inputArray.get(xIndex).get(yIndex);
                dynamicBoardArray.get(i).set(j, value);
                yIndex++;
            }
            xIndex++;
        }

    }

    private void enlarge(int width_factor, int height_factor) {
        for(int f = 0; f < height_factor; f++) {
            for (int i = 0; i < original_x_size; i++) {
                dynamicBoardArray.get(i).add(Boolean.FALSE);
            }
            original_y_size++;
            System.out.println(dynamicBoardArray.size()+" "+ dynamicBoardArray.get(0).size());
        }

        for(int f = 0; f < width_factor; f++) {

            dynamicBoardArray.add(new ArrayList());
            for (int i = 0; i < original_y_size; i++) {
                dynamicBoardArray.get(original_x_size).add(i, Boolean.FALSE);
            }
            original_x_size++;
            System.out.println(dynamicBoardArray.size()+" "+ dynamicBoardArray.get(0).size());
        }

    }

    ////////////////////////   KUN FOR Å TESTE!
    public void print(List<List<Boolean>> array) {
        for(int i = 0; i < array.size(); i++) {
            for(int j = 0; j < array.get(0).size(); j++) {
                System.out.print(array.get(i).get(j) == true ? "■" : "□");
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
    }


}