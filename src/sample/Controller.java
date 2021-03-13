package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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

    boolean toolSelected = false;
    boolean eraseSelected = false;

    GraphicsContext brushTool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e->{

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
                brushTool.setFill(colorpicker.getValue());
                brushTool.fillRoundRect(x,y,size,size,size,size);
            }
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

    @FXML
    private void drawRect(Rectangle rect){
        brushTool.setFill(Color.WHITESMOKE);
        brushTool.fillRect(rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());
        brushTool.setFill(Color.GREEN);
        brushTool.setStroke(Color.BLUE);
    }


    public void drawRectangle(ActionEvent actionEvent) {
        Rectangle rect = new Rectangle();
        rect.setX(50);
        rect.setY(50);
        rect.setWidth(10);
        rect.setHeight(10);
        drawRect(rect);
    }
}
