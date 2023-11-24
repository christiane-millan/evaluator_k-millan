/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EK;

import Sintaxis.IOBinFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 
 * @author Christian Millán <ceduardo.millan@gmail.com>
 */
public class Evaluation {
    GoldListPairConfused goldList;
    ArrayList<MeasureListPairString> allMeasureList;
    private String objetive;
    private String toEvaluate;
    private String metric;
    private int levelMetric;
    private int levelPrint;
    private String fileOutput;
    
    
    IOBinFile binFile; 
    /**
     * Crea un objeto Evaluation con una lista de pares (objetive) a evaluar y con la
     * ruta de los resultados de la medida a evaluar (toEvaluate)
     * @param objetive ruta del archivo que contiene los pares objetivo
     * @param toEvaluate  ruta de la carpeta que contiene los archivos con los resultados
     * de las medidas
     * @param levelFMeasure
     * @param levelPrint
     * @param fileOutput
     */
    public Evaluation (String objetive, String toEvaluate, int levelMetric, int levelPrint, String fileOutput){
        goldList = new GoldListPairConfused(objetive);
        allMeasureList = new ArrayList();
        this.toEvaluate = toEvaluate;
        this.levelMetric = levelMetric;
        this.levelPrint = levelPrint;
        this.fileOutput = fileOutput;
        
        binFile = new IOBinFile();
    }
    
    public Evaluation (MainEvaluation m){
        goldList = new GoldListPairConfused(m.objetive);
        allMeasureList = new ArrayList();
        this.toEvaluate = m.toEvaluate;
        this.metric = m.metric;
        this.levelMetric = m.levelMetric;
        this.levelPrint = m.levelPrint;
        this.fileOutput = m.fileOutput;
        
        binFile = new IOBinFile();
    }
    
