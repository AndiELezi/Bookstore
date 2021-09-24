package com.mycompany.bookstore.service;

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
import java.util.concurrent.atomic.AtomicInteger;

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
        createSecondSheet();
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
        var lambdaContext = new Object() {
            int rowIndex = 1;
        }; //to access it inside lambda

        CellStyle bodyStyle = xssfWorkbook.createCellStyle();
        CellStyle dateStyle = xssfWorkbook.createCellStyle();
        CreationHelper creationHelper = xssfWorkbook.getCreationHelper();
        XSSFFont bodyFont = xssfWorkbook.createFont();
        bodyFont.setFontHeight(BODY_SIZE);
        bodyStyle.setFont(bodyFont);
        dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mm-yyyy"));
        dateStyle.setFont(bodyFont);
        bookRepository.findAll().forEach(book -> {
            Row bodyRow = bookSheet.createRow(lambdaContext.rowIndex);
            addCell(bodyRow, 0, book.getIsbn(), bodyStyle);
            addCell(bodyRow, 1, book.getTitle(), bodyStyle);
            addCell(bodyRow, 2, book.getDescription(), bodyStyle);
            addCell(bodyRow, 3, book.getPublicationDate(), dateStyle);
            addCell(bodyRow, 4, book.getCategory().getName(), bodyStyle);
            addCell(bodyRow, 5, book.getSeries().getName(), bodyStyle);
            addCell(bodyRow, 6, book.getPrice(), bodyStyle);
            addCell(bodyRow, 7, book.isActive(), bodyStyle);
            lambdaContext.rowIndex++;
        });
    }

    private void createSecondSheet() {
        bookSeriesSheet = xssfWorkbook.createSheet("Book Series");
        createSecondSheetHeader();
        createSecondSheetBody();
    }

    private void createSecondSheetHeader() {
        var lambdaContext = new Object() {
            int colNumb = 0;
        }; //to use and modify inside lambda
        Row header = bookSeriesSheet.createRow(0);
        CellStyle headerStyle = xssfWorkbook.createCellStyle();
        XSSFFont headerFont = xssfWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(HEADER_SIZE);
        headerStyle.setFont(headerFont);
        bookSeriesRepository.findAll().forEach(bookSeries -> {
            addCell(header, lambdaContext.colNumb, bookSeries.getName(), headerStyle);
            lambdaContext.colNumb++;
        });
    }

    private void createSecondSheetBody() {
        var lambdaRowContext = new Object() {
            int maxRowIndex = 0;
        };
        var lambdaColContext = new Object() {
            int colNo = 0;
        };
        CellStyle bodyStyle = xssfWorkbook.createCellStyle();
        XSSFFont bodyFont = xssfWorkbook.createFont();
        bodyFont.setFontHeight(BODY_SIZE);
        bodyStyle.setFont(bodyFont);
        bookSeriesRepository.findAll().forEach(bookSeries -> {
            AtomicInteger currentRowIndex = new AtomicInteger(1); //to us inside nested lambda
            bookSeries.getBooks().forEach(book -> {
                Row currentRow;
                if (currentRowIndex.get() > lambdaRowContext.maxRowIndex) {
                    lambdaRowContext.maxRowIndex = currentRowIndex.get();
                    currentRow = bookSeriesSheet.createRow(currentRowIndex.get());
                } else {
                    currentRow = bookSeriesSheet.getRow(currentRowIndex.get());
                }
                addCell(currentRow, lambdaColContext.colNo, book.getTitle(), bodyStyle);
                currentRowIndex.getAndIncrement();
            });
            lambdaColContext.colNo++;
        });
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
