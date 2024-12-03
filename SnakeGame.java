
//import Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.*;
public class SnakeGame {

    static int HEIGHT;
    static int WIDTH;

    char[][] grid;

    LinkedList<Point> snake = new LinkedList<>();

    static char FOOD ='*';

    Queue<Point> foodQueue = new LinkedList<>();

    boolean isGameOver = false;

    int score =0;
    Stack<Direction> momentStack = new Stack<>();





    /**
     * Initializes the game grid with empty spaces.
     */
    public void initializeGrid(int HEIGHT, int WIDTH) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        if(HEIGHT <3 || HEIGHT>10 || WIDTH<3 || WIDTH>10){
            System.out.println("The Grid dimensions must be between 3 and 10 inclusive");
            return;
        }
        grid = new char[HEIGHT][WIDTH];
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                grid[i][j] = ' ';
            }
        }

    }

    /**
     * Initializes the snake at the center of the grid.
     */
    public void initializeSnake() {

        int startX = HEIGHT/2;
        int startY = WIDTH/2;
        Point startingPoint = new Point(startX,startY);
        snake.add(startingPoint);
        grid[startX][startY] ='o';
    }

    /**
     * Spawns food on the grid.
     */
    public void spawnFood() {
        int emptyCells =0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]==' '){
                    emptyCells++;
                }
            }
        }

        if(emptyCells ==0){
            regenerateGrid();
            if(isGameOver()){
                System.out.println("Game is over.....");
            }
        }
        if(!isGameOver()){
            int randomX = 0 + (int)(Math.random() * ((HEIGHT -1 - 0) + 1));
            int randomY = 0 + (int)(Math.random() * ((WIDTH -1 - 0) + 1));

            while(grid[randomX][randomY]!=' '){
                 randomX = 0 + (int)(Math.random() * ((HEIGHT -1 - 0) + 1));
                 randomY = 0 + (int)(Math.random() * ((WIDTH -1 - 0) + 1));
            }
            Point randomPoint = new Point(randomX, randomY);
            foodQueue.add(randomPoint);
            grid[randomX][randomY] = FOOD;

        }
    }

    /**
     * Regenerates the grid if needed.
     */
    private void regenerateGrid() {
        int food =0;
        int snakebodyLength =0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]==FOOD){
                    food++;
                }
                else if (grid[i][j]=='o'){
                    snakebodyLength++;
                }
            }
        }

        if(food==grid.length*grid[0].length){
            for(int i=0;i<grid.length;i++){
                for(int j=0;j<grid[0].length;j++){
                    grid[i][j] = ' ';
                }
            }

            initializeSnake();

        }
        else if (snakebodyLength == grid.length*grid[0].length){
            isGameOver = true;
            return;
        }


    }



    /**
     * Moves the snake in the specified direction.
     *
     */
    public void move(Direction direction) {
        momentStack.add(direction);
        regenerateGrid();
        if(isGameOver()){
            System.out.println("There is no further movement");
            return;
        }
        Point currentHead =  snake.getFirst();
        Point newHead = calculateNewHead(currentHead,direction);
        if(newHead.x<0 || newHead.x>=grid.length || newHead.y<0 || newHead.y>=grid[0].length){
            isGameOver = true;
            if(isGameOver()){
                System.out.println("Snake got hit to wall so game over");
                return;
            }
        }

        if(grid[newHead.x][newHead.y]==FOOD){
            score++;
            grid[newHead.x][newHead.y]=' ';
            spawnFood();
        }

        else if(isSnakeBody(newHead)){
            isGameOver = true;
            if(isGameOver()){
                System.out.println("Snake has eaten it's own body");
            }
        }

        else{
            Point tail = snake.removeLast();
            grid[tail.x][tail.y]=' ';
            snake.addFirst(newHead);
            grid[newHead.x][newHead.y]='o';
        }




    }

    /**
     * Checks if a given point is part of the snake's body.
     *
     */
    private boolean isSnakeBody(Point point) {
        if(grid[point.x][point.y]=='o'){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Calculates the new head position based on the current head and direction.
     *
     */
    private Point calculateNewHead(Point head, Direction direction) {
        if(direction.equals(Direction.UP)){
            Point newHead = new Point(head.x-1,head.y);
            return newHead;
        }
        else if(direction.equals(Direction.DOWN)){
            Point newHead = new Point(head.x+1,head.y);
            return newHead;
        }
        else if(direction.equals(Direction.LEFT)){
            Point newHead = new Point(head.x,head.y-1);
            return newHead;
        }
        else{
            Point newHead = new Point(head.x,head.y+1);
            return newHead;
        }
    }

    /**
     * Draws the current state of the grid.
     */
    public void draw() {
        if(grid==null || grid.length==0){
            System.out.println("grid can't be null");
            return;
        }
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }

    }

    /**
     * Checks if the game is over.
     *
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the current score.
     *
     */
    public int getScore() {
        return score;
    }

    /**
     * Main method to run the Snake Game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
            SnakeGame game = new SnakeGame();
            game.initializeGrid(5,5);
            game.initializeSnake();
            game.spawnFood();
            while(game.isGameOver()==false){
                game.draw();
                Scanner DirObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Enter direction (U/D/L/R)");
                String dir = DirObj.nextLine();
                String Dir = dir.toUpperCase();
                if(Dir.equals("U")){
                    game.move(Direction.UP);
                }
                else if(Dir.equals("D")){
                    game.move(Direction.DOWN);
                }
                else if(Dir.equals("R")){
                    game.move(Direction.RIGHT);
                }
                else if(Dir.equals("L")){
                    game.move(Direction.LEFT);
                }
                else{
                    System.out.println("Invalid input move");
                }

            }

            System.out.println("Game over! Your score:" + " " + game.getScore());


    }
}




