public class Tape {

  public Cell currentCell;

  // Default constructor
  // Initializes the tape with single empty cell
  public Tape() {
    this.currentCell = new Cell();
  }

  // Method to get the current cell
  // This method returns the pointer that points to the current cell
  public Cell getCurrentCell() {
    return currentCell;
  }

  // Method to get and set the content at the current cell
  // This method returns the character from the current cell
  public char getContent() {
    return currentCell.getContent();
  }

  // Method that changes the character in the current cell to the specified value
  public void setContent(char ch) {
    currentCell.setContent(ch);
  }

  // Method that moves the current cell one position to the left along the tape
  // If the current cell is the leftmost cell that exists then a new cell will be created and
  // added to the tape at the left of the current cell and then the current cell pointer
  // will be moved to point to the new cell
  public void moveLeft() {
    if (currentCell.getPrev() == null) {
      Cell newCell = new Cell();
      newCell.setNext(currentCell);
      currentCell.setPrev(newCell);
    }

    // move tape to left
    currentCell = currentCell.getPrev();
  }


  // Method that moves the current cell one position to teh right along the tape.
  // If the current cell is the rightmost cell that exists, then a new cell will be created and
  // addedd to the tape at the right of the current cell and then the current cell pointer
  // will be moved to poin to the new cell
  public void moveRight() {
    if (currentCell.getNext() == null) {
      Cell newCell = new Cell();
      newCell.setPrev(currentCell);
      currentCell.setNext(newCell);
    }

    // move tape to right
    currentCell = currentCell.getNext();
  }

  // Method that returns a string consisting of the chars from all the cells on the tape read
  // left to right except that leading or trailing black characters will be discarded
  // Pointer does not move here
  public String getTapeContents() {

    // Temporary cell to traverse the tape and fetch content
    Cell tempCell = currentCell;

    // Moving the pointer to first cell of the tape
    while (tempCell.getPrev() != null) {
      tempCell = tempCell.getPrev();
    }

    // String to store the contect of the tape
    String contents = "";

    // String to store temporary parts of teh tape with spaces
    String pendingSpaces = "";

    // Traverse the tape from left to right
    while (tempCell != null) {
      // If the first cell is not empty we first add any temporary sequances previously observed to the contents
      // Then add the current cell content
      if (tempCell.getContent() != " ") {
        contents += pendingSpaces + tempCell.getContent();
        pendingSpaces = "";
      }
      else {
        // If the current cell has a blank space, we add it to the pendingSpaces String
        pendingSpaces += tempCell.getContent();
      }

      tempCell = tempCell.getNext();

    }

    return contents;
  }

}
