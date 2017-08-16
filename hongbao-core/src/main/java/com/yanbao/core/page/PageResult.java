package com.yanbao.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhuzh
 * @date 2016年11月30日
 */
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 2505311294797637364L;

	/** 当前页 */
	private Integer pageNo;
	/** 每页大小 */
	private Integer pageSize;
	/** 总共记录条数 */
	private Integer totalSize;
	/** 结果集 */
	private List<T> rows = new ArrayList<T>();

	public PageResult() {
		super();
	}

	public PageResult(Integer pageNo, Integer pageSize, Integer totalSize, List<T> rows) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		this.rows = rows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Integer getTotalPage(){
		if (totalSize==null||totalSize==0){
			return 0;
		}
		return totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1;
	}

}