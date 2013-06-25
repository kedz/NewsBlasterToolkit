package edu.columbia.cs.nbtk.summary;

import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/21/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummaryFragment {


    private final File sourceFile;
    private final URL sourceURL;
    private final String fragmentText;

    public SummaryFragment(File sourceFile, URL sourceUrl, String fragmentText) {
        this.sourceFile = sourceFile;
        this.sourceURL = sourceUrl;
        this.fragmentText = fragmentText;
    }

    public File getSourceFile() {return sourceFile;}
    public URL getSourceURL() {return sourceURL;}
    public String getFragmentText() {return fragmentText;}

}
