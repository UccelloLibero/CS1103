/**
  A program that administers and grades math quiz with randomly generated questions of different kinds.
  Questions ask the user to perform simple addition, subtraction, multiplication and division problems.
  Each question selects a problem randomly, and the numbers that occur in the problem are randomly generated.
  User has two chances to answer the question; first try gives full credit, second try if correct half credit.
  If after second try answer is incorrect notify and compute the correct answer.
  When finished print out number of correct, incorrect and score.
*/

import java.io.*;
import java.util.Random;

public class RandomMathQuiz {

  public static void main(String[] args) {

    int num1;
    int num2;
    int operator;
    int question;
    int answer;
    int correct;
    int incorrect;
    int score = 0;

    System.out.println("Hi, what is your name?");
    String name = TextIO.getln();
    System.out.println("Hello " + name + ". Please answer the following questions.");

    Random generator = new Random();

    // addition
    public int AdditionProblem() {
      num1 = generator.nextInt(50);
      num2 = generator.nextInt(50);
      answer = num1 + num2;
      return answer;
    }

    // subtraction
    public int SubtracitonProblem() {
      num1 = generator.nextInt(50);
      num2 = generator.nextInt(50);
      answer = num1 - num2;
      return answer;
    }

    // multiplication
    public int MultiplicationProblem() {
      num1 = generator.nextInt(50);
      num2 = generator.nextInt(50);
      answer = num1 * num2;
      return answer;
    }

    // division
    public int DivisionProblem() {
      num1 = generator.nextInt(50);
      num2 = generator.nextInt(50);
      answer = num1 / num2;
      return answer;
    }


    while(true) {

      operator = generator.nextInt(4);

      switch (operator) {
        // AdditionProblem
        case 0:
          System.out.println(num1 + " + " + num2 + " = ");
          AdditionProblem();
          break;
        // SubtractionProblem
        case 1:
          System.out.println(num1 + " - " + num2 + " = ");
          SubrtacitonProblem();
          break;
        // MultiplicationProblem
        case 2:
          System.out.println(num1 + " * " + num2 + " = ");
          MultiplicationProblem();
          break;
        // DivisionProblem
        case 3:
          System.out.println(num1 + " / " + num2 + " = ");
          DivisionProblem();
          break;
      }

      int userInput = TextIO.getln();

      if (userInput == answer) {
        correct++;
        System.out.println("You are correct.");
        score += 10;
      }
      else {
        incorrect++;
        System.out.println("You are incorrect. The correct answer is " + answer + ".");
        score -= 5;
      }
    }

  }





}
