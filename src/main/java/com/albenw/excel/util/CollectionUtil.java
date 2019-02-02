package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.base.IndexingField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author alben.wong
 * @date 2019-01-04.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public static boolean isNotEmpty(Collection coll) {
        return !CollectionUtil.isEmpty(coll);
    }

    /**
     * 根据annotation的 index 排序字段
     *
     * @param clazz
     * @return
     */
    public static List<IndexingField> sortImportFieldByIndex(Class<?> clazz){
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<IndexingField> fields = new ArrayList<IndexingField>();
        List<IndexingField> annoNullFields = new ArrayList<IndexingField>();
        for (Field field : fieldsArr) {
            ImportField ec = AnnotationUtil.getImportColumn(field);
            if (ec == null) {
                continue;
            }
            int index = ec.index();
            fields.add(new IndexingField(field, index));
        }
        fields.addAll(annoNullFields);

        return fields;
    }

    /**
     * 根据annotation的 index 排序字段
     *
     * @param clazz
     * @return
     */
    public static List<IndexingField> sortExportFieldByIndex(Class<?> clazz){
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<IndexingField> fields = new ArrayList<>();
        List<IndexingField> annoNullFields = new ArrayList<>();
        for (Field field : fieldsArr) {
            ExportField ec = AnnotationUtil.getExportColumn(field);
            if (ec == null) {
                continue;
            }
            int index = ec.index();
            fields.add(new IndexingField(field, index));
        }
        fields.addAll(annoNullFields);
        sortByIndex(fields);
        return fields;
    }

    private static void sortByIndex(List<IndexingField> list){
        Collections.sort(list, new Comparator<IndexingField>() {
            @Override
            public int compare(IndexingField o1, IndexingField o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
    }

}
