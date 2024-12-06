package PackageBlackjack;




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Game extends Application{
	
	public static void createSaveFiles() {
		SaveData defaultData = new SaveData();
		 try(BufferedWriter save1 = Files.newBufferedWriter(Paths.get("save1.xml"));
				 BufferedWriter save2 = Files.newBufferedWriter(Paths.get("save2.xml")); 
					BufferedWriter save3 = Files.newBufferedWriter(Paths.get("save3.xml"));) {
				
				JAXB.marshal(defaultData, save1);
				JAXB.marshal(defaultData, save2);
				JAXB.marshal(defaultData, save3); 

		      }
		      catch (IOException ioException) {
		         System.err.println("Error opening file. Terminating.");
		      } 

	}
	
	
	public void start(Stage primary) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
		Scene scene = new Scene(root);
		primary.setTitle("Blackjack Remastered");
		primary.setScene(scene);
		primary.show();
	}
	
	public static void main(String [] args) {
		
		
		try(BufferedReader testSaveData1 = Files.newBufferedReader(Paths.get("save1.xml"));
				BufferedReader testSaveData2 = Files.newBufferedReader(Paths.get("save2.xml"));
				BufferedReader testSaveData3 = Files.newBufferedReader(Paths.get("save3.xml"));){
			System.out.print("Files exist! Booting up game. . . .");
		
		}catch(IOException e) {
			System.out.print("Files do not exist! Creating new save files and Booting up game. . . .");
			createSaveFiles();
		}
		
		launch(args);
	}
	
    
}

