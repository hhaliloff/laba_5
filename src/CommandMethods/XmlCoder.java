/**
 * Класс для перевода Ticket в текст и обратно.
 * Используется для сохранения в файл.
 */
package CommandMethods;

import CollectionClasses.*;

public class XmlCoder {
    public static String code(Ticket ticket) {
        int ticket_id = ticket.getId();
        String ticket_name = ticket.getName();

        Coordinates coordinates = ticket.getCoordinates();
        long x = coordinates.getX();
        int y = coordinates.getY();

        java.time.LocalDate creationDate = ticket.getCreationDate();
        float price = ticket.getPrice();
        String comment = ticket.getComment();
        boolean refundable = ticket.getRefundable();
        TicketType type = ticket.getType();

        Venue venue = ticket.getVenue();
        int venue_id = venue.getId();
        String venue_name = venue.getName();
        int capacity = venue.getCapacity();
        VenueType venue_type = venue.getType();

        Address address = venue.getAddress();
        String street = address.getStreet();

        Location town = address.getTown();
        Float location_x = town.getX();
        int location_y = town.getY();
        double location_z = town.getZ();
        String location_name = town.getName();


        String xml = """
                <ticket>
                    <id>%d</id>
                    <name>%s</name>
                    <coordinates>
                        <x>%d</x>
                        <y>%d</y>
                    </coordinates>
                    <creationDate>%s</creationDate>
                    <price>%f</price>
                    <comment>%s</comment>
                    <refundable>%b</refundable>
                    <type>%s</type>
                    <venue>
                        <id>%d</id>
                        <name>%s</name>
                        <capacity>%d</capacity>
                        <type>%s</type>
                        <address>
                            <street>%s</street>
                            <town>
                                <x>%f</x>
                                <y>%d</y>
                                <z>%f</z>
                                <name>%s</name>
                            </town>
                        </address>
                    </venue>
                </ticket>
                
                """.formatted(ticket_id, ticket_name, x, y, creationDate, price, comment, refundable, type, venue_id, venue_name, capacity, venue_type, street, location_x, location_y, location_z, location_name);

        return xml;
    }

    public static Ticket decode(String xml) {
        int ticket_id = Integer.parseInt(xml.substring(xml.indexOf("<id>")+4, xml.indexOf("</id>")));
        String ticket_name = xml.substring(xml.indexOf("<name>")+6, xml.indexOf("</name>"));

        xml = xml.substring(xml.indexOf("</name>")+7);

        long x = Long.parseLong(xml.substring(xml.indexOf("<x>")+3, xml.indexOf("</x>")));
        int y = Integer.parseInt(xml.substring(xml.indexOf("<y>")+3, xml.indexOf("</y>")));
        java.time.LocalDate creationDate = java.time.LocalDate.parse(xml.substring(xml.indexOf("<creationDate>")+14, xml.indexOf("</creationDate>")));
        float price = Float.parseFloat(xml.substring(xml.indexOf("<price>")+7, xml.indexOf("</price>")));
        String comment = xml.substring(xml.indexOf("<comment>")+9, xml.indexOf("</comment>"));
        boolean refundable = Boolean.parseBoolean(xml.substring(xml.indexOf("<refundable>")+12, xml.indexOf("</refundable>")));
        TicketType type = TicketType.valueOf(xml.substring(xml.indexOf("<type>")+6, xml.indexOf("</type>")));
        String venue_name = xml.substring(xml.indexOf("<name>")+6, xml.indexOf("</name>"));
        int capacity = Integer.parseInt(xml.substring(xml.indexOf("<capacity>")+10, xml.indexOf("</capacity>")));

        xml = xml.substring(xml.indexOf("<capacity>")+10);

        VenueType venue_type = VenueType.valueOf(xml.substring(xml.indexOf("<type>")+6, xml.indexOf("</type>")));
        String street = xml.substring(xml.indexOf("<street>")+8, xml.indexOf("</street>"));

        xml = xml.substring(xml.indexOf("<street>")+8);

        Float location_x = Float.parseFloat(xml.substring(xml.indexOf("<x>")+3, xml.indexOf("</x>")));
        int location_y = Integer.parseInt(xml.substring(xml.indexOf("<y>")+3, xml.indexOf("</y>")));
        double location_z = Double.parseDouble(xml.substring(xml.indexOf("<z>")+3, xml.indexOf("</z>")));
        String location_name = xml.substring(xml.indexOf("<name>")+6, xml.indexOf("</name>"));

        Location location = new Location(location_x, location_y, location_z, location_name);
        Address address = new Address(street, location);
        Venue venue = new Venue(venue_name, capacity, venue_type, address);
        Coordinates coordinates = new Coordinates(x, y);
        Ticket ticket = new Ticket(ticket_name, coordinates, creationDate, price, comment, refundable, type, venue);

        return ticket;
    }
}
