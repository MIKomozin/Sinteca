package testTask;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class Main {
    //путь к папке где находятся файлы для считывания и записи
    public static final String PATH = "C:\\Users\\Максим\\Desktop\\Test\\";
    public static final String OUTPUT = PATH + "output.txt";
    public static final String INPUT = PATH + "input.txt";

    public static void main(String[] args) throws Exception {
        //проверим существование выходного файла. Если его нет, то создадим его
        if (!Files.exists(Paths.get(OUTPUT))) {
            Logger.getLogger("main").info("Выходного файла не существует. Создадим пустой файл с таким же именем");
            Files.createFile(Paths.get(OUTPUT));
        }

        //проверим существование входного файла. Если такого файла нет, то предупредим об этом
        if (!Files.exists(Paths.get(INPUT))) {
            throw new Exception("Входного файла не существует");
        }

        compareStringAndWrite(readFile(INPUT), OUTPUT);
    }

    public static Arrays readFile(String inputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        List<String> array_n = new ArrayList<>();
        List<String> array_m = new ArrayList<>();
        String line;
        boolean firstLine = true;//флаг первой строки
        int count = 0;//подсчет итераций
        int n = 0;
        while ((line = reader.readLine()) != null) {
            //первая строка число строк n
            if (firstLine) {
                n = Integer.parseInt(line);
                firstLine = false;
                continue;
            }
            if (count < n) {
                count++;//количество итераций для точного определения количества добавленных строк в массив
                array_n.add(line);
                continue;
            }
            if (count == n){
                count++;
                continue;
            }
            count++;
            array_m.add(line);
        }
        reader.close();
        Arrays arrays = new Arrays();
        //определяем массив большего размера
        if (array_n.size() >= array_m.size()) {
            arrays.setArray_Max(array_n);
            arrays.setArray_Min(array_m);
        } else  {
            arrays.setArray_Max(array_m);
            arrays.setArray_Min(array_n);
        }
        return arrays;
    }

    public static void compareStringAndWrite(Arrays arrays, String outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        Map<String, Integer> map = new HashMap<>();

        for (String line1 : arrays.getArray_Max()) {
            String[] words1 = line1.toLowerCase().split(" ");
            for (String line2 : arrays.getArray_Min()) {
                String[] words2 = line2.toLowerCase().split(" ");
                int count = checkWords(words1, words2);//количество совпадений между строками (количество одинаковых слов имеющихся в сравниваемых строках)
                map.put(line2, count);//получаем для строки line1 некую мапу, где ключ это строка с которой сравнивают, значение - это количество сопадений с данной строкой
            }
            String bestLine = null;//наиболее подходящая строка среди сравниваемых строк (с наибольшим количеством совпадений)
            int max = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    bestLine = entry.getKey();
                }
            }
            map.clear();
            if (bestLine == null) {
                writer.write(line1 + ":" + "?");
                writer.newLine();
            } else {
                writer.write(line1 + ":" + bestLine);
                writer.newLine();
            }
        }
        writer.close();
    }

    public static int checkWords(String[] words1, String[] words2) {
        int count = 0;//количество совпадений
        for (String word1: words1) {
            for (String word2: words2) {
                //берем слова больше длиной больше 2 для исключения большего кол-ва предлогов (в, на, по и т.д.)
                if (word1.length() > 2 && word2.length() > 2) {
                    if (word1.contains(word2) || word2.contains(word1)) {
                        count++;//есть совпадение добавляем 1
                    }
                }
            }
        }
        return count;
    }
}

