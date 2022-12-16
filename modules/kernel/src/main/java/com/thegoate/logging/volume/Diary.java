package com.thegoate.logging.volume;

public interface Diary {
    String mostRecentEntry();
    void writeEntry(String entry);
}
