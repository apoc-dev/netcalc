import javax.swing.*;
import javax.swing.event.*;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This represents an Network Calculator Gui
 * 
 * @author apoc
 */
public class NetCalcGui {
    
    JFrame mFrame;
    JPanel mPanel;

    GridBagConstraints c;

    JLabel ipaddrdesc;
    JLabel subnetdesc;
    JLabel ipaddrbinarydesc;
    JLabel ipaddrhexadesc;
    JLabel shortsuffixdesc;
    JLabel netaddrdesc;
    JLabel netfirstdesc;
    JLabel netlastdesc;
    JLabel netbroaddesc;
    JLabel netaddrsumdesc;
    JLabel netReserveddesc;

    JFormattedTextField ipaddr1;
    JFormattedTextField ipaddr2;
    JFormattedTextField ipaddr3;
    JFormattedTextField ipaddr4;

    ArrayList<JFormattedTextField> iptexts;

    JFormattedTextField subnet1;
    JFormattedTextField subnet2;
    JFormattedTextField subnet3;
    JFormattedTextField subnet4;

    ArrayList<JFormattedTextField> subnettexts;


    JLabel ipaddrbinary;
    JLabel ipaddrhexa;
    JLabel shortsuffix;
    JLabel netaddr;
    JLabel netfirst;
    JLabel netlast;
    JLabel netbroad;
    JLabel netaddrsum;
    JLabel netReserved;

    NetCalcController controller;

    /**
     * Constructor
     * 
     * @param controller - controller parent object
     * 
     */
    public NetCalcGui(NetCalcController controller){

        this.controller = controller;

        mFrame = new JFrame("NetCalc");

        mFrame.setResizable(false);
        mFrame.setLayout(new BorderLayout());
        mFrame.setSize(new Dimension(500, 350));
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mPanel = new JPanel();
        mPanel.setBackground(Color.GRAY);
        mPanel.setLayout(new GridBagLayout());
        mPanel.setSize(new Dimension(500, 350));
        
        c = new GridBagConstraints();
        
        initGUI();
        
        mFrame.add(mPanel);
        mFrame.setVisible(true);

    }
  
    /**
     * Inits the whole gui objects and adds basic listeners
     * 
     * Does not handle any logic - see NetCalcController for that 
     */  
    private void initGUI(){

        ipaddrdesc = new JLabel("IP Address");
        subnetdesc = new JLabel("Subnet");
        ipaddrbinarydesc = new JLabel("IP Address Binary");
        ipaddrhexadesc = new JLabel("IP Address Hexa");
        shortsuffixdesc = new JLabel("Short Suffix");
        netaddrdesc = new JLabel("Network Address");
        netfirstdesc = new JLabel("Networks First Addr");
        netlastdesc = new JLabel("Networks Last Addr");
        netbroaddesc = new JLabel("Networks Broadcast Addr");
        netaddrsumdesc = new JLabel("Network addr sum");
        netReserveddesc = new JLabel("Reserved Network");


        ipaddrbinary = new JLabel("Enter correct combination");
        ipaddrhexa = new JLabel("Enter correct combination");
        shortsuffix = new JLabel("Enter correct combination");
        netaddr = new JLabel("Enter correct combination");
        netfirst = new JLabel("Enter correct combination");
        netlast = new JLabel("Enter correct combination");
        netbroad = new JLabel("Enter correct combination");
        netaddrsum = new JLabel("Enter correct combination");
        netReserved = new JLabel("Enter correct combination");

        iptexts = new ArrayList();
        ipaddr1 = new JFormattedTextField();
        ipaddr2 = new JFormattedTextField();
        ipaddr3 = new JFormattedTextField();
        ipaddr4 = new JFormattedTextField();

        iptexts.add(ipaddr1);
        iptexts.add(ipaddr2);
        iptexts.add(ipaddr3);
        iptexts.add(ipaddr4);

        subnettexts = new ArrayList();
        subnet1 = new JFormattedTextField();
        subnet2 = new JFormattedTextField();
        subnet3 = new JFormattedTextField();
        subnet4 = new JFormattedTextField();

        subnettexts.add(subnet1);
        subnettexts.add(subnet2);
        subnettexts.add(subnet3);
        subnettexts.add(subnet4);

        ArrayList<JComponent> labels0 = new ArrayList<>();
        labels0.add(ipaddrdesc);
        labels0.add(subnetdesc);
        labels0.add(ipaddrbinarydesc);
        labels0.add(ipaddrhexadesc);
        labels0.add(shortsuffixdesc);
        labels0.add(netaddrdesc);
        labels0.add(netfirstdesc);
        labels0.add(netlastdesc);
        labels0.add(netbroaddesc);
        labels0.add(netaddrsumdesc);
        labels0.add(netReserveddesc);

        ArrayList<JComponent> labels1 = new ArrayList<>();
        labels1.add(ipaddrbinary);
        labels1.add(ipaddrhexa);
        labels1.add(shortsuffix);
        labels1.add(netaddr);
        labels1.add(netfirst);
        labels1.add(netlast);
        labels1.add(netbroad);
        labels1.add(netaddrsum);
        labels1.add(netReserved);

        //loop to create ip text fiels
        initTxtFields(iptexts, 0);
        initTxtFields(subnettexts, 1);

        for (int i = 0; i < labels0.size(); i++) {
            c.gridx = 0;
            c.gridy = i;
            c.weightx = 0.5;
            c.insets = new Insets(5,5,5,5);
            c.fill = GridBagConstraints.HORIZONTAL;
            mPanel.add(labels0.get(i),c);
        }

        for (int i = 0; i < labels1.size(); i++) {
            c.gridx = 1;
            c.gridy = i+2;
            c.weightx = 1.0;
            c.gridwidth = 4;
            c.insets = new Insets(5,5,5,5);
            mPanel.add(labels1.get(i),c);
        }

    }

