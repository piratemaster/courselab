package sample;

public interface IRedBlackTree<T extends Comparable<T>> {

    void add(T o);  //Добавить элемент в дерево

    boolean remove(T o);    //Удалить элемент из дерева

    boolean contains(T o);  //Возвращает true, если элемент содержится в дереве
}