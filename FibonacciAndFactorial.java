/*
@author Maya
This program computes a fibonacci and factorial of a given non-negative integer relying on recursion.
The integer is supplied by the user relying on TextIO to capture the user input.

**/

import java.math.BigInteger;

public class FibonacciAndFactorial {

  // gather input from the user and evaluate it's validity
  public static void main(String[] args) {

    // initializing the number that will represent the non-negative integer for computing
    int N;

    while (true) {

      // provide instructions
      System.out.println("Please enter a positive integer.");

      System.out.println("(Enter 0 to exit the program.)");

      N = TextIO.getlnInt();

      if (N == 0) {
        System.out.println("Bye, Bye. Try again later.");
        break;
      }
      else if (N < 0) {
        System.out.println("Sorry but negative numbers are not allowed.");
        continue;
      }


      BigInteger NasBigInteger = BigInteger.valueOf(N);
      System.out.println(" factorial(" + N + ") is " + factorial(NasBigInteger) + ".");

      if (N > 40){
      System.out.println("The value of N is too grand to compute fibonacci(N) recursevly.");
      }
      else {
        System.out.println("fibonacci(" + N + ") is " + fibonacci(N) + "(recursevly).");
      }

      System.out.println("fibonacci(" + N + ") is " + fibonacci_nonrecursive(N) + "(non-recursevly).");
    }

  }

  // Compute fibonacci using recursion
  static int fibonacci(int N) {
    assert N >= 0 : "fibonacci(N) is only defined for non-negative N integers";
    assert N <= 40 : "N is too grand to compute fibonacci(N) recursevly";

    if (N == 0 || N == 1) {
      // base case: the answer is 1
      return 1;
    }
    else {
      // Recursive function that computes the answer
      return fibonacci(N - 1) + fibonacci(N - 2);
    }
  }

  // Compute fibonacci(N) using for loop
  static BigInteger fibonacci_nonrecursive(int N) {
    assert N >= 0: "fibonacci(N) is only defined for non-negative N integers";
    if (N == 0 || N == 1) {
      // fibonacci(0) = fibonacci(1) = 1;
      return BigInteger.ONE;
    }
    else {
      // fibonacci(i - 2)
      BigInteger f0 = BigInteger.ONE;
      // fibonacci(i - 1)
      BigInteger f1 = BigInteger.ONE;

      for (int i = 2; i <= N; i++) {
        // compute fibonacci(i)
        BigInteger fi = f0.add(f1);
        f0 = f1;
        f1 = fi;
      }
      // return fibonacci(N)
      return f1;
    }
  }

  // Compute factorial(N) using recursion
  static BigInteger factorial(BigInteger N) {
    assert N.signum() >= 0: "factorial(N) is only defined for non-negative N integers";

    if (N.equals(BigInteger.ZERO)) {
      // base case: the answer is 1
      return new BigInteger("1");
    }
    else {
      // recursive function that computes the answer
      BigInteger factorialOfNMinus1 = factorial(N.subtract(BigInteger.ONE));
      return N.multiply(factorialOfNMinus1);
    }
  }

}
