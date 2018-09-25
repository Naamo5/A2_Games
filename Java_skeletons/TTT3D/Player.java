import java.util.*;

public class Player {

  private static final int depthMax = 1;
  private int playerMax;
  private GameState nextMove;

  /**
  * Performs a move
  *
  * @param gameState
  *            the current state of the board
  * @param deadline
  *            time before which we must have returned
  * @return the next state the board is in after our move
  */
  public GameState play(final GameState gameState, final Deadline deadline) {
    Vector<GameState> nextStates = new Vector<GameState>();
    gameState.findPossibleMoves(nextStates);

    if (nextStates.size() == 0) {
      // Must play "pass" move if there are no other moves possible.
      return new GameState(gameState, new Move());
    }

    /**
    * Here you should write your algorithms to get the best next move, i.e.
    * the best next state. This skeleton returns a random move instead.
    */

    playerMax = gameState.getNextPlayer();
    alphabeta(gameState, depthMax, -Float.MAX_VALUE, Float.MAX_VALUE);

    return nextMove;
    // Random random = new Random();
    // return nextStates.elementAt(random.nextInt(nextStates.size()));
  }

  /**
  *  ALPHABETA
  *
  */
  private float alphabeta(GameState gameState, int depth, float alpha, float beta){
    // state : the current state we are analyzing
    // alpha : the current best value achievable by A
    // beta : the current best value achievable by B
    // returns the minimax value of the state

    Vector<GameState> nextStates = new Vector<GameState>();
    gameState.findPossibleMoves(nextStates);
    int player = gameState.getNextPlayer();

    float v;

    if (nextStates.size() == 0 || depth == 0) v = eval3D(gameState);

    else if (player == playerMax) {
      v = -Float.MAX_VALUE;

      //Let's initialize nextMove as the first child of the tree
      if (depth==depthMax) nextMove = nextStates.elementAt(0);

      for (int i = 0; i < nextStates.size(); i++){
        v = Math.max(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta));
        if (depth == depthMax && v>alpha) nextMove = nextStates.elementAt(i);
        alpha = Math.max(alpha,v);
        if (beta <= alpha) break;
      }
    }

