package com.EmployeeDataAna.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExcelFileService {
    @Value("${excel.file.path}")
    private String excelFilePath;

    public List<Employee> readExcelData() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        List<Employee> employeeDataList = new ArrayList<>();

        for (Row row : sheet) {
            Employee employeeData = new Employee();

            // Assuming the order of columns in the Excel file matches the order in the EmployeeData class

            // Assuming positionId is an integer column
            String positionId = row.getCell(0).getStringCellValue();
            employeeData.setPositionId(positionId);

            // Assuming positionStatus is a string column
            String positionStatus = row.getCell(1).getStringCellValue();
            employeeData.setPositionStatus(positionStatus);

            // Assuming time is a string column
            
            XSSFCell timecell;
            String time=null;
            
            timecell = (XSSFCell) row.getCell(2);
            if (null != timecell.getCellType()) {
                switch (timecell.getCellType()) {
                    case BLANK:
                    	employeeData.setTimeIn(time);
                        break;
                    case NUMERIC:
                    	SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                        Date dates = timecell.getDateCellValue();
                        time = inputFormat.format(dates);
                        employeeData.setTimeIn(time);
                        break;
                    case FORMULA:
                    	employeeData.setTimeIn(time);
                        break;
                    case STRING:
                    	time = row.getCell(2).getStringCellValue();
                        employeeData.setTimeIn(time);
                        break;
                    default:
                        break;
                }
            }

            // Assuming timeOut is a string column
            
            XSSFCell timeoutcell;
            String timeOut=null;
            
            timeoutcell = (XSSFCell) row.getCell(3);
            if (null != timeoutcell.getCellType()) {
                switch (timeoutcell.getCellType()) {
                    case BLANK:
                    	employeeData.setTimeOut(timeOut);
                        break;
                    case NUMERIC:
                    	SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                        Date dates = timeoutcell.getDateCellValue();
                        timeOut = inputFormat.format(dates);
                        employeeData.setTimeOut(timeOut);
                        break;
                    case FORMULA:
                    	employeeData.setTimeOut(timeOut);
                        break;
                    case STRING:
                    	timeOut = row.getCell(3).getStringCellValue();
                        employeeData.setTimeOut(timeOut);
                        break;
                    default:
                        break;
                }
            }


            // Assuming timecardHours is a string column
            
            XSSFCell timecardHoursCell;
            String timecardHours;

            timecardHoursCell=(XSSFCell) row.getCell(4);
            if (timecardHoursCell == null || timecardHoursCell.getCellType() == CellType.BLANK) {
            	timecardHours = "";
            	employeeData.setTimecardHours(timecardHours);
          } else if (timecardHoursCell.getCellType() == CellType.STRING) {
        	  timecardHours = row.getCell(4).getStringCellValue();
        	  employeeData.setTimecardHours(timecardHours);
          } else if (timecardHoursCell.getCellType() == CellType.NUMERIC) {
        	  timecardHours = String.valueOf(timecell.getNumericCellValue());
              employeeData.setTimecardHours(timecardHours);
          }

            // Assuming payCycleStartDate is a string column
            
            XSSFCell payCycleStartDatecell;
            String payCycleStartDate = null;
            
            payCycleStartDatecell = (XSSFCell) row.getCell(5);
            if (null != payCycleStartDatecell.getCellType()) {
                switch (payCycleStartDatecell.getCellType()) {
                    case BLANK:
                    	employeeData.setPayCycleStartDate(payCycleStartDate);
                        break;
                    case NUMERIC:
                    	SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date dates = payCycleStartDatecell.getDateCellValue();
                        payCycleStartDate = inputFormat.format(dates);
                        employeeData.setPayCycleStartDate(payCycleStartDate);
                        break;
                    case FORMULA:
                    	employeeData.setPayCycleStartDate(payCycleStartDate);
                        break;
                    case STRING:
                    	payCycleStartDate = row.getCell(5).getStringCellValue();
                        employeeData.setPayCycleStartDate(payCycleStartDate);
                        break;
                    default:
                        break;
                }
            }



            // Assuming payCycleEndDate is a string column
            
            XSSFCell payCycleEndDatecell;
            String payCycleEndDate = null;
            
            payCycleEndDatecell = (XSSFCell) row.getCell(6);
            if (null != payCycleEndDatecell.getCellType()) {
                switch (payCycleEndDatecell.getCellType()) {
                    case BLANK:
                    	employeeData.setPayCycleEndDate(payCycleEndDate);
                        break;
                    case NUMERIC:
                    	SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date dates = payCycleEndDatecell.getDateCellValue();
                        payCycleEndDate = inputFormat.format(dates);
                        employeeData.setPayCycleEndDate(payCycleEndDate);
                        break;
                    case FORMULA:
                    	employeeData.setPayCycleEndDate(payCycleEndDate);
                        break;
                    case STRING:
                    	payCycleEndDate = row.getCell(6).getStringCellValue();
                        employeeData.setPayCycleEndDate(payCycleEndDate);
                        break;
                    default:
                        break;
                }
            }

            // Assuming employeeName is a string column
            
            XSSFCell employeeNamecell;
            String employeeName = null;

            employeeNamecell=(XSSFCell) row.getCell(7);
            if (employeeNamecell == null || employeeNamecell.getCellType() == CellType.BLANK) {
            	employeeName = "";
            	employeeData.setEmployeeName(employeeName);
          } else if (employeeNamecell.getCellType() == CellType.STRING) {
        	  employeeName = row.getCell(7).getStringCellValue();
        	  employeeData.setEmployeeName(employeeName);
          } else if (employeeNamecell.getCellType() == CellType.NUMERIC) {
        	  employeeName = String.valueOf(timecell.getNumericCellValue());
              employeeData.setEmployeeName(employeeName);
          }

            // Assuming fileNumber is an integer column
            
            XSSFCell fileNumbercell;
            String fileNumber = null;

            fileNumbercell=(XSSFCell) row.getCell(8);
            if (fileNumbercell == null || fileNumbercell.getCellType() == CellType.BLANK) {
            	fileNumber = "";
            	employeeData.setFileNumber(fileNumber);
          } else if (fileNumbercell.getCellType() == CellType.STRING) {
        	  fileNumber = row.getCell(8).getStringCellValue();
        	  employeeData.setFileNumber(fileNumber);
          } else if (fileNumbercell.getCellType() == CellType.NUMERIC) {
        	  fileNumber = String.valueOf(timecell.getNumericCellValue());
              employeeData.setFileNumber(fileNumber);
          }

            // Add the employeeData object to the list
            employeeDataList.add(employeeData);
        }

        workbook.close();
        fileInputStream.close();

        return employeeDataList;
    }
}


