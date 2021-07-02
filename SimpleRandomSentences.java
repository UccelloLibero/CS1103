/**
This program generates random sentences.
If the conjunction is short then the program will print a simple sentence that is plausible.
If the conjunction is longer then it will print a silly sentence.

The program generates random sentences in a new row every two seconds until it is halted.
Way to halt try Control-C.
*/

import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public class SimpleRandomSentences {

    private static final String[] conjunction = { "and", "or", "but", "because" };
    private static final String[] properNoun = { "Fred", "Jane", "Richard Nixon", "Miss America" };
    private static final String[] commonNoun = { "man", "woman", "fish", "elephant", "unicorn" };
    private static final String[] determiner = { "a", "the", "every", "some" };
    private static final String[] adjective = { "big", "tiny", "pretty", "bald" };
    private static final String[] intransitiveVerb = { "runs", "jumps", "talks", "sleeps" };
    private static final String[] transitiveVerb = { "loves", "hates", "sees", "knows", "looks for", "finds" };

    public static void main(String[] args) {

      List<String[]> arrayList = new ArrayList<>();
      arrayList.add(conjunction);
      arrayList.add(properNoun);
      arrayList.add(commonNoun);
      arrayList.add(determiner);
      arrayList.add(adjective);
      arrayList.add(intransitiveVerb);
      arrayList.add(transitiveVerb);

      while (true) {
        sentence();
        System.out.println(".\n\n");
        try {
          Thread.sleep(2000);
        }
        catch (InterruptedException e) {

        }
      }
    }

    private static void sentence() {
      int c = (int)(Math.random()*conjunction.length);
      double x = Math.random();
      if (x < 0.2) {
        simpleSentence();
      }
      else{
        sillySentence();
      }
    }

    private static void simpleSentence() {
      nounPhrase();
      verbPhrase();
    }

    private static void sillySentence() {
      nounPhrase();
      System.out.print(" who is ");
      determinerPhrase();
      commonNounPhrase();
      transitiveVerbPhrase();
      adjectivePhrase();
      System.out.print(" surprise ");
      System.out.print(" while it ");
      verbPhrase();
    }

    private static void nounPhrase() {
      int pn = (int)(Math.random()*properNoun.length);
      System.out.print(" "+  properNoun[pn] + " ");
    }

    private static void determinerPhrase() {
      int dn = (int)(Math.random()*determiner.length);
      System.out.print(" "+ determiner[dn] + " ");
    }

    private static void commonNounPhrase() {
      int cn = (int)(Math.random()*commonNoun.length);
      System.out.print(" "+ commonNoun[cn] + " ");
    }

    private static void transitiveVerbPhrase() {
      int tv = (int)(Math.random()*transitiveVerb.length);
      System.out.print(" "+ transitiveVerb[tv] + " ");
    }

    private static void adjectivePhrase() {
      int a = (int)(Math.random()*adjective.length);
      System.out.print(" " + adjective[a] + " ");
    }

    private static void verbPhrase() {
      int iv = (int)(Math.random()*intransitiveVerb.length);
      System.out.print(" " +  intransitiveVerb[iv]);
    }

  }
