package edu.columbia.cs.nbtk.summary;

import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/21/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsblasterSummary {


    private final File todayXml;
    private final DateTime timestamp;
    private final SummaryCategory category;
    private final String title;
    private final List<SummaryFragment> summaryFragments;

    private String summaryText;

    public NewsblasterSummary(File todayXml, DateTime timestamp, SummaryCategory category, String title, List<SummaryFragment> summaryFragments) {
        this.todayXml = todayXml;
        this.timestamp = timestamp;
        this.category = category;
        this.title = title;
        this.summaryFragments = summaryFragments;
    }

    public String getTitle() {return title;}

    public String getSummaryText() {
        if (summaryText==null) {
            StringBuilder buffer = new StringBuilder();
            for(SummaryFragment fragment : summaryFragments)  {
                buffer.append(fragment.getFragmentText());
            }

            summaryText = buffer.toString();
            buffer = null;
        }
        return summaryText;
    }

}
