package PackageBlackjack;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PowerupsCell extends ListCell<String> {

	
	private VBox vbox = new VBox(4.0);
	private Label powerupTitle = new Label();
	
	public PowerupsCell() {
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(15));
		
		powerupTitle.setWrapText(true);
		powerupTitle.setTextAlignment(TextAlignment.CENTER);
		powerupTitle.setFont(new Font(15.0));
		vbox.getChildren().add(powerupTitle);
		setPrefWidth(USE_PREF_SIZE);
	}
	
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item.equals(null)) {
			setGraphic(null);
		}
		else {
			
			powerupTitle.setText(item);
			
			setGraphic(vbox);
	}
}
}
