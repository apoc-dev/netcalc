import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.naming.spi.ResolveResult;

import org.ini4j.*;

/**
 * This represents an ip network and uses Adress class
 * 
 * @author apoc
 */
public class Network {

    Address ipaddr;
    Address subnetmask;
    int shortsuffix;
    Address netaddr;
    Address netfirst;
    Address netlast;
    Address netbroad;
    int netaddrsum;
    int netAddrUsableSum;

    String reservedDesc;

    ArrayList<Network> reserveds;

    /**
     * Constructor
     * 
     * @param ipaddr - represents ip address in Address format
     * @param subnetmask - represents subnetmask in Address format
     */
    public Network(Address ipaddr, Address subnetmask){
        this.ipaddr = ipaddr;
        if(!checkIfValidSubnetMask(subnetmask)){
            throw new IllegalArgumentException();
        };
        this.subnetmask = subnetmask;
        this.reservedDesc = "";
        calcNetAddr();
        calcShortSuffix();
        calcBroadcastAddr();
        calcHostNum();
        calcFirstAddr();
        calcLastAddr();
        
    }

    /**
     * Constructor
     * 
     * @param ipaddr - represents ip address in Address format
     * @param subnetmask - represents subnetmask in Address format
     * @param reservedDesc - represents info about the network
     */
    public Network(Address ipaddr, Address subnetmask, String reservedDesc){
        this.ipaddr = ipaddr;
        if(!checkIfValidSubnetMask(subnetmask)){
            throw new IllegalArgumentException();
        };
        this.subnetmask = subnetmask;
        this.reservedDesc = reservedDesc;
        calcNetAddr();
        calcShortSuffix();
        calcBroadcastAddr();
        calcHostNum();
        calcFirstAddr();
        calcLastAddr();
        
    }

    /**
     * Checks if subnetmask is a valid subnetmask
     * 
     * @param smask - subnetmask to check
     * @return - returns true if subnetmask is valid
     */
    private boolean checkIfValidSubnetMask(Address smask){

        int[][] binary = smask.getBinaryAddr();

        boolean flag = false;

        for (int i = 0; i < binary.length; i++) {
            for (int j = 0; j < binary[i].length; j++) {
                if(binary[i][j] == 0){
                    flag = true;
                }
                if(flag){
                    if(binary[i][j] == 1){
                        return false;
                    }
                }
            }
        }

        return true;

    }

    /**
     * Inits the reserved.ini file and creates Network objects 
     * 
     */
    private void initIni() throws IOException{

        Wini ini = new Wini(new File("src/reserved.ini"));
        
        Set<String> sectionNames = ini.keySet();
        
        reserveds = new ArrayList<>();

        for (String s : sectionNames) {
            Ini.Section section = ini.get(s);

            String[] ipa = section.get("ipa").split("[.]", 0);
            String[] smask = section.get("smask").split("[.]", 0);
            String reservedDesc = section.get("description");
            Address ipaA = new Address(Integer.parseInt(ipa[0]), Integer.parseInt(ipa[1]), Integer.parseInt(ipa[2]), Integer.parseInt(ipa[3]));
            Address smaskA = new Address(Integer.parseInt(smask[0]), Integer.parseInt(smask[1]), Integer.parseInt(smask[2]), Integer.parseInt(smask[3]));

            Network net = new Network(ipaA, smaskA, reservedDesc);

            reserveds.add(net);
        }
    }

    /**
     * Checks if current Network maps anz reserved network
     * 
     * @return String representing the "map"
     */
    public String checkReservedNetworks(){

        try {
            initIni();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Network network : reserveds) {
            if(checkNetworkBits(this, network)){
                return network.reservedDesc;
            }
            
        }

        return "false";
    }

