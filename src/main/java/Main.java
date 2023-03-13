import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        //# 1: csv_json парсер
       // String[] employee = ("2,Ivan,Petrov,RU,23".split(","));
       // try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
       //     writer.writeNext(employee);
       // } catch (IOException e) {
       //     e.printStackTrace();
       // }
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String jsonFilename = "data.json";
        writeString(json, jsonFilename);

        //# 2: xml_json парсер
        //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //DocumentBuilder builder = factory.newDocumentBuilder();
        //Document document = builder.newDocument();
//
        //Element root = document.createElement("root");
        //document.appendChild(root);
        //Element company = document.createElement("company");
        //root.appendChild(company);
        //Element staff = document.createElement("staff");
        //company.appendChild(staff);
        //Element employee = document.createElement("employee");
        //employee.setAttribute("id", "1");
        //employee.setAttribute("firstName", "John");
        //employee.setAttribute("lastName", "Smith");
        //employee.setAttribute("country", "USA");
        //employee.setAttribute("age", "25");
        //staff.appendChild(employee);
        //Element employee1 = document.createElement("employee");
        //employee1.setAttribute("id", "2");
        //employee1.setAttribute("firstName", "Ivan");
        //employee1.setAttribute("lastName", "Petrov");
        //employee1.setAttribute("country", "RU");
        //employee1.setAttribute("age", "23");
        //staff.appendChild(employee1);
//
        //DOMSource domSource = new DOMSource(document);
        //StreamResult streamResult = new StreamResult(new File("data.xml"));
//
        //TransformerFactory transformerFactory = TransformerFactory.newInstance();
        //Transformer transformer = transformerFactory.newTransformer();
        //transformer.transform(domSource, streamResult);

        String xmlFilename = "data.xml";
        List<Employee> list2 = parseXML(xmlFilename);
        String json2 = listToJson(list2);
        String jsonFilename2 = "data2.json";
        writeString(json2, jsonFilename2);
    }
    private static List<Employee> parseXML(String xmlFilename) throws ParserConfigurationException,
            IOException, SAXException {
        List<String> elements = new ArrayList<>();
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlFilename));
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("employee")) {
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node_ = nodeList1.item(j);
                    if (Node.ELEMENT_NODE == node_.getNodeType()) {
                        elements.add(node_.getTextContent());
                    }
                }
                list.add(new Employee(
                        Long.parseLong(elements.get(0)),
                        elements.get(1),
                        elements.get(2),
                        elements.get(3),
                        Integer.parseInt(elements.get(4))));
                elements.clear();
            }
        }
        return list;
    }

    private static void writeString(String json, String jsonFilename) {
        try (FileWriter file = new FileWriter(jsonFilename)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

}
