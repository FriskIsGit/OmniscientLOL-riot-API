package program.structs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableFormat{
    private final boolean centered;
    private final List<Column> columnList = new ArrayList<>();
    private final List<List<Object>> data = new ArrayList<>();
    private final List<Separator> separators = new ArrayList<>();

    class Column{
        String name;
        int width, floatPrecision = 1;
        boolean summarize = false; //strings will not be summarized
        public Column(String name){
            this.name = name;
            this.width = name.length() + 1;
        }
        public Column(String name, int minWidth){
            this.name = name;
            this.width = Math.max(name.length() + 1, minWidth);
        }

        @Override
        public String toString(){
            if(centered){
                return padCentered(name, width).toString();
            }
            return padToLength(name, width).toString();
        }
    }

    static class Separator{
        int index, summaryStart = -1;
        char separatingChr;
        boolean summary = false;
        public Separator(int index, char separatingChr){
            this.index = index;
            this.separatingChr = separatingChr;
        }
        public Separator(int index, int summaryStart){
            this.index = index;
            this.summary = true;
            this.summaryStart = summaryStart;
        }

    }
    public TableFormat(boolean centered){
        this.centered = centered;
    }
    public TableFormat(){
        centered = false;
    }

    public void addColumnDefinition(String name){
        Column column = new Column(name);
        columnList.add(column);
    }
    public void addColumnDefinition(String name, int minWidth){
        Column column = new Column(name, minWidth);
        columnList.add(column);
    }
    public void addColumnDefinition(String name, int minWidth, boolean summarize){
        Column column = new Column(name, minWidth);
        column.summarize = summarize;
        columnList.add(column);
    }
    public void addColumnDefinition(String name, boolean summarize){
        Column column = new Column(name);
        column.summarize = summarize;
        columnList.add(column);
    }
    public void writeEmptyRow(){
        separators.add(new Separator(data.size(), ' '));
        data.add(Collections.emptyList());
    }
    public void writeSeparatorRow(char fillChr){
        separators.add(new Separator(data.size(), fillChr));
        data.add(Collections.emptyList());
    }
    //summary of everything that comes before it
    public void writeSummaryRow(int summaryStartRow){
        if(summaryStartRow < 0 || summaryStartRow >= data.size()){
            throw new IndexOutOfBoundsException("Summary start row is out of bounds, given: " + summaryStartRow);
        }
        separators.add(new Separator(data.size(), summaryStartRow));
        data.add(Collections.emptyList());
    }

    //list is copied but may reference items inside of it
    public void writeToRow(List<Object> values){
        if(values.size() == 0){
            return;
        }
        if(values.size() != columnList.size()){
            throw new IndexOutOfBoundsException(values.size() + " given != " + columnList.size() + " declared");
        }
        List<Object> copy = new ArrayList<>(values.size());
        copy.addAll(values);
        data.add(copy);
    }
    public List<Object> getRow(int index){
        if(index >= data.size()){
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for list of size: " + data.size());
        }
        return data.get(index);
    }
    public void clearRows(){
        data.clear();
        separators.clear();
    }
    private static StringBuilder padToLength(String str, int length){
        StringBuilder padded = new StringBuilder(str);
        int diff = length - str.length();
        for (int i = 0; i < diff; i++){
            padded.append(' ');
        }
        return padded;
    }
    private static StringBuilder padCentered(String str, int width){
        StringBuilder padded = new StringBuilder();
        int diff = width - str.length();
        int half = diff/2;
        for (int i = 0; i < half; i++){
            padded.append(' ');
        }
        padded.append(str);
        for (int i = half; i < diff; i++){
            padded.append(' ');
        }
        return padded;
    }
    private static String doubleFormat(double val, int precision){
        return String.format("%." + precision + "f", val);
    }
    private static String floatFormat(float val, int precision){
        return String.format("%." + precision + "f", val);
    }

    private static StringBuilder line(int length, char chr){
        StringBuilder padded = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            padded.append(chr);
        }
        return padded;
    }

    @Override
    public String toString(){
        StringBuilder columnHeaders = new StringBuilder("\n");
        int columns = columnList.size();
        int totalWidth = 0;
        int[] widths = new int[columns];
        for (int i = 0; i < columns; i++){
            Column column = columnList.get(i);
            widths[i] = column.width;
            columnHeaders.append(column);
            if(i != columns -1){
                columnHeaders.append(' ');
            }
            totalWidth += widths[i];
        }
        StringBuilder str = new StringBuilder();
        str.append(line(totalWidth + columns, '—'));
        str.append(columnHeaders);
        str.append('\n');
        str.append(line(totalWidth + columns, '—'));
        str.append('\n');
        for (int i = 0; i < data.size(); i++){
            boolean skip = false;
            for (Separator separator : separators){
                if (separator.index == i){
                    skip = true;
                    if(separator.summary){
                        str.append(summaryImpl(separator.summaryStart, i, widths));
                    }else{
                        str.append(line(totalWidth + columns, separator.separatingChr)).append('\n');
                    }
                    break;
                }
            }
            if(skip)
                continue;

            List<Object> row = data.get(i);
            if (row.size() != columns){
                throw new IndexOutOfBoundsException("Num of elements in row at index " + i + " != num of declared columns");
            }
            int rowLen = row.size();
            for (int j = 0; j < rowLen; j++){
                Object obj = row.get(j);
                if(obj == null){
                    str.append(padToLength("", widths[j])).append(' ');
                    continue;
                }
                String el;
                Column column = columnList.get(j);
                if (obj instanceof String){
                    el = (String) obj;
                }else if (obj instanceof Double){
                    double number = (Double) obj;
                    el = doubleFormat(number, column.floatPrecision);
                }else if (obj instanceof Float){
                    float number = (Float) obj;
                    el =  floatFormat(number, column.floatPrecision);
                }
                else{
                    el = obj.toString();
                }

                if (centered){
                    str.append(padCentered(el, widths[j]));
                }else{
                    str.append(padToLength(el, widths[j]));
                }

                if (j != rowLen - 1){
                    str.append(' ');
                }
            }
            str.append('\n');
        }
        return str.toString();
        //return columnList.toString() + '\n' + data;
    }

    private StringBuilder summaryImpl(int startIndex, int terminatingIndex, int[] widths){
        StringBuilder str = new StringBuilder();
        int columns = columnList.size();
        Object[] summary = new Object[columns];
        for (int i = startIndex; i < terminatingIndex; i++){
            boolean skip = false;
            for (Separator separator : separators){
                //separators are ignored (not counted)
                if (separator.index == i){
                    skip = true;
                    break;
                }
            }
            if (skip)
                continue;
            //add up
            List<Object> row = data.get(i);
            int rowLen = row.size();
            for (int j = 0; j < rowLen; j++){
                Object obj = row.get(j);
                Column column = columnList.get(j);
                if (obj instanceof Double){
                    double number = (Double) obj;
                    if (column.summarize)
                        updateSummaryForDouble(summary, j, number);
                }else if (obj instanceof Float){
                    double number = (Float) obj;
                    if (column.summarize)
                        updateSummaryForDouble(summary, j, number);
                }else if (obj instanceof Integer){
                    int number = (Integer) obj;
                    if (column.summarize)
                        updateSummaryForInteger(summary, j, number);
                }
            }
        }

        // formatted display
        for (int i = 0; i < columns; i++){
            Object obj = summary[i];
            Column column = columnList.get(i);
            if (obj == null || !column.summarize){
                str.append(padToLength("", widths[i])).append(' ');
                continue;
            }
            String el;
            if (obj instanceof Double){
                double number = (Double) obj;
                el = doubleFormat(number, column.floatPrecision);
            }else if (obj instanceof Float){
                float number = (Float) obj;
                el = floatFormat(number, column.floatPrecision);
            }else{
                el = obj.toString();
            }
            if (centered){
                str.append(padCentered(el, widths[i]));
            }else{
                str.append(padToLength(el, widths[i]));
            }
            if (i != summary.length - 1){
                str.append(' ');
            }
        }
        str.append('\n');
        return str;
    }

    private static void updateSummaryForDouble(Object[] summary, int index, double val){
        double existing = summary[index] == null ? 0d : (double)summary[index];
        summary[index] = existing + val;
    }

    private static void updateSummaryForInteger(Object[] summary, int index, int val){
        int existing = summary[index] == null ? 0 : (int)summary[index];
        summary[index] = existing + val;
    }
}
