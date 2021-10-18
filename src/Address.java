/**
 * This represents an ip adress in all number formats
 * 
 * @author apoc
 */

public class Address {
    
    int[][] binary;
    int[] decimal;
    String[] hex;

    /**
     * Constructor
     * 
     * @param binary - binary format of ip address
     */
    public Address(int[][] binary){
        this.binary = binary;
        this.decimal = convertAddressToInt(binary);
        this.hex = convertAddresstoHex(decimal[0], decimal[1], decimal[2], decimal[3]);
    }

    /**
     * Constructor
     * 
     * @param block1 - represents block of ip address
     * @param block2 - represents block of ip address
     * @param block3 - represents block of ip address
     * @param block4 - represents block of ip address
     */
    public Address(int block1, int block2, int block3, int block4){
        binary = convertAddressToBinary(block1, block2, block3, block4);
        decimal = convertAddressToInt(binary);
        hex = convertAddresstoHex(block1, block2, block3, block4);
    }

    /**
     * Converts binary value to int value
     * 
     * @param binaryArr - binary format of value
     * @return - return int value representing binary value
     */
    private int convertBinaryToInt(int[] binaryArr){
        String temp = "";

        for (int i = 0; i < binaryArr.length; i++) {
            temp = temp + binaryArr[i];
        }
        
        int binary = Integer.parseInt(temp);

        int dec = 0;
        int index = 0;

        while(true){
            if (binary == 0){
                break;
            }
            else{
                int t = binary%10;
                dec += t*Math.pow(2, index);
                binary /= 10;
                index++;
            }
        }

        return dec;
    }

    /**
     * Converts binary address to int address
     * 
     * @param binaryaddr - binary format of value
     * @return - returns int address
     */
    private int[] convertAddressToInt(int[][] binaryaddr){
        int[] temp = new int[4];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = convertBinaryToInt(binaryaddr[i]);
        }

        return temp;
        
    }

    /**
     * Converts int address to binary address
     * 
     * @param b1 - block of int ip address block
     * @param b2 - block of int ip address block
     * @param b3 - block of int ip address block
     * @param b4 - block of int ip address block
     * 
     * @return - returns address in binary format
     */
    private int[][] convertAddressToBinary(int b1, int b2, int b3, int b4){

        int[][] temp = new int[4][8];

        temp[0] = convertIntToBinary(b1);
        temp[1] = convertIntToBinary(b2);
        temp[2] = convertIntToBinary(b3);
        temp[3] = convertIntToBinary(b4);

        return temp;
    }

    /**
     * Converts int value into binary format
     * 
     * @param x - int value
     * @return - returns int value in binary format
     */
    private int[] convertIntToBinary(int x){
        if (x > 255){
            return new int[8];
        }
        //255 are 8x1
        int[] binaryX = new int[8];
        int index = 0;
        while(x > 0){

            int temp = x % 2;

            binaryX[index++] = temp;

            x = x/2;
        }
        
        int[] result = new int[binaryX.length];

        for (int i = 0; i < binaryX.length; i++) {
            result[binaryX.length - 1 - i] = binaryX[i];
        }

        return result;        
    }

    /**
     * Converts address to hex format
     * 
     * @param b1 - block of int ip address block
     * @param b2 - block of int ip address block
     * @param b3 - block of int ip address block
     * @param b4 - block of int ip address block
     * 
     * @return - returns address in hex format
     */
    private String[] convertAddresstoHex(int b1, int b2, int b3, int b4){
        
        String[] hexAddress = new String[4];

        hexAddress[0] = convertIntToHexa(b1);
        hexAddress[1] = convertIntToHexa(b2);
        hexAddress[2] = convertIntToHexa(b3);
        hexAddress[3] = convertIntToHexa(b4);
        
        return hexAddress;

    }

    /**
     * Converts int value into hex format
     * 
     * @param x - int value
     * @return - returns int value in hex format
     */
    private String convertIntToHexa(int x){
        char arr[] = {'A', 'B', 'C', 'D', 'E', 'F'};

        String hex = "";

        int r = 0;
        int index = 0;

        while (x > 0){

            r = x % 16;

            if (r >= 10){
                hex = arr[r - 10] + hex;
            }
            else{
                hex = r + hex;
            }

            x /= 16;
        }

        return hex;

    }

    /**
     * Returns binary address to string
     * 
     * @return - returns address value in string format
     */
    public String binaryToString(){
        String temp = "";
        for (int block = 0; block < binary.length; block++) {
            for (int digit = 0; digit < binary[block].length; digit++) {
                temp = temp + Integer.toString(binary[block][digit]);
            }
            temp = temp + " ";
        }
        return temp;
    }

    /**
     * Returns hex address to string
     * 
     * @return - returns address value in string format
     */
    public String hexToString(){
        String temp = "";
        for (String block : hex) {
            temp = temp + block + " ";
        }

        return temp;
    }

    /**
     * Returns int address to string
     * 
     * @return - returns address value in string format
     */
    public String intToString(){
        String temp = "";
        for (int i = 0; i < decimal.length; i++) {
            temp = temp + Integer.toString(decimal[i]) + " ";
        }
        return temp;
    }  

    /**
     * Returns binary address
     * 
     * @return - returns address value
     */
    public int[][] getBinaryAddr(){
        return binary;
    }

    /**
     * Returns binary address
     * 
     * @return - returns address value in one array
     */
    public int[] getCompleteBinaryAddr(){
        int[] complete = new int[32];

        for (int i = 0; i < binary.length; i++) {
            for (int j = 0; j < binary[i].length; j++) {
                complete[i*j] = binary[i][j];
            }
        }

        return complete;

    }

}
