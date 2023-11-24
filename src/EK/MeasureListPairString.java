/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EK;

import Sintaxis.ManejoArchivos;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * @author Christian Millán <ceduardo.millan@gmail.com>
 */
public class MeasureListPairString {
    private String name;
    private ArrayList<PairString> measureList = new ArrayList();
    private int levelPrint;
    
    /**
     * Crea un arraylist con todos los archivos que contiene los resultados de la medida a evaluar
     * @param file
     * @param path 
     */
    public MeasureListPairString(String file, String path, String formatt, int levelPrint){
        this.name = file;
        this.levelPrint = levelPrint;
        ManejoArchivos io = new ManejoArchivos();
        String[] lines = io.Read_Text_File_Fast(path + File.separator + file +".txt");
        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i].split(" ");
            measureList.add(new PairString(line[0], line[1], Double.parseDouble(line[2])));
        }
        if ("ED".equals(formatt)) {
            sortMeasuresMajToMin();
        } else
            sortMeasuresMinToMaj();  
        
        setRankAutomatic();
    }
    
    /**
     * Ordena los resultados de mayor a menor
     */
    private void sortMeasuresMajToMin(){
        Collections.sort(getMeasureList(), new Comparator<PairString>() {
            public int compare(PairString p1, PairString p2) {
		return new Double(p2.getMeasure()).compareTo(new Double (p1.getMeasure()));
            }
        });
    }
    
    /**
     * Ordena los resultados de menor a mayor
     */
    private void sortMeasuresMinToMaj(){
        Collections.sort(getMeasureList(), new Comparator<PairString>() {
            public int compare(PairString p1, PairString p2) {
		return new Double(p1.getMeasure()).compareTo(new Double (p2.getMeasure()));
            }
        });
    }
    
    public int howManyObjectiveRank(int rank){
        int count = 0;
        for (int i = 0; i < measureList.size(); i++) {
            if (measureList.get(i).isIsThereInGolden() && measureList.get(i).getRank() == rank) {
                count ++;
            }
        }
        return count;
    }
    
    private void setRankAutomatic(){
        int rank = 1;
        double pivot = measureList.get(0).getMeasure();
        for (int i = 0; i < measureList.size(); i++) {
            if (measureList.get(i).getMeasure() != pivot) {
                pivot = measureList.get(i).getMeasure();
                rank ++; 
            }
            measureList.get(i).setRank(rank);
            
        }
    }
    
    public int howManyAreIn(ArrayList subSet, int index){
        int count = 0;
        for (int i = 0; i < subSet.size(); i++) {
            PairConfused pc = (PairConfused)subSet.get(i);
            for (int j = index; j >= 0; j--) {
                PairString ps = measureList.get(j);
                if (ps.areSame(pc)) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public int whoIsTheLastRank(int rank){
        int i = 0;
        while (i < measureList.size() ){
            if (measureList.get(i).getRank() > rank ) {
                break;
            }
            i++;
        }
        
        return (i-1);
    }
    
//    private int howManyHaveTheRank(int rank){
//        for (int i = 0; i < ; i++) {
//            
//        }
//    }
    
     public int howManyAreInRank(ArrayList subSet, int index){
        int count = 0;
        for (int i = 0; i < subSet.size(); i++) {
            PairConfused pc = (PairConfused)subSet.get(i);

            for (int j = whoIsTheLastRank(index); j >= 0; j--) {
                PairString ps = measureList.get(j);
                if (ps.areSame(pc)) {
                    count++;
                }
            }
        }
        return count;
    }
    
    
    public void recall (ArrayList subSet){
        NumberFormat formatter = new DecimalFormat("#0.000000");
        int truePositives =  0;
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int auxLevelPrint = levelPrint;
        if (levelPrint >= 3) {
            System.out.println("\t" + "Level" +"\t" + "Score" + "\t\t" + "Recall" + "\t\t" + "Precision" + "\t" + "FMeasure" );
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < measureList.size(); i++) {
            truePositives = howManyAreIn(subSet, i);
            measureList.get(i).setRecall((double)truePositives/(double)subSet.size());
            measureList.get(i).setPrecision((double)truePositives/(double)(i+1));
            measureList.get(i).setFmeasure((2*truePositives)/(2*truePositives + ((i+1)-truePositives)+((double)subSet.size()-truePositives) ));
//            if (measureList.get(i).getRecall() == 0.0D && measureList.get(i).getPrecision() == 0.0D ) {
//                measureList.get(i).setFmeasure(0.0D);
//            }else
//                measureList.get(i).setFmeasure((2*measureList.get(i).getRecall()* measureList.get(i).getPrecision()) / (measureList.get(i).getRecall() + measureList.get(i).getPrecision()));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (auxLevelPrint >= 3) {
                if (measureList.get(i).getRecall() == 1.0D) {
                    auxLevelPrint = 0;
                }
                System.out.println("\t" + ((measureList.get(i).isIsThereInGolden())?"* ":"  ") + (i+1) + "\t" + formatter.format(measureList.get(i).getMeasure()) 
                        + "\t" + formatter.format(measureList.get(i).getRecall()) + "\t" + formatter.format(measureList.get(i).getPrecision()) + "\t" + formatter.format(measureList.get(i).getFmeasure())
                        + "\t" + measureList.get(i).getStringOne() + " " + measureList.get(i).getStringTwo());
//                if (measureList.get(i).isIsThereInGolden()) {
//                    auxLevelPrint = 0;
//                }
            }
            
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }
   
    public boolean nextRankChange(int index) {
        if ((index+1) < measureList.size()) {
            if (measureList.get(index).getRank() == measureList.get(index+1).getRank()) {
                return false;
            } else
                return true;
        } else
            return true;
    }
    
    public void recallRAGH (ArrayList subSet) {       
        NumberFormat formatter = new DecimalFormat("#0.000000");
        int truePositives =  0;
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int auxLevelPrint = levelPrint;
        if (levelPrint >= 3) {
            System.out.println("\t" + "Level" +"\t" + "Score" + "\t\t" + "Recall" + "\t\t" + "Precision" + "\t" + "FMeasure" );
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < measureList.size(); i++) {
            truePositives = howManyAreInRank(subSet, measureList.get(i).getRank());
            Double x = ((double)truePositives/(double)subSet.size());
            if (x.isNaN()) {
                x = 0.0D;
            }
            measureList.get(i).setRecall(x);
            measureList.get(i).setPrecision((double)truePositives/(double) (whoIsTheLastRank(measureList.get(i).getRank())+1));
            //measureList.get(i).setFmeasure((2*measureList.get(i).getRecall()*measureList.get(i).getPrecision()) / (measureList.get(i).getRecall() + measureList.get(i).getPrecision()));
            if (measureList.get(i).getRecall() == 0.0D && measureList.get(i).getPrecision() == 0.0D ) {
                measureList.get(i).setFmeasure(0.0D);
            }else
                measureList.get(i).setFmeasure((2*measureList.get(i).getRecall()* measureList.get(i).getPrecision()) / (measureList.get(i).getRecall() + measureList.get(i).getPrecision()));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (auxLevelPrint >= 3) {
                if (measureList.get(i).getRecall() == 1.0D && nextRankChange(i)  ) {
                    auxLevelPrint = 0;
                }
                System.out.println("\t" + ((measureList.get(i).isIsThereInGolden())?"* ":"  ") + (measureList.get(i).getRank()) + "\t" + formatter.format(measureList.get(i).getMeasure()) 
                        + "\t" + formatter.format(measureList.get(i).getRecall()) + "\t" + formatter.format(measureList.get(i).getPrecision()) + "\t" + formatter.format(measureList.get(i).getFmeasure())
                        + "\t" + measureList.get(i).getStringOne() + " " + measureList.get(i).getStringTwo());
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }   
    }
    
  
    public String toString(){
        return getMeasureList().toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the measureList
     */
    public ArrayList getMeasureList() {
        return measureList;
    }

    /**
     * @param measureList the measureList to set
     */
    public void setMeasureList(ArrayList measureList) {
        this.measureList = measureList;
    }
}
