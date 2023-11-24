/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EK;

import Sintaxis.ManejoArchivos;
import Sintaxis.MyListArgs;
import Sintaxis.MySintaxis;

/**
 *
 * @author Christian Millán <ceduardo.millan@gmail.com>
 */
public class MainEvaluation {

    ManejoArchivos io;
    MyListArgs param;
    String configFile;

    String objetive;
    String toEvaluate;
    String typeEvaluation;
    String formatt;
    String print;
    String metric;
    int levelMetric;
    int levelPrint;
    String fileOutput;

    public MainEvaluation(String[] args) {
        io = new ManejoArchivos();
        param = new MyListArgs(args);
        configFile = param.ValueArgsAsString("-CONFIG", "");

        if (!configFile.equals("")) {
            param.AddArgsFromFile(configFile);
        }

        String Sintaxis = "-OBJETIVE:str -TOEVALUATE:str -FORMATTK:SIM:ED -TYPEK:KDRK:MIL [-SHOW:ALL:RES:BTTR:OFF] -METRIC:PR:RE:FM [-NMT:int] [-FOUT:str] ";
        MySintaxis review = new MySintaxis(Sintaxis, param);

        objetive        = param.ValueArgsAsString("-OBJETIVE"   , ""    );
        toEvaluate      = param.ValueArgsAsString("-TOEVALUATE" , ""    );
        formatt         = param.ValueArgsAsString("-FORMATTK"   , "ED"  );
        typeEvaluation  = param.ValueArgsAsString("-TYPEK"      , "MIL" );
        print           = param.ValueArgsAsString("-SHOW"       , "OFF" );
        metric          = param.ValueArgsAsString("-METRIC"     , "FM"  );
        levelMetric    = param.ValueArgsAsInteger("-NMT"       , 4     );
        fileOutput      = param.ValueArgsAsString("-FOUT"       , ""    );

        switch (print) {
            case "ALL":
                levelPrint = 3;
                break;
            case "RES":
                levelPrint = 2;
                break;
            case "BTTR":
                levelPrint = 1;
                break;
            case "OFF":
                levelPrint = 0;
                break;
            default:
                levelPrint = 0;
        }

        fileOutput = ("".equals(fileOutput)) ? toEvaluate + ".txt" : fileOutput;
    }

    /**
     * Ejecuta los pasos de la evaliación: 1. Creación de la gold lista 2.
     * loadEvaluation carga todos lo pares y el resultado de la medida de cada
     * par 3.
     */
    public void run() {
//        long ti, tf;
//        ti = System.currentTimeMillis();
        //Evaluation evaluation = new Evaluation(objetive, toEvaluate, levelMetric, levelPrint, fileOutput);
        Evaluation evaluation = new Evaluation(this);
//        tf = System.currentTimeMillis();
//        System.out.println("crear evaluation: " + (tf-ti));
//        ti = System.currentTimeMillis();
        evaluation.loadEvaluations(formatt);
//        tf = System.currentTimeMillis();
//        System.out.println("Load evaluation: " + (tf-ti));
//        ti = System.currentTimeMillis();
        if ("KDRK".equals(typeEvaluation)) {
            evaluation.computeRecallKondrak();
            evaluation.computeMacroAveragingKondrak();
        } else {
            evaluation.computeRecallRAGH();
//            tf = System.currentTimeMillis();
//            System.out.println("Compute recall: " + (tf-ti));
//            ti = System.currentTimeMillis();
            evaluation.computeMacroAveragingRAGH();
//            tf = System.currentTimeMillis();
//            System.out.println("compute recall macro: " + (tf-ti));
        }

    }

    public float[] run(boolean b) {
       float[] fMeasure ={0.0F};     
        //Evaluation evaluation = new Evaluation(objetive, toEvaluate, levelMetric, levelPrint, fileOutput);
        Evaluation evaluation = new Evaluation(this);
        evaluation.loadEvaluations(formatt);
        if ("KDRK".equals(typeEvaluation)) {
            evaluation.computeRecallKondrak();
            evaluation.computeMacroAveragingKondrak();
        } else {
            evaluation.computeRecallRAGH();
            fMeasure = evaluation.computeMacroAveragingRAGH(b);
        }
        return fMeasure;
    }
    
    public static void main(String[] args) {
        MainEvaluation m = new MainEvaluation(args);
        m.run();
    }

}
