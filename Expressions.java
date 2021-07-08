/**
 Creating a class for experimenting with expression trees.
 This class includes a nested abstrace class and several subclasses that
 represent nodes in an expression tree.
*/

public class Expressions {

  // The main routine that tests some of the things that are defined bellow.
  public static void main(String[] args) {

    System.out.println("Testing expression creation and evaluation.\n");

    ExpNode e1 = new Bin0pNode("+", new VariableNode(), new ConstNode(3));
    ExpNode e2 = new Bin0pNode("*", new ConstNode(2), new VariableNode());
    ExpNode e3 = new Bin0pNode("-", e1, e2);
    ExpNode e4 = new Bin0pNode("/", e1, new ConstNode(-3));

    System.out.println("For x = 3: ");
    System.out.println("  " + e1 + " = " + e1.value(3));
    System.out.println("  " + e2 + " = " + e2.value(3));
    System.out.println("  " + e3 + " = " + e3.value(3));
    System.out.println("  " + e4 + " = " + e4.value(3));

    System.out.println("nTesting copying...");
    System.out.println("  copy of " + e1 + " gives " + copy(e1));
    System.out.println("  copy of " + e2 + " gives " + copy(e2));
    System.out.println("  copy of " + e3 + " gives " + copy(e3));
    System.out.println("  copy of " + e4 + " gives " + copy(e4));

    // make a copy of e3, where e3.left is e1
    ExpNode e3copy = copy(e3);
    // make a mdification to e1
    ((Bin0pNode)e1).left = new ConstNode(17);
    System.out.println("  modified e3: " + e3 + "; copy should be unmodified: " + e3copy);

    System.out.println("nChecking test data...");
    double[][] dt = makeTestData();
    for (int i = 0; i < dt.length; i++) {
      System.out.println("  x = " + dt[i][0] + "; y = " + dt[i][1]);

    }
  }

  // Method that makes a full copy of the tree because the ExpNode is
  // the root of an expression tree.
  // The tree that is returned is constructed entierly of new nodes meaning
  // that there are no pointeres back into the old copy.

  static ExpNode copy(ExpNode root) {

    if (root instanceof ConstNode) {
      return new ConstNode(((ConstNode).root).number);
    }
    else if (root instanceof VariableNode) {
      return new VariableNode();
    }
    else {
      Bin0pNode node = (Bin0pNode)root;
      // Left and right operand trees have to be copied not only referenced
      return new Bin0pNode(node.op, copy(node.left). copy(node.right));
    }
  }

  // Method that returns an n-by-2 array containing sample input/output pairs.
  // If the returned value is called data, then data[i][0] is the i-th input (or x)
  // value and data[i][1] is the corresponding output (or y) value.
  // This method is used only for testing.

  static double[][] makeTestData() {
    double[][] data = new double[21][2];
    double xmax = 5;
    double xmin = -5;
    double dx = (xmax - xmin) / (data.length -1);
    for (int i = 0; i < data.length; i++) {
      double x = xmin + dx * i;
      double y = 2.5 * x * x * x - x * x / 3 + 3 * x;
      data[i][0] = x;
      data[i][1] = y;
    }
    return data;
  }

  // Definitions of Expression node subclasses
  // An abstract class that represents a genreal node in an expression tree.
  // Every such node must be able to compute its own value at a given input value, x.
  // Also, nodes should override the standard toString() method to return a fully parameterized
  // string representation of the expression.

  static abstract class ExpNode {
    abstract double value(double x);
    // toString() should be defined in each subclass
  }

  // A node in an expression tree that represents a constant numerical value.
  static class ConstNode extends ExpNode {
    // the number of this node
    double number;
    ConstNode(double number) {
      this.number = number;
    }

    double value(double x) {
      return number;
    }

    public String toString() {
      if (number < 0) {
        // adding parentheses around the negative number
        return "(" + number + ")";
      }
      else {
        // convert the number into a string
        return "" + number;
      }
    }
  }

  // A node in an expression tree that represents the variable x.
  static class VariableNode extends ExpNode {
    VariableNode() {}

    double value(double x) {
      return x;
    }

    public String toString() {
      return "x";
    }
  }

  // A node in an expression tree that represents one of the binary operators:
  // +, -, * or /
  static class Bin0pNode extends ExpNode {
    // the operand which must be one of the following: +, -, * or /
    char op;

    // the expression tree for the left and right operands
    ExpNode left, right;

    Bin0pNode(char op, ExpNode left, ExpNode right) {
      if (op != "+" && op != "-" && op != "*" && op != "/") {
        throw new IllegalArgumentException("'" + op + "' is not a legal operator to perform the calculation.");
      }
      this.op = op;
      this.left = left;
      this.right = right;
    }

    double value(double x) {
      // value of the left operand expression tree
      double a = left.value(x);

      // value of the right operand expression tree
      double b = right.value(x);

      switch (op) {
        case "+":
          return a + b;
        case "-":
          return a - b;
        case "*":
          return a * b;
        default:
          return a / b;
      }
    }

    public String toString() {
      return "(" + left.toString() + op + right.toString() + ")";
    }
  }
}
