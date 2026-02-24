import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException {
        LinkedList<Ticket> collection = new LinkedList<>();
        String path = System.getenv("TICKETS_FILE");
        Scanner scanner = new Scanner(System.in);
        Boolean running = true;


        Location test_location = new Location(100.0f, 99, 834, "Lenina");
        Address test_address = new Address("Lenina_A", test_location);
        Venue test_venue = new Venue("Lenina_V", 1000, VenueType.BAR, test_address);
        Coordinates test_coordinates = new Coordinates(100, 99);
        Ticket test_ticket = new Ticket("Lenina_T", test_coordinates, java.time.LocalDate.now(), 1000, "comment", true, TicketType.BUDGETARY, test_venue);
        collection.add(test_ticket);

        String xml = XmlCoder.code(test_ticket);
        System.out.println(xml);
        //System.out.println(XmlCoder.decode(xml));

        System.out.println();

        while (running) {
            System.out.println("Введите команду > ");
            String command = scanner.nextLine();
            switch (command) {
                case "add":

            }
        }

    }
}