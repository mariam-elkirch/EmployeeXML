/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private Button nextBtn;
    @FXML
    private Button prevBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button insertBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField firstNameTxt;
    @FXML
    private TextField lastNameTxt;
    @FXML
    private TextField emailTxt;
    @FXML
    private TextField addressTxt;
    public static int size = 0;
    public int numClicks = 0;
    public int prevNumClick;
    public boolean flag = true;
    public boolean enterNext = false;
    public boolean enterPrev = false;

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    public void setEditable(boolean edit) {
        idTxt.setEditable(edit);
        firstNameTxt.setEditable(edit);
        lastNameTxt.setEditable(edit);
        emailTxt.setEditable(edit);
        addressTxt.setEditable(edit);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean flagupdate = false;
                if (flag == true) {
                    setEditable(true);
                    idTxt.setText("");
                    firstNameTxt.setText("");
                    lastNameTxt.setText("");
                    emailTxt.setText("");
                    addressTxt.setText("");

                    updateBtn.setText("Save");
                    flag = false;
                } else {

                    try {

                        File inputFile = new File("employees.xml");
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(inputFile);
                        doc.getDocumentElement().normalize();
                        Node emp = doc.getElementsByTagName("employee").item(0);
                        if (!idTxt.getText().isEmpty() && !firstNameTxt.getText().isEmpty() && !lastNameTxt.getText().isEmpty() && !emailTxt.getText().isEmpty() && !addressTxt.getText().isEmpty()) {
                            NamedNodeMap attr = emp.getAttributes();
                            Node nodeAttr = attr.getNamedItem("id");
                            nodeAttr.setTextContent(idTxt.getText());
                            Node emps = doc.getFirstChild();
                            // loop the supercar child node
                            NodeList list = emp.getChildNodes();

                            for (int temp = 0; temp < list.getLength(); temp++) {
                                Node node = list.item(temp);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) node;
                                    flagupdate = true;
                                    if ("firstname".equals(eElement.getNodeName())) {

                                        eElement.setTextContent(firstNameTxt.getText());

                                    }
                                    if ("lastname".equals(eElement.getNodeName())) {

                                        eElement.setTextContent(lastNameTxt.getText());

                                    }
                                    if ("email".equals(eElement.getNodeName())) {

                                        eElement.setTextContent(emailTxt.getText());

                                    }
                                    if ("address".equals(eElement.getNodeName())) {

                                        eElement.setTextContent(addressTxt.getText());

                                    }

                                }
                            }
                            NodeList childNodes = emps.getChildNodes();

                            for (int count = 0; count < childNodes.getLength(); count++) {
                                Node node = childNodes.item(count);

                                emps.removeChild(node);

                            }

                            // write the content on console
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer transformer = tf.newTransformer();
                            DOMSource src = new DOMSource(doc);
                            StreamResult res = new StreamResult("employees.xml");
                            transformer.transform(src, res);
                            if (flagupdate == true) {
                                Alert a = new Alert(AlertType.INFORMATION);
                                a.setContentText("First Employee updated successfully");
                                a.show();
                            } else {
                                Alert a = new Alert(AlertType.INFORMATION);
                                a.setContentText("We cannot update employee");
                                a.show();
                            }
                            updateBtn.setText("Update");
                            setEditable(false);
                            flag = true;
                        } else {
                            Alert a = new Alert(AlertType.INFORMATION);
                            a.setContentText("Enter all fields");
                            a.show();

                        }
                    } catch (TransformerConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TransformerException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        });
        deleteBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    // Document doc = Docc.getDoc();
                    // NodeList nList = initializeXml();
                    File inputFile = new File("employees.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(inputFile);
                    doc.getDocumentElement().normalize();
                    Node employee = doc.getElementsByTagName("employees").item(0);
                    // Get the list of child nodes of employee
                    NodeList list = employee.getChildNodes();

                    Node node = list.item(1);
                    //Remove "name" node
                    if ("employee".equals(node.getNodeName())) {
                        employee.removeChild(node);
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setContentText("First Employee deleted successfully");
                        a.show();
                    } else {
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setContentText("Sorry we cannot delete employee");
                        a.show();

                    }

                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    DOMSource src = new DOMSource(doc);
                    StreamResult res = new StreamResult("employees.xml");
                    transformer.transform(src, res);

                    //////////////
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
        searchBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean flagExist = false;
                if (flag == true) {
                    idTxt.setEditable(true);
                    idTxt.setText("");
                    firstNameTxt.setText("");
                    lastNameTxt.setText("");
                    emailTxt.setText("");
                    addressTxt.setText("");

                    searchBtn.setText("Save");
                    flag = false;
                } else {

                    try {
                        Document doc = Docc.getDoc();
                        NodeList nList = initializeXml();
                        String idd = idTxt.getText();
                        for (int x = 0, size = nList.getLength(); x < size; x++) {
                            if (idd.equals(nList.item(x).getAttributes().getNamedItem("id").getNodeValue())) {
                                System.out.println("yessssssssssss" + nList.item(x));
                                Node node = nList.item(x);
                                flagExist = true;
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    //Print each employee's detail
                                    Element eElement = (Element) node;
                                    firstNameTxt.setText(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                                    lastNameTxt.setText(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                                    emailTxt.setText(eElement.getElementsByTagName("email").item(0).getTextContent());
                                    addressTxt.setText(eElement.getElementsByTagName("address").item(0).getTextContent());
                                    System.out.println("Employee idddd : " + eElement.getAttribute("id"));
                                    System.out.println("First Nameeee : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                                    System.out.println("Last Nameeeee : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                                    // System.out.println("Location : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                                }

                            }
                            System.out.println(nList.item(x).getAttributes().getNamedItem("id").getNodeValue());
                        }
                        searchBtn.setText("Search by ISBN");
                        setEditable(false);
                        flag = true;
                        if (flagExist == false) {
                            Alert a = new Alert(AlertType.INFORMATION);
                            a.setContentText("Sorry we cannot find this employee");
                            a.show();
                        }

                        //////////////
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        });
        insertBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (flag == true) {
                    setEditable(true);
                    idTxt.setText("");
                    firstNameTxt.setText("");
                    lastNameTxt.setText("");
                    emailTxt.setText("");
                    addressTxt.setText("");

                    insertBtn.setText("Save");
                    flag = false;
                } else {

                    try {

                        File inputFile = new File("employees.xml");
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(inputFile);
                        doc.getDocumentElement().normalize();
                        Node employees = doc.getElementsByTagName("employees").item(0);
                        // Add a new node
                        Element emp = doc.createElement("employee");
                        //int nextId = 7;
                        if (!idTxt.getText().isEmpty() && !firstNameTxt.getText().isEmpty() && !lastNameTxt.getText().isEmpty() && !emailTxt.getText().isEmpty() && !addressTxt.getText().isEmpty()) {
                            emp.setAttribute("id", idTxt.getText()); // setting employee ID
                            Element first = doc.createElement("firstname");
                            Element second = doc.createElement("lastname");
                            Element email = doc.createElement("email");
                            Element address = doc.createElement("address");
                            first.appendChild(doc.createTextNode(firstNameTxt.getText()));
                            second.appendChild(doc.createTextNode(lastNameTxt.getText()));
                            email.appendChild(doc.createTextNode(emailTxt.getText()));
                            address.appendChild(doc.createTextNode(addressTxt.getText()));
                            emp.appendChild(first);
                            emp.appendChild(second);
                            emp.appendChild(email);
                            emp.appendChild(address);
                            employees.appendChild(emp);
                            // write the content to the xml file
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer transformer = tf.newTransformer();
                            DOMSource src = new DOMSource(doc);
                            StreamResult res = new StreamResult("employees.xml");
                            transformer.transform(src, res);

                            insertBtn.setText("insert");
                            setEditable(false);
                            flag = true;
                        } else {
                            Alert a = new Alert(AlertType.INFORMATION);
                            a.setContentText("Enter All fields");
                            a.show();
                        }
                    } catch (TransformerConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TransformerException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        });
        nextBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // try {
                    setEditable(false);
                    NodeList nList = initializeXml();
                    // prevNumClick = size;
                    //  try {
                    //numClicks++;
                    System.out.println("nextttttttttttttttt click" + numClicks);
                    enterNext = true;
                   
                    if (numClicks < nList.getLength()) {
                        nList = initializeXml();
                        Node node = nList.item(numClicks);
                        System.out.println("");    //Just a separator
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            //Print each employee's detail
                            Element eElement = (Element) node;
                            idTxt.setText(eElement.getAttribute("id"));
                            firstNameTxt.setText(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                            lastNameTxt.setText(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                            emailTxt.setText(eElement.getElementsByTagName("email").item(0).getTextContent());
                            addressTxt.setText(eElement.getElementsByTagName("address").item(0).getTextContent());
                            System.out.println("Employee id : " + eElement.getAttribute("id"));
                            System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                            System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                            // System.out.println("Location : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                            if (numClicks != nList.getLength() - 1) {
                                numClicks++;
                            }
                        }
                    } else {
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setContentText("No More Employees");
                        a.show();

                    }

                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        prevBtn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setEditable(false);
                try {
                    NodeList nList = initializeXml();
                    System.out.println("prevvvvvvvvvvvv click" + numClicks);
                    enterPrev = true;
                      
                    // numClicks--;
                    if (numClicks >= 0) {
                        Node node = nList.item(numClicks);
                        System.out.println(numClicks);

                        System.out.println("");    //Just a separator
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            //Print each employee's detail
                            Element eElement = (Element) node;
                            idTxt.setText(eElement.getAttribute("id"));
                            firstNameTxt.setText(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                            lastNameTxt.setText(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                            emailTxt.setText(eElement.getElementsByTagName("email").item(0).getTextContent());
                            addressTxt.setText(eElement.getElementsByTagName("address").item(0).getTextContent());
                            System.out.println("Employee id : " + eElement.getAttribute("id"));
                            System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                            System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                            if (numClicks != 0) {
                                numClicks--;
                            }
                            // System.out.println("Location : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                        }


                        /* }catch(ArrayIndexOutOfBoundsException exception) {
                        
                             }*/
                    } else {
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setContentText("No More Employees");
                        a.show();
                    }

                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public NodeList initializeXml() throws ParserConfigurationException, SAXException, IOException {
        File inputFile = new File("employees.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        Docc.setDoc(doc);
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("employee");

        System.out.println("----------------------------");
        System.out.println(nList);
        size = nList.getLength() - 1;
        /* for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            System.out.println("");    //Just a separator
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //Print each employee's detail
                Element eElement = (Element) node;
                System.out.println("Employee id : " + eElement.getAttribute("id"));
                System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
               // System.out.println("Location : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
            }

        }*/
        return nList;
    }

}

class Docc {

    static Document doc;

    public static Document getDoc() {
        return doc;
    }

    public static void setDoc(Document doc) {
        Docc.doc = doc;
    }

}
