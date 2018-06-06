package sample;

import javafx.beans.binding.IntegerBinding;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTree<T extends Comparable<T>> implements IRedBlackTree<T>, Iterable<T>, Iterator<T> {

    enum NodeColor {        //Перечисление цветов узла дерева
        RED,
        BLACK,
        NONE
    }

    public class Node {         //Класс реализующий узел дерева
        private T _value;       //Значение узла дерева

        private NodeColor _color;   //Цвет узла

        private Node _parent;   //Родительский узел

        private Node _left;     //Левый дочерниый узел

        private Node _right;    //Правый дочерний узел

        public Node() {         //Конструктор по-умолчанию
            _value = null;
            _color = NodeColor.NONE;
            _parent = null;
            _left = null;
            _right = null;
        }

        public Node(T value, NodeColor color) {     //Конструктор с параметрами, позволящими задать цвет и значение узла
            _value = value;
            _color = color;
            _parent = _nil;
            _left = _nil;
            _right = _nil;
        }

        public boolean isFree() {
            return _value == null || _value == _nil;
        }

        public boolean isLeftFree() {
            return _left == null || _left == _nil;
        }

        public boolean isRightFree() {
            return _right == null || _right == _nil;
        }

        public boolean isParentFree() {
            return _parent == null || _parent == _nil;
        }

        public T getValue() {
            return _value;
        }

        public void setValue(T value) {
            _value = value;
        }

        public Node getParent() {
            return _parent;
        }

        public void setParent(Node node) {
            _parent = node;
        }

        public Node getLeft() {
            return _left;
        }

        public void setLeft(Node node) {
            _left = node;
            if(_left != null && _left != _nil) _left._parent = this;
        }

        public Node getRight() {
            return _right;
        }

        public void setRight(Node node) {
            _right = node;
            if(_right != null && _right != _nil) _right._parent = this;
        }

        public boolean isBlack() {
            return _color == NodeColor.BLACK;
        }

        public void makeBlack() {
            _color = NodeColor.BLACK;
        }

        public boolean isRed() {
            return _color == NodeColor.RED;
        }

        public void makeRed() {
            _color = NodeColor.RED;
        }

        public NodeColor getColor() {
            return _color;
        }

        public void setColor(NodeColor color) {
            _color = color;
        }

        public Node getGrandfather() {              //Возвращет "дедушку" узла дерева
            if(_parent != null && _parent != _nil)
                return _parent._parent;
            return null;
        }

        public Node getSuccessor()      //Возвращает следующий по значению узел дерева
        {
            Node temp = null;
            Node node = this;
            if(!node.isRightFree()) {
                temp = node.getRight();
                while(!temp.isLeftFree())
                    temp = temp.getLeft();
                return temp;
            }
            temp = node.getParent();
            while(temp != _nil && node == temp.getRight()) {
                node = temp;
                temp = temp.getParent();
            }
            return temp;
        }

        public String getColorName() {
            return ((isBlack()) ? "BLACK" : "RED");
        }

    }


    private Node _root;     //Корень дерева

    private Node _nil;      //Ограничитель, который обозначает нулевую ссылку

    private Node _current;  //Ссылка на элемент на который указывает итератор

    private boolean _isRemoved;     //Флаг удаления элемента через итератор

    public RedBlackTree() {         //Конструктор по-умолчанию
        _root = new Node();
        _nil = new Node();
        _nil._color = NodeColor.BLACK;
        _root._parent = _nil;
        _root._right = _nil;
        _root._left = _nil;
        _isRemoved = false;
    }

    private static <T extends Comparable<T>> void leftRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {     //метод, осуществляюший левый поворот дерева tree относительно узла node
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeRight = node.getRight();
        if(nodeParent != tree._nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeRight);
            else
                nodeParent.setRight(nodeRight);
        }
        else {
            tree._root = nodeRight;
            tree._root.setParent(tree._nil);
        }
        node.setRight(nodeRight.getLeft());
        nodeRight.setLeft(node);
    }

    private static <T extends Comparable<T>> void rightRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {    //метод, осуществляюший правый поворот дерева tree относительно узла node
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeLeft = node.getLeft();
        if(nodeParent != tree._nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeLeft);
            else
                nodeParent.setRight(nodeLeft);
        }
        else {
            tree._root = nodeLeft;
            tree._root.setParent(tree._nil);
        }
        node.setLeft(nodeLeft.getRight());
        nodeLeft.setRight(node);
    }

    public static <T extends Comparable<T>> void printTree(RedBlackTree<T> tree, GraphicsContext gc, double x, double y) {      //Вывод дерева
        ArrayList<RedBlackTree<T>.Node> nodes = new ArrayList<RedBlackTree<T>.Node>();
        nodes.add(0,tree._root);
        printNodes(tree, nodes, gc, x, y);
    }

    private static Controller __printTree = new Controller();

    private static <T extends Comparable<T>> void printNodes(RedBlackTree<T> tree, ArrayList<RedBlackTree<T>.Node> nodes, GraphicsContext gc, double x, double y) {     //Вывод узлов дерева
        int childsCounter = 0;
        int nodesAmount = nodes.size();
        ArrayList<RedBlackTree<T>.Node> childs = new ArrayList<RedBlackTree<T>.Node>();
        x=x/2;
        double xDraw;
        for(int i = 0; i < nodesAmount; i++) {
            xDraw = (2 * i + 1) * x - 10;
            if(nodes.get(i) != null && nodes.get(i) != tree._nil) {
                __printTree._printTree(nodes.get(i).getValue().toString());
                __printTree.drawing(gc, nodes.get(i).getColorName(), xDraw, y, nodes.get(i).getValue().toString());
                __printTree.drawgraf(nodes.get(i).isLeftFree(), nodes.get(i).isRightFree(), x, y, gc, i);
                if(!nodes.get(i).isLeftFree()) {
                    childs.add(nodes.get(i).getLeft());
                    childsCounter++;
                }
                else {
                    childs.add(null);
                }
                if(!nodes.get(i).isRightFree()) {
                    childs.add(nodes.get(i).getRight());
                    childsCounter++;
                }
                else {
                    childs.add(null);
                }
            }
            else {
                __printTree._printTree("nil");
                childs.add(null);
                childs.add(null);
            }
        }
        y+=60;
        if(childsCounter != 0)
            printNodes(tree, childs, gc, x, y);
    }

    @Override
    public void add(T o) {      //Реализация метода добавления элемента дарева. На основе добавляемого значения создается узел красного цвета
        Node node = _root;
        Node temp = _nil;
        Node newNode = new Node((T)o, NodeColor.RED);
        while(node != null && node != _nil && !node.isFree()) {
            temp = node;
            if((Integer)newNode.getValue() < (Integer)node.getValue() || newNode.getValue().equals(node.getValue()))
                node = node.getLeft();
            else
                node = node.getRight();
        }
        newNode.setParent(temp);
        if(temp == _nil)
            _root.setValue(newNode.getValue());
        else {
            if((Integer)newNode.getValue() < (Integer)temp.getValue() || newNode.getValue().equals(temp.getValue()))
                temp.setLeft(newNode);
            else
                temp.setRight(newNode);
        }
        newNode.setLeft(_nil);
        newNode.setRight(_nil);
        fixInsert(newNode);
    }

    private void fixInsert(Node node) {     //Исправление древа для сохранения свойств красно-черного дерева
        Node temp;
        while(!node.isParentFree() && node.getParent().isRed()) {
            if(node.getParent() == node.getGrandfather().getLeft()) {
                temp = node.getGrandfather().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    node = node.getGrandfather();
                }
                else {
                    if(node == node.getParent().getRight()) {
                        node = node.getParent();
                        leftRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    rightRotate(this, node.getGrandfather());
                }
            }
            else {
                temp = node.getGrandfather().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    node = node.getGrandfather();
                }
                else {
                    if(node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rightRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfather().makeRed();
                    leftRotate(this, node.getGrandfather());
                }
            }
        }
        _root.makeBlack();
    }

    @Override
    public boolean remove(T o) {        //Реализация удаления элемента дерева true - элемент был удален, false - элемента нет или его удаление невозможно
        return remove(findNode(o));
    }

    private boolean remove(Node node)
    {
        Node temp = _nil, successor = _nil;

        if(node == null || node == _nil)
            return false;

        if(node.isLeftFree() || node.isRightFree())
            successor = node;
        else
            successor = node.getSuccessor();

        if(!successor.isLeftFree())
            temp = successor.getLeft();
        else
            temp = successor.getRight();
        temp.setParent(successor.getParent());

        if(successor.isParentFree())
            _root = temp;
        else if(successor == successor.getParent().getLeft())
            successor.getParent().setLeft(temp);
        else
            successor.getParent().setRight(temp);

        if(successor != node) {
            node.setValue(successor.getValue());
        }
        if(successor.isBlack())
            fixRemove(temp);
        return true;
    }

    private void fixRemove(Node node)       //Исправляет дерево после удаления элемента для сохранения красно-черных свойств дерева.
    {
        Node temp;
        while(node != _root && node.isBlack()) {
            if(node == node.getParent().getLeft()) {
                temp = node.getParent().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    leftRotate(this, node.getParent());
                    temp = node.getParent().getRight();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getRight().isBlack()) {
                        temp.getLeft().makeBlack();
                        temp.makeRed();
                        rightRotate(this, temp);
                        temp = node.getParent().getRight();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getRight().makeBlack();
                    leftRotate(this, node.getParent());
                    node = _root;
                }
            }
            else {
                temp = node.getParent().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    rightRotate(this, node.getParent());
                    temp = node.getParent().getLeft();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getLeft().isBlack()) {
                        temp.getRight().makeBlack();
                        temp.makeRed();
                        leftRotate(this, temp);
                        temp = node.getParent().getLeft();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getLeft().makeBlack();
                    rightRotate(this, node.getParent());
                    node = _root;
                }
            }
        }
        node.makeBlack();
    }

    @Override
    public boolean contains(T o) {      //Реализует функцию проверки на содержание элемента в дереве true - элемент найден, false элемент не найден
        return (findNode(o) != _nil);
    }


    private Node findNode(T o) {        //Поиск узла дерева со значением o
        Node node = _root;
        while(node != null && node != _nil && node.getValue().compareTo(o) != 0) {
            if(node.getValue().compareTo(o) > 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }

    private Node first(){        //Метод для получения первого(наименьшего) элемента структуры
        Node node = _root;
        while(node.getLeft() != null && node.getLeft() != _nil) {
            if(!node.isLeftFree())
                node = node.getLeft();
        }
        return node;
    }

    @Override
    public Iterator<T> iterator() {
        _current = null;
        _isRemoved = false;
        return this;
    }

    @Override
    public boolean hasNext() {
        if(_current != null) {
            if(!_isRemoved) {
                RedBlackTree<T>.Node node = _current.getSuccessor();
                return (node != null && node != _nil);
            }
            return (_current != null && _current != _nil);
        }
        else {
            return (_root != null && !_root.isFree());
        }
    }

    @Override
    public T next() {
        if(_current != null) {
            if(!_isRemoved)
                _current = _current.getSuccessor();
            else
                _isRemoved = false;
        }
        else {
            _current = first();
        }
        if(_current == null || _current == _nil)
            throw new NoSuchElementException();
        return _current.getValue();
    }

    @Override
    public void remove() {
        if(_current != null && !_isRemoved) {
            remove(_current);
            _isRemoved = true;
        }
        else
            throw new IllegalStateException();
    }
}