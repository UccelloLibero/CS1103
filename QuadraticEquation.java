/**
  This program can compute and print two possible solutions to a quadratic equation of the form:
  A * X * X + B * X + C = 0 where the values for A, B and C are provided by the user.
  Two possible solutions can be calculated based on the quadratic folmula which states that
  X is equal to -B plus or minus the square root of B * B minus 4 * A * C, all of that over 2 * A
  There are two return statements which would need to be commented/uncommented appropriately in order to use both plus and minus version of the formula
  Maybe consider combining two roots into array and returning the array instead of only one root at a time 
  Non- Standard class TextIO is in charge for capturing the user input.
  TextIO code provided by the professor
*/

public class QuadraticEquation {

  public static void main(String[] args) {

    // coefficients of the equation
    double A, B, C;

    // one possible solution computed for the QuadraticEquation if discriminant is greater than 0
    double solution;

    // value set to true if the user wants to solve antoher equation
    boolean goAgain;

    // display instruction for the program
    System.out.println("***** Let's solve some quadratic equations! *****");
    System.out.println("***** Remember that the form of the equation looks like this: *****");
    System.out.println("***** A * X * X + B * X + C = 0 *****");
    System.out.println("***** Please provide the values for A, B and C. *****");


    // do..while loop that will run until the user decided to stop
    do {

      System.out.println();
      System.out.println();

      // get value for A
      System.out.println("Please enter value for A: ");
      A = TextIO.getlnDouble();

      // get value for B
      System.out.println("Please enter value for B: ");
      B = TextIO.getlnDouble();

      // get value for C
      System.out.println("Please enter value for C: ");
      C = TextIO.getlnDouble();

      System.out.println();

      // try..catch statement to compute the solutions
      try {
        solution = root(A, B, C);

        // print out the two possible solutions
        System.out.println("One possible solution of the equations is:  " + solution);
      }
      catch (IllegalArgumentException e) {
        System.out.println("Hmmm, no possible solution based on the values you provided.");
        System.out.println(e.getMessage());
      }

      // ask to go goAgain
      System.out.println();
      System.out.println("Do you want to try again?");
      // set the value for goAgain based on user's input
      goAgain = TextIO.getlnBoolean();

    } while (goAgain);

  }

  // a subrutine to calculate the square root of the equation and returns the larger of the two
  // throws an error if the coefficient A is 0 or negative number
  static public double root(double A, double B, double C) throws IllegalArgumentException {
    if (A == 0) {
      throw new IllegalArgumentException("The value of A cannot be 0.");
    }
    else {
      double determinant = B * B - 4 * A * C;
      if (determinant < 0) {
        throw new IllegalArgumentException("Determinant is less than 0.");
      }
      return (-B + Math.sqrt(determinant)) / (2 * A); // this is only one possible option

      // uncomment the line of code bellow  and comment the other return statement to check another posible solution of the equation
      // return (-B - Math.sqrt(determinant)) / (2 * A); // this is another option
    }

  }
}