    /**
     * inits a list of text field
     * 
     * @param list - the list of JFormattedTextFields
     * 
     */
    private void initTxtFields(ArrayList<JFormattedTextField> list, int y){
        for (int i = 0; i < list.size(); i++) {
            c.gridx = i+1;
            c.gridy = y;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5,5,5,5);
            JFormattedTextField txtf = list.get(i);
            txtf.setColumns(3);
            
            //filters input to textfield (maybe move this to controller?)
            txtf.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {                   
                   if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                      txtf.setEditable(true);
                   } else {
                      txtf.setEditable(false);
                   }
                }
             });

            //calls controller
            txtf.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    
                }
                public void removeUpdate(DocumentEvent e) {
                    try {
                        parseInput(iptexts, subnettexts);
                    } catch (Exception e2) {
                        resetText();
                    }
                }

                public void insertUpdate(DocumentEvent e) {
                    try {
                        parseInput(iptexts, subnettexts);
                    } catch (Exception e2) {
                        resetText();
                    }
                    
                }
            });

            mPanel.add(txtf,c);
        }
    }

    /**
     * Parses the input into a basic format
     * so controller can proccess the data
     * 
     * @param iptexts - the list of ip address input fields
     * @param subnettexts - the list of subnet address input fields
     * @throws Exception - throws exception if parsing fails
     * 
     */
    private void parseInput(ArrayList<JFormattedTextField> iptexts, ArrayList<JFormattedTextField> subnettexts) throws Exception{
        int[] ipa = new int[4];
        int[] smask = new int[4];
        for (int j = 0; j < iptexts.size(); j++) {
            ipa[j] = Integer.parseInt(iptexts.get(j).getText());
        }
        for (int j = 0; j < subnettexts.size(); j++) {
            smask[j] = Integer.parseInt(subnettexts.get(j).getText());
        }
        controller.commitInput(ipa, smask);
    }
    
    /**
     * updates content based on input
     * 
     * @param net - the network to show
     * 
     */
    public void updateContent(Network net){
        ipaddrbinary.setText((net.getIpaddr().binaryToString()));
        ipaddrhexa.setText((net.getIpaddr().hexToString()));
        shortsuffix.setText((Integer.toString(net.getShortsuffix())));
        netaddr.setText((net.getNetaddr().intToString()));
        netfirst.setText((net.getNetfirst().intToString()));
        netlast.setText((net.getNetlast().intToString()));
        netbroad.setText((net.getNetbroad().intToString()));
        netaddrsum.setText(( Integer.toString(net.getNetAddrUsableSum()) ));
        netReserved.setText(net.checkReservedNetworks());
    }

    /**
     * Resets view to default (error)
     * 
     */
    private void resetText(){
        ipaddrbinary.setText("Enter correct combination");
        ipaddrhexa.setText("Enter correct combination");
        shortsuffix.setText("Enter correct combination");
        netaddr.setText("Enter correct combination");
        netfirst.setText("Enter correct combination");
        netlast.setText("Enter correct combination");
        netbroad.setText("Enter correct combination");
        netaddrsum.setText("Enter correct combination");
        netReserved.setText("Enter correct combination");

    }
}