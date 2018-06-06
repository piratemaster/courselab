package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;


public class Controller{
    @FXML
    Button button1, InsertButton, DeleteButton;
    @FXML
    Label labelInsert, labelDelete;
    @FXML
    ComboBox<String> DeleteBox;
    @FXML
    TextField InsertTextBox;
    @FXML
    Canvas DrawZone;

    private GraphicsContext gc;

    private static Random rand = new Random();
    private static final int MAX_ELEMENTS = 20;
    private static final int MAX_NUMBER = 100;

    private static RedBlackTree<Integer> rbt = new RedBlackTree<>();

    public void change() {
        InsertTextBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}")) {
                    InsertTextBox.setText(oldValue);
                }
            }
        });
    }

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    private static ArrayList<String> treeElement=null;

    public static void _printTree (String _treeElement) {
        treeElement.add(_treeElement);
    }


    public void TreeGenButton(){
        rbt = null;
        rbt = new RedBlackTree<>();
        treeElement=null;
        DeleteBox.getItems().clear();
        int testInsertElementsAmount;
        double x=DrawZone.getWidth();
        double y=60;
        gc = DrawZone.getGraphicsContext2D();
        gc.beginPath();
        gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
        gc.moveTo(0,0);
        int num;
        try {
            testInsertElementsAmount = rand.nextInt(MAX_ELEMENTS - 1) + 1;
            for(int i = 0; i < testInsertElementsAmount; i++) {
                num = rand.nextInt(MAX_NUMBER);
                if(i == 0 || !rbt.contains(num)) rbt.add(num);
            }
            treeElement = new ArrayList<>();
            RedBlackTree.printTree(rbt, gc, x, y);
            for(int i = 0; i<treeElement.size(); i++) {
                if((treeElement.get(i)).equals("nil"));
                else
                    DeleteBox.getItems().add(treeElement.get(i));
            }
            if(!labelDelete.isVisible()) {
                labelDelete.setVisible(true);
                labelInsert.setVisible(true);
                DeleteBox.setVisible(true);
                InsertTextBox.setVisible(true);
                InsertButton.setVisible(true);
                DeleteButton.setVisible(true);
            }
            DeleteBox.getSelectionModel().selectFirst();
            DeleteButton.setDisable(false);
            DeleteBox.getSelectionModel().selectFirst();
            InsertTextBox.setDisable(false);
            InsertButton.setDisable(false);
       }
       catch(Exception e) {
           alert.setTitle("Ошибка");
           alert.setHeaderText(null);
           alert.setContentText(e.getMessage());
           alert.showAndWait();
       }
    }

    public void TreeInsertElementButton(){
        int num;
        try {
            if(DeleteBox.getItems().size()<30) {
                num = Integer.parseInt(InsertTextBox.getText());
                if(rbt.contains(num)){
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Такой элемент присутствует в дереве");
                    alert.showAndWait();
                }
                else {
                    double x = DrawZone.getWidth();
                    double y = 60;
                    gc = DrawZone.getGraphicsContext2D();
                    gc.beginPath();
                    gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
                    gc.moveTo(0,0);
                    rbt.add(num);
                    RedBlackTree.printTree(rbt, gc, x, y);
                    DeleteBox.getItems().add(Integer.toString(num));
                }
            }
            else {
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Превышено количество элементов");
                alert.showAndWait();
            }
        }
        catch(Exception e) {
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполните поле ввода");
            alert.showAndWait();
        }
    }


    public void TreeRemoveElementButton() {
    int num;
        try {
            double x=DrawZone.getWidth();
            double y=60;
            gc = DrawZone.getGraphicsContext2D();
            gc.beginPath();
            gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
            gc.moveTo(0,0);
            DeleteBox.getSelectionModel().selectFirst();
            num = Integer.parseInt(DeleteBox.getSelectionModel().getSelectedItem().replaceAll("[\\D]", ""));
            rbt.remove(num);
            DeleteBox.getItems().remove(DeleteBox.getSelectionModel().getSelectedItem());
            RedBlackTree.printTree(rbt, gc, x, y);
            DeleteBox.getSelectionModel().selectFirst();
            if(DeleteBox.getItems().isEmpty()) {
                DeleteButton.setDisable(true);
                DeleteBox.setDisable(true);
                InsertTextBox.setDisable(true);
                InsertButton.setDisable(true);
            }
        }
        catch(Exception e) {
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка");
            alert.showAndWait();
        }
    }


    public void drawing(GraphicsContext gc, String color, double xDraw, double y, String nodeValue){
        if(!color.equals("nil")) {
            if (color.equals("BLACK")) gc.setFill(Color.BLACK);
            if (color.equals("RED")) gc.setFill(Color.RED);
            gc.fillOval(xDraw, y, 30, 30);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial Black", 15));
            gc.fillText(nodeValue, xDraw + 5, y + 20);
            gc.setFill(Color.BLACK);
        }
    }
    public void drawgraf(boolean isLeftFree, boolean isRightFree, double x, double y, GraphicsContext gc, int i){
        double xDraw;
        double xDraw1 = (2 * i + 1) * x + 10 - 10;
        if(!isLeftFree){
            gc.moveTo(xDraw1+5, y + 30);
            xDraw = (2 * i) * x + x/2 - 10;
            gc.setFill(Color.BLACK);
            gc.lineTo((xDraw+15), y + 60);
            gc.moveTo(xDraw, y + 30);
        }
        if(!isRightFree){
            gc.moveTo(xDraw1+5, y + 30);
            xDraw = (2 * i + 2) * x - x/2 + 10 - 10;
            gc.setFill(Color.BLACK);
            gc.lineTo((xDraw+5), y + 60);
            gc.moveTo(xDraw, y + 30);
        }
        gc.stroke();
    }
}