package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

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
	public void onBtNewAction() {
		System.out.println("OnBtNewAction");
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

}
