package edu.columbia.cs.nbtk.feature;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/23/13
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistillationFeatureExtractor {

    public static List<Map<String,Integer>> extractKeywordOverlapFeatures(Document document) {

        List<Map<String,Integer>> overlapCountMaps = new LinkedList<Map<String, Integer>>();

        NodeList summaries = document.getElementsByTagName("Summary");
        for(int s = 0; s < summaries.getLength(); s++) {


            LinkedList<String> titleUnigrams = new LinkedList<String>();
            LinkedList<String> titleBigrams = new LinkedList<String>();

            Element summaryElement = (Element) summaries.item(s);
            Element titleElement = (Element) summaryElement.getElementsByTagName("Title").item(0);
            if (titleElement!= null) {

                NodeList titleWords = titleElement.getElementsByTagName("Word");
                for(int w = 0; w < titleWords.getLength(); w++) {

                    //do bigrams

                    if (w==0) {
                        Element w1 = (Element)titleWords.item(w);
                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);

                        titleBigrams.add(("<S> " + lemma1.toLowerCase()).intern());

                    }

                    if (w+1<titleWords.getLength()) {
                        Element w1 = (Element)titleWords.item(w);
                        Element w2 = (Element)titleWords.item(w+1);
                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                        String lemma2 = w2.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma2 = StringEscapeUtils.unescapeXml(lemma2);

                        titleUnigrams.add(lemma1.intern());
                        titleBigrams.add((lemma1.toLowerCase()+" "+lemma2.toLowerCase()).intern());

                    } else if (w+1 >= titleWords.getLength()) {
                        Element w1 = (Element)titleWords.item(w);
                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);

                        titleUnigrams.add(lemma1.intern());
                        titleBigrams.add((lemma1.toLowerCase() + " <E>").intern());

                    }

                }



                NodeList summarySentences = summaryElement.getElementsByTagName("Sentence");
                for(int sentIdx = 0; sentIdx < summarySentences.getLength(); sentIdx++) {

                    LinkedList<String> localTitleUnigrams = new LinkedList<String>(titleUnigrams);
                    LinkedList<String> localTitleBigrams = new LinkedList<String>(titleBigrams);
                    Map<String,Integer> wordCounts = new TreeMap<String, Integer>();

                    Element sentenceElement = (Element) summarySentences.item(sentIdx);
                    NodeList summaryWords = sentenceElement.getElementsByTagName("Word");
                    for(int w = 0; w < summaryWords.getLength();w++) {

                        if (w==0) {

                            Element w1 = (Element)summaryWords.item(w);
                            String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                            lemma1 = StringEscapeUtils.unescapeXml(lemma1);

                            String startBigram = ("<S> " + lemma1).intern();

                            if (localTitleBigrams.contains(startBigram)) {

                                localTitleBigrams.remove(startBigram);
                                wordCounts.put(startBigram,1);

                            }

                        }

                        String summaryUnigram = null;
                        String summaryBigram = null;

                        if (w+1<summaryWords.getLength()) {
                            Element w1 = (Element)summaryWords.item(w);
                            Element w2 = (Element)summaryWords.item(w+1);
                            String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                            lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                            String lemma2 = w2.getElementsByTagName("Lemma").item(0).getTextContent();
                            lemma2 = StringEscapeUtils.unescapeXml(lemma2);


                            summaryUnigram = lemma1.intern();
                            summaryBigram = (lemma1+" "+lemma2).intern();

                        } else if (w+1 >= summaryWords.getLength()) {
                            Element w1 = (Element)summaryWords.item(w);
                            String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                            lemma1 = StringEscapeUtils.unescapeXml(lemma1);

                            summaryUnigram = lemma1.intern();
                            summaryBigram = (lemma1.toLowerCase() + " <E>").intern();

                        }


                        if (localTitleUnigrams.contains(summaryUnigram)) {

                            localTitleUnigrams.remove(summaryUnigram);

                            if (wordCounts.containsKey(summaryUnigram)) {
                                int previousCount = wordCounts.get(summaryUnigram);
                                wordCounts.put(summaryUnigram,previousCount+1);
                            } else {
                                wordCounts.put(summaryUnigram,1);
                            }

                        }


                        if (localTitleBigrams.contains(summaryBigram)) {

                            localTitleBigrams.remove(summaryBigram);

                            if (wordCounts.containsKey(summaryBigram)) {
                                int previousCount = wordCounts.get(summaryBigram);
                                wordCounts.put(summaryBigram,previousCount+1);

                            } else {
                                wordCounts.put(summaryBigram,1);
                            }
                        }


                    }

                    overlapCountMaps.add(wordCounts);

                }

            }
        }

        return overlapCountMaps;

    }


    public static List<Map<String,Integer>> extractNeOverlapFeatures(Document document) {

        List<Map<String,Integer>> overlapCountMaps = new LinkedList<Map<String, Integer>>();

        NodeList summaries = document.getElementsByTagName("Summary");
        for(int s = 0; s < summaries.getLength(); s++) {

            LinkedList<String> titleNeWords = new LinkedList<String>();

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
                        titleNeWords.add((lemma1+" "+neTag).intern());

                    }
                }
            }

            NodeList sentences = summaryElement.getElementsByTagName("Sentence");
            for(int sentIdx = 0; sentIdx < sentences.getLength(); sentIdx++) {

                Element sentence = (Element) sentences.item(sentIdx);
                LinkedList<String> localTitleNeWords = new LinkedList<String>(titleNeWords);
                Map<String,Integer> wordCounts = new TreeMap<String, Integer>();


                NodeList summaryWords = sentence.getElementsByTagName("Word");
                for(int w = 0; w < summaryWords.getLength();w++) {
                    Element w1 = (Element)summaryWords.item(w);
                    String neTag = w1.getElementsByTagName("Ne").item(0).getTextContent();

                    if (!neTag.equals("O")) {

                        String lemma1 = w1.getElementsByTagName("Lemma").item(0).getTextContent();
                        lemma1 = StringEscapeUtils.unescapeXml(lemma1);
                        String summaryNeWord = (lemma1+" "+neTag).intern();

                        if (localTitleNeWords.contains(summaryNeWord)) {

                            localTitleNeWords.remove(summaryNeWord);

                            if (wordCounts.containsKey(summaryNeWord)) {
                                int previousCount = wordCounts.get(summaryNeWord);
                                wordCounts.put(summaryNeWord,previousCount+1);

                            } else {
                                wordCounts.put(summaryNeWord,1);
                            }

                        }

                    }
                }

                overlapCountMaps.add(wordCounts);
            }



        }

        return overlapCountMaps;
    }


}
