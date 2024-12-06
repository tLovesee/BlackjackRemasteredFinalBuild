package PackageBlackjack;

import java.util.Random;
import java.util.ResourceBundle;

import javax.xml.bind.JAXB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class BlackjackController implements Initializable{

	SaveData data1;
	SaveData data2;
	SaveData data3;
	String currentSaveData;
	
	boolean usedPowerup = false;


	Random rand = new Random();

	//Preparing the playing card deck for use
	ArrayList<Card> allPlayingCards = new ArrayList<Card>();
	final String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
	final String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	final String[] powerups = {"Buster","Swapper","Peek","Hand Reset","Duplicate","Double down","Take 2"};

	private static final int cardWidth = 100;
	private static final int cardHeight = 140;


	Player player = new Player();
	Player dealer = new Player();



	int wins = 0;
	boolean giveUp = false;
	int betPool = 0;
	int dealerPullLimit;
	int winStreak = 0;
	int loseStreak = 0;

	public SaveData grabSelectedSaveFile() {
		if(data1.isBeingUsed() == true) {

			currentSaveData = "save1.xml";
			return data1;
		} else if(data2.isBeingUsed() == true) {

			currentSaveData = "save2.xml";
			return data2;
		} else if (data3.isBeingUsed() == true){

			currentSaveData = "save3.xml";
			return data3;
		} else {
			return new SaveData();
		}

	}


	public void loadSaveData(Player p1) {

		try(BufferedReader loadSaveData1 = Files.newBufferedReader(Paths.get("save1.xml"));
				BufferedReader loadSaveData2 = Files.newBufferedReader(Paths.get("save2.xml"));
				BufferedReader loadSaveData3 = Files.newBufferedReader(Paths.get("save3.xml"));){

			data1 = JAXB.unmarshal(loadSaveData1, SaveData.class);
			data2 = JAXB.unmarshal(loadSaveData2, SaveData.class);
			data3 = JAXB.unmarshal(loadSaveData3, SaveData.class);

			p1.money = grabSelectedSaveFile().getSavedMoney();
			wins = grabSelectedSaveFile().getWinCount();
			dealerPullLimit = grabSelectedSaveFile().getDiffSelected();

			String enteredByUser = grabSelectedSaveFile().getTableSkin();
			System.out.println(enteredByUser);
			String colorYes = enteredByUser.substring(6).toLowerCase();
			System.out.println(colorYes);
			backgroundColorPane.setStyle("-fx-background-color: " + colorYes);





		}catch(IOException e) {
			e.printStackTrace();
		}
	}



	@FXML
	private Label moneyDisplay;


	@FXML
	private Button powerUpsButton;


	@FXML
	private ListView<String> powerUpsListView;

	private final ObservableList<String> powerUpsObservable = FXCollections.observableArrayList();


	@FXML
	private Button giveUpButton;

	@FXML
	private Button hitButton;

	@FXML
	private Label playerTotal;

	@FXML
	private HBox playerVisualDeck;

	@FXML
	private Label dealerTotal;

	@FXML
	private HBox dealerVisualDeck;

	@FXML
	private Button standButton;

	@FXML
	private Button startButton;

	@FXML
	private Label winTotalCounter;

	@FXML
	private Label gameTitleText;

	@FXML
	private Label winTotal;

	@FXML
	private Text cardInfoDisplay;

	@FXML
	private Pane backgroundColorPane;

	@FXML
	private TextField betTextField;

	@FXML
	private Button betEnter;

	@FXML
	private RadioMenuItem easyDifficulty;

	@FXML
	private RadioMenuItem normalDifficulty;

	@FXML
	private RadioMenuItem hardDifficulty;

	@FXML
	private ToggleGroup difficultyToggleGroup;

	@FXML
	private Button backToMenuButton;



	void startGame() {
		//Hides 'title' and 'start' button
		startButton.setVisible(false);
		backToMenuButton.setVisible(false);
		gameTitleText.setVisible(false);
		playerVisualDeck.setVisible(false);
		dealerVisualDeck.setVisible(false);
		dealerTotal.setVisible(false);


		betTextField.setVisible(true);
		betEnter.setVisible(true);
	}


	@FXML
	void betEnterPressed(ActionEvent event) {
		try {
			System.out.println(player.getMoney());
			player.money -= Integer.parseInt(betTextField.getText());
			betPool += Integer.parseInt(betTextField.getText());
			moneyDisplay.setText(String.valueOf(player.getMoney()));
			betEnter.setVisible(false);
			betTextField.setVisible(false);
			gameStart();
		} catch (Exception NumberFormatException) {
			betTextField.clear();
			betTextField.setPromptText("Invalid Input");

		} 



	}


	@FXML
	void giveUpButtonPressed(ActionEvent event) {
		giveUp = true;
		gameEnd();
	}

	@FXML
	void hitButtonPressed(ActionEvent event) {

		//Grabs a random card from the main deck
		Card playerNewCard = allPlayingCards.get(0);
		allPlayingCards.remove(0);


		//Adds the card to your deck
		player.addCard(playerNewCard);
		addCardImage(playerVisualDeck, playerNewCard);

		//Displays what card you pulled in both UI and console
		//cardInfoDisplay.setText( "You pulled a " + playerNewCard.getCardType() + " of " + playerNewCard.getCardSymbol() );

		System.out.printf("\n%s %s %d\n",playerNewCard.getCardType(),playerNewCard.getCardSymbol(),player.getScore());

		//Achievement 1
		if(grabSelectedSaveFile().getAchievements().get(0).isUnlocked() == false) {
			grabSelectedSaveFile().getAchievements().get(0).setUnlocked(true);
		}

		//Changes player's score in UI
		playerTotal.setText(String.valueOf(player.getScore()));

		//Achievement 3 (For any cards after first two) "Ace Up My Sleeve","Get an Ace in your Hand"
		if(grabSelectedSaveFile().getAchievements().get(2).isUnlocked() == false && player.hasAce == true) {
			grabSelectedSaveFile().getAchievements().get(2).setUnlocked(true);
		}


		//Game will stop if player hits a blackjack
		if(player.getScore() == 21) {

			dealerTurn();
		}
		//Game will stop if player busts (score higher than 21)
		else if(player.getBust() == true) {
			gameEnd();
		}
	}

	@FXML
	void standButtonPressed(ActionEvent event) {
		hitButton.setVisible(false);
		giveUpButton.setVisible(false);
		standButton.setVisible(false);

		//Achievement 2 "I'm Good, Thanks","Stand in a Game"
		if(grabSelectedSaveFile().getAchievements().get(1).isUnlocked() == false) {
			grabSelectedSaveFile().getAchievements().get(1).setUnlocked(true);
		}

		dealerTurn();
	}

	@FXML
	void startButtonPressed(ActionEvent event) {
		startGame();

	}

	public void gameEnd() {

		//Reveals dealer's hidden card
		ImageView firstCardPreviousImage = (ImageView) dealerVisualDeck.getChildren().get(0);
		Card firstCardPrevious = dealer.getPlayerDeck().get(0);
		firstCardPreviousImage.setImage(firstCardPrevious.cardImage);

		//Resets booleans
		boolean didWin = false;
		boolean didPush = false;

		//hides buttons
		hitButton.setVisible(false);
		standButton.setVisible(false);
		giveUpButton.setVisible(false);
		powerUpsButton.setVisible(false);
		powerUpsListView.setVisible(false);

		dealerTotal.setText(String.valueOf(dealer.getScore()));

		//Re-shows the 'start' button and 'title'
		startButton.setVisible(true);
		backToMenuButton.setVisible(true);
		gameTitleText.setVisible(true);

		//Game Result Logic
		if(giveUp == true) {
			;// skips everything else if give up button is pressed

		} else if( (dealer.getScore() == player.getScore()) && (player.getBust() == false && dealer.getBust() == false) ){ // TIE

			didPush = true;

		} else if ( (dealer.getScore() > player.getScore() && dealer.getBust() == false) || player.getBust() == true) { //DEALER WIN

			didWin = false;

		} else if ( (dealer.getScore() < player.getScore() && player.getBust() == false) || dealer.getBust() == true) { //PLAYER WIN

			didWin = true;
			wins++;
			winTotal.setText(String.valueOf(wins));

		}

		//'Title' text displays different message depending on the results
		if(giveUp == true) {
			didWin = false;
			gameTitleText.setText("You gave up. . . . Play Again?");
		} else if(didPush == true) {
			gameTitleText.setText("You pushed/Tied! Play Again?");
			player.money += betPool;
		} else if(didWin == false) {

			gameTitleText.setText("You lose! Play Again?");
			loseStreak += 1;
			winStreak = 0;

			//Achievement 4 "Dealer’s Delight","Lose a Game"
			if(grabSelectedSaveFile().getAchievements().get(3).isUnlocked() == false) {
				grabSelectedSaveFile().getAchievements().get(3).setUnlocked(true);
			}

			//Achievement 11 "6th time's the charm","Lose 5 games in a row"
			if(grabSelectedSaveFile().getAchievements().get(10).isUnlocked() == false && loseStreak <= 5) {
				grabSelectedSaveFile().getAchievements().get(10).setUnlocked(true);
			}

		} else {
			String newPowerup = powerups[rand.nextInt(powerups.length)];
			gameTitleText.setText("You Win! A '"+newPowerup+"' powerup has been added to your inventory!");

			//Achievement 9 "Putting it all on the line","Win a game after betting everything you have"
			if(grabSelectedSaveFile().getAchievements().get(8).isUnlocked() == false && player.money <= 0) {
				grabSelectedSaveFile().getAchievements().get(8).setUnlocked(true);
			}

			player.money += betPool * 2;
			winStreak += 1;
			loseStreak = 0;
			grabSelectedSaveFile().getPowerups().add(newPowerup);



			//Achievement 5 "Blackjack Boss","Win a Game"
			if(grabSelectedSaveFile().getAchievements().get(4).isUnlocked() == false) {
				grabSelectedSaveFile().getAchievements().get(4).setUnlocked(true);
			}

			//Achievement 6 "Never tell me the odds","Win 5 games in a row"
			if(grabSelectedSaveFile().getAchievements().get(5).isUnlocked() == false && winStreak >= 5) {
				grabSelectedSaveFile().getAchievements().get(5).setUnlocked(true);
			}
			
			
			//Achievement 7 "Fix the odds in your favor","Use an item to win a game"
			if(grabSelectedSaveFile().getAchievements().get(6).isUnlocked() == false && usedPowerup == true) {
				grabSelectedSaveFile().getAchievements().get(6).setUnlocked(true);
			}

			//Achievement 8 "Battle of wits","Win a game on hard mode"
			if(grabSelectedSaveFile().getAchievements().get(7).isUnlocked() == false && grabSelectedSaveFile().getDiffSelected() == 19) {
				grabSelectedSaveFile().getAchievements().get(7).setUnlocked(true);
			}

			//Achievement 10 "Champion gambler","Win 50 games total"
			if(grabSelectedSaveFile().getAchievements().get(9).isUnlocked() == false && wins >= 50) {
				grabSelectedSaveFile().getAchievements().get(9).setUnlocked(true);
			}

			//Achievement 13 "Risky play","Win a game with a score less than 17"
			if(grabSelectedSaveFile().getAchievements().get(12).isUnlocked() == false && player.getScore() < 17) {
				grabSelectedSaveFile().getAchievements().get(12).setUnlocked(true);
			}


			//Achievement 15 "It’s the name of the game!","Win a game with 21"
			if(grabSelectedSaveFile().getAchievements().get(14).isUnlocked() == false && player.getScore() == 21) {
				grabSelectedSaveFile().getAchievements().get(14).setUnlocked(true);
			}


		}

		moneyDisplay.setText(String.valueOf(player.getMoney()));
		//'start' button text changes
		startButton.setText("PLAY AGAIN");
		betPool = 0;


		grabSelectedSaveFile().setWinCount(wins);
		grabSelectedSaveFile().setSavedMoney(player.getMoney());
		grabSelectedSaveFile().setHasBeenUsed(true);

		try(BufferedWriter testSaveData = Files.newBufferedWriter(Paths.get(currentSaveData));){
			JAXB.marshal(grabSelectedSaveFile(), testSaveData);
		}catch(IOException e) {
			e.printStackTrace();
		}


	}

	public void dealerTurn() {
		//Dealer continues to pull cards until their score is greater than or equal to 17
		while(dealer.getScore() < dealerPullLimit || (dealer.getScore() == dealerPullLimit && dealer.hasAce == true && dealer.hasUsedAce == false)) {

			Card dealerNewCard = allPlayingCards.get(0);
			allPlayingCards.remove(0);

			dealer.addCard(dealerNewCard);
			addCardImage(dealerVisualDeck,dealerNewCard);


		}

		//Dealer busts if their score is higher than 21
		if(dealer.getScore() > 21) {
			dealer.isBust = true;
		}




		//Game end function called
		gameEnd();
	}

	public void gameStart(){



		//Achievement 14 "Big winner","Have 100,000 money at one time"
		if(grabSelectedSaveFile().getAchievements().get(13).isUnlocked() == false && player.getMoney() >= 100000) {
			grabSelectedSaveFile().getAchievements().get(13).setUnlocked(true);
		}


		hitButton.setVisible(true);
		giveUpButton.setVisible(true);
		standButton.setVisible(true);
		playerVisualDeck.setVisible(true);
		dealerVisualDeck.setVisible(true);
		powerUpsButton.setVisible(true);
		dealerTotal.setVisible(false);
		dealerTotal.setVisible(true);
		dealerTotal.setText("?");

		allPlayingCards.clear();

		playerVisualDeck.getChildren().clear();
		dealerVisualDeck.getChildren().clear();

		//Initializes the playing card deck
		for (int i = 0; i < suits.length; i++) {

			for (int j = 0; j < ranks.length;j++) {

				Card playingCard = new Card(suits[i],ranks[j]);
				allPlayingCards.add(playingCard);
			}

		}

		Collections.shuffle(allPlayingCards);

		System.out.println("\nGame Start\n\n\n\n");

		cardInfoDisplay.setText(" ");
		giveUp = false;
		player.reset();
		dealer.reset();

		System.out.print(player.score);
		System.out.print(dealer.score);

		//Players first two cards

		Card playerFirstCard = allPlayingCards.get(0);
		allPlayingCards.remove(0);
		Card playerSecondCard = allPlayingCards.get(0);
		allPlayingCards.remove(0);

		//Achievement 3 (For first two cards) "Ace Up My Sleeve","Get an Ace in your Hand"
		if(grabSelectedSaveFile().getAchievements().get(2).isUnlocked() == false && player.hasAce == true) {
			grabSelectedSaveFile().getAchievements().get(2).setUnlocked(true);
		}

		player.addCard(playerFirstCard);
		player.addCard(playerSecondCard);
		addCardImage(playerVisualDeck,playerFirstCard);
		addCardImage(playerVisualDeck,playerSecondCard);

		//Dealers First Two Cards


		Card dealerFirstCard = allPlayingCards.get(0);
		allPlayingCards.remove(0);
		Card dealerSecondCard = allPlayingCards.get(0);
		allPlayingCards.remove(0);

		dealer.addCard(dealerFirstCard);
		dealer.addCard(dealerSecondCard);
		addCardImage(dealerVisualDeck,dealerFirstCard);
		addCardImage(dealerVisualDeck,dealerSecondCard);

		System.out.printf("\n%d\n", player.getScore());

		//Shows both dealer and player scores
		playerTotal.setText(String.valueOf(player.score));


		//Game will stop if player hits a blackjack
		if(player.getScore() == 21) {
			dealerTurn();
		}
		//Game will stop if player busts automatically at the start (NOTE: it is not actually possible to have a starting hand of more than 21. This is more of an exception handler than anything.
		else if(player.getBust() == true) {
			gameEnd();
		}

	}

	@FXML
	void backToMenuButtonPressed(ActionEvent event) throws IOException{
		Stage stage;
		Parent root;

		stage = (Stage) backToMenuButton.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));


		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void powerUpsButtonPressed(ActionEvent event) {
		powerUpsListView.setVisible(true);

		powerUpsObservable.clear();
		powerUpsObservable.addAll(grabSelectedSaveFile().getPowerups());
		powerUpsListView.setItems(powerUpsObservable);
		powerUpsListView.setCellFactory(
				new Callback <ListView<String>, ListCell<String>>(){

					public ListCell<String> call (ListView<String> listView){
						return new PowerupsCell();
					}
				});
	}

	public void addCardImage(HBox d, Card c) {
		ImageView newCardPicTest = new ImageView();
		newCardPicTest.setFitHeight(cardHeight);
		newCardPicTest.setFitWidth(cardWidth);
		System.out.println(d.getId());
		if(d.getChildren().size() <= 0 && d.getId().equals("dealerVisualDeck")) {
			Image backCard = new Image (Card.class.getResourceAsStream("cardPngs/blue.png"));
			newCardPicTest.setImage(backCard);
		} else {
			newCardPicTest.setImage(c.cardImage);
		}
		d.getChildren().add(newCardPicTest);
	}

	public void consumePowerup(String pu) {

		//"Buster","Swapper","Peek","Hand Reset","Duplicate","Double down","Take 2"
		System.out.println(pu);
		powerUpsListView.setVisible(false);
		
		usedPowerup = true;


		switch(pu) {
		case "Buster":
			System.out.print("Buster used");
			Card busterCard = new Card(suits[rand.nextInt(suits.length)],"10");
			dealer.addCard(busterCard);
			addCardImage(dealerVisualDeck, busterCard);



			break;

		case "Swapper":
			System.out.print("Swapper used");

			int playerHighestCardIndex = 0;

			int dealerHighestCardIndex = 0;

			for(Card c : player.getPlayerDeck()) {
				if(c.getCardValue() > player.getPlayerDeck().get(playerHighestCardIndex).getCardValue()) {
					playerHighestCardIndex = player.playerDeck.indexOf(c);
				}
			}

			for(Card c : dealer.getPlayerDeck()) {
				if(c.getCardValue() > dealer.getPlayerDeck().get(dealerHighestCardIndex).getCardValue()) {
					dealerHighestCardIndex = dealer.playerDeck.indexOf(c);

				}
			}

			System.out.println();
			System.out.println(player.playerDeck.size());
			System.out.println(playerVisualDeck.getChildren().size());
			System.out.println(playerHighestCardIndex);


			System.out.println();
			System.out.println(dealer.playerDeck.size());
			System.out.println(dealerVisualDeck.getChildren().size());
			System.out.println(dealerHighestCardIndex);

			Card tempPlayerCard = player.getPlayerDeck().get(playerHighestCardIndex);
			Card tempDealerCard = dealer.getPlayerDeck().get(dealerHighestCardIndex);

			ImageView playerCardSwap = (ImageView) playerVisualDeck.getChildren().get(playerHighestCardIndex);
			ImageView dealerCardSwap = (ImageView) dealerVisualDeck.getChildren().get(dealerHighestCardIndex);

			player.score -= player.getPlayerDeck().get(playerHighestCardIndex).cardValue;
			player.getPlayerDeck().set(playerHighestCardIndex, tempDealerCard);
			player.score += player.getPlayerDeck().get(playerHighestCardIndex).cardValue;
			playerTotal.setText(String.valueOf(player.getScore()));

			dealer.score -= dealer.getPlayerDeck().get(dealerHighestCardIndex).cardValue;
			dealer.getPlayerDeck().set(dealerHighestCardIndex, tempPlayerCard);
			dealer.score += dealer.getPlayerDeck().get(dealerHighestCardIndex).cardValue;
			dealerTotal.setText(String.valueOf(dealer.getScore()));



			playerCardSwap.setImage(tempDealerCard.cardImage);
			dealerCardSwap.setImage(tempPlayerCard.cardImage);

			if(player.getScore() >= 21 || dealer.getScore() > 21) {
				gameEnd();
			}



			break;

		case "Peek":

			System.out.print("Peek used");
			Card unveiledCard = dealer.getPlayerDeck().get(0);
			ImageView unveiledCardImageView = (ImageView) dealerVisualDeck.getChildren().get(0);
			unveiledCardImageView.setImage(unveiledCard.cardImage);
			break;

		case "Hand Reset":

			player.playerDeck.clear();
			playerVisualDeck.getChildren().clear();
			player.setScore(0);

			Card playerFirstCard = allPlayingCards.get(0);
			allPlayingCards.remove(0);
			Card playerSecondCard = allPlayingCards.get(0);
			allPlayingCards.remove(0);

			player.addCard(playerFirstCard);
			player.addCard(playerSecondCard);
			addCardImage(playerVisualDeck,playerFirstCard);
			addCardImage(playerVisualDeck,playerSecondCard);
			playerTotal.setText(String.valueOf(player.getScore()));

			if(player.getScore() >= 21) {
				gameEnd();
			}


			System.out.print("Hand Reset used");
			break;

		case "Duplicate":

			Card duplicateCard = player.getPlayerDeck().get(player.playerDeck.size()-1);
			player.addCard(duplicateCard);
			addCardImage(playerVisualDeck, duplicateCard);
			playerTotal.setText(String.valueOf(player.getScore()));
			System.out.print("Duplicate used");
			break;

		case "Double down":

			betPool *= 2;
			System.out.print("Double down used");
			break;

		case "Take 2":

			Card dealerFirstCard = allPlayingCards.get(0);
			allPlayingCards.remove(0);
			Card dealerSecondCard = allPlayingCards.get(0);
			allPlayingCards.remove(0);

			dealer.addCard(dealerFirstCard);
			dealer.addCard(dealerSecondCard);
			addCardImage(dealerVisualDeck,dealerFirstCard);
			addCardImage(dealerVisualDeck,dealerSecondCard);


			if(dealer.getScore() > 21) {
				gameEnd();
			}

			System.out.print("Take 2 used");
			break;

		default:
			break;	


		}

		if(grabSelectedSaveFile().getUsedPowerups().contains(pu) == false) {
			grabSelectedSaveFile().getUsedPowerups().add(pu);
		}
		grabSelectedSaveFile().getUsedPowerups().add(pu);

		//Achievement 12 Check
		if(grabSelectedSaveFile().getAchievements().get(11).isUnlocked() == false) {
			achievement12Check();
		} else {
			System.out.println("Achievement 12 should be unlocked");
		}


	}

	void achievement12Check() {

		ArrayList<String> playerTempCheck = grabSelectedSaveFile().getUsedPowerups();
		if(playerTempCheck.contains("Buster") &&
				playerTempCheck.contains("Swapper") &&
				playerTempCheck.contains("Peek") &&
				playerTempCheck.contains("Hand Reset") &&
				playerTempCheck.contains("Duplicate") &&
				playerTempCheck.contains("Double down") &&
				playerTempCheck.contains("Take 2")
				) {
			grabSelectedSaveFile().getAchievements().get(11).setUnlocked(true);
		}
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadSaveData(player);
		winTotal.setText(String.valueOf(wins));
		moneyDisplay.setText(String.valueOf(player.getMoney()));

		powerUpsListView.setOnMouseClicked(event -> {
			consumePowerup(powerUpsListView.getSelectionModel().getSelectedItem());
			grabSelectedSaveFile().getPowerups().remove(powerUpsListView.getSelectionModel().getSelectedIndex());
		});


		startGame();

	}





}
