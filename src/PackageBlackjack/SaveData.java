package PackageBlackjack;

import java.util.ArrayList;


public class SaveData {
	private int savedMoney;
	private int winCount;
	private ArrayList<Achievement> achievements = new ArrayList<Achievement>();
	private ArrayList<String> powerups = new ArrayList<String>();
	private ArrayList<String> usedPowerups = new ArrayList<String>();
	private String tableSkin;
	
	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(ArrayList<Achievement> achievements) {
		this.achievements = achievements;
	}

	private boolean hasBeenUsed = false;
	private boolean beingUsed = false;
	private int diffSelected; // 15 = EASY, 17 = NORMAL, 19 = HARD
	
	//Insert Arraylist powerups here
	
	
	public int getDiffSelected() {
		return diffSelected;
	}

	public void setDiffSelected(int diffSelected) {
		this.diffSelected = diffSelected;
	}

	public boolean isBeingUsed() {
		return beingUsed;
	}

	public void setBeingUsed(boolean beingUsed) {
		this.beingUsed = beingUsed;
	}

	public boolean isHasBeenUsed() {
		return hasBeenUsed;
	}

	public void setHasBeenUsed(boolean hasBeenUsed) {
		this.hasBeenUsed = hasBeenUsed;
	}

	public SaveData() {
		super();
		this.savedMoney = 100;
		this.winCount = 0;
		this.hasBeenUsed = false;
		this.beingUsed = false;
		this.diffSelected = 17;
		achievements.add(new Achievement("Hit me Baby One More Time","Draw a Card",false)); //0 Achievement 1 done
		achievements.add(new Achievement("I'm Good, Thanks","Stand in a Game",false)); //1 Achievement 2 done
		achievements.add(new Achievement("Ace Up My Sleeve","Get an Ace in your Hand",false)); //2 Achievement 3 done
		achievements.add(new Achievement("Dealer’s Delight","Lose a Game",false)); //3 Achievement 4 done
		achievements.add(new Achievement("Blackjack Boss","Win a Game",false)); //4 Achievement 5 done
		achievements.add(new Achievement("Never tell me the odds","Win 5 games in a row",false)); //5 Achievement 6 done
		achievements.add(new Achievement("Fix the odds in your favor","Use an item to win a game",false)); //6 Achievement 7
		achievements.add(new Achievement("Battle of wits","Win a game on hard mode",false)); //7 Achievement 8 done
		achievements.add(new Achievement("Putting it all on the line","Win a game after betting everything you have",false)); //8 Achievement 9 done 
		achievements.add(new Achievement("Champion gambler","Win 50 games total",false)); //9 Achievement 10 done
		achievements.add(new Achievement("6th time's the charm","Lose 5 games in a row",false)); //10 Achievement 11 done
		achievements.add(new Achievement("Master manipulator","Use every item type",false)); //11 Achievement 12 done
		achievements.add(new Achievement("Risky play","Win a game with a score less than 17",false)); //12 Achievement 13 done
		achievements.add(new Achievement("Big winner","Have 100,000 money at one time",false)); //13 Achievement 14 done
		achievements.add(new Achievement("It’s the name of the game!","Win a game with 21",false)); //14 Achievement 15
		this.tableSkin = "Color.GREEN";
	}

	public int getSavedMoney() {
		return savedMoney;
	}

	public void setSavedMoney(int savedMoney) {
		this.savedMoney = savedMoney;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public ArrayList<String> getPowerups() {
		return powerups;
	}

	public void setPowerups(ArrayList<String> powerups) {
		this.powerups = powerups;
	}

	public ArrayList<String> getUsedPowerups() {
		return usedPowerups;
	}

	public void setUsedPowerups(ArrayList<String> usedPowerups) {
		this.usedPowerups = usedPowerups;
	}

	public String getTableSkin() {
		return tableSkin;
	}

	public void setTableSkin(String tableSkin) {
		this.tableSkin = tableSkin;
	}

	
	
	
}
