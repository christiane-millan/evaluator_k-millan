/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EK;

/**
 * 
 * @author Christian Millán 
 */
public class PairConfused {
    private String pairOne;
    private String pairTwo;
    
    /**
     * Crea un objeto con un par de palabras confusas
     * @param pairOne String de la palabra confusa uno
     * @param pairTwo Strind de la palabra confusa dos
     */
    public PairConfused(String pairOne, String pairTwo){
        this.pairOne = pairOne;
        this.pairTwo = pairTwo;
    }
    
    public String toString(){
        return this.getPairOne() + " " + this.getPairTwo();
    }

    /**
     * @return the pairOne
     */
    public String getPairOne() {
        return pairOne;
    }

    /**
     * @param pairOne the pairOne to set
     */
    public void setPairOne(String pairOne) {
        this.pairOne = pairOne;
    }

    /**
     * @return the pairTwo
     */
    public String getPairTwo() {
        return pairTwo;
    }

    /**
     * @param pairTwo the pairTwo to set
     */
    public void setPairTwo(String pairTwo) {
        this.pairTwo = pairTwo;
    }
}
