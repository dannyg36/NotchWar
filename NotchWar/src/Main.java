import java.util.ArrayList;

public class Main {
	
    private static Player[] players = new Player[2]; // Array to hold two players
    private static ArrayList<Card> warPile = new ArrayList<>();

    public static void main(String[] args) {
    	// Initialize each player in the array
        players[0] = new Player();
        players[1] = new Player();
    	Deck deck = new Deck();
    	deck.shuffle();
        deal(deck);
        playNotchWar();
    }
    
    public static void deal(Deck deck) {    // The deal method gives each player 26 cards at the start
        int count = 0;
        while (deck.size() > 0) {
            players[count % 2].put(deck.getCard());
            count++;
        }
    }
    
    public static void playRound() {      // Play one round of NotchWar
    	if (players[0].isEmpty() || players[1].isEmpty()) return;
    	
    	Card card0 = players[0].get();   // This is where the two cards are taken out
        Card card1 = players[1].get();
        
        int diff = Math.abs(card0.compareTo(card1));   // Compare the two cards to see who wins
        Player winner = determineWinner(card0, card1, diff);
        
        if (winner != null) {
            winner.put(card0);
            winner.put(card1);  // The winner should receive the two cards that were taken out of the players' decks above
        }
        else {
        	System.out.println("TIE");   // In the code below, when two people draw, winner returns null and that feeds into this if statement
        }
        
        System.out.println("Player 0 has " + players[0].size() + ", Player 1 has " + players[1].size());
        
    }
    
    private static Player determineWinner(Card card0, Card card1, int diff) {
        if (diff == 0) {
        	System.out.println(card0 + " versus " + card1);
        	System.out.println("WAR!");
            return war(players[0], players[1], card0, card1);   // If the two cards are the same rank, then we go to war
        } 
        else if (diff == 1) {
        	System.out.println(card0 + " versus " + card1 + " (Notched!)");
            return card0.getRank() < card1.getRank() ? players[0] : players[1];  // If the two cards are one apart, we check which one is smaller and that one wins
        } 
        else {
        	System.out.println(card0 + " versus " + card1);
            return card0.getRank() > card1.getRank() ? players[0] : players[1];  // Bigger card wins
        }
    }
    
    public static Player war(Player p0, Player p1, Card card0, Card card1) {
        Card FirstCard0 = card0;  // Need to store these first two cards because they get taken out later
        Card FirstCard1 = card1;
        Card lastFaceUpCardP0 = null;
        Card lastFaceUpCardP1 = null;
        
            int player0CardsNeeded = Math.min(p0.size(), 4);  // Finding the minimum amount of cards needed
            int player1CardsNeeded = Math.min(p1.size(), 4);

            ArrayList<Card> tempCards0 = drawCardsForWar(p0, player0CardsNeeded);  // These are the cards that each player draws for war
            ArrayList<Card> tempCards1 = drawCardsForWar(p1, player1CardsNeeded);
            
            // Ensure there are cards to play face-up
            if (!tempCards0.isEmpty()) {
                lastFaceUpCardP0 = tempCards0.remove(tempCards0.size() - 1);
            }
            if (!tempCards1.isEmpty()) {
                lastFaceUpCardP1 = tempCards1.remove(tempCards1.size() - 1);
            }
            
            if (lastFaceUpCardP0 != null) warPile.add(lastFaceUpCardP0);
            if (lastFaceUpCardP1 != null) warPile.add(lastFaceUpCardP1);

            // Checking if one of the players can't continue to provide a fourth card
            if (tempCards0.size() < 3 || tempCards1.size() < 3) {
                // Handling the scenario where one player runs out of cards or cannot provide enough cards
                Card faceUp0 = lastFaceUpCardP0;  // Player 0's last available face-up card
                Card faceUp1 = lastFaceUpCardP1;  // Player 1's last available face-up card

                // Ensuring null checks for scenario where one of the players had no cards to contribute
                if (faceUp0 == null || faceUp1 == null) {
                    if (faceUp0 == null && faceUp1 != null) {
                        faceUp0 = FirstCard0;
                    } 
                    else if (faceUp1 == null && faceUp0 != null) {
                        faceUp1 = FirstCard1;
                    } 
                    else {
                        // Both have no cards to compare, decide based on existing piles
                        Player winner = null;
                        return winner;
                    }
                }
                return warHelp(p0, p1, faceUp0, faceUp1, tempCards0, tempCards1, warPile);
            }
            else {
                return warHelp(p0, p1, lastFaceUpCardP0, lastFaceUpCardP1, tempCards0, tempCards1, warPile);
            }
        }
    
    public static Player warHelp(Player p0, Player p1, Card card0, Card card1, ArrayList<Card> tempCards0, ArrayList<Card> tempCards1, ArrayList<Card> pile) {
    	int compareResult = card0.compareTo(card1);
    	Player winner = null;
        if (compareResult != 0) {
        	if (Math.abs(compareResult) == 1) {
            	System.out.println(card0 + " versus " + card1 + " (WarNotched!)");  // Checking if cards flipped for war are one bigger/smaller than the other 
                winner = card0.getRank() < card1.getRank() ? p0 : p1;
            }
        	else if (compareResult > 0) {
                System.out.println(card0 + " versus " + card1);  // If card0 > card1, p0 wins
        		winner = p0;
        	}
        	else {
        		System.out.println(card0 + " versus " + card1);  // If card1 > card0, p1 wins
        		winner = p1;
        	}
            pile.addAll(tempCards0);
            pile.addAll(tempCards1);
            winner.addAll(pile);      // card0 isn't the same rank as card1, so add all of the unflipped cards from war to a pile and add that pile to the winner's deck
            pile.clear();
            return winner;
        } else {
        	System.out.println(card0 + " versus " + card1);
            System.out.println("More WAR!");   // card0 is the same rank as card1. Add the unflipped cards from war to a pile and go through war again
            pile.addAll(tempCards0);
            pile.addAll(tempCards1);
            return war(p0, p1, card0, card1);
        }
    }
    
    private static ArrayList<Card> drawCardsForWar(Player player, int count) {    // Simple function to draw the top cards of a player's deck for war
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !player.isEmpty(); i++) {
            cards.add(player.get());
        }
        return cards;
    }

    private static void playNotchWar() {     // Function that plays the war
        while (!players[0].isEmpty() && !players[1].isEmpty()) {
            playRound();
        }
        System.out.println("The winner is... Player " + (players[0].isEmpty() ? 1 : 0) + "!");
    }
}