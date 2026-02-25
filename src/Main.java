import CollectionClasses.*;
import CommandMethods.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException {
        LinkedList<Ticket> collection = new LinkedList<>();
        String path = System.getenv("TICKETS_FILE");
        Scanner scanner = new Scanner(System.in);
        Boolean running = true;

        //чтение файла
        CollectionToFile.file_to_collection(collection, path);

        //тест(удалить)
        Location test_location = new Location(100.0f, 99, 834, "Lenina");
        Address test_address = new Address("Lenina_A", test_location);
        Venue test_venue = new Venue("Lenina_V", 1000, VenueType.BAR, test_address);
        Coordinates test_coordinates = new Coordinates(100, 99);
        Ticket test_ticket = new Ticket("Lenina_T", test_coordinates, java.time.LocalDate.now(), 1000, "comment", true, TicketType.BUDGETARY, test_venue);
        collection.add(test_ticket);

        String test_string = XmlCoder.code(test_ticket) + XmlCoder.code(test_ticket);

        //интерфейс
        while (running) {
            System.out.println("Введите команду > ");
            String command = scanner.nextLine();
            switch (command) {
                case "exit":
                    running = false;
                    break;

                case "add":
                    Ticket ticket = Add.add();
                    collection.add(ticket);
                    IdController.ChangeIds(collection);
                    System.out.println("билет успешно добавлен в коллекцию!");
                    break;

                case "update id":
                    int id = ErrorCatcher.notInt(scanner, "Введите id билета, который хотите обновить: ");
                    if (id > collection.size() || id < 0) {
                        System.out.println("Билета с таким id не существует!");
                    } else {
                        collection.set(id, Add.add());
                        IdController.ChangeIds(collection);
                        System.out.println("билет с индексом " + id + " успешно обновлён!");
                    }
                    break;

                case "remove_by_id":
                    int id_remove = ErrorCatcher.notInt(scanner, "Введите id билета, который хотите удалить: ");
                    if (id_remove > collection.size() || id_remove < 0) {
                        System.out.println("Билета с таким id не существует!");
                    } else {
                        collection.remove(id_remove);
                        IdController.ChangeIds(collection);
                        System.out.println("билет с индексом " + id_remove + " успешно удалён из коллекции!");
                    }
                    break;

                case "clear":
                    collection.clear();
                    IdController.ChangeIds(collection);
                    System.out.println("Коллекция успешно очищена!");
                    break;

                case "save":
                    CollectionToFile.collection_to_file(collection, path);
                    System.out.println("Коллекция успешно сохранена в файл!");
                    break;

                case "show":
                    String all_elements = "";
                    for (Ticket t : collection) {
                        all_elements = all_elements + ", " + t.getName();
                    }
                    System.out.println(all_elements.substring(2));
                    break;

                case "info":
                    java.time.LocalDate earliest_date = LocalDate.MAX;
                    for (Ticket t : collection) {
                        if (t.getCreationDate().isBefore(earliest_date)) {;
                            earliest_date = t.getCreationDate();
                        }
                    }
                    System.out.println("Это коллекция билетов на мероприятия Халилова Чингиза и Федорова Вячеслава. Мы собираемся сходить на всех них, так что покупаем билеты заранее. Поэтому дата создания коллекции - это дата создания самого старого билета в ней.");
                    System.out.println("Тип коллекции: " + collection.getClass());
                    System.out.println("Дата создания: " + earliest_date);
                    System.out.println("Количество элементов в коллекции: " + collection.size());
                    break;

                case "help":
                    System.out.println("""
                            Список команд:
                            help : вывести справку по доступным командам
                            info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)
                            show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                            add {element} : добавить новый элемент в коллекцию
                            update id {element} : обновить значение элемента коллекции, id которого равен заданному
                            remove_by_id id : удалить элемент из коллекции по его id
                            clear : очистить коллекцию
                            save : сохранить коллекцию в файл
                            execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                            exit : завершить программу (без сохранения в файл)
                            remove_last : удалить последний элемент из коллекции
                            add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                            shuffle : перемешать элементы коллекции в случайном порядке
                            filter_less_than_venue venue : вывести элементы, значение поля venue которых меньше заданного
                            filter_greater_than_venue venue : вывести элементы, значение поля venue которых больше заданного
                            print_field_descending_price : вывести значения поля price всех элементов в порядке убывания""");
                    break;
            }
        }
    }
}