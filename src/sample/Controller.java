package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
//import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Slider slider;
    @FXML
    private Button buttonSave;
    @FXML
    private ColorPicker colorpicker;
    @FXML
    private TextField bsize;
    @FXML
    private Canvas canvas;
    @FXML
    private Button newCanvas;
    @FXML
    private VBox startVBox;

    @FXML
    private Button brush;

    @FXML
    private ContextMenu menu;

    //java.util.List<String> fontFamilies = Font.getFamilies();
    ObservableList<String> fontNames    = FXCollections.observableArrayList(Font.getFontNames());

    boolean toolSelected = false;
    boolean eraseSelected = false;
    private int x1=100,y1=100;

    GraphicsContext brushTool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e->{
            double r = colorpicker.getValue().getRed();
            double b = colorpicker.getValue().getBlue();
            double g = colorpicker.getValue().getGreen();
            Color color = new Color(r,g,b,(slider.getValue()*1.0)/100.0);
            brushTool.setFill(color);

            double size;
            if(bsize.getText().trim().isEmpty())
                bsize.setText("10");
            size=Double.parseDouble(bsize.getText());
            double x = e.getX()-size;
            double y = e.getY()-size;

            if(eraseSelected && !bsize.getText().trim().isEmpty()) {
                brushTool.clearRect(x, y, size, size);
            }
            else if(toolSelected && !bsize.getText().trim().isEmpty()) {
                brushTool.fillRoundRect(x,y,size,size,size,size);
            }
        });

        //Canvas right click context menu for shapes
        menu = new ContextMenu();
        MenuItem item1 = new MenuItem("Rectangle");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField cWidth = new TextField();
                cWidth.setPromptText("Width");
                cWidth.setAlignment(Pos.CENTER);

                TextField cHeight = new TextField();
                cHeight.setPromptText("Height");
                cHeight.setAlignment(Pos.CENTER);

                CheckBox checkbox = new CheckBox("Fill shape");

                Button createButton = new Button();
                createButton.setText("OK");

                VBox vBox = new VBox();
                vBox.getChildren().addAll(cWidth,cHeight,checkbox,createButton);
                vBox.setPrefHeight(200);
                vBox.setPrefWidth(300);
                vBox.setSpacing(5);
                vBox.setAlignment(Pos.CENTER);

                Stage createStage = new Stage();
                AnchorPane root = new AnchorPane();
                root.getChildren().add(vBox);

                Scene canvasScene = new Scene(root);
                createStage.setTitle("Create Rectangle");
                createStage.setScene(canvasScene);
                createStage.show();

                createButton.setOnAction(actionEvent1 -> {
                    boolean filled = checkbox.isSelected();
                    Rectangle rect = new Rectangle();
                    rect.setX(x1);
                    rect.setY(y1);
                    rect.setWidth((int)Double.parseDouble(cWidth.getText()));
                    rect.setHeight((int)Double.parseDouble(cHeight.getText()));
                    drawRect(rect,filled);
                    createStage.close();
                });
            }
        });

        MenuItem item2 = new MenuItem("Circle");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField radius = new TextField();
                radius.setPromptText("Radius");
                radius.setAlignment(Pos.CENTER);

                CheckBox checkbox = new CheckBox("Fill shape");

                Button createButton = new Button();
                createButton.setText("OK");

                VBox vBox = new VBox();
                vBox.getChildren().addAll(radius,checkbox,createButton);
                vBox.setPrefHeight(150);
                vBox.setPrefWidth(200);
                vBox.setSpacing(5);
                vBox.setAlignment(Pos.CENTER);

                Stage createStage = new Stage();
                AnchorPane root = new AnchorPane();
                root.getChildren().add(vBox);

                Scene canvasScene = new Scene(root);
                createStage.setTitle("Create Circle");
                createStage.setScene(canvasScene);
                createStage.show();

                createButton.setOnAction(actionEvent1 -> {
                    boolean filled = checkbox.isSelected();
                    Circle circ = new Circle();
                    circ.setCenterX(x1);
                    circ.setCenterY(y1);
                    circ.setRadius((int)Double.parseDouble(radius.getText()));
                    drawCirc(circ,filled);
                    createStage.close();
                });
            }
        });

        MenuItem item3 = new MenuItem("Text");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField text = new TextField();
                text.setPromptText("Text here");
                text.setAlignment(Pos.CENTER);

                ComboBox comboBox = new ComboBox();
                comboBox.setItems(fontNames);
                comboBox.setValue("Agency FB");

                Spinner spinner = new Spinner();
                spinner.setEditable(true);
                spinner.setPromptText("Font size");
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
                spinner.setValueFactory(valueFactory);


                CheckBox checkbox = new CheckBox("Fill Text");

                Button createButton = new Button();
                createButton.setText("OK");

                VBox vBox = new VBox();
                vBox.getChildren().addAll(text,comboBox,spinner,checkbox,createButton);
                vBox.setPrefHeight(150);
                vBox.setPrefWidth(200);
                vBox.setSpacing(5);
                vBox.setAlignment(Pos.CENTER);

                Stage createStage = new Stage();
                AnchorPane root = new AnchorPane();
                root.getChildren().add(vBox);

                Scene canvasScene = new Scene(root);
                createStage.setTitle("Create Text");
                createStage.setScene(canvasScene);
                createStage.show();

                createButton.setOnAction(actionEvent1 -> {
                    drawText(text.getText(),checkbox.isSelected(),new Font(comboBox.getValue().toString(),Integer.parseInt(spinner.getValue().toString())));
                    createStage.close();
                });
            }
        });

        menu.getItems().addAll(item1, item2,item3);
        canvas.setOnContextMenuRequested(e -> {
            x1=(int)e.getSceneX()-15;
            y1=(int)e.getSceneY()-45;
            menu.show(canvas, e.getScreenX(), e.getScreenY());
        });

        brush.setOnMouseClicked(e->{
            if(toolSelected) {
                toolSelected = false;
                brush.setText("Paint");
                eraseSelected = true;
            }
            else {
                toolSelected = true;
                brush.setText("Erase");
                eraseSelected = false;
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(Main.primaryStage);

                if(file != null){
                    try {
                        WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });
    }

    @FXML
    public void newCanvas(javafx.event.ActionEvent actionEvent) {
        TextField cWidth = new TextField();
        cWidth.setPromptText("Width");
        cWidth.setAlignment(Pos.CENTER);

        TextField cHeight = new TextField();
        cHeight.setPromptText("Height");
        cHeight.setAlignment(Pos.CENTER);

        Button createButton = new Button();
        createButton.setText("Create Canvas");

        VBox vBox = new VBox();
        vBox.getChildren().addAll(cWidth,cHeight,createButton);
        vBox.setPrefHeight(200);
        vBox.setPrefWidth(300);
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);

        Stage createStage = new Stage();
        AnchorPane root = new AnchorPane();
        root.getChildren().add(vBox);

        Scene canvasScene = new Scene(root);
        createStage.setTitle("Create Canvas");
        createStage.setScene(canvasScene);
        createStage.show();

        createButton.setOnAction(actionEvent1 -> {
            double canvasWidth = Double.parseDouble(cWidth.getText());
            double canvasHeight = Double.parseDouble(cHeight.getText());
            brushTool.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            startVBox.getChildren().remove(canvas);
            canvas=new Canvas(canvasWidth,canvasHeight);
            Main.primaryStage.setWidth(Math.max(canvasWidth,500));
            Main.primaryStage.setHeight(Math.max(canvasHeight,500));
            startVBox.getChildren().add(canvas);
            brushTool = canvas.getGraphicsContext2D();
            createStage.close();
        });
    }
    
    private void drawRect(Rectangle rect,boolean filled){
        if(filled) {
            brushTool.setFill(colorpicker.getValue());
            brushTool.fillRect(rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
        }
        else {
            brushTool.setStroke(colorpicker.getValue());
            brushTool.strokeRect(rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
        }
    }

    private void drawCirc(Circle circ,boolean filled){
        if(!filled) {
            brushTool.setStroke(colorpicker.getValue());
            brushTool.strokeOval(circ.getCenterX() - circ.getRadius(), circ.getCenterY() - circ.getRadius(), circ.getRadius() * 2, circ.getRadius() * 2);
        }
        else {
            brushTool.setFill(colorpicker.getValue());
            brushTool.fillOval(circ.getCenterX() - circ.getRadius(), circ.getCenterY() - circ.getRadius(), circ.getRadius() * 2, circ.getRadius() * 2);
        }
    }

    private void drawText(String text,boolean filled, Font font){
        brushTool.setFont(font);
        if(!filled) {
            brushTool.setStroke(colorpicker.getValue());
            brushTool.strokeText(text,x1,y1);
        }
        else {
            brushTool.setFill(colorpicker.getValue());
            brushTool.fillText(text,x1,y1);
        }
    }

}
