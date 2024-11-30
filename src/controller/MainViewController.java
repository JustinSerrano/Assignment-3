package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class MainViewController {

	@FXML
	private Button btnSearch, btnClear, btnBuy;

	@FXML
	private TextField inputSerialNumber, inputName, inputType;

	@FXML
	private Label lblSerialNumber, lblName, lblType, lblResult;

	@FXML
	private RadioButton rbSerialNumber, rbName, rbType;

	@FXML
	void BuyListener(ActionEvent event) {

	}

	@FXML
	void searchListener(ActionEvent event) {

	}

}
