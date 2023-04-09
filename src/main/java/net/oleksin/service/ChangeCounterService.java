package net.oleksin.service;

import net.oleksin.model.CsvRow;

import java.util.List;

public interface ChangeCounterService {
    void countChange(List<CsvRow> csvRows);
}
