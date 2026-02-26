/**
 * Класс для обновления id у всех билетов.
 */
package CommandMethods;

import CollectionClasses.Ticket;

import java.util.LinkedList;

public class IdController {
    public static LinkedList<Ticket> ChangeIds(LinkedList<Ticket> collection) {
        int list_length = collection.size();
        for (int i = 0; i < list_length; i++) {
            Ticket one_element = collection.get(i);
            one_element.setId(i);
            collection.set(i, one_element);
        }
        return collection;
    }
}
