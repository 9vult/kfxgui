package moe.ninevolt.kfxgui.gui.windows;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import moe.ninevolt.kfxgui.template.Swatch;

/**
 * Window that displays the exported text generated
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class SwatchWindow extends Stage {
    
    private TableView<Swatch> table;
    private TableColumn<Swatch, String> nameColumn;
    private TableColumn<Swatch, String> valueColumn;

    private Scene scene;
    // setStyle("-fx-background-color: " + item.getHTMLColor());
    // setStyle("-fx-text-fill: " + (item.getLuminosity() < 128 ? "#FFFFFF" : "#000000"));

    /**
     * Initialize the window
     */
    public SwatchWindow() {
        this.table = new TableView<>();
        this.nameColumn = new TableColumn<>("Name");
        this.valueColumn = new TableColumn<>("Value");
        
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<Swatch, String>("name"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<Swatch, String>("value"));
        this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.nameColumn.setOnEditCommit((e) -> {
            Swatch s = e.getRowValue();
            s.setName(e.getNewValue());
        });

        this.valueColumn.setOnEditCommit((e) -> {
            Swatch s = e.getRowValue();
            s.setValue(e.getNewValue());
            table.getSelectionModel().getSelectedCells().get(0);
        });

        table.getColumns().add(nameColumn);
        table.getColumns().add(valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(FXCollections.observableList(Swatch.swatches));
        table.setEditable(true);
        if (table.getItems().size() == 0) {
            table.getItems().add(new Swatch("white", "&HFFFFFF&"));
            table.getItems().add(new Swatch("black", "&H000000&"));
        }

        table.setRowFactory(new Callback<TableView<Swatch>,TableRow<Swatch>>() {
            @Override
            public TableRow<Swatch> call(TableView<Swatch> arg0) {
                TableRow<Swatch> row = new TableRow<Swatch>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if ((event.getClickCount() == 2)) {
                            //create a new Item and intialize it ...
                            Swatch s = new Swatch("", "");
                            table.getItems().add(s);
                        } 
                    }
                });
                return row;
            }
        });

        this.scene = new Scene(table, 310, 605);
        this.setScene(scene);
        this.setTitle("Swatches");
    }

}
