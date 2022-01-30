package moe.ninevolt.kfxgui.gui.windows;

import java.util.Comparator;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private TableColumn<Swatch, Swatch> previewColumn;

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

        setupPreviewColumn();

        this.nameColumn.setOnEditCommit((e) -> {
            Swatch s = e.getRowValue();
            s.setName(e.getNewValue());
        });

        this.valueColumn.setOnEditCommit((e) -> {
            Swatch s = e.getRowValue();
            s.setValue(e.getNewValue());
            table.getSelectionModel().getSelectedCells().get(0);
            table.refresh();
        });

        table.getColumns().add(nameColumn);
        table.getColumns().add(valueColumn);
        table.getColumns().add(previewColumn);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(FXCollections.observableList(Swatch.swatches));
        table.setEditable(true);
        if (table.getItems().size() == 0) {
            table.getItems().add(new Swatch("white", "&HFFFFFF&", "#FFFFFF"));
            table.getItems().add(new Swatch("black", "&H000000&", "#000000"));
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
                            Swatch s = new Swatch("", "", "");
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

    /**
     * Helper method to set up the Preview column
     */
    private void setupPreviewColumn() {
        this.previewColumn = new TableColumn<>("Preview");
        this.previewColumn.setMinWidth(50);
        this.previewColumn.setCellValueFactory(new Callback<CellDataFeatures<Swatch, Swatch>, ObservableValue<Swatch>>() {
            @Override
            public ObservableValue<Swatch> call(CellDataFeatures<Swatch, Swatch> features) {
                return new ReadOnlyObjectWrapper<>(features.getValue());
            }
        });
        this.previewColumn.setComparator(new Comparator<Swatch>() {
            @Override public int compare(Swatch s1, Swatch s2) { return s1.htmlColor.get().compareTo(s2.htmlColor.get()); }
        });

        this.previewColumn.setCellFactory(new Callback<TableColumn<Swatch, Swatch>, TableCell<Swatch, Swatch>>() {
            @Override
            public TableCell<Swatch, Swatch> call(TableColumn<Swatch, Swatch> previewColumn) {
                return new TableCell<Swatch, Swatch>() {
                    final Pane pane = new Pane();
                    @Override
                    public void updateItem(final Swatch swatch, boolean empty) {
                        super.updateItem(swatch, empty);
                        if (swatch != null && !swatch.getValue().equals("")) {
                            pane.setStyle("-fx-background-color: " + (swatch.htmlColor.get().length() == 7 ? swatch.htmlColor.get() : "#FFFFFF"));
                            setGraphic(pane);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

}
