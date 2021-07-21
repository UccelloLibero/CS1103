public class TestHashTable {
  public static void main(String[] args) {

    // initial size of table is 2
    HashTable table = new HashTable();

    String key, value;

    while(true) {

      System.out.println("\nMenu:");
      System.out.println("1. test put(key, value)");
      System.out.println("2. test get(key)");
      System.out.println("3. test containsKey(key)");
      System.out.println("4. test remove(key)");
      System.out.println("5. show complete content of hash table.");
      System.out.println("6. EXIT");
      System.out.print("Enter your choice: ");

      switch (TextIO.getlnInt()) {
        case 1:
          System.out.print("\n Key = ");
          key = TextIO.getln();
          System.out.print("Value = ");
          value = TextIO.getln();
          table.put(key, value);
          break;
        case 2:
          System.out.print("\n Key = ");
          key = TextIO.getln();
          System.out.println("Value is " + table.get(key));
          break;
        case 3:
          System.out.print("\n Key = ");
          key = TextIO.getln();
          System.out.println("containsKey(" + key + ")" + " is " + table.containsKey(key));
          break;
        case 4:
          System.out.print("\n Key = ");
          key = TextIO.getln();
          table.remove(key);
          break;
        case 5:
          table.dump();
          break;
        case 6:
          return; // End program
        default:
          System.out.println(" Illegal input.");
          break;


      }
      System.out.println("\n Hash table size is "+ table.size());
    }
  }
}