    else {
      v = Float.MAX_VALUE;
      for (int i = 0; i < nextStates.size(); i++){
        v = Math.min(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta));
        beta  = Math.min(beta, v);
        if (beta <= alpha) break;
      }
    }
    return v;
  }


  private float eval3D(GameState gameState){
    int player = playerMax;
    float eval = 0;

    //Analyse the x-Layers
    for (int x = 0; x < gameState.BOARD_SIZE; x++){
      eval += evalLayer(xBoard(gameState,x), player);
    }

    //Analyse the y-Layers
    for (int y = 0; y < gameState.BOARD_SIZE; y++){
      eval += evalLayer(yBoard(gameState,y), player);
    }

    //Analyse the z-Layers
    for (int z = 0; z < gameState.BOARD_SIZE; z++){
      eval += evalLayer(zBoard(gameState,z), player);
    }


    int counter;
    int holes;
    double eval_temp = 0;

    //Analyse the 4 super-diagonal
        //Diag from (0,0,0) to (3,3,3)
    counter = 0;
    holes = 1;
    for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
      if (gameState.at(pos,pos,pos) != player && gameState.at(pos,pos,pos) != Constants.CELL_EMPTY) holes = 0;
      if (gameState.at(pos,pos,pos) == player) counter++;
    }
    eval_temp = eval_temp + Math.pow(10,counter)*holes;

        //Diag from (3,0,0) to (0,3,3)
    counter = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos1,pos2,pos2) != player && gameState.at(pos1,pos2,pos2) != Constants.CELL_EMPTY) holes = 0;
      if (gameState.at(pos1,pos2,pos2) == player) counter++;
    }
    eval_temp = eval_temp + Math.pow(10,counter)*holes;

        //Diag from (0,3,0) to (3,0,3)
    counter = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos2,pos1,pos2) != player && gameState.at(pos2,pos1,pos2) != Constants.CELL_EMPTY) holes = 0;
      if (gameState.at(pos2,pos1,pos2) == player) counter++;
    }
    eval_temp = eval_temp + Math.pow(10,counter)*holes;

          //Diag from (0,0,3) to (3,3,0)
    counter = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos1,pos1,pos2) != player && gameState.at(pos1,pos1,pos2) != Constants.CELL_EMPTY) holes = 0;
      if (gameState.at(pos1,pos1,pos2) == player) counter++;
    }
    eval_temp = eval_temp + Math.pow(10,counter)*holes;
    eval = eval + (float)eval_temp;

    return eval;
  }


  private float evalLayer(Vector<Vector<Integer>> board, int player){
    double eval = 0;
    int counter = 0;
    int holes;
    int size = board.size();

    //Sum the marks of the player in the rows
    for (int row = 0; row < size; row++){
      counter = 0;
      holes = 1;
      for (int col = 0; col < size; col++){
        if (board.get(col).get(row) != player && board.get(col).get(row) != Constants.CELL_EMPTY) holes = 0;
        if (board.get(col).get(row) == player) counter++;
      }
      eval = eval + Math.pow(10,counter)*holes;
    }

    //Sum the marks of the player in the Columns
    for (int col = 0; col < size; col++){
      counter = 0;
      holes = 1;
      for (int row = 0; row < size; row++){
        if (board.get(col).get(row) != player && board.get(col).get(row) != Constants.CELL_EMPTY) holes = 0;
        if (board.get(col).get(row) == player) counter++;
      }
      eval = eval + Math.pow(10,counter)*holes;
    }

    //Sum the marks of the player in the diagonal
    counter = 0;
    holes = 1;
    for (int pos = 0; pos < size; pos++){
      if (board.get(pos).get(pos) != player && board.get(pos).get(pos) != Constants.CELL_EMPTY) holes = 0;
      if (board.get(pos).get(pos) == player) counter++;
    }
    eval = eval + Math.pow(10,counter)*holes;

    //Sum the marks of the player in the anti-diagonal
    counter = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < size; pos1++){
      int pos2 = size-1-pos1;
      if (board.get(pos1).get(pos2) != player && board.get(pos1).get(pos2) != Constants.CELL_EMPTY) holes = 0;
      if (board.get(pos1).get(pos2) == player) counter++;
    }
    eval = eval + Math.pow(10,counter)*holes;

    float evalOut = (float)eval;
    return evalOut;
  }


  private Vector<Vector<Integer>> xBoard(GameState gameState, int x){
    int size = gameState.BOARD_SIZE;
    Vector<Vector<Integer>>  board= new Vector<Vector<Integer>>();
    for (int y = 0; y < size; y++){
      Vector<Integer> vector = new Vector<Integer>();
      for (int z = 0; z < size; z++){
        vector.add(gameState.at( x, y, z));
      }
      board.add(vector);
    }
    return board;
  }

  private Vector<Vector<Integer>> yBoard(GameState gameState, int y){
    int size = gameState.BOARD_SIZE;
    Vector<Vector<Integer>>  board= new Vector<Vector<Integer>>();
    for (int z = 0; z < size; z++){
      Vector<Integer> vector = new Vector<Integer>();
      for (int x = 0; x < size; x++){
        vector.add(gameState.at( x, y, z));
      }
      board.add(vector);
    }
    return board;
  }

  private Vector<Vector<Integer>> zBoard(GameState gameState, int z){
    int size = gameState.BOARD_SIZE;
    Vector<Vector<Integer>>  board= new Vector<Vector<Integer>>();
    for (int x = 0; x < size; x++){
      Vector<Integer> vector = new Vector<Integer>();
      for (int y = 0; y < size; y++){
        vector.add(gameState.at( x, y, z));
      }
      board.add(vector);
    }
    return board;
  }


}
