package PackageBlackjack;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class AchievementCell extends ListCell<Achievement> {
	
	private VBox vbox = new VBox(8.0);
	private Label labelTitle = new Label();
	private Label labelDescription = new Label();
	
	
	public AchievementCell() {
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(25));
		
		labelTitle.setWrapText(true);
		labelTitle.setTextAlignment(TextAlignment.CENTER);
		labelTitle.setFont(new Font(30.0));
		vbox.getChildren().add(labelTitle);
		
		labelDescription.setWrapText(true);
		labelDescription.setTextAlignment(TextAlignment.CENTER);
		vbox.getChildren().add(labelDescription);
		
		setPrefWidth(USE_PREF_SIZE);
	}
	
	@Override
	protected void updateItem(Achievement item, boolean empty) {
		super.updateItem(item, empty);
		if(empty || item==null) {
			setGraphic(null);
		}
		else {
			
			labelTitle.setText(item.getTitle());
			labelDescription.setText(item.getDescription());
			if(item.isUnlocked() == true) {
				vbox.setOpacity(1);
			} else {
				vbox.setOpacity(0.15);
			}
			setGraphic(vbox);
	}
}
}
