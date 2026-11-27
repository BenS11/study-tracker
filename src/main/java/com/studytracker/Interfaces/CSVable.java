package com.studytracker.Interfaces;

/**
 * Indicates an object can be turned into and stored as a CSV.
 * Classes implementing this should have a static method to construct from a CSV
 */
public interface CSVable {
    String toCSV();
}