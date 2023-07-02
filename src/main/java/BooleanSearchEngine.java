import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> index; // обратный индекс


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        pdfsDir = new File("D:\\NETOLOGIA\\pcs-final-diplom\\pcs-final-diplom\\pdfs");
        File[] files = pdfsDir.listFiles();


        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                PdfReader reader = new PdfReader(file);
                PdfDocument doc = new PdfDocument(reader);
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                    PdfPage page = doc.getPage(i);
                    String text = PdfTextExtractor.getTextFromPage(page); // извлекаем текст со страницы
                    String[] wordsArray = text.split("\\P{IsAlphabetic}+");
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(wordsArray));// делим текст на слова \\P{IsAlphabetic}+ означает "любой символ, который не является буквой"


                    Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота. Пример мапы: (после=2, актуализация=1, последующей=2, рекламные=1)
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);  //freqs - словарь, где ключами являются уникальные слова из списка words, а значениями - количество вхождений каждого слова в тексте.
                    }


                    index = new HashMap<>();
                    for (var word : words) { // перебираем слова в массиве
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase(); // приведем слово к нижнему регистру
                        PageEntry pageEntry = new PageEntry(file.getName(), page.getDocument().getPageNumber(page), freqs.get(word));
                        List<PageEntry> pageEntryList = index.get(word);
                        if (pageEntryList == null) {
                            pageEntryList = new ArrayList<>();
                            index.put(word, pageEntryList);// добавляем pageEntryList в HashMap<>()
                        }
                        pageEntryList.add(pageEntry);
                        Collections.sort(pageEntryList);
                    }
//                    System.out.println(index);

                }
                doc.close();
                reader.close();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntries = index.get(word);
        if (pageEntries == null) {
            return Collections.emptyList();
        } else {
            return pageEntries;
        }
    }

}