    public boolean existMeasureList(String name){
        for (MeasureListPairString allMeasureList1 : allMeasureList) {
            if (allMeasureList1.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Carga todos lo archivos de la ruta toEvaluate en un arreglo 
     */
    public void loadEvaluations (String formatt){
        for (int i = 0; i < goldList.size(); i++) {
            MeasureListPairString mlps = new MeasureListPairString(goldList.getPairConfusedOne(i),toEvaluate, formatt, levelPrint);
            if (!existMeasureList(mlps.getName())) {
                allMeasureList.add(mlps);
            }
            mlps = new MeasureListPairString(goldList.getPairConfusedTwo(i),toEvaluate, formatt, levelPrint);
            if (!existMeasureList(mlps.getName())) {
                allMeasureList.add(mlps);
            }
        }
    }
    /**
     * Calcula recuerdo, precisión y fmeasure en todos las listas de medidas de acuerdo 
     * al articulo de Kondrak 
     */
    public void computeRecallKondrak(){
        for (int i = 0; i < allMeasureList.size(); i++) {
            ArrayList r = goldList.setContainsOnLeft(allMeasureList.get(i).getName());
            
            if (levelPrint >= 3) {
                System.out.println((i+1) + "\t[" + allMeasureList.get(i).getName() + "] \t_____________________________________________");
            }
            
            allMeasureList.get(i).recall(r);
        }
        
    }
    
    /**
     * Calcula el macro-averagin de todos los archivos de acuerdo al articulo de Kondrak
     */
    public void computeMacroAveragingKondrak(){
        double[] recallMA = new double[allMeasureList.size()];
        double[] precisionMA = new double[allMeasureList.size()];
        double[] fmeasureMA = new double[allMeasureList.size()];
        
        NumberFormat formatter = new DecimalFormat("#0.00000");
        
        
        for (int i = 0; i < recallMA.length; i++) {
            recallMA[i] = 0;
            precisionMA[i] = 0;
            fmeasureMA[i] = 0;
        }
        for (int i = 0; i < allMeasureList.get(0).getMeasureList().size(); i++) {
            for (int j = 0; j < allMeasureList.size(); j++) {
                PairString ps = (PairString)allMeasureList.get(j).getMeasureList().get(i);
                recallMA[i] += ps.getRecall();
                precisionMA[i] += ps.getPrecision();
                fmeasureMA[i] += ps.getFmeasure();
            }   
        }    
        
        
        if (levelPrint >= 2) {
            System.out.println("____________________________________________________________");
            System.out.println("Numero de pares diferentes fue de:" + goldList.size());
            System.out.println("El numero de comparaciones total entre ellas fue de:");
            System.out.println("L\tRecall\tPreci\tFMeasure");
            for (int i = 0; i < recallMA.length; i++) {
                System.out.println((i+1)+":\t"+formatter.format(recallMA[i]/recallMA.length)+"\t"+formatter.format(precisionMA[i]/precisionMA.length)+"\t" + formatter.format(fmeasureMA[i]/fmeasureMA.length) );
            }    
        }
        if (levelPrint  == 1) {
            System.out.println(""+ formatter.format(fmeasureMA[levelMetric]/fmeasureMA.length));        
        }
        
    }
    
    public void computeRecallRAGH(){
        //System.out.println("RAGH");
        for (int i = 0; i < allMeasureList.size(); i++) {
            ArrayList r = goldList.setContainsOnLeft(allMeasureList.get(i).getName());
            if (levelPrint >= 3) {
                
                System.out.println((i+1) + "\t[" + allMeasureList.get(i).getName() + "] \t_____________________________________________");
            }
            allMeasureList.get(i).recallRAGH(r);
        }
    }
    
    public void computeMacroAveragingRAGH(){
        double[] recallMA = new double[allMeasureList.size()];
        double[] precisionMA = new double[allMeasureList.size()];
        double[] fmeasureMA = new double[allMeasureList.size()];
        int[] rank   = new int[allMeasureList.size()];
        int[] rec = new int[allMeasureList.size()];
        
        NumberFormat formatter = new DecimalFormat("#0.00000");
        
        for (int i = 0; i < recallMA.length; i++) {
            recallMA[i] = 0;
            precisionMA[i] = 0;
            fmeasureMA[i] = 0;
        }
        for (int i = 0; i < allMeasureList.get(0).getMeasureList().size(); i++) {
            for (int j = 0; j < allMeasureList.size(); j++) {
                int index = allMeasureList.get(j).whoIsTheLastRank(i+1);
                if (index > -1) {
                    PairString ps = (PairString)allMeasureList.get(j).getMeasureList().get(index);
                    recallMA[i] += ps.getRecall();
                    precisionMA[i] += ps.getPrecision();
                    fmeasureMA[i] += ps.getFmeasure();
                    rank[i] += allMeasureList.get(j).howManyObjectiveRank(i+1);
                    rec[i] += (index+1);
                }
            }   
            
        }   
        
        if (levelPrint >= 2) {
            System.out.println("____________________________________________________________");
            System.out.println("Numero de pares diferentes fue de:" + goldList.size());
            System.out.println("Mumero de palabras diferentes fue de:" + allMeasureList.size());
            System.out.println("El numero de comparaciones total entre ellas fue de: " + (allMeasureList.size() * allMeasureList.get(0).getMeasureList().size()));
            System.out.println("L\tRank\tAc(R)\tREC(L)\tAc(REC)"
                    + "\tRecall\tPreci\tFMeasure");
            int Ac = 0;
            int AcRec = 0;
            int auxLevelPrint = levelPrint;
            for (int i = 0; i < recallMA.length; i++) {
                
                Ac += rank[i];
                AcRec += rec[i];
                if (auxLevelPrint >= 2) {
                    if (rank[i]>0) {
                        System.out.println((i+1)+":\t" + rank[i] + "\t" + Ac + "\t" + rec[i] +"\t" + AcRec + "\t"
                                +formatter.format(recallMA[i]/recallMA.length)+"\t"+formatter.format(precisionMA[i]/precisionMA.length)+"\t" + formatter.format(fmeasureMA[i]/fmeasureMA.length) );    
                    }
                }
                if (recallMA[i] == 1.0D) {
                    auxLevelPrint = 0;
                }
            }  
            int sumRank = 0;
            for (int i = 0; i < rank.length; i++) {
                sumRank += rank[i];
            }
            
            int sumRec = 0;
            for (int i = 0; i < rec.length; i++) {
                sumRec += rec[i];
            }
            
            //System.out.println("Sum(Rank):" + sumRank + "\tSum(REC:)" + sumRec  );
            System.out.println("Sum(Rank):" + sumRank  );
            System.out.println("L\t: Posicion entre las comparaciones de las medidas");
            System.out.println("Rank\t: Numero de comparaciones objetivo encontradas en esa posición");
            System.out.println("Ac(R)\t: El acumulado de Rank");
            System.out.println("REC(L)\t: Número de comparaciones encontradas hasta esa posición");
            System.out.println("Ac(REC)\t: El acumulado de REC");
            System.out.println("Recall\t: Recall = TP / (FN+TP)");
            System.out.println("Preci\t: Precision = TP / (Tp+FP)");
            System.out.println("F-measure\t: (2*Recall*Precision)/(Recall+Precision)");
        }
        
        if (levelPrint  >= 1) {
            float[] ADDfmeasureMA = new float[1];
            if ("FM".equals(metric)) {
                for (int i = 0; i < levelMetric; i++) {
                    ADDfmeasureMA[0] += (fmeasureMA[i]/fmeasureMA.length);
                }
            }
            if ("PR".equals(metric)) {
                for (int i = 0; i < levelMetric; i++) {
                    ADDfmeasureMA[0] += (precisionMA[i]/precisionMA.length);
                }
            }
            System.out.println(""+ formatter.format(ADDfmeasureMA[0]));
            if (!"".equals(fileOutput)) {
                binFile.WriteBinFloatFileIEEE754(fileOutput, ADDfmeasureMA);
            }
        }
        
    }
    
    public float[] computeMacroAveragingRAGH(boolean b){
        double[] recallMA = new double[allMeasureList.size()];
        double[] precisionMA = new double[allMeasureList.size()];
        double[] fmeasureMA = new double[allMeasureList.size()];
        int[] rank   = new int[allMeasureList.size()];
        int[] rec = new int[allMeasureList.size()];
        
        NumberFormat formatter = new DecimalFormat("#0.00000");
        
        
        for (int i = 0; i < recallMA.length; i++) {
            recallMA[i] = 0;
            precisionMA[i] = 0;
            fmeasureMA[i] = 0;
        }
        for (int i = 0; i < allMeasureList.get(0).getMeasureList().size(); i++) {
            for (int j = 0; j < allMeasureList.size(); j++) {
                int index = allMeasureList.get(j).whoIsTheLastRank(i+1);
                if (index > -1) {
                    PairString ps = (PairString)allMeasureList.get(j).getMeasureList().get(index);
                    recallMA[i] += ps.getRecall();
                    precisionMA[i] += ps.getPrecision();
                    fmeasureMA[i] += ps.getFmeasure();
                    rank[i] += allMeasureList.get(j).howManyObjectiveRank(i+1);
                    rec[i] += (index+1);
                }
            }   
            
        }   
        
        if (levelPrint >= 2) {
            int Ac = 0;
            int AcRec = 0;
            int auxLevelPrint = levelPrint;
            for (int i = 0; i < recallMA.length; i++) {
                
                Ac += rank[i];
                AcRec += rec[i];
                if (auxLevelPrint >= 2) {
                    if (rank[i]>0) {
                       // System.out.println((i+1)+":\t" + rank[i] + "\t" + Ac + "\t" + rec[i] +"\t" + AcRec + "\t"
                               // +formatter.format(recallMA[i]/recallMA.length)+"\t"+formatter.format(precisionMA[i]/precisionMA.length)+"\t" + formatter.format(fmeasureMA[i]/fmeasureMA.length) );    
                    }
                }
                if (recallMA[i] == 1.0D) {
                    auxLevelPrint = 0;
                }
            }  
            int sumRank = 0;
            for (int i = 0; i < rank.length; i++) {
                sumRank += rank[i];
            }
            
            int sumRec = 0;
            for (int i = 0; i < rec.length; i++) {
                sumRec += rec[i];
            }
        }
        
        float[] ADDfmeasureMA = new float[1];
        if (levelPrint  >= 1) {  
            if ("FM".equals(metric)) {
                for (int i = 0; i < levelMetric; i++) {
                    ADDfmeasureMA[0] += (fmeasureMA[i]/fmeasureMA.length);
                }
            }
            if ("PR".equals(metric)) {
                for (int i = 0; i < levelMetric; i++) {
                    ADDfmeasureMA[0] += (precisionMA[i]/precisionMA.length);
                }
            }
//            System.out.println(""+ formatter.format(ADDfmeasureMA[0]));
        }
        return ADDfmeasureMA;
    }
}
