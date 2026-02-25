package CommandMethods;

import CollectionClasses.Ticket;

import java.util.LinkedList;

public class Remove {
    public static LinkedList<Ticket> remove_by_id(LinkedList<Ticket> collection, int id) {
        collection.remove(id);
        IdController.ChangeIds(collection);
        System.out.println("Объект класса с индексом " + id + " успешно удалён из коллекции!");
        return collection;
    }
}
