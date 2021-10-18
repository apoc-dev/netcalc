
/**
 * The controller of NetCalc Program
 * 
 * @author apoc
 * 
 */

public class NetCalcController {
    
    NetCalcGui gui;

    NetCalcController(){
        this.gui = new NetCalcGui(this);
    }

    /**
     * Does forward data to model
     * 
     * @param ipa - ip address in array format
     * @param smask - subnet mask in array format
     * 
     */
    public void commitInput(int[] ipa, int[] smask) throws Exception{

        if (!checkIfInputValid(ipa) || !checkIfInputValid(smask)){
            throw new Exception();
        }

        Address a = new Address(ipa[0], ipa[1], ipa[2], ipa[3]);
        Address b = new Address(smask[0], smask[1], smask[2], smask[3]);
        Network c = new Network(a, b);

        gui.updateContent(c);

    }
    
    /**
     * Checks if input from gui is generally valid
     * Does NOT check data validition
     * 
     * @param values - numbers entered in gui
     * @return - returns true if values are generally valid for processing
     */
    private boolean checkIfInputValid(int[] values){

        if(values.length > 5){
            return false;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] > 255){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        new NetCalcController();
    }

}
