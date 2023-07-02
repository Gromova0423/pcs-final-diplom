public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;


    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

//    public PageEntry(){
//        this.pdfName = getPdfName();
//        this.page  = getPage();
//        this.count = getCount();
//    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }

    @Override
    public int compareTo(PageEntry o) {
        if (o.count > count) {
            return 1; // возвращаем положительное число, чтобы отсортировать текущий объект после объекта o
        } else if (o.count < count) {
            return -1; // возвращаем отрицательное число, чтобы отсортировать текущий объект перед объектом o
        }
        return 0; // возвращаем ноль, если значения count равны
    }


}
