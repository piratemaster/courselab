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

    public static void _printTree (String _treeElement, String elementColor){
        String[] t = new String[2];
        t[0] =_treeElement;
        t[1] = elementColor;
        treeElement.add(t);
    }


    public void TreeGenButton(){
        rbt = null;
        rbt = new RedBlackTree<Integer>();
        treeElement=null;
        DeleteBox.getItems().clear();
        int testInsertElementsAmount;
        int num;
        try {
            gc = DrawZone.getGraphicsContext2D();
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
            RedBlackTree.printTree(rbt);
            for(int i = 0; i<treeElement.size(); i++) {
                DeleteBox.getItems().add(i,treeElement.get(i)[0]);
            }
            labelDelete.setVisible(true);
            labelInsert.setVisible(true);
            DeleteBox.setVisible(true);
            DeleteBox.getSelectionModel().selectFirst();
            InsertTextBox.setVisible(true);
            InsertButton.setVisible(true);
            DeleteButton.setVisible(true);
            DeleteButton.setDisable(false);
            drawing(gc);
        }
        catch(Exception e) {
            System.out.println("Got error: " + e.getMessage());
        }
    }

    public void TreeInsertElementButton(){
        int num;
        try {
            num = Integer.parseInt(InsertTextBox.getText());
            rbt.add(num);
            RedBlackTree.printTree(rbt);
            DeleteBox.getItems().add(Integer.toString(num));
            if(DeleteBox.isDisable()) {
                DeleteButton.setDisable(false);
                DeleteBox.getSelectionModel().selectFirst();
            }
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
            DeleteBox.getSelectionModel().selectFirst();
            num = Integer.parseInt(DeleteBox.getSelectionModel().getSelectedItem().replaceAll("[\\D]", ""));
            System.out.println(rbt.remove(num));
            DeleteBox.getItems().remove(DeleteBox.getSelectionModel().getSelectedItem());
            RedBlackTree.printTree(rbt);
            DeleteBox.getSelectionModel().selectFirst();
            if(DeleteBox.getItems().isEmpty()) DeleteButton.setDisable(true);
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка");
            alert.showAndWait();
        }
    }
    public void drawing(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        double x = DrawZone.getWidth()/2;
        double y = 60;
        gc.fillOval(x, y, 30, 30);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Times New Roman",15));
        gc.fillText("-1",x+10,y+20);
        gc.setFill(Color.BLACK);
        gc.fillOval(x, y, 30, 30);
    }
}
