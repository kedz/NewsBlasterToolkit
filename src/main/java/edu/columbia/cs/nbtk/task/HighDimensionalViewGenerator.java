package edu.columbia.cs.nbtk.task;

import edu.columbia.cs.nbtk.feature.DistillationFeatureExtractor;
import edu.columbia.cs.nbtk.util.NBConfig;
import edu.columbia.cs.nlptk.util.OrderedWordList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/23/13
 * Time: 9:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class HighDimensionalViewGenerator {

    private static NBConfig config = NBConfig.getInstance();

    /* XML File creation infrastructure */
    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder documentBuilder;

    public HighDimensionalViewGenerator() throws ParserConfigurationException {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    private static OrderedWordList keywordOverlapFeatureList;
    private static OrderedWordList neOverlapFeatureList;
    static {
        keywordOverlapFeatureList = new OrderedWordList(new File(config.getProperty("nb.keyword.overlap.index")));
        neOverlapFeatureList = new OrderedWordList(new File(config.getProperty("nb.ne.overlap.index")));
    }


    public void writeKeywordOverlapView(File distillationDir, File viewFile) throws IOException, SAXException {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(viewFile), "UTF-8"));

        int rowNumber = 1;
        for(File disFile : distillationDir.listFiles()) {

            Document document = documentBuilder.parse(disFile);
            List<Map<String,Integer>> dataRowVectors = DistillationFeatureExtractor.extractKeywordOverlapFeatures(document);

            for(Map<String,Integer> countMap : dataRowVectors) {

                for(int i = 0; i < keywordOverlapFeatureList.size(); i++) {

                    String keywordOverlapFeature = keywordOverlapFeatureList.get(i);

                    if (countMap.containsKey(keywordOverlapFeature)) {
                        writer.write(rowNumber+","+(i+1)+","+1);
                        writer.newLine();
                        writer.flush();
                    }

                }

                rowNumber++;

            }

        }

        writer.close();
    }

    public void writeNeOverlapView(File distillationDir, File viewFile) throws IOException, SAXException {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(viewFile), "UTF-8"));


        int rowNumber = 1;

        for(File disFile : distillationDir.listFiles()) {

            Document document = documentBuilder.parse(disFile);
            List<Map<String,Integer>> dataRowVectors = DistillationFeatureExtractor.extractNeOverlapFeatures(document);

            for(Map<String,Integer> countMap : dataRowVectors) {

                for(int i = 0; i < neOverlapFeatureList.size(); i++) {

                    String neOverlapFeature = neOverlapFeatureList.get(i);

                    if (countMap.containsKey(neOverlapFeature)) {


                        writer.write(rowNumber+","+(i+1)+","+1);
                        writer.newLine();
                        writer.flush();

                    }

                }

                rowNumber++;

            }

        }

        writer.close();
    }


    public static void main(String[] args) {

        NBConfig config = NBConfig.getInstance();

        try {

            File distillationDir = new File(config.getProperty("nb.distillation.dir"));
            File keywordView = new File(config.getProperty("nb.keyword.overlap.view"));
            File neView = new File(config.getProperty("nb.ne.overlap.view"));

            HighDimensionalViewGenerator viewGenerator = new HighDimensionalViewGenerator();

            System.out.println("Writing keyword overlap view to: "+keywordView);
            viewGenerator.writeKeywordOverlapView(distillationDir, keywordView);

            System.out.println("Writing ne overlap view to: "+neView);
            viewGenerator.writeNeOverlapView(distillationDir, neView);




        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } catch (SAXException saxe) {
            saxe.printStackTrace();
            System.exit(-1);
        } catch (ParserConfigurationException pce) {
            System.err.println("XML Parser failed to initialize!");
            pce.printStackTrace();
            System.exit(-1);
        }
    }

}
