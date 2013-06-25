package edu.columbia.cs.nbtk.task;

import edu.columbia.cs.nbtk.feature.DistillationFeatureExtractor;
import edu.columbia.cs.nbtk.util.NBConfig;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/22/13
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparseFeatureIndexGenerator {

    private Set<String> keywordOverlapFeatures = new TreeSet<String>();
    private Set<String> bigramOverlapFeatures = new TreeSet<String>();
    private Set<String> neOverlapFeatures = new TreeSet<String>();
    private Map<String, Map<String,Integer>> relevanceFeedbackFeatures = new TreeMap<String, Map<String, Integer>>();

    public void loadDistillationDir(File distillationDir)
            throws ParserConfigurationException, IOException, SAXException {

        /* XML File creation infrastructure */
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        File[] disFiles = distillationDir.listFiles();
        for(File disFile : disFiles) {
            System.out.println("Processing: "+disFile);
            Document document = documentBuilder.parse(disFile);

            List<Map<String,Integer>> keywordOverlapCounts = DistillationFeatureExtractor.extractKeywordOverlapFeatures(document);
            for(Map<String,Integer> countMap : keywordOverlapCounts) {
                keywordOverlapFeatures.addAll(countMap.keySet());
            }

            //extractBigramOverlapFeatures(document);
            List<Map<String,Integer>> neOverlapCounts = DistillationFeatureExtractor.extractNeOverlapFeatures(document);
            for(Map<String,Integer> countMap : neOverlapCounts) {
                neOverlapFeatures.addAll(countMap.keySet());
            }

            extractRelevanceFeedbackFeatures(document);


        }




    }

//    private void extractBigramOverlapFeatures(Document document) {
//
//        NodeList summaries = document.getElementsByTagName("Summary");
//        for(int s = 0; s < summaries.getLength(); s++) {
//
//            HashSet<String> titleBigrams = new HashSet<String>();
//
//            Element summaryElement = (Element) summaries.item(s);
//            Element titleElement = (Element) summaryElement.getElementsByTagName("Title").item(0);
//            if (titleElement!=null) {
//                NodeList titleWords = titleElement.getElementsByTagName("Word");
//                for(int w = 0; w < titleWords.getLength(); w++) {
//
//                    if (w==0) {
//                        Element w1 = (Element)titleWords.item(w);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        titleBigrams.add(("<S> " + lemma1.toLowerCase()).intern());
//
//                    } else if (w+1<titleWords.getLength()) {
//                        Element w1 = (Element)titleWords.item(w);
//                        Element w2 = (Element)titleWords.item(w+1);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        String lemma2 = w2.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma2 = StringEscapeUtils.unescapeXml(lemma2);
//                        titleBigrams.add((lemma1.toLowerCase()+" "+lemma2.toLowerCase()).intern());
//                    } else if (w+1 >= titleWords.getLength()) {
//                        Element w1 = (Element)titleWords.item(w);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        titleBigrams.add((lemma1.toLowerCase() + " <E>").intern());
//                    }
//
//                }
//            }
//
//            NodeList sentences = summaryElement.getElementsByTagName("Sentence");
//            for(int sentIdx = 0; sentIdx < sentences.getLength(); sentIdx++) {
//                Element sentence = (Element) sentences.item(sentIdx);
//                NodeList words = sentence.getElementsByTagName("Word");
//                for(int w = 0; w < words.getLength();w++) {
//
//                    String summaryBigram = null;
//
//                    if (w==0) {
//                        Element w1 = (Element)words.item(w);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        summaryBigram = ("<S> " + lemma1.toLowerCase()).intern();
//
//                    } else if (w+1<words.getLength()) {
//                        Element w1 = (Element)words.item(w);
//                        Element w2 = (Element)words.item(w+1);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        String lemma2 = w2.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma2 = StringEscapeUtils.unescapeXml(lemma2);
//                        summaryBigram = (lemma1.toLowerCase()+" "+lemma2.toLowerCase()).intern();
//                    } else if (w+1 >= words.getLength()) {
//                        Element w1 = (Element)words.item(w);
//                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                        summaryBigram = (lemma1.toLowerCase() + " <E>").intern();
//                    }
//
//                    if (titleBigrams.contains(summaryBigram)) {
//                        bigramOverlapFeatures.add(summaryBigram);
//                    }
//
//                }
//
//            }
//
//        }
//
//    }
//
//    //Change to unigram
//    private void extractUnigramOverlapFeatures(Document document) {
//
//        NodeList summaries = document.getElementsByTagName("Summary");
//        for(int s = 0; s < summaries.getLength(); s++) {
//
//            HashSet<String> titleUnigrams = new HashSet<String>();
//
//            Element summaryElement = (Element) summaries.item(s);
//            Element titleElement = (Element) summaryElement.getElementsByTagName("Title").item(0);
//            if (titleElement!= null) {
//
//                NodeList titleWords = titleElement.getElementsByTagName("Word");
//                for(int w = 0; w < titleWords.getLength(); w++) {
//
//                    Element w1 = (Element)titleWords.item(w);
//                    String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                    lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                    titleUnigrams.add(lemma1.intern());
//                }
//
//
//                Element sentencesElement = (Element) summaryElement.getElementsByTagName("Sentences").item(0);
//                NodeList summaryWords = sentencesElement.getElementsByTagName("Word");
//                for(int w = 0; w < summaryWords.getLength();w++) {
//                    Element w1 = (Element)summaryWords.item(w);
//                    String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
//                    lemma1 = StringEscapeUtils.unescapeXml(lemma1);
//                    String summaryUnigram = lemma1.intern();
//
//                    if (titleUnigrams.contains(summaryUnigram)) {
//                        unigramOverlapFeatures.add(summaryUnigram);
//                    }
//
//                }
//
//            }
//        }
//
//    }

    private void extractNeOverlapFeatures(Document document) {

        NodeList summaries = document.getElementsByTagName("Summary");
        for(int s = 0; s < summaries.getLength(); s++) {

            HashSet<String> titleNeWords = new HashSet<String>();

            Element summaryElement = (Element) summaries.item(s);
            Element titleElement = (Element) summaryElement.getElementsByTagName("Title").item(0);
            if (titleElement!= null) {
                NodeList titleWords = titleElement.getElementsByTagName("Word");
                for(int w = 0; w < titleWords.getLength(); w++) {

                    Element w1 = (Element)titleWords.item(w);
                    String neTag = w1.getElementsByTagName("Ne").item(0).getTextContent();

                    if (!neTag.equals("O")) {

                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                        titleNeWords.add((lemma1.toLowerCase()+" "+neTag).intern());

                    }
                }
            }

            Element sentencesElement = (Element) summaryElement.getElementsByTagName("Sentences").item(0);
            NodeList summaryWords = sentencesElement.getElementsByTagName("Word");
            for(int w = 0; w < summaryWords.getLength();w++) {
                Element w1 = (Element)summaryWords.item(w);
                String neTag = w1.getElementsByTagName("Ne").item(0).getTextContent();

                if (!neTag.equals("O")) {

                    String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                    lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                    String summaryNeWord = (lemma1.toLowerCase()+" "+neTag).intern();

                    if (titleNeWords.contains(summaryNeWord)) {
                        neOverlapFeatures.add(summaryNeWord);
                    }

                }
            }


        }

    }


    private void extractRelevanceFeedbackFeatures(Document document) {

        NodeList summaries = document.getElementsByTagName("Summary");
        for(int s = 0; s < summaries.getLength(); s++) {

            HashSet<String> titleWords = new HashSet<String>();
            HashSet<String> summaryWords = new HashSet<String>();

            Element summaryElement = (Element) summaries.item(s);
            Element titleElement = (Element) summaryElement.getElementsByTagName("Title").item(0);

            if (titleElement!=null) {
                NodeList titleWordsList = titleElement.getElementsByTagName("Word");
                for(int w = 0; w < titleWordsList.getLength(); w++) {

                    Element w1 = (Element)titleWordsList.item(w);
                    String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                    lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                    titleWords.add(lemma1.intern());

                }


                Element sentencesElement = (Element) summaryElement.getElementsByTagName("Sentences").item(0);
                NodeList summaryWordsList = sentencesElement.getElementsByTagName("Word");
                for(int w = 0; w < summaryWordsList.getLength();w++) {
                    Element w1 = (Element)summaryWordsList.item(w);
                    String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                    lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                    String summaryWord = lemma1.intern();

                    summaryWords.add(summaryWord);

                }


                for(String titleWord : titleWords) {

                    if (!relevanceFeedbackFeatures.containsKey(titleWord)) {
                        relevanceFeedbackFeatures.put(titleWord,new TreeMap<String,Integer>());
                    }

                    for(String summaryWord : summaryWords) {

                        Map<String, Integer> countMap = relevanceFeedbackFeatures.get(titleWord);

                        if (!countMap.containsKey(summaryWord)) {
                            countMap.put(summaryWord,1);
                        } else {

                            int previousCount = countMap.get(summaryWord);
                            countMap.put(summaryWord,previousCount+1);

                        }

                    }


                }

            }
        }

    }


    public void writeKeywordOverlapIndex(File keywordOverlapIndexFile) throws IOException {

        System.out.println("Writing " + keywordOverlapFeatures.size() + " keyword overlap features to: " + keywordOverlapIndexFile);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keywordOverlapIndexFile), "UTF-8"));
        for(String keywordOverlapFeature : keywordOverlapFeatures) {
            writer.write(keywordOverlapFeature);
            writer.newLine();
            writer.flush();
        }

        writer.close();

    }

    public void writeBigramOverlapIndex(File bigramOverlapIndexFile) throws IOException {

        System.out.println("Writing " + bigramOverlapFeatures.size() + " bigram overlap features to: " + bigramOverlapIndexFile);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bigramOverlapIndexFile),"UTF-8"));
        for(String bigramOverlapFeature : bigramOverlapFeatures) {
            writer.write(bigramOverlapFeature);
            writer.newLine();
            writer.flush();
        }

        writer.close();

    }

    public void writeNeOverlapIndex(File neOverlapIndexFile) throws IOException {

        System.out.println("Writing " + neOverlapFeatures.size() + " named entity overlap features to: " + neOverlapIndexFile);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(neOverlapIndexFile),"UTF-8"));
        for(String neOverlapFeature : neOverlapFeatures) {
            writer.write(neOverlapFeature);
            writer.newLine();
            writer.flush();
        }

        writer.close();

    }

    public void writeRelevanceFeedbackFiles(File rfDir) throws IOException {

        System.out.println("Writing relevance feedback features to: " + rfDir);


        for(String titleWord : relevanceFeedbackFeatures.keySet()) {

            if (relevanceFeedbackFeatures.get(titleWord).size()>0) {

                String fileSafeTitleWord = titleWord.replaceAll(File.separator, "\\"+File.separator);

                File titleRf = new File(rfDir+File.separator+fileSafeTitleWord+".rf");
                Map<String,Integer> countsMap = relevanceFeedbackFeatures.get(titleWord);


                int totalCounts = 0;
                for(String summaryWord : countsMap.keySet()) {
                    totalCounts += countsMap.get(summaryWord);
                }

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(titleRf),"UTF-8"));
                for(String summaryWord : countsMap.keySet()) {
                    double prob = ((double) countsMap.get(summaryWord))/totalCounts;
                    writer.write(summaryWord+" "+Math.log(prob));
                    writer.newLine();
                    writer.flush();
                }

                writer.close();

            }
        }

    }



    public static void main(String[] args) {

        NBConfig config = NBConfig.getInstance();

        SparseFeatureIndexGenerator indexGenerator = new SparseFeatureIndexGenerator();

        File distillationDir = new File(config.getProperty("nb.distillation.dir"));
        File keywordOverlapIndex = new File(config.getProperty("nb.keyword.overlap.index"));
        File neOverlapIndex = new File(config.getProperty("nb.ne.overlap.index"));
        File rfDir = new File(config.getProperty("nb.rf.dir"));

        try {

            indexGenerator.loadDistillationDir(distillationDir);

            indexGenerator.writeKeywordOverlapIndex(keywordOverlapIndex);
            //indexGenerator.writeBigramOverlapIndex(bigramOverlapIndex);
            indexGenerator.writeNeOverlapIndex(neOverlapIndex);
            indexGenerator.writeRelevanceFeedbackFiles(rfDir);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } catch (SAXException saxe) {
            saxe.printStackTrace();
            System.exit(-1);
        }




    }



}
