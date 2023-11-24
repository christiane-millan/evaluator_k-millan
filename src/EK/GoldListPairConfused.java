/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EK;

import Sintaxis.ManejoArchivos;
import java.util.ArrayList;

/**
 * 
 * @author Christian Millán <ceduardo.millan@gmail.com>
 */
public class GoldListPairConfused {
    ArrayList<PairConfused> goldList;
    
    /**
     * Crea un objeto que almacena todos los pares objetivo que serviran para evaluar la medida
     * @param file String con la ruta del archivo que contiene los pares objetivo
     */
    public GoldListPairConfused(String file){
        ManejoArchivos io = new ManejoArchivos();
        goldList = new ArrayList();
        String[] lines = io.Read_Text_File_Fast(file);
        for (int i = 0; i < lines.length; i++) {
            String[] lin = lines[i].split(" ");
            goldList.add(i, new PairConfused(lin[0], lin[1]));
        }
    }
    
    
    public int size(){
        return goldList.size();
    }
    
    public String getPairConfusedOne(int i){
        return goldList.get(i).getPairOne();
    }
    
    public String getPairConfusedTwo(int i){
        return goldList.get(i).getPairTwo();
    }
    
    public ArrayList setContainsOnLeft (String name){
        ArrayList result = new ArrayList();
        for (int i = 0; i < goldList.size(); i++) {
            if (goldList.get(i).getPairOne().equals(name)) {
                result.add(goldList.get(i));
            }
        }
        return result;
    }
    
    @Override
    public String toString(){
        return goldList.toString();
    }
    
    public static void main(String[] args) {
        GoldListPairConfused gold = new GoldListPairConfused("ParesMedicina.txt");
        System.out.println(gold);
    }
}
