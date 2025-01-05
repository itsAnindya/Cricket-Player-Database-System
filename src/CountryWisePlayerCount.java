import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class CountryWisePlayerCount {
    public TableView<CountryPlayerCount> countryWisePlayerCountTable;
    public TableColumn<CountryPlayerCount, String> countryColumn;
    public TableColumn<CountryPlayerCount, Integer> playerCountColumn;
    public Button closeButton;
    
    private void loadCountryWisePlayerCountTable () {
        List<CountryPlayerCount> counts = Player.PlayerList.stream().collect(Collectors.groupingBy(Player::getCountry, Collectors.counting())).entrySet().stream().map(entry->new CountryPlayerCount(entry.getKey(), entry.getValue().intValue())).sorted((p1, p2)->p1.getCountry().compareToIgnoreCase(p2.getCountry())).toList();
        ObservableList<CountryPlayerCount> observableList = FXCollections.observableList(counts);
        countryWisePlayerCountTable.setItems(observableList);
        countryWisePlayerCountTable.getSortOrder().add(countryColumn);
    }
    
    public void initialize () {
        countryColumn.setCellValueFactory((new PropertyValueFactory<>("country")));
        playerCountColumn.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
        
        countryColumn.setSortable(true);
        countryColumn.setSortType(TableColumn.SortType.ASCENDING);
        playerCountColumn.setSortable(true);
        playerCountColumn.setSortType(TableColumn.SortType.ASCENDING);
        
        loadCountryWisePlayerCountTable();
    }
    
    public void closeButtonClicked () {
        ((Stage) closeButton.getScene().getWindow()).close();
    }
}
