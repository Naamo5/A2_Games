import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    private static final int depthMax = 3;
    private int playerMax;
    private GameState nextMove;

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
        //minimax(gameState,depthMax);
        alphabeta(gameState, depthMax, -Float.MAX_VALUE, Float.MAX_VALUE);

        return nextMove;

        //Random random = new Random();
        //return nextStates.elementAt(random.nextInt(nextStates.size()));
    }


    /**
     *  MINIMAX
     *
     */
    private float minimax(GameState gameState, int depth){
      // state : the current state we are analyzing
      // returns a heuristic value that approximates a utility function of the state
      Vector<GameState> nextStates = new Vector<GameState>();
      gameState.findPossibleMoves(nextStates);
      int player = gameState.getNextPlayer();

      if (nextStates.size() == 0 || depth == 0) return evalNbrMarkDef(gameState);

      else{
        float v;
        if (player == playerMax){
          float bestPossible = -Float.MAX_VALUE;

          //Let's initialize nextMove as the first child of the tree
          if (depth==depthMax) nextMove = nextStates.elementAt(0);

          for (int i = 0; i < nextStates.size(); i++){
            v = minimax(nextStates.elementAt(i), depth-1);
            if (depth == depthMax && v>bestPossible) nextMove = nextStates.elementAt(i);
            bestPossible = Math.max(bestPossible, v);
          }
          return bestPossible;
        }

        else{
          float bestPossible = Float.MAX_VALUE;
          for (int i = 0; i < nextStates.size(); i++){
            v = minimax(nextStates.elementAt(i), depth-1);
            bestPossible  = Math.min(bestPossible, v);
          }
          return bestPossible;
        }
      }
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

      if (nextStates.size() == 0 || depth == 0){
        v = evalNbrMarkDef(gameState);
        //System.err.println(v);
      }

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



    /**
     *  EVALSIMPLE
     *
     */
    private float evalSimple(GameState gameState){
      int player = playerMax;
      float eval = 0;

      //Sum the marks of the player in the rows
      for (int row = 0; row < gameState.BOARD_SIZE; row++){
        for (int col = 0; col < gameState.BOARD_SIZE; col++){
            if (gameState.at(row, col) == player) eval++;
        }
      }

      //Sum the marks of the player in the Columns
      for (int col = 0; col < gameState.BOARD_SIZE; col++){
        for (int row = 0; row < gameState.BOARD_SIZE; row++){
          if (gameState.at(row, col) == player) eval++;
        }
      }

      //Sum the marks of the player in the diagonal
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(pos, pos) == player) eval++;
      }

      //Sum the marks of the player in the anti-diagonal
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at(pos1,pos2) == player) eval++;
      }

      return eval;
    }

    private float evalNbrMark(GameState gameState){
      int player = playerMax;
      double eval = 0;
      int counter = 0;

      //Sum the marks of the player in the rows
      for (int row = 0; row < gameState.BOARD_SIZE; row++){
        counter = 0;
        for (int col = 0; col < gameState.BOARD_SIZE; col++){
            if (gameState.at(row, col) == player) counter++;
        }
        eval = eval + Math.pow(10,counter);
      }

      //Sum the marks of the player in the Columns
      for (int col = 0; col < gameState.BOARD_SIZE; col++){
        counter = 0;
        for (int row = 0; row < gameState.BOARD_SIZE; row++){
          if (gameState.at(row, col) == player) counter++;
        }
        eval = eval + Math.pow(10,counter);
      }

      //Sum the marks of the player in the diagonal
      counter = 0;
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(pos, pos) == player) counter++;
      }
      eval = eval + Math.pow(10,counter);

      //Sum the marks of the player in the anti-diagonal
      counter = 0;
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at(pos1,pos2) == player) counter++;
      }
      eval = eval + Math.pow(10,counter);

      float evalOut = (float)eval;
      return evalOut;
    }

    private float evalNbrMarkDef(GameState gameState){
      int player = playerMax;
      double eval = 0;
      int counter = 0;
      int holes = 1;

      //Sum the marks of the player in the rows
      for (int row = 0; row < gameState.BOARD_SIZE; row++){
        counter = 0;
        holes = 1;
        for (int col = 0; col < gameState.BOARD_SIZE; col++){
          if (gameState.at(row, col) != player) holes = 0;
          if (gameState.at(row, col) == player) counter++;
        }
        eval = eval + Math.pow(10,counter)*holes;
      }

      //Sum the marks of the player in the Columns
      for (int col = 0; col < gameState.BOARD_SIZE; col++){
        counter = 0;
        holes = 1;
        for (int row = 0; row < gameState.BOARD_SIZE; row++){
          if (gameState.at(row, col) != player) holes = 0;
          if (gameState.at(row, col) == player) counter++;
        }
        eval = eval + Math.pow(10,counter)*holes;
      }

      //Sum the marks of the player in the diagonal
      counter = 0;
      holes = 1;
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(pos, pos) != player) holes = 0;
        if (gameState.at(pos, pos) == player) counter++;
      }
      eval = eval + Math.pow(10,counter)*holes;

      //Sum the marks of the player in the anti-diagonal
      counter = 0;
      holes = 1;
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at(pos1, pos2) != player) holes = 0;
        if (gameState.at(pos1, pos2) == player) counter++;
      }
      eval = eval + Math.pow(10,counter)*holes;

      float evalOut = (float)eval;
      return evalOut;
    }

}
