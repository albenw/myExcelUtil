package com.albenw.excel.base.listener;

import java.util.List;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
public interface ReadEventListener<T> {

    boolean readRow(T rowData);

    boolean readBatch(List<T> rowDatas);

    void readFinished();

    boolean parseException(int rowNum, int colNum, T rowData, Exception e);

}
