package CommandMethods;

import CollectionClasses.Ticket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CollectionToFile {
    public static void collection_to_file(LinkedList<Ticket> collection, String path) throws IOException {
        String file_xml_out = """
                <collection>
                    """;
        for (Ticket ticket : collection) {
            XmlCoder.code(ticket);
            file_xml_out = file_xml_out + XmlCoder.code(ticket);
        }
        file_xml_out = file_xml_out + """
                </collection>
                """;
        FileWriterReader.write(file_xml_out, path);
    }

    public static void file_to_collection(LinkedList<Ticket> collection, String path) throws IOException {
        String file_xml = FileWriterReader.read(path);
        List<String> xml_codes = new LinkedList<>();

        while (file_xml.contains("<ticket>")) {
            String one_code = file_xml.substring(file_xml.indexOf("<ticket>"), file_xml.indexOf("</ticket>"));
            xml_codes.add(one_code);
            file_xml = file_xml.substring(file_xml.indexOf("</ticket>")+9);
        }

        for (String code : xml_codes) {
            collection.add(XmlCoder.decode(code));
        }
    }
}
