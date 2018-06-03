package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;


public class Controller{
    @FXML
    Button button1, InsertButton, DeleteButton;
    @FXML
    Label label1, labelInsert, labelDelete;
    @FXML
    ComboBox<String> DeleteBox;
    @FXML
    TextField InsertTextBox;
    @FXML
    Canvas DrawZone;

    GraphicsContext gc;

    private static Random rand = new Random();
    private static final int MAX_ELEMENTS = 20;
    private static final int MAX_NUMBER = 100;

    public static RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();

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

    private static ArrayList<String[]> treeElement=null;

    public static void _printTree (String _treeElement, String elementColor) {
        String[] t = new String[2];
        t[0] = _treeElement;
        t[1] = elementColor;
        treeElement.add(t);
    }

    int n;

    public void TreeGenButton(){
        n=0;
        rbt = null;
        rbt = new RedBlackTree<Integer>();
        treeElement=null;
        DeleteBox.getItems().clear();
        int testInsertElementsAmount;
        double x=DrawZone.getWidth();
        double y=60;
        gc = DrawZone.getGraphicsContext2D();
        gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
        gc.moveTo(0,0);
        int num;
       try {
            System.out.println("\n\nStart test #1");
            testInsertElementsAmount = rand.nextInt(MAX_ELEMENTS - 1) + 1;
            System.out.println(" Insertion [" + testInsertElementsAmount + "]:");
            for(int j = 0; j < testInsertElementsAmount; j++) {
                num = rand.nextInt(MAX_NUMBER);
                System.out.println("  Insert " + num + " into tree");
                rbt.add(num);
            }
            System.out.println("Tree:");
            treeElement = new ArrayList<>();
            RedBlackTree.printTree(rbt, gc, x, y, n);
            for(int i = 0; i<treeElement.size(); i++) {
                if((treeElement.get(i)[0]).equals("nil"));
                else
                DeleteBox.getItems().add(treeElement.get(i)[0]);
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
            System.out.println("Got error: " + e.getMessage());
       }
    }

    public void TreeInsertElementButton(){
        int num;
        try {
            n=0;
            double x=DrawZone.getWidth();
            double y=60;
            gc = DrawZone.getGraphicsContext2D();
            gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
            gc.moveTo(0,0);
            num = Integer.parseInt(InsertTextBox.getText());
            rbt.add(num);
            RedBlackTree.printTree(rbt, gc, x, y, n);
            DeleteBox.getItems().add(Integer.toString(num));
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполните поле ввода");
            alert.showAndWait();
        }
    }


    public void TreeRemoveElementButton() {
    int num;
        try {
            n=0;
            double x=DrawZone.getWidth();
            double y=60;
            gc = DrawZone.getGraphicsContext2D();
            gc.clearRect(0,0,DrawZone.getWidth(),DrawZone.getHeight());
            gc.moveTo(0,0);
            DeleteBox.getSelectionModel().selectFirst();
            num = Integer.parseInt(DeleteBox.getSelectionModel().getSelectedItem().replaceAll("[\\D]", ""));
            System.out.println(rbt.remove(num));
            DeleteBox.getItems().remove(DeleteBox.getSelectionModel().getSelectedItem());
            RedBlackTree.printTree(rbt, gc, x, y, n);
            DeleteBox.getSelectionModel().selectFirst();
            if(DeleteBox.getItems().isEmpty()) {
                DeleteButton.setDisable(true);
                InsertTextBox.setDisable(true);
                InsertButton.setDisable(true);
            }
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка");
            alert.showAndWait();
        }
    }


    public void drawing(GraphicsContext gc, String color, double xDraw, double y, String nodeValue){
        if(!color.equals("nil")) {
          /*  gc.setFill(Color.color(0.9, 0.165, 0.49));
            gc.fillRect(xDraw, y, 60, 30);
            gc.setFill(Color.WHITE);
            gc.fillText(nodeValue,xDraw+35, y+20);
        }*/
        //else {
            if (color.equals("BLACK")) gc.setFill(Color.BLACK);
            if (color.equals("RED")) gc.setFill(Color.RED);
            gc.fillOval(xDraw, y, 30, 30);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial Black", 15));
            gc.fillText(nodeValue, xDraw + 5, y + 20);
            gc.setFill(Color.BLACK);
        }
    }
    public void drawgraf(boolean isLeftFree, boolean isRightFree, double xDraw, double y, GraphicsContext gc, int i){
        if(isLeftFree==false){
            gc.moveTo(xDraw, y);
            gc.setFill(Color.BLACK);
            gc.lineTo((xDraw/2), y + 60);
            gc.stroke();
            gc.moveTo(xDraw, y);
        }
        if(isRightFree==false){
            gc.moveTo(xDraw, y);
            gc.setFill(Color.BLACK);
            gc.lineTo((xDraw + xDraw/2), y + 60);
            gc.stroke();
            gc.moveTo(xDraw, y);
        }
    }
}