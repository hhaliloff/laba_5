import CollectionClasses.*;
import CommandMethods.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


class Main {
    public static Boolean RunCommand(
            String command,
            Scanner scanner,
            LinkedList<Ticket> collection,
            String path,
            Set<String> scriptsInProcess
    ) throws IOException {
        String[] parts = command.split(" ");
        String cmd = parts[0];


        //интерфейс
        switch (cmd) {
            case "exit":
                return false;

            case "add":
                Ticket ticket = Add.add();
                collection.add(ticket);
                IdController.ChangeIds(collection);
                System.out.println("билет успешно добавлен в коллекцию!");
                break;

            case "update":
                if (parts.length < 2) {
                    System.out.println("Ошибка: нужно указать id");
                    break;
                }

                int id = Integer.parseInt(parts[1]);

                if (id >= collection.size() || id < 0) {
                    System.out.println("Билета не существует");
                } else {
                    collection.set(id, Add.add());
                }
                break;

            case "remove_by_id":
                if (parts.length < 2) {
                    System.out.println("Ошибка: нужно указать id");
                    break;
                }

                int id_remove = Integer.parseInt(parts[1]);

                if (id_remove >= collection.size() || id_remove < 0) {
                    System.out.println("Билета с таким id не существует!");
                } else {
                    collection.remove(id_remove);
                    IdController.ChangeIds(collection);
                    System.out.println("билет c ID " + id_remove + " успешно удалён!");
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
                    if (t.getCreationDate().isBefore(earliest_date)) {
                        ;
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

            case "remove_last":
                if (!collection.isEmpty()) {
                    collection.removeLast();
                    System.out.println("Последний элемент коллекции удален!");
                } else {
                    System.out.println("Коллекция пуста.");
                }
                break;

            case "add_if_min":
                Ticket new_ticket = Add.add();
                if (collection.isEmpty()) {
                    collection.add(new_ticket);
                    System.out.println("Элемент добавлен в коллекцию, так как коллекция пуста.");
                }

                Ticket minTicket = collection.get(0);

                for (Ticket t : collection) {
                    if (t.getPrice() < minTicket.getPrice()) {
                        minTicket = t;
                    }
                }

                if (new_ticket.getPrice() < minTicket.getPrice()) {
                    collection.add(new_ticket);
                    System.out.println("Элемент добавлен.");
                } else {
                    System.out.println("Элемент не добавлен, так как его id больше или равен минимальному id в коллекции.");
                }
                break;

            case "shuffle":
                Collections.shuffle(collection);
                System.out.println("Коллекция перемешана!");
                break;

            case "filter_less_than_venue":
                if (parts.length < 2) {
                    System.out.println("Ошибка: нужно указать вместимость");
                    break;
                }

                int id_less_ven = Integer.parseInt(parts[1]);

                List<Ticket> filteredTickets = new LinkedList<>();
                for (Ticket tacket : collection) {
                    if (tacket.getVenue().getCapacity() < id_less_ven) {
                        filteredTickets.add(tacket);
                    }
                }
                if (filteredTickets.isEmpty()) {
                    System.out.println("Нет элементов с вместимостью меньшим, чем заданное.");
                } else {
                    System.out.println("Элементы с вместимостью меньше заданного:");
                    for (Ticket tacket : filteredTickets) {
                        System.out.println("Ticket ID: " + tacket.getId() + ", Name: " + tacket.getName() + ", Venue Capacity: " + tacket.getVenue().getCapacity());
                    }
                }
                break;

            case "filter_greater_than_venue":
                if (parts.length < 2) {
                    System.out.println("Ошибка: нужно указать вместимость");
                    break;
                }

                int id_greater_ven = Integer.parseInt(parts[1]);

                List<Ticket> filteredTicketsGreater = new LinkedList<>();
                for (Ticket tacket : collection) {
                    if (tacket.getVenue().getCapacity() > id_greater_ven) {
                        filteredTicketsGreater.add(tacket);
                    }
                }
                if (filteredTicketsGreater.isEmpty()) {
                    System.out.println("Нет элементов с вместимостью большим, чем заданное.");
                } else {
                    System.out.println("Элементы с вместимостью больше заданного:");
                    for (Ticket tacket : filteredTicketsGreater) {
                        System.out.println("Ticket ID: " + tacket.getId() + ", Name: " + tacket.getName() + ", Venue Capacity: " + tacket.getVenue().getCapacity());
                    }
                }
                break;

            case "print_field_descending_price":
                List<Ticket> sortedCollection = new LinkedList<>(collection);
                Collections.sort(sortedCollection, Comparator.comparingDouble(Ticket::getPrice).reversed());
                System.out.println("Значения поля price в порядке убывания:");
                for (Ticket tacket : sortedCollection) {
                    System.out.println("Ticket ID: " + tacket.getId() + ", Price: " + tacket.getPrice());
                }
                break;

            case "execute_script": {
                if (parts.length < 2) {
                    System.out.println("Ошибка: нужно указать имя файла");
                    break;
                }

                String file_name = parts[1];

                // защита от рекурсии
                if (scriptsInProcess.contains(file_name)) {
                    System.out.println("Ошибка: рекурсивный вызов скрипта запрещён: " + file_name);
                    break;
                }

                scriptsInProcess.add(file_name);

                try (Scanner fileScanner = new Scanner(new java.io.File(file_name))) {
                    while (fileScanner.hasNextLine()) {
                        String scriptCommand = fileScanner.nextLine().trim();

                        if (scriptCommand.isEmpty()) {
                            continue; // пропускаем пустые строки
                        }

                        System.out.println("Выполняется команда из скрипта: " + scriptCommand);

                        boolean stillRunning = RunCommand(scriptCommand, scanner, collection, path, scriptsInProcess);
                        if (!stillRunning) {
                            scriptsInProcess.remove(file_name);
                            return false; // exit внутри скрипта завершает программу
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при чтении файла: " + e.getMessage());
                } finally {
                    scriptsInProcess.remove(file_name);
                }

                break;
            }

            default:
                System.out.println("Неизвестная команда: " + command);
                break;
        }

        return true;
    }

    public static void main(String[] args) throws IOException {
        LinkedList<Ticket> collection = new LinkedList<>();
        String path = System.getenv("TICKETS_FILE");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // чтение файла
        CollectionToFile.file_to_collection(collection, path);

        // набор выполняющихся сейчас скриптов (для защиты от рекурсии)
        Set<String> scriptsInProcess = new HashSet<>();

        // интерфейс
        while (running) {
            System.out.println("Введите команду > ");
            String command = scanner.nextLine().trim();

            running = RunCommand(command, scanner, collection, path, scriptsInProcess);
        }

    }
}