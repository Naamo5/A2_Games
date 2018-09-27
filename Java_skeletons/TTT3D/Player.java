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

    if (nextStates.size() == 0 || depth == 0){
      v = eval3D(gameState);
      System.err.println(v);
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
  *  EVAL3D
  *
  */
  private float eval3D(GameState gameState){
    int player = playerMax;
    double eval = 0;
    int opponent = player % 2 + 1;
    int counterAtt;
    int counterDef;
    int holes;

    //Analyse the x-Layer
    for (int x = 0; x < gameState.BOARD_SIZE; x++){
      for (int y = 0; y < gameState.BOARD_SIZE; y++){
        counterAtt = 0;
        counterDef = 0;
        holes = 1;
        for (int z = 0; z < gameState.BOARD_SIZE; z++){
          if (gameState.at(x,y,z) == opponent){
            holes = 0;
            counterDef++;
          }
          if (gameState.at(x,y,z) == player) counterAtt++;
        }
        eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
        if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
      }

      //Sum the marks of the player in the diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(x,pos,pos) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(x, pos, pos) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

      //Sum the marks of the player in the anti-diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at( x, pos1, pos2) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(x, pos1, pos2) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
    }

    //Analyse the y-Layer
    for (int y = 0; y < gameState.BOARD_SIZE; y++){
      for (int z = 0; z < gameState.BOARD_SIZE; z++){
        counterAtt = 0;
        counterDef = 0;
        holes = 1;
        for (int x = 0; x < gameState.BOARD_SIZE; x++){
          if (gameState.at(x,y,z) == opponent){
            holes = 0;
            counterDef++;
          }
          if (gameState.at(x,y,z) == player) counterAtt++;
        }
        eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
        if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
      }

      //Sum the marks of the player in the diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(pos,y,pos) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(pos, y, pos) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

      //Sum the marks of the player in the anti-diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at( pos2, y, pos1) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(pos2, y, pos1) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
    }

    //Analyse the z-Layer
    for (int z = 0; z < gameState.BOARD_SIZE; z++){
      for (int x = 0; x < gameState.BOARD_SIZE; x++){
        counterAtt = 0;
        counterDef = 0;
        holes = 1;
        for (int y = 0; y < gameState.BOARD_SIZE; y++){
          if (gameState.at(x,y,z) == opponent){
            holes = 0;
            counterDef++;
          }
          if (gameState.at(x,y,z) == player) counterAtt++;
        }
        eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
        if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
      }

      //Sum the marks of the player in the diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
        if (gameState.at(pos,pos,z) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(pos, pos, z) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

      //Sum the marks of the player in the anti-diagonal
      counterAtt = 0;
      counterDef = 0;
      holes = 1;
      for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
        int pos2 = gameState.BOARD_SIZE-1-pos1;
        if (gameState.at( pos1, pos2, z) == opponent){
          holes = 0;
          counterDef++;
        }
        if (gameState.at(pos1, pos2, z) == player) counterAtt++;
      }
      eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
      if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);
    }




    //Analyse the 4 super-diagonal
        //Diag from (0,0,0) to (3,3,3)
    counterAtt = 0;
    counterDef = 0;
    holes = 1;
    for (int pos = 0; pos < gameState.BOARD_SIZE; pos++){
      if (gameState.at(pos,pos,pos) == opponent){
        counterDef++;
        holes = 0;
      }
      if (gameState.at(pos,pos,pos) == player) counterAtt++;
    }
    eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
    if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);


        //Diag from (3,0,0) to (0,3,3)
    counterAtt = 0;
    counterDef = 0;    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos1,pos2,pos2) == opponent){
        counterDef++;
        holes = 0;
      }
      if (gameState.at(pos1,pos2,pos2) == player) counterAtt++;
    }
    eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
    if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

        //Diag from (0,3,0) to (3,0,3)
    counterAtt = 0;
    counterDef = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos2,pos1,pos2) == opponent){
        counterDef++;
        holes = 0;
      }
      if (gameState.at(pos2,pos1,pos2) == player) counterAtt++;
    }
    eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
    if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

          //Diag from (0,0,3) to (3,3,0)
    counterAtt = 0;
    counterDef = 0;
    holes = 1;
    for (int pos1 = 0; pos1 < gameState.BOARD_SIZE; pos1++){
      int pos2 = gameState.BOARD_SIZE-1-pos1;
      if (gameState.at(pos1,pos1,pos2) == opponent){
        counterDef++;
        holes = 0;
      }
      if (gameState.at(pos1,pos1,pos2) == player) counterAtt++;
    }
    eval = eval + counterAtt*Math.pow(80,2*counterAtt)*holes;
    if (counterDef == gameState.BOARD_SIZE-1 && counterAtt == 0) eval = eval - Math.pow(80,2*gameState.BOARD_SIZE-1);

    float evalOut = (float)eval;
    return evalOut;
  }
}
