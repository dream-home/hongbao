package com.yanbao.core.page;

/**
 * Created by summer on 2016-12-02:09:57;
 */
public class DBPage {

    private static final int default_page = 1;
    private static final int default_page_size = 20;
    private static final int default_total_row = 2000;

    private int page = default_page;
    private int pageSize = default_page_size;
    private int totalRow = default_total_row;

    public static DBPage page(int pageNo, int pageSize) {
        DBPage dbPage = new DBPage();
        dbPage.setPage(pageNo);
        dbPage.setTotalRow(Integer.MAX_VALUE);
        dbPage.setPageSize(pageSize);
        return dbPage;
    }

    public static DBPage maxPage() {
        DBPage dbPage = new DBPage();
        dbPage.setPage(1);
        dbPage.setTotalRow(Integer.MAX_VALUE);
        dbPage.setPageSize(Integer.MAX_VALUE);
        return dbPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = (page < 1) ? default_page : page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = (pageSize < 1) ? default_page_size : pageSize;
    }

    private int _getStartRow() {
        return (getPage() - 1) * getPageSize();
    }

    public int getStartRow() {
        return _getStartRow();
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalPage() {
        int totalRow1 = getTotalRow();
        int pageSize1 = getPageSize();
        int page = totalRow1 / pageSize1;
        page += (totalRow1 % pageSize1 == 0 ? 0 : 1);

        return page;
    }

    public static void main(String[] args) {
        DBPage dbPage = DBPage.page(4, 20);
        dbPage.setTotalRow(28);
        System.out.println(dbPage.getStartRow());
    }

}
