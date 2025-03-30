import java.util.*;

public class Deck {  // This is slow--can you think of a better way?  (There are many...)
	HashSet myCards = new HashSet();
	char[] suits = { 'S', 'H', 'C', 'D' };
	char[] ranks = { 'A', 'K', 'Q', 'J', 'T',
					 '9', '8', '7', '6', '5',
					 '4', '3', '2' };
	Iterator cardGetter;   // This is used in a really ugly way
	Random random = new Random();
		
	public Deck() {
		init();
	}
	
	private void init() {
		for (int i = 0; i < suits.length; i++)
			for (int j = 0; j < ranks.length; j++)
				myCards.add(new Card(ranks[j], suits[i]));
		cardGetter = myCards.iterator();
	}
	
	// Note why we need to adhere to abstractions.  The shuffle 
	// method doesn't really shuffle at all upon inspection.  
// However, in conjunction with getCard(), the behavior of
// shuffling and getting cards from the deck is perfectly
// reasonable.
	//
	// While I would not recommend writing shuffle() this way in 
// general, the point is that the Deck abstraction should be
// treated like a black box, not an open book.

	public void shuffle() {
		myCards.clear();
		init();
	}
	
	public Card getCard() {
		Card c;
		cardGetter = myCards.iterator(); 
		int next = random.nextInt(size()) + 1;
		c = (Card) cardGetter.next();
		for (int i = 1; i < next; i++) {
			c = (Card) cardGetter.next();
		}
		cardGetter.remove();  // Note that if a card is dealt, 
		return c;             // it is taken out of the deck
	}
	
	public boolean isEmpty() {
		return myCards.isEmpty();
	}
	
	public int size() {
		return myCards.size();
	}
}
