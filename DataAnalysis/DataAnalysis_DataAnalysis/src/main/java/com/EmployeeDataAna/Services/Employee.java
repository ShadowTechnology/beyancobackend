package com.EmployeeDataAna.Services;

public class Employee {
    public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionStatus() {
		return positionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}
	public String getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getTimecardHours() {
		return timecardHours;
	}
	public void setTimecardHours(String timecardHours) {
		this.timecardHours = timecardHours;
	}
	public String getPayCycleStartDate() {
		return payCycleStartDate;
	}
	public void setPayCycleStartDate(String payCycleStartDate) {
		this.payCycleStartDate = payCycleStartDate;
	}
	public String getPayCycleEndDate() {
		return payCycleEndDate;
	}
	public void setPayCycleEndDate(String payCycleEndDate) {
		this.payCycleEndDate = payCycleEndDate;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	private String positionId;
    private String positionStatus;
    private String timeIn;
    private String timeOut;
    private String timecardHours;
    private String payCycleStartDate;
    private String payCycleEndDate;
    private String employeeName;
    private String fileNumber;

}


