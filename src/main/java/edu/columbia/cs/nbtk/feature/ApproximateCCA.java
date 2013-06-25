package edu.columbia.cs.nbtk.feature;

import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveDouble;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/24/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApproximateCCA {


    private File viewX;
    private File viewY;
    private Map<Integer,Set<Integer>> xSamples = new HashMap<Integer,Set<Integer>>();
    private Map<Integer,Set<Integer>> ySamples = new HashMap<Integer,Set<Integer>>();

    private int numXDimensions;
    private int numYDimensions;

    private int numSamples = 0;

    private ArrayList<Integer> xCounts;
    private ArrayList<Integer> yCounts;
    private LinkedList<String> coOccurenceCountTriples = new LinkedList<String>();

    public ApproximateCCA(File viewX, File viewY) throws IOException {

        this.numXDimensions = loadView(viewX,xSamples);

        this.numYDimensions = loadView(viewY,ySamples);

        System.out.println("X View: " + numSamples + " samples, with " + numXDimensions + " dimensions.");
        System.out.println("Y View: " + numSamples + " samples, with " + numYDimensions + " dimensions.");


        //makeCounts();
        makeCounts2();

    }


    public int loadView(File view, Map<Integer,Set<Integer>> samples) throws IOException {

        Set<Integer> activeFeatures = new HashSet<Integer>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(view), "UTF-8"));

        int maxFeatureIndex = 0;
        Set<Integer> sampleCountSet = new HashSet<Integer>();

        while(reader.ready()) {
            String line = reader.readLine();
            String[] splitLine = line.split(",");
            int sampleNum = Integer.parseInt(splitLine[0]);
            int featureNum = Integer.parseInt(splitLine[1]);
            int featureValue = Integer.parseInt(splitLine[2]);

            if (featureValue > 0) {
                if (samples.containsKey(sampleNum)) {
                    samples.get(sampleNum).add(featureNum);
                } else {
                    samples.put(sampleNum,new HashSet<Integer>());
                    samples.get(sampleNum).add(featureNum);
                }


            }

            if (featureNum>maxFeatureIndex)
                maxFeatureIndex=featureNum;
            if (sampleNum>numSamples)
                numSamples=sampleNum;

        }

        return maxFeatureIndex;

    }

    private void makeCounts() {

        System.out.println("Building co-occurrence count vectors and matrices...");
        xCounts = new ArrayList<Integer>(numXDimensions);
        for(int i = 0; i < numXDimensions; i++)
            xCounts.add(0);
        yCounts = new ArrayList<Integer>(numYDimensions);
        for(int i = 0; i < numYDimensions; i++)
            yCounts.add(0);

        for(int i = 0; i < numXDimensions; i++) {


            for(int j = 0; j < numYDimensions; j++) {



                int countX = 0;
                int countY = 0;
                int countXY = 0;

                for(int n = 0; n < numSamples; n++) {

                    boolean xIsActive = false;
                    if (xSamples.containsKey(n))
                        xIsActive = xSamples.get(n).contains(i);

                    if (xIsActive)
                        countX++;

                    boolean yIsActive = false;
                    if (ySamples.containsKey(n))
                        yIsActive = ySamples.get(n).contains(j);

                    if (yIsActive)
                        countY++;

                    if (xIsActive && yIsActive)
                        countXY++;


                }

                if (countX>0)
                    xCounts.set(i,countX);

                if (countY>0)
                    yCounts.set(j,countX);

                if (countXY>0)
                    coOccurenceCountTriples.add(i+","+j+","+countXY);




            }




        }



    }

    private void makeCounts2() {

        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("x = csvread('/home/chris/newsblaster/views/dummy1.view')");

        octave.eval("X = ");

        //OctaveDouble b = octave.get(OctaveDouble.class, "b");
        //System.out.println(b);
        octave.close();


    }

    public void runCca() {

        System.out.println("Here");
        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        //octave.eval("O = spalloc("+numXDimensions+","+numYDimensions+","+coOccurenceCountTriples.size()+");");
        octave.eval("b = [ 1.0, 2.0, 3.0; 4.0, 5.0 ,6.0]");
        OctaveDouble b = octave.get(OctaveDouble.class, "b");
        System.out.println(b);
        octave.close();

    }

}