    /**
     * Checks if network address a maps network address b
     * 
     * @param a - Network a
     * @param b - Network b
     * @return - String representing the "map"
     */
    private boolean checkNetworkBits(Network a, Network b){

        int[] aComplete = a.netaddr.getCompleteBinaryAddr();
        int[] bComplete = b.netaddr.getCompleteBinaryAddr();

        for (int i = 0; i < a.shortsuffix; i++) {
            if(aComplete[i] != bComplete[i]){
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates the network address of current Network
     * 
     */
    private void calcNetAddr(){

        int[][] ipa = this.ipaddr.getBinaryAddr();
        int[][] smask = this.subnetmask.getBinaryAddr();

        this.netaddr = new Address(anding(ipa, smask));

    }

    /**
     * Calculates the short suffix of current Network
     * 
     */
    private void calcShortSuffix(){

        int[][] smask = this.subnetmask.getBinaryAddr();

        int suffix = 0;

        for (int i = 0; i < smask.length; i++) {
            for (int j = 0; j < smask[i].length; j++) {
                if(smask[i][j] == 1)
                {
                    suffix = suffix + 1;
                }
            }
        }
        this.shortsuffix = suffix;
    }

    /**
     * Calculates the network broadcast address of current Network
     * 
     */
    private void calcBroadcastAddr(){
        int[][] ipa = this.ipaddr.getBinaryAddr();
        int[][] smaskInv = inversing(this.subnetmask.getBinaryAddr());

        this.netbroad = new Address(oring(ipa, smaskInv));

    }

    /**
     * Calculates the network host number of current Network
     * 
     */
    private void calcHostNum(){

        int hostbits = 32 - this.shortsuffix;

        int num = (int) Math.pow(2, hostbits);

        this.netaddrsum = num;
        this.netAddrUsableSum = num - 2;

    }

    /**
     * Calculates the network first host of current Network
     * 
     */
    private void calcFirstAddr(){
        this.netfirst = new Address(calcHigherAddr(this.netaddr.getBinaryAddr()));
    }

    /**
     * Calculates the network last host of current Network
     * 
     */
    private void calcLastAddr(){

        int[][] last = this.netbroad.getBinaryAddr();

        last = calcLowerAdress(last);

        this.netlast = new Address(last);
    }

    /**
     * Logical ANDING
     * 
     * @param a - binary[] a
     * @param b - binary[] b
     * 
     * @return result of anding
     * 
     */
    private int[][] anding(int[][] a, int[][] b){
        int[][] result = new int[4][8];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if(a[i][j] == 1 && b[i][j] == 1){
                    result[i][j] = 1;
                }
                else{
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    /**
     * Logical ORING
     * 
     * @param a - binary[] a
     * @param b - binary[] b
     * 
     * @return result of oring
     * 
     */
    private int[][] oring(int[][] a, int[][] b){
        int[][] result = new int[4][8];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if(a[i][j] == 0 && b[i][j] == 0){
                    result[i][j] = 0;
                }
                else{
                    result[i][j] = 1;
                }
            }
        }
        
        return result;
    }

    /**
     * Logical INVERSING
     * 
     * @param a - binary[] a
     * 
     * @return result of inversing
     * 
     */
    private int[][] inversing(int[][] a){
        int[][] result = new int[4][8];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if(a[i][j] == 0){
                    result[i][j] = 1;
                }
                else{
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    /**
     * Calculates the next higher address
     * 
     * @param b - binary[] b
     * 
     * @return next higher ip address in binary format
     * 
     */
    private int[][] calcHigherAddr(int[][] a){
        int[][] result = a;

        for (int i = a.length-1; i >= 0; i--) {
            for (int j = a[i].length-1; j >= 0; j--) {
                if(a[i][j] == 1){
                    result[i][j] = 0;
                }else{
                    result[i][j] = 1;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Calculates the next lower address
     * 
     * @param b - binary[] b
     * 
     * @return next lower ip address in binary format
     * 
     */
    private int[][] calcLowerAdress(int[][] a){

        int[][] result = a;

        for (int i = a.length-1; i >= 0; i--) {
            for (int j = a[i].length-1; j >= 0; j--) {
                if(a[i][j] == 0){
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                    return result;
                }
            }
        }


        return result;
    }

    /**
     * Returns String version of network
     * 
     * @return string representing all values of network (debug)
     * 
     */
    public String toString(){
        return "IP: " + ipaddr.intToString() + "| Smask: " + subnetmask.intToString() + "| NetAddr: " + netaddr.intToString() + "| Suffix: " + shortsuffix + "| Broadcast: " + this.netbroad.intToString() + "| Sum: " + this.netAddrUsableSum + "| First: " + this.netfirst.intToString() + "| Last: " + this.netlast.intToString();
    }
    
    /**
     * Returns ipaddr
     * 
     * @return ip address
     * 
     */
    public Address getIpaddr() {
        return ipaddr;
    }
    
    /**
     * Returns subnetmask
     * 
     * @return networks subnetmask
     * 
     */
    public Address getSubnetmask() {
        return subnetmask;
    }
    
    /**
     * Returns shortsuffix
     * 
     * @return networks shortsuffix
     * 
     */
    public int getShortsuffix() {
        return shortsuffix;
    }
    
    /**
     * Returns network address
     * 
     * @return networks network address
     * 
     */
    public Address getNetaddr() {
        return netaddr;
    }
    
    /**
     * Returns networks first address
     * 
     * @return networks networks first address
     * 
     */
    public Address getNetfirst() {
        return netfirst;
    }
    
    /**
     * Returns networks last address
     * 
     * @return networks networks last address
     * 
     */
    public Address getNetlast() {
        return netlast;
    }
    
    /**
     * Returns networks broadcast address
     * 
     * @return networks networks broadcast address
     * 
     */
    public Address getNetbroad() {
        return netbroad;
    }
    
    /**
     * Returns networks address host sum
     * 
     * @return networks networks address host sum
     * 
     */
    public int getNetaddrsum() {
        return netaddrsum;
    }
    
    /**
     * Returns networks usable host sum
     * 
     * @return networks networks usable host sum
     * 
     */
    public int getNetAddrUsableSum() {
        return netAddrUsableSum;
    }



}
