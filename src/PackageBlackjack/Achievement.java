package PackageBlackjack;

public class Achievement {
	private String title;
	private String description;
	private boolean unlocked;
	
	public Achievement() {
		this.title = "test";
		this.description = "test";
		this.unlocked = false;
	}
	public Achievement(String title, String description, boolean unlocked) {
		super();
		this.title = title;
		this.description = description;
		this.unlocked = unlocked;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUnlocked() {
		return unlocked;
	}
	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
	
	
	
}
