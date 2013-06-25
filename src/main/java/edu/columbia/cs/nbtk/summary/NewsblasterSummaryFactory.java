package edu.columbia.cs.nbtk.summary;

import edu.columbia.cs.nbtk.util.NewsblasterMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/21/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsblasterSummaryFactory {

    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;


    public NewsblasterSummaryFactory() throws ParserConfigurationException {
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }

    public Collection<NewsblasterSummary> getSummariesFromTodayXmlFile(File todayXml) throws IOException, SAXException {





        InputStream inputStream= new FileInputStream(todayXml);
        Reader reader = new InputStreamReader(inputStream,"UTF-8");
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");

        Document document = dBuilder.parse(is);

        Collection<NewsblasterSummary> summaries = null;

        if(isOldFormat(document)) {
            summaries =  parseOldTodayXmlFormat(todayXml, document);
        } else {
             summaries = parseCurrentTodayXmlFormat(todayXml, document);
        }

        int numSum = document.getElementsByTagName("Summary").getLength();

        if (numSum!=summaries.size())  {
            System.out.println("Bad count: "+todayXml);
            System.exit(-1);
        }

        return summaries;
    }

    private boolean isOldFormat(Document document) {


        NodeList events = document.getElementsByTagName("Event");
        if (events.getLength()==0)
            return true;

        return false;

    }

    private Collection<NewsblasterSummary> parseOldTodayXmlFormat(File todayXml, Document document) throws MalformedURLException{

        List<NewsblasterSummary> summariesList = new ArrayList<NewsblasterSummary>();


        NodeList categories = document.getElementsByTagName("Category");
        for(int c = 0; c < categories.getLength(); c++) {

            SummaryCategory currentCategory =
                    SummaryCategory.getCategory(
                            ((Element) categories.item(c)).getAttributes().getNamedItem("name").getNodeValue()
                    );
            //System.out.println(currentCategory);

            NodeList clusters = ((Element)categories.item(c)).getElementsByTagName("Cluster");
            for(int e = 0; e < clusters.getLength(); e++) {

                Element clusterElement = (Element)clusters.item(e);
                String currentTitle = clusterElement.getAttributes().getNamedItem("title").getTextContent();
                currentTitle =  StringEscapeUtils.unescapeXml(currentTitle);

                //System.out.println(currentTitle);

                NodeList summaries = ((Element)clusters.item(e)).getElementsByTagName("Summary");
                for(int s = 0; s < summaries.getLength(); s++) {

                    Element summaryElement = (Element)summaries.item(s);
                    if (summaryElement.getAttributes().getNamedItem("generator").getNodeValue().equals("dems")) {

                        NodeList sentences = ((Element)summaries.item(s)).getElementsByTagName("Sentence");
                        List<SummaryFragment> summaryFragments = new ArrayList<SummaryFragment>(sentences.getLength());

                        for(int f = 0; f < sentences.getLength(); f++) {

                            Element fragmentElement = (Element)sentences.item(f);
                            String label = fragmentElement.getAttributes().getNamedItem("label").getNodeValue();
                            File sourceFile = new File("needs_to_be_implemented");
                            URL sourceUrl = new URL("http://needs_to_be_implemented");
                            String fragmentText = fragmentElement.getAttributes().getNamedItem("text").getNodeValue();
                            fragmentText = StringEscapeUtils.unescapeXml(fragmentText);

                            SummaryFragment summaryFragment = new SummaryFragment(sourceFile,sourceUrl,fragmentText);
                            summaryFragments.add(summaryFragment);

                        }

                        DateTime timestamp = NewsblasterMapper.getTodayXmlTimestamp(todayXml);
                        NewsblasterSummary newsblasterSummary =
                                new NewsblasterSummary(todayXml,timestamp, currentCategory, currentTitle, summaryFragments);
                        summariesList.add(newsblasterSummary);

                    } else {

                        NodeList sentences = ((Element)summaries.item(s)).getElementsByTagName("Sentence");
                        List<SummaryFragment> summaryFragments = new ArrayList<SummaryFragment>(sentences.getLength());

                        for(int f = 0; f < sentences.getLength(); f++) {

                            Element fragmentElement = (Element)sentences.item(f);
                            //String label = fragmentElement.getAttributes().getNamedItem("label").getNodeValue();
                            File sourceFile = new File("needs_to_be_implemented");
                            URL sourceUrl = new URL("http://needs_to_be_implemented");
                            String fragmentText = fragmentElement.getAttributes().getNamedItem("text").getNodeValue();
                            fragmentText = StringEscapeUtils.unescapeXml(fragmentText);

                            SummaryFragment summaryFragment = new SummaryFragment(sourceFile,sourceUrl,fragmentText);
                            summaryFragments.add(summaryFragment);

                        }

                        DateTime timestamp = NewsblasterMapper.getTodayXmlTimestamp(todayXml);
                        NewsblasterSummary newsblasterSummary =
                                new NewsblasterSummary(todayXml,timestamp, currentCategory, currentTitle, summaryFragments);
                        summariesList.add(newsblasterSummary);


                    }


                }


            }




        }

        return summariesList;

    }

    private Collection<NewsblasterSummary> parseCurrentTodayXmlFormat(File todayXml, Document document) throws MalformedURLException{


        List<NewsblasterSummary> summariesList = new ArrayList<NewsblasterSummary>();

        NodeList categories = document.getElementsByTagName("Category");
        for(int c = 0; c < categories.getLength(); c++) {

            SummaryCategory currentCategory =
                    SummaryCategory.getCategory(
                            ((Element) categories.item(c)).getAttributes().getNamedItem("name").getNodeValue()
                    );
            //System.out.println(currentCategory);

            NodeList events = ((Element)categories.item(c)).getElementsByTagName("Event");
            for(int e = 0; e < events.getLength(); e++) {

                Element clusterElement = (Element)events.item(e);
                String currentTitle = clusterElement.getAttributes().getNamedItem("title").getTextContent();
                currentTitle =  StringEscapeUtils.unescapeXml(currentTitle);

                //System.out.println(currentTitle);

                NodeList summaries = ((Element)events.item(e)).getElementsByTagName("Summary");
                for(int s = 0; s < summaries.getLength(); s++) {

                    Element summaryElement = (Element)summaries.item(s);

                    NodeList sentences = ((Element)summaries.item(s)).getElementsByTagName("Fragment");
                    List<SummaryFragment> summaryFragments = new ArrayList<SummaryFragment>(sentences.getLength());

                    for(int f = 0; f < sentences.getLength(); f++) {

                        Element fragmentElement = (Element)sentences.item(f);
                        String label = fragmentElement.getAttributes().getNamedItem("label").getNodeValue();
                        File sourceFile = new File("needs_to_be_implemented");
                        URL sourceUrl = new URL("http://needs_to_be_implemented");
                        String fragmentText = fragmentElement.getAttributes().getNamedItem("text").getNodeValue();
                        fragmentText = StringEscapeUtils.unescapeXml(fragmentText);

                        SummaryFragment summaryFragment = new SummaryFragment(sourceFile,sourceUrl,fragmentText);
                        summaryFragments.add(summaryFragment);

                    }

                    DateTime timestamp = NewsblasterMapper.getTodayXmlTimestamp(todayXml);
                    NewsblasterSummary newsblasterSummary =
                            new NewsblasterSummary(todayXml,timestamp, currentCategory, currentTitle, summaryFragments);
                    summariesList.add(newsblasterSummary);



                }


            }




        }

        return summariesList;

    }


}
