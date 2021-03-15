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
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
import javafx.scene.image.Image;

public class Controller implements Initializable {
    @FXML
    private Pane pane;
    @FXML
    private Button mode;
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
    private Button open;
    @FXML
    private Button brush;

    @FXML
    private ContextMenu menu;

    //java.util.List<String> fontFamilies = Font.getFamilies();
    ObservableList<String> fontNames    = FXCollections.observableArrayList(Font.getFontNames());

    boolean toolSelected = false;
    boolean eraseSelected = false;
    boolean skyMode = true;
    private int x1=100,y1=100;

    GraphicsContext brushTool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();
        brushTool.setFill(colorpicker.getValue());
        brushTool.fillRect(0,0,886,641);
        canvas.setOnMouseDragged(e->{
            Color color = new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0);
            brushTool.setFill(color);

            double size;
            if(bsize.getText().trim().isEmpty())
                bsize.setText("10");
            size=Double.parseDouble(bsize.getText());
            double x = e.getX()-size;
            double y = e.getY()-size;

            if(eraseSelected && !bsize.getText().trim().isEmpty()) {
                brushTool.setFill(Color.WHITE);
                brushTool.fillRect(x, y, size, size);
            }
            else if(toolSelected && !bsize.getText().trim().isEmpty()) {
                brushTool.setFill(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
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
                if(!skyMode)
                    checkbox.setStyle("-fx-text-fill:white;");

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
                if(skyMode) {
                    mode.setText("Dark Mode");
                    root.getStylesheets().remove(getClass().getResource("stylesDark.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                }
                else {
                    mode.setText("Light Mode");
                    root.getStylesheets().remove(getClass().getResource("styles.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("stylesDark.css").toExternalForm());
                }
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
                if(!skyMode)
                    checkbox.setStyle("-fx-text-fill:white;");
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
                if(skyMode) {
                    mode.setText("Dark Mode");
                    root.getStylesheets().remove(getClass().getResource("stylesDark.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                }
                else {
                    mode.setText("Light Mode");
                    root.getStylesheets().remove(getClass().getResource("styles.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("stylesDark.css").toExternalForm());
                }
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
                if(!skyMode)
                    checkbox.setStyle("-fx-text-fill:white;");
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
                if(skyMode) {
                    mode.setText("Dark Mode");
                    root.getStylesheets().remove(getClass().getResource("stylesDark.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                }
                else {
                    mode.setText("Light Mode");
                    root.getStylesheets().remove(getClass().getResource("styles.css").toExternalForm());
                    root.getStylesheets().add(getClass().getResource("stylesDark.css").toExternalForm());
                }
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
        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpeg) (*.png) (*.jpg)","*.jpeg","*.jpg)","*.png");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(Main.primaryStage);

                if(file != null){
                    try {
                        String imagepath = file.toURI().toURL().toString();
                        Image image = new Image(imagepath);
                        brushTool.drawImage(image,0,0,Math.min(canvas.getWidth(),image.getWidth()),Math.min(image.getHeight(),canvas.getHeight()));
                        //brushTool.drawImage(image,0,0,image.getWidth(),image.getHeight());
                        //brushTool.drawImage(image,0,0,Math.min(canvas.getWidth(),image.getWidth()),Math.min(canvas.getHeight(),image.getHeight()));
                    } catch (Exception ex) {
                        System.out.println("Galat: "+ ex.getMessage());
                    }
                }
            }
            });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("Image files (*.jpeg) (*.png) (*.jpg)","*.jpeg","*.jpg)","*.png");
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
            Main.primaryStage.setWidth(Math.max(canvasWidth+50,900));
            Main.primaryStage.setHeight(canvasHeight+200);

            startVBox.getChildren().add(canvas);
            brushTool = canvas.getGraphicsContext2D();
            brushTool.setFill(Color.WHITE);
            brushTool.fillRect(0, 0, canvasWidth, canvasHeight);
            brushTool.setFill(colorpicker.getValue());

            createStage.close();
        });
    }
    
    private void drawRect(Rectangle rect,boolean filled){
        if(filled) {
            brushTool.setFill(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.fillRect(rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
        }
        else {
            brushTool.setStroke(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.strokeRect(rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
        }
    }

    private void drawCirc(Circle circ,boolean filled){
        if(!filled) {
            brushTool.setStroke(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.strokeOval(circ.getCenterX() - circ.getRadius(), circ.getCenterY() - circ.getRadius(), circ.getRadius() * 2, circ.getRadius() * 2);
        }
        else {
            brushTool.setFill(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.fillOval(circ.getCenterX() - circ.getRadius(), circ.getCenterY() - circ.getRadius(), circ.getRadius() * 2, circ.getRadius() * 2);
        }
    }

    private void drawText(String text,boolean filled, Font font){
        brushTool.setFont(font);
        if(!filled) {
            brushTool.setStroke(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.strokeText(text,x1,y1);
        }
        else {
            brushTool.setFill(new Color(colorpicker.getValue().getRed(),colorpicker.getValue().getGreen(),colorpicker.getValue().getBlue(),(slider.getValue()*1.0)/100.0));
            brushTool.fillText(text,x1,y1);
        }
    }

    @FXML
    private void modeClicked(javafx.event.ActionEvent actionEvent) {
        skyMode = (!skyMode);
        if(skyMode) {
            mode.setText("Dark Mode");
            pane.getStylesheets().remove(getClass().getResource("stylesDark.css").toExternalForm());
            pane.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        }
        else {
            mode.setText("Light Mode");
            pane.getStylesheets().remove(getClass().getResource("styles.css").toExternalForm());
            pane.getStylesheets().add(getClass().getResource("stylesDark.css").toExternalForm());
        }
    }
}
