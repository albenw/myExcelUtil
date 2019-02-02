package com.albenw.excel.base.listener;

import java.util.List;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
public interface ReadEventListener<T> {

    void readRow(T rowData);

    void readBatch(List<T> rowDatas);

    void readFinished();

}
