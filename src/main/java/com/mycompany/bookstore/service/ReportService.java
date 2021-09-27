package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookSeries;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.repository.BookSeriesRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Service class for managing Reports.
 */
@Service
public class ReportService {
    private final static int HEADER_SIZE = 14;
    private final static int BODY_SIZE = 12;

    private final BookRepository bookRepository;
    private final BookSeriesRepository bookSeriesRepository;
    private final XSSFWorkbook xssfWorkbook;
    private XSSFSheet bookSheet;
    private XSSFSheet bookSeriesSheet;

    public ReportService(BookRepository bookRepository, BookSeriesRepository bookSeriesRepository) {
        this.bookRepository = bookRepository;
        this.bookSeriesRepository = bookSeriesRepository;
        this.xssfWorkbook = new XSSFWorkbook();
    }

    public void exportToExel(HttpServletResponse httpServletResponse) throws IOException {
        createFirstSheet();
        createSecondSheet(bookSeriesRepository.findAll()); // to avoid calling same data from database
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        xssfWorkbook.write(servletOutputStream);
    }


    private void createFirstSheet() {
        bookSheet = xssfWorkbook.createSheet("Books");
        createFirstSheetHeader();
        createFirstSheetBody();
    }

    private void createFirstSheetHeader() {
        Row header = bookSheet.createRow(0);
        CellStyle headerStyle = xssfWorkbook.createCellStyle();
        XSSFFont headerFont = xssfWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(HEADER_SIZE);
        headerStyle.setFont(headerFont);
        addCell(header, 0, "ISBN", headerStyle);
        addCell(header, 1, "Title", headerStyle);
        addCell(header, 2, "Description", headerStyle);
        addCell(header, 3, "Publication date", headerStyle);
        addCell(header, 4, "Category", headerStyle);
        addCell(header, 5, "Series", headerStyle);
        addCell(header, 6, "Price", headerStyle);
        addCell(header, 7, "Active", headerStyle);
    }

    private void createFirstSheetBody() {
        int rowIndex = 1;

        CellStyle bodyStyle = xssfWorkbook.createCellStyle();
        CellStyle dateStyle = xssfWorkbook.createCellStyle();
        CreationHelper creationHelper = xssfWorkbook.getCreationHelper();
        XSSFFont bodyFont = xssfWorkbook.createFont();
        bodyFont.setFontHeight(BODY_SIZE);
        bodyStyle.setFont(bodyFont);
        dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy"));
        dateStyle.setFont(bodyFont);

        for (Book book : bookRepository.findAll()) {
            Row bodyRow = bookSheet.createRow(rowIndex);
            addCell(bodyRow, 0, book.getIsbn(), bodyStyle);
            addCell(bodyRow, 1, book.getTitle(), bodyStyle);
            addCell(bodyRow, 2, book.getDescription(), bodyStyle);
            addCell(bodyRow, 3, book.getPublicationDate(), dateStyle);
            addCell(bodyRow, 4, book.getCategory().getName(), bodyStyle);
            var bookSeries = book.getSeries() == null ? "Null" : book.getSeries().getName();
            addCell(bodyRow, 5, bookSeries, bodyStyle);
            addCell(bodyRow, 6, book.getPrice(), bodyStyle);
            addCell(bodyRow, 7, book.isActive(), bodyStyle);
            rowIndex++;
        }
    }

    private void createSecondSheet(List<BookSeries> bookSeriesList) {
        bookSeriesSheet = xssfWorkbook.createSheet("Book Series");
        createSecondSheetHeader(bookSeriesList);
        createSecondSheetBody(bookSeriesList);
    }

    private void createSecondSheetHeader(List<BookSeries> bookSeriesList) {
        int colNumb = 0;
        Row header = bookSeriesSheet.createRow(0);
        CellStyle headerStyle = xssfWorkbook.createCellStyle();
        XSSFFont headerFont = xssfWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(HEADER_SIZE);
        headerStyle.setFont(headerFont);

        for (BookSeries bookSeries : bookSeriesList) {
            addCell(header, colNumb, bookSeries.getName(), headerStyle);
            colNumb++;
        }

    }

    private void createSecondSheetBody(List<BookSeries> bookSeriesList) {
        int maxRowIndex = 0;
        int colNo = 0;
        CellStyle bodyStyle = xssfWorkbook.createCellStyle();
        XSSFFont bodyFont = xssfWorkbook.createFont();
        bodyFont.setFontHeight(BODY_SIZE);
        bodyStyle.setFont(bodyFont);
        for (BookSeries bookSeries : bookSeriesList) {
            int currentRowIndex = 1;
            for (Book book : bookSeries.getBooks()) {
                Row currentRow;
                if (currentRowIndex > maxRowIndex) {
                    maxRowIndex = currentRowIndex;
                    currentRow = bookSeriesSheet.createRow(currentRowIndex);
                } else {
                    currentRow = bookSeriesSheet.getRow(currentRowIndex);
                }
                addCell(currentRow, colNo, book.getTitle(), bodyStyle);
                currentRowIndex++;
            }
            colNo++;
        }
    }

    private void addCell(Row row, int col, Object value, CellStyle style) {
        bookSheet.autoSizeColumn(col);
        if (bookSeriesSheet != null) {
            bookSeriesSheet.autoSizeColumn(col);
        }
        Cell cell = row.createCell(col);
        if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            java.util.Date publicationDate = new java.util.Date(((Date) value).getTime());
            cell.setCellValue(publicationDate);
        }
        cell.setCellStyle(style);
    }

}
