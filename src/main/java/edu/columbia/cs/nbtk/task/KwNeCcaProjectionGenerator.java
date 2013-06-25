package edu.columbia.cs.nbtk.task;

import edu.columbia.cs.nbtk.feature.ApproximateCCA;
import edu.columbia.cs.nbtk.util.NBConfig;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/24/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KwNeCcaProjectionGenerator {

    public static void main(String[] args) {

        NBConfig config = NBConfig.getInstance();
        File keywordView = new File(config.getProperty("nb.keyword.overlap.view"));
        File namedEntityView = new File(config.getProperty("nb.ne.overlap.view"));


        try {

            ApproximateCCA cca = new ApproximateCCA(keywordView,namedEntityView);
            //cca.runCca();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }



    }


}
