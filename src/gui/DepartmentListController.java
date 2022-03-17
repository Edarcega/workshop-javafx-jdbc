package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartmentService service;

	// Refer�ncas para os quatro componentes: Bot�o table view e as duas colunas da
	// tabela

	@FXML
	private TableView<Department> tableViewDepartment;// Refer�ncia para tabela

	@FXML
	private TableColumn<Department, Integer> tableColumnId;// O primeiro tipo � a entidade e a segunda � o tipo da
															// coluna

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = gui.util.Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj,"/gui/departmentForm.fxml", parentStage);
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	// Inicializa��o do comprtamento da tabela
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Ajuste para a tablea acompanhar a altura da janela principal (stage)
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// Metodo responsavel por acessar os servi�os carregar os departameentos e
	// repassar para o observable list.
	// o Observable list � associado ao table view e os departamentos s�o
	// apresentados na tela
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("o Service estava nulo");
		}

		List<Department> list = service.findAll();

		obsList = FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList);

	}
	
	// Metodo para criar uma janela de di�logo
	// Quando uma janela de di�logo � criada � necess�rio informar quem (Qual o stage) criou a janela
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(service);
			controller.subscribeDataChangeListener(this);
			controller.updatateFormData();
			
			Stage dialogStage = new Stage();
			
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		}catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
