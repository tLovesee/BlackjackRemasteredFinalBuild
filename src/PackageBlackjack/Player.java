package PackageBlackjack;


import java.util.ArrayList;
import java.util.Random;


public class Player{
    static Random rand = new Random();

//Variables
    ArrayList<Card> playerDeck = new ArrayList<Card>();
    int score;
    boolean isBust;
    boolean hasAce;
    boolean hasUsedAce;
    int money;

    public Player(){
        this.score = 0;
        this.money = 100;
    }

//Gets

    public ArrayList<Card> getPlayerDeck() {
        return playerDeck;
    }
    
    public int getScore(){
        return score;
    }

    public boolean getBust(){
        return isBust;
    }
//Sets
    public void setScore(int ns){
        score+=ns;
    }
    public void setBust(boolean nb){
        isBust = nb;
    }
    public int getMoney() {
    	return money;
    }

    

    
    
    public void addCard(Card c){

        
        //Creates the card via constructor
        
        
        //Adds the card to the deck
        playerDeck.add(c);
        
        if(c.getCardType().equals("Ace")) {
        	hasAce = true;
        }
        
        
        //Runs if your score is over 21 and you haven't reversed an ace yet
        if( (score + c.cardValue) > 21 && this.hasUsedAce == false) {
        	
        	//Changes the first ace found
        	for (Card i : playerDeck) {
        		
        		//finds the first ace in your deck and changes it to one, as well as bumps the score down by ten
        		  if(i.cardType.equals("Ace")) {
        			  System.out.println("\n\nAce Transformed!\n\n");
        			  i.cardValue = 1;
        			  this.score -= 10;
        			  hasUsedAce = true;
        			  break;
        		  }
        		 
        	}
        	 
        }
        
        
        //Future Ace Case Scenarios
        if((c.getCardType().equals("Ace") && this.score > 11)) {
        	c.cardValue = 1;
        } else if ((c.getCardType().equals("Ace") && this.score < 11)) {
        	c.cardValue = 11;
        }

        //Adds the Cards Final Score to the Player's Score
        score += c.getCardValue();
       
        //Player will Bust if their score is higher than 21
        if(score > 21) {
        	isBust = true;
        }
        
    }
    
   
    
    //resets players stats
    public void reset(){
        
        this.isBust = false;
        this.hasAce = false;
        this.hasUsedAce = false;
        this.score = 0;
        this.playerDeck.clear();

    }

    public void setPlayerDeck(ArrayList<Card> newPlayerDeck) {
        playerDeck = newPlayerDeck;
    }


}