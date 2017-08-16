package com.yanbao.core.page;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.yanbao.util.DateTimeUtil;

/**
 * 
 * @author zhuzh
 * @date 2016年11月30日
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 28122347460961243L;
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 当前页 */
	private Integer pageNo = 1;
	/** 每页大小 */
	private Integer pageSize = 10;
	/** 每页大小 */
	public static Integer bigPageSize = 2000;
	/** 查询开始时间 */
	private String startTime;
	/** 查询结束时间 */
	private String endTime;
	/** 查询开始记录索引 */
	private Integer startRow;
	/** 时间排序 */
	private Integer timeSort;
	/** 价格排序 */
	private Integer priceSort;
	
	
	private String fromTimeOne;
	
	private String stopTimeOne;
	
	

	public Date getFromTimeOne() {
		Date date=null;
		try {
			if(null==fromTimeOne||"".equals(fromTimeOne)){
				return date;
			}
			date = formatter.parse(fromTimeOne);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public void setFromTimeOne(String fromTimeOne) {
		this.fromTimeOne = fromTimeOne;
	}

	public Date getStopTimeOne() {
		Date date=null;
		try {
			if(null==stopTimeOne||"".equals(stopTimeOne)){
				return date;
			}
			date = formatter.parse(stopTimeOne);
			/*Calendar   calendar   =   new   GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		    date=calendar.getTime();   //这个时间就是日期往后推一天的结果
*/		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public void setStopTimeOne(String stopTimeOne) {
		this.stopTimeOne = stopTimeOne;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if (pageNo == null || pageNo < 1) {
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null || pageSize < 1) {
			pageSize = 10;
		}
		this.pageSize = pageSize;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getStartRow() {
		if (pageNo == null || pageNo < 1) {
			startRow = 1;
		} else {
			startRow = (pageNo - 1) * pageSize;
		}
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getTimeSort() {
		return timeSort;
	}

	public void setTimeSort(Integer timeSort) {
		this.timeSort = timeSort;
	}

	public Integer getPriceSort() {
		return priceSort;
	}

	public void setPriceSort(Integer priceSort) {
		this.priceSort = priceSort;
	}

}
