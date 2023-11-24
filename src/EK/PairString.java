/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EK;

/**
 * 
 * @author Christian Millán <ceduardo.millan@gmail.com>
 */
public class PairString {
    private String stringOne;
    private String stringTwo;
    private double measure;
    private double recall;
    private double precision;
    private double fmeasure;
    private int rank;
    private boolean isThereInGolden;
    
    public PairString (String stringOne, String stringTwo, double measure){
        this.stringOne = stringOne;
        this.stringTwo = stringTwo;
        this.measure = measure;
        this.isThereInGolden = false;
    }
    
    /**
     * Comprueba si un par confuso es igual al par de cadenas
     * @param pc Objeto de tipo PairConfused 
     * @return valor falso o verdadero
     */
    public boolean areSame(PairConfused pc){
        boolean r =(this.stringOne.equals(pc.getPairOne()) && this.stringTwo.equals(pc.getPairTwo()));
        this.setIsThereInGolden(r);
        return r;
    }
    
    /**
     * Convierte el objeto en un String
     * @return 
     */
    @Override
    public String toString(){
        return getStringOne() + " " + getStringTwo() + " " + getMeasure();
    }

    /**
     * @return the stringOne
     */
    public String getStringOne() {
        return stringOne;
    }

    /**
     * @param stringOne the stringOne to set
     */
    public void setStringOne(String stringOne) {
        this.stringOne = stringOne;
    }

    /**
     * @return the stringTwo
     */
    public String getStringTwo() {
        return stringTwo;
    }

    /**
     * @param stringTwo the stringTwo to set
     */
    public void setStringTwo(String stringTwo) {
        this.stringTwo = stringTwo;
    }

    /**
     * @return the measure
     */
    public double getMeasure() {
        return measure;
    }

    /**
     * @param measure the measure to set
     */
    public void setMeasure(double measure) {
        this.measure = measure;
    }

    /**
     * @return the recall
     */
    public double getRecall() {
        return recall;
    }

    /**
     * @param recall the recall to set
     */
    public void setRecall(double recall) {
        this.recall = recall;
    }

    /**
     * @return the precision
     */
    public double getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     * @return the fmeasure
     */
    public double getFmeasure() {
        return fmeasure;
    }

    /**
     * @param fmeasure the fmeasure to set
     */
    public void setFmeasure(double fmeasure) {
        this.fmeasure = fmeasure;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the isThereInGolden
     */
    public boolean isIsThereInGolden() {
        return isThereInGolden;
    }

    /**
     * @param isThereInGolden the isThereInGolden to set
     */
    public void setIsThereInGolden(boolean isThereInGolden) {
        if (!this.isThereInGolden) {
            this.isThereInGolden = isThereInGolden;
        }
        
    }
}
