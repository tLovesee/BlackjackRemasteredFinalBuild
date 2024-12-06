package PackageBlackjack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javax.xml.bind.JAXB;

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
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainMenuController implements Initializable {

	
	@FXML
    private ListView<Achievement> achievementList;
	
	private final ObservableList<Achievement> achievementObservable = FXCollections.observableArrayList(); 
	
	@FXML
	private Button achievementsButton;

	@FXML
	private Button playButton;

	@FXML
	private Button saveFileButton;

	@FXML
	private Label saveFilePrompt;

	@FXML
	private Button save1Button;

	@FXML
	private Button save2Button;

	@FXML
	private Button save3Button;

	@FXML
	private RadioMenuItem easyDifficulty;

	@FXML
	private RadioMenuItem hardDifficulty;

	@FXML
	private RadioMenuItem normalDifficulty;

	@FXML
	private ToggleGroup difficultyButtons;

	@FXML
	private MenuBar mainMenuBar;

	@FXML
    private Button backFromAchievements;
	
	@FXML
	private ToggleGroup tableSkinOptions;
	
    @FXML
    private RadioMenuItem redMenuItem;
	@FXML
    private RadioMenuItem orangeMenuItem;
	@FXML
	private RadioMenuItem yellowMenuItem;
	@FXML
    private RadioMenuItem greenMenuItem;
	@FXML
    private RadioMenuItem blueMenuItem;
	@FXML
	private RadioMenuItem indigoMenuItem;
	@FXML
    private RadioMenuItem violetMenuItem;

	SaveData data1;
	SaveData data2;
	SaveData data3;
	String currentSaveData;

	int wins;

	Player player = new Player();

	public enum dealerDifficulty{
		EASY(15),
		NORMAL(17),
		HARD(19);

		public int difficulty;

		dealerDifficulty(int diff){
			this.difficulty=diff;
		}

		public int getDifficulty() {
			return difficulty;
		}
	}

	dealerDifficulty dDiff = dealerDifficulty.NORMAL;
	
	String pTableColor;

	public void startUp() {
		
		easyDifficulty.setUserData(dealerDifficulty.EASY);
		normalDifficulty.setUserData(dealerDifficulty.NORMAL);
		hardDifficulty.setUserData(dealerDifficulty.HARD);
		
		redMenuItem.setUserData("Color.RED");
		orangeMenuItem.setUserData("Color.ORANGE");
		yellowMenuItem.setUserData("Color.YELLOW");
		greenMenuItem.setUserData("Color.GREEN");
		blueMenuItem.setUserData("Color.BLUE");
		indigoMenuItem.setUserData("Color.INDIGO");
		violetMenuItem.setUserData("Color.VIOLET");
		
		
		saveFilePrompt.setVisible(true);
		save1Button.setVisible(true);
		save2Button.setVisible(true);
		save3Button.setVisible(true);

		achievementsButton.setVisible(false);
		playButton.setVisible(false);
		saveFileButton.setVisible(false);
		mainMenuBar.setVisible(false);

	}

	public void loadSaveData(Player p1, String sf) {

		try(BufferedReader loadSaveData1 = Files.newBufferedReader(Paths.get("save1.xml"));
				BufferedReader loadSaveData2 = Files.newBufferedReader(Paths.get("save2.xml"));
				BufferedReader loadSaveData3 = Files.newBufferedReader(Paths.get("save3.xml"));
				){

			data1 = JAXB.unmarshal(loadSaveData1, SaveData.class);
			data2 = JAXB.unmarshal(loadSaveData2, SaveData.class);
			data3 = JAXB.unmarshal(loadSaveData3, SaveData.class);


			

			if (sf.equals("save1.xml")) {
				data1.setBeingUsed(true);
				data2.setBeingUsed(false);
				data3.setBeingUsed(false);
				
			} else if (sf.equals("save2.xml")) {
				data1.setBeingUsed(false);
				data2.setBeingUsed(true);
				data3.setBeingUsed(false);

			} else if (sf.equals("save3.xml")) {
				data1.setBeingUsed(false);
				data2.setBeingUsed(false);
				data3.setBeingUsed(true);
			}
			
			System.out.print(grabSelectedSaveFile().getDiffSelected());
			
			
			
			p1.money = grabSelectedSaveFile().getSavedMoney();
			wins = grabSelectedSaveFile().getWinCount();
			
			if(grabSelectedSaveFile().getDiffSelected() == 15) {
				easyDifficulty.setSelected(true);
			} else if (grabSelectedSaveFile().getDiffSelected() == 17) {
				normalDifficulty.setSelected(true);
			} else if (grabSelectedSaveFile().getDiffSelected() == 19) {
				hardDifficulty.setSelected(true);
			}
			
			
			
			
			



			//System.out.printf("%s\t%s\t%s\n", data1.isBeingUsed(),data2.isBeingUsed(),data3.isBeingUsed());

			BufferedWriter saveSaveData1 = Files.newBufferedWriter(Paths.get("save1.xml"));
			BufferedWriter saveSaveData2 = Files.newBufferedWriter(Paths.get("save2.xml"));
			BufferedWriter saveSaveData3 = Files.newBufferedWriter(Paths.get("save3.xml"));

			JAXB.marshal(data1, saveSaveData1);
			JAXB.marshal(data2, saveSaveData2);
			JAXB.marshal(data3, saveSaveData3);

			saveFilePrompt.setVisible(false);
			save1Button.setVisible(false);
			save2Button.setVisible(false);
			save3Button.setVisible(false);

			achievementsButton.setVisible(true);
			playButton.setVisible(true);
			saveFileButton.setVisible(true);
			mainMenuBar.setVisible(true);
			
			achievementObservable.clear();
			achievementObservable.addAll(grabSelectedSaveFile().getAchievements());
			//System.out.println();
			//System.out.println(grabSelectedSaveFile().getAchievements().size());
			//System.out.println(achievementObservable);
			
			achievementList.setItems(achievementObservable);
			achievementList.setCellFactory(
		    		  new Callback <ListView<Achievement>, ListCell<Achievement>>(){
		    			  
		    			  public ListCell<Achievement> call (ListView<Achievement> listView){
		    				  return new AchievementCell();
		    			  }
		    		  });
			






		}catch(IOException e) {
			e.printStackTrace();
		}
	}








	@FXML
	void openAchievements(ActionEvent event) {
		achievementList.setVisible(true);
		backFromAchievements.setVisible(true);
	}

	@FXML
    void backFromAchievementsButton(ActionEvent event) {
		achievementList.setVisible(false);
		backFromAchievements.setVisible(false);
    }
	
	@FXML
	void openSaveFiles(ActionEvent event) {
		startUp();
	}


	@FXML
	void save1Selected(ActionEvent event) {
		loadSaveData(player,"save1.xml");

	}

	@FXML
	void save2Selected(ActionEvent event) {
		loadSaveData(player,"save2.xml");
	}

	@FXML
	void save3Selected(ActionEvent event) {
		loadSaveData(player,"save3.xml");
	}
	
	
	

	public SaveData grabSelectedSaveFile() {
		if(data1.isBeingUsed() == true) {
			currentSaveData = "save1.xml";
			return data1;
		} else if(data2.isBeingUsed() == true) {
			currentSaveData = "save2.xml";
			return data2;
		} else {
			currentSaveData = "save3.xml";
			return data3;
		}
	}

	@FXML
	void difficultySelected(ActionEvent event) {
		dDiff = (dealerDifficulty) difficultyButtons.getSelectedToggle().getUserData();
		grabSelectedSaveFile().setDiffSelected(dDiff.getDifficulty());
		//System.out.print(grabSelectedSaveFile().getDiffSelected());
		
		if(grabSelectedSaveFile().getDiffSelected() == 15){
			easyDifficulty.setSelected(true);
			
		} else if (grabSelectedSaveFile().getDiffSelected() == 17) {
			normalDifficulty.setSelected(true);
			
		} else if (grabSelectedSaveFile().getDiffSelected() == 19) {
			hardDifficulty.setSelected(true);
			
		}
		
		try(BufferedWriter changeDiffData = Files.newBufferedWriter(Paths.get(currentSaveData));){
			JAXB.marshal(grabSelectedSaveFile(), changeDiffData);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
    void tableSkinSelected(ActionEvent event) {
		pTableColor = (String) tableSkinOptions.getSelectedToggle().getUserData();
		grabSelectedSaveFile().setTableSkin(pTableColor);
		
		String currentSelectedTableSkin = grabSelectedSaveFile().getTableSkin();
		System.out.println(currentSelectedTableSkin);
		
		if(currentSelectedTableSkin.equals("Color.RED")) {
			redMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.ORANGE")) {
			orangeMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.YELLOW")) {
			yellowMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.GREEN")) {
			greenMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.BLUE")) {
			blueMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.INDIGO")) {
			indigoMenuItem.setSelected(true);
		} else if (currentSelectedTableSkin.equals("Color.VIOLET")) {
			violetMenuItem.setSelected(true);
		}
		
		
		try(BufferedWriter changeSkinData = Files.newBufferedWriter(Paths.get(currentSaveData));){
			JAXB.marshal(grabSelectedSaveFile(), changeSkinData);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
    }


	@FXML
	void startGame(ActionEvent event) throws Exception {
		Stage stage;
		Parent root;

		stage = (Stage) playButton.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("prototype1.fxml"));


		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		startUp();
	}

}