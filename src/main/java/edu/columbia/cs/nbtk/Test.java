package edu.columbia.cs.nbtk;

import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveDouble;
import edu.columbia.cs.nbtk.util.NBConfig;
import edu.columbia.cs.nlptk.util.OrderedWordList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/23/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {


    public static void main(String[] args) throws IOException {

//        NBConfig config = NBConfig.getInstance();
//        File dir = new File(config.getProperty("nb.rf.dir"));
//
//        for(File rfFile : dir.listFiles()) {
//
//            double totalProb = 0.0;
//            BufferedReader reader = new BufferedReader(new FileReader(rfFile));
//            while(reader.ready()) {
//
//                String[] line = reader.readLine().split(" ");
//                totalProb += Double.parseDouble(line[1]);
//
//            }
//            reader.close();
//
//            if (Math.abs(totalProb-1.0) > .00001)  {
//
//                System.out.println(rfFile+" : "+totalProb);
//
//            }
//
//        }

//        OrderedWordList keywordList = new OrderedWordList(new File("/home/chris/newsblaster/indexes/keyword.overlap.index"));
//        OrderedWordList uniList = new OrderedWordList(new File("/home/chris/newsblaster/indexes/unigram.overlap.index2"));
//        OrderedWordList biList = new OrderedWordList(new File("/home/chris/newsblaster/indexes/bigram.overlap.indexb"));
//
//        List<String> kList = keywordList.getWordList();
//        System.out.println(kList.size());
//
//        for(String uni : uniList.getWordList()) {
//            kList.remove(uni);
//        }
//
//        System.out.println(kList.size());
//
//        for(String bigram : biList.getWordList()) {
//            System.out.println("B: "+bigram);
//            kList.remove(bigram);
//        }
//
//        System.out.println("\n\n\n");
//
//        System.out.println(kList.size());
//        for(String remainder : kList) {
//            System.out.println(remainder);
//        }


        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        //octave.eval("O = spalloc("+numXDimensions+","+numYDimensions+","+coOccurenceCountTriples.size()+");");
        octave.eval("b = [ 1.0, 2.0, 3.0; 4.0, 5.0 ,6.0]");
        OctaveDouble b = octave.get(OctaveDouble.class, "b");
        System.out.println(b);
        octave.close();

    }

}
