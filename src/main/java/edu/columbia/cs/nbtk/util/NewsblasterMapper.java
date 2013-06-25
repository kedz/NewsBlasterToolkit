package edu.columbia.cs.nbtk.util;



import edu.columbia.cs.nbtk.summary.NewsblasterSummary;
import edu.columbia.cs.nbtk.summary.NewsblasterSummaryFactory;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/20/13
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsblasterMapper {

    private static Map<File,DateTime> todayToDateGenerated = new HashMap<File, DateTime>();
    private static Map<DateTime,Set<File>> summariesByYear = new TreeMap<DateTime, Set<File>>();
    private static Map<DateTime,Set<File>> summariesByYearMonth = new TreeMap<DateTime, Set<File>>();
    private static Map<DateTime,Set<File>> summariesByYearMonthDay = new TreeMap<DateTime, Set<File>>();

    private static Set<File> allSummaries = new TreeSet<File>();

    private static List<File>  knownLocations = new ArrayList<File>();
    //                                                   (YEAR)        (MONTH)       (DAY)          (HOUR)          (MIN)        (SEC)
    private static Pattern datePattern = Pattern.compile("(\\d{4})[^\\d](\\d{2})[^\\d](\\d{2})[^\\d](\\d{2})[^\\d](\\d{2})[^\\d](\\d{2})");


    private NewsblasterMapper() {}

    public static void mapDirectory(File directory) {

        knownLocations.add(directory);

        File[] fileList = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        for(int i = 0; i < fileList.length; i++) {

            String dirName = fileList[i].getName();
            Matcher m = datePattern.matcher(dirName);
            if (m.find()) {

                File todayXml = findTodayXml(fileList[i]);
                if (todayXml!=null) {

                    int year = Integer.parseInt(m.group(1));
                    int month = Integer.parseInt(m.group(2));
                    int day = Integer.parseInt(m.group(3));

                    int hour = Integer.parseInt(m.group(4));
                    int min = Integer.parseInt(m.group(5));
                    int sec = Integer.parseInt(m.group(6));

                    DateTime timestamp = new DateTime(year,month,day,hour,min,sec);
                    DateTime yearStamp = new DateTime(year,1,1,0,0);
                    DateTime yearMonthStamp = new DateTime(year,month,1,0,0,0);
                    DateTime yearMonthDayStamp = new DateTime(year,month,day,0,0,0);

                    if (!summariesByYear.containsKey(yearStamp))
                        summariesByYear.put(yearStamp, new TreeSet<File>());
                    summariesByYear.get(yearStamp).add(todayXml);

                    if (!summariesByYearMonth.containsKey(yearMonthStamp))
                        summariesByYearMonth.put(yearMonthStamp, new TreeSet<File>());
                    summariesByYearMonth.get(yearMonthStamp).add(todayXml);

                    if (!summariesByYearMonthDay.containsKey(yearMonthDayStamp))
                        summariesByYearMonthDay.put(yearMonthDayStamp, new TreeSet<File>());
                    summariesByYearMonthDay.get(yearMonthDayStamp).add(todayXml);

                    todayToDateGenerated.put(todayXml,timestamp);
                    allSummaries.add(todayXml);
                }
            }

        }
    }


    private static File findTodayXml(File topDir) {

        String potentialName = topDir.toString()+File.separator+topDir.getName()+".xml";
        File potentialTodayXml = new File(potentialName);
        if (potentialTodayXml.exists())
            return potentialTodayXml;

        potentialName = topDir.toString()+File.separator+"data"+File.separator+"today.xml";
        potentialTodayXml = new File(potentialName);
        if (potentialTodayXml.exists())
            return potentialTodayXml;

        System.err.println("Warning: no today.xml found in newsblaster dir: "+topDir.toString());
        return null;
    }

    public static Collection<File> getAllSummaryXml() {return allSummaries;}

    public static Collection<File> getSummaryXmlFromYear(int year) {
        DateTime keyDate = new DateTime(year,1,1,0,0,0);
        if (summariesByYear.containsKey(keyDate))
            return summariesByYear.get(keyDate);

        else return new HashSet<File>();
    }

    public static DateTime getTodayXmlTimestamp(File todayXml) {
        if (todayToDateGenerated.containsKey(todayXml))
            return todayToDateGenerated.get(todayXml);
        return null;
    }

    public static Collection<File> getSummaryXmlOnOrAfterDate(DateTime onOrAfterThisDate) {

        TreeSet<File> summaryFiles = new TreeSet<File>();

        for(DateTime date : summariesByYearMonthDay.keySet()) {

            if (date.compareTo(onOrAfterThisDate)>=0) {
                summaryFiles.addAll(summariesByYearMonthDay.get(date));
            }

        }

        return summaryFiles;
    }

    public static void main(String[] args) {

        try {
        NBConfig nbConfig = NBConfig.getInstance();

        //NewsblasterMapper nbMapper = new NewsblasterMapper();
        NewsblasterMapper.mapDirectory(new File(nbConfig.getProperty("nb.archive.dir")));
        NewsblasterSummaryFactory factory = new NewsblasterSummaryFactory();

        DateTime year = new DateTime(2009,1,1,1,0,0);
        for(File file : NewsblasterMapper.getSummaryXmlOnOrAfterDate(year) ) {
            System.out.println(file);
            Collection<NewsblasterSummary> summaries = factory.getSummariesFromTodayXmlFile(file);


        }

        } catch (Exception e) {}

    }

}
