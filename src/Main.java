import CollectionClasses.*;
import CommandMethods.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        LinkedList<Ticket> collection = new LinkedList<>();
        String path = System.getenv("TICKETS_FILE");
        Scanner scanner = new Scanner(System.in);
        AtomicReference<Boolean> running = new AtomicReference<>(true);

        //чтение файла
        CollectionToFile.file_to_collection(collection, path);

        //тест(удалить)
        Location test_location = new Location(100.0f, 99, 834, "Lenina");
        Address test_address = new Address("Lenina_A", test_location);
        Venue test_venue = new Venue("Lenina_V", 1000, VenueType.BAR, test_address);
        Coordinates test_coordinates = new Coordinates(100, 99);
        Ticket test_ticket = new Ticket("Lenina_T", test_coordinates, LocalDate.now(), 1000, "comment", true, TicketType.BUDGETARY, test_venue);
        collection.add(test_ticket);

        String test_string = XmlCoder.code(test_ticket) + XmlCoder.code(test_ticket);

        Map<String, Runnable> commands = new HashMap<>();
        commands.put("exit", () -> running.set(false));
        commands.put("add", () -> {
            Ticket ticket = Add.add();
            collection.add(ticket);
            IdController.ChangeIds(collection);
            System.out.println("Билет успешно добавлен в коллекцию!");
        });
        commands.put("update id", () -> {
            int id = ErrorCatcher.notInt(scanner, "Введите id билета, который хотите обновить: ");
            if (id >= collection.size() || id < 0) {
                System.out.println("Билета с таким id не существует!");
            } else {
                collection.set(id, Add.add());
                IdController.ChangeIds(collection);
                System.out.println("Билет с индексом " + id + " успешно обновлён!");
            }
        });
        commands.put("remove_by_id", () -> {
            int id_remove = ErrorCatcher.notInt(scanner, "Введите id билета, который хотите удалить: ");
            if (id_remove >= collection.size() || id_remove < 0) {
                System.out.println("Билета с таким id не существует!");
            } else {
                collection.remove(id_remove);
                IdController.ChangeIds(collection);
                System.out.println("Билет с индексом " + id_remove + " успешно удалён из коллекции!");
            }
        });
        commands.put("clear", () -> {
            collection.clear();
            IdController.ChangeIds(collection);
            System.out.println("Коллекция успешно очищена!");
        });
        commands.put("save", () -> {
            try {
                CollectionToFile.collection_to_file(collection, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Коллекция успешно сохранена в файл!");
        });
        commands.put("show", () -> {
            String all_elements = "";
            for (Ticket t : collection) {
                all_elements = all_elements + ", " + t.getName();
            }
            System.out.println(all_elements.substring(2));
        });
        commands.put("info", () -> {
            LocalDate earliest_date = LocalDate.MAX;
            for (Ticket t : collection) {
                if (t.getCreationDate().isBefore(earliest_date)) {
                    earliest_date = t.getCreationDate();
                }
            }
            System.out.println("Тип коллекции: " + collection.getClass());
            System.out.println("Дата создания: " + earliest_date);
            System.out.println("Количество элементов в коллекции: " + collection.size());
        });

        commands.put("remove_last", () -> {
            if (!collection.isEmpty()) {
                collection.removeLast();
                System.out.println("Последний элемент коллекции удален!");
            } else {
                System.out.println("Коллекция пуста.");
            }
        });

        commands.put("add_if_min", () -> {
            Ticket new_ticket = Add.add();
            if (collection.isEmpty()) {
                collection.add(new_ticket);
                System.out.println("Элемент добавлен в коллекцию, так как коллекция пуста.");
            } else {
                Ticket minTicket = Collections.min(collection, Comparator.comparingInt(Ticket::getId));
                if (new_ticket.getId() < minTicket.getId()) {
                    collection.add(new_ticket);
                    System.out.println("Элемент добавлен.");
                } else {
                    System.out.println("Элемент не добавлен, так как его id больше или равен минимальному id в коллекции.");
                }
            }
        });

        commands.put("shuffle", () -> {
            Collections.shuffle(collection);
            System.out.println("Коллекция перемешана!");
        });

        commands.put("filter_less_than_venue", () -> {
            int id_less_ven = ErrorCatcher.notInt(scanner, "Введите значение venue: ");
            List<Ticket> filteredTickets = new LinkedList<>();
            for (Ticket tacket : collection) {
                if (tacket.getVenue().getCapacity() < id_less_ven) {
                    filteredTickets.add(tacket);
                }
            }
            if (filteredTickets.isEmpty()) {
                System.out.println("Нет элементов с venue меньшим, чем заданное.");
            } else {
                System.out.println("Элементы с venue меньше заданного:");
                for (Ticket tacket : filteredTickets) {
                    System.out.println("Ticket ID: " + tacket.getId() + ", Name: " + tacket.getName() + ", Venue Capacity: " + tacket.getVenue().getCapacity());
                }
            }
        });

        commands.put("filter_greater_than_venue", () -> {
            int id_greater_ven = ErrorCatcher.notInt(scanner, "Введите значение venue: ");
            List<Ticket> filteredTicketsGreater = new LinkedList<>();
            for (Ticket tacket : collection) {
                if (tacket.getVenue().getCapacity() > id_greater_ven) {
                    filteredTicketsGreater.add(tacket);
                }
            }
            if (filteredTicketsGreater.isEmpty()) {
                System.out.println("Нет элементов с venue большим, чем заданное.");
            } else {
                System.out.println("Элементы с venue больше заданного:");
                for (Ticket tacket : filteredTicketsGreater) {
                    System.out.println("Ticket ID: " + tacket.getId() + ", Name: " + tacket.getName() + ", Venue Capacity: " + tacket.getVenue().getCapacity());
                }
            }
        });

        commands.put("print_field_descending_price", () -> {
            List<Ticket> sortedCollection = new LinkedList<>(collection);
            Collections.sort(sortedCollection, Comparator.comparingDouble(Ticket::getPrice).reversed());
            System.out.println("Значения поля price в порядке убывания:");
            for (Ticket tacket : sortedCollection) {
                System.out.println("Ticket ID: " + tacket.getId() + ", Price: " + tacket.getPrice());
            }
        });

        // Интерфейс
        while (running.get()) {
            System.out.println("Введите команду > ");
            String command = scanner.nextLine().trim();
            if (commands.containsKey(command)) {
                commands.get(command).run();  // Выполняем команду
            } else {
                System.out.println("Неизвестная команда.");
            }
        }
    }
        public static void executeScript(String fileName, LinkedList<Ticket> collection, Map<String, Runnable> commands) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String command;
                while ((command = br.readLine()) != null) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        System.out.println("Выполняется команда из скрипта: " + command);
                        if (commands.containsKey(command)) {
                            commands.get(command).run();
                        } else {
                            System.out.println("Неизвестная команда: " + command);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
            }
        }
    }

