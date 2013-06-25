package edu.columbia.cs.nbtk.summary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/21/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SummaryCategory {
    US("U.S."),
    WORLD("World"),
    FINANCE("Finance"),
    ENTERTAINMENT("Entertainment"),
    SCI_TECH("Science/Technology"),
    SPORTS("Sports"),
    NONE("N/A");

    private String value;
    private static Map<String,SummaryCategory> textToCategory = new HashMap<String, SummaryCategory>();
    static {
        textToCategory.put(US.getText(),US);
        textToCategory.put(WORLD.getText(),WORLD);
        textToCategory.put(FINANCE.getText(),FINANCE);
        textToCategory.put(ENTERTAINMENT.getText(), ENTERTAINMENT);
        textToCategory.put(SCI_TECH.getText(),SCI_TECH);
        textToCategory.put(SPORTS.getText(),SPORTS);
    }

    private SummaryCategory(String value) {
        this.value = value.intern();
    }
    public String getText() {
        return value;
    }

    public static SummaryCategory getCategory(String categoryText) {
        if (textToCategory.containsKey(categoryText))
            return textToCategory.get(categoryText);
        return NONE;
    }



    @Override
    public String toString() {
        return value;
    }

}
