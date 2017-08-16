package com.yanbao.vo;

public class GoodsTransferVo {
	//页数
	private Integer pageNum;
	//每页条数
	private Integer pageSize;
	//总页数
	private Integer pageCount;
	//原分类ID
	private String originalType;
	//新分类ID
	private String newType;
	//全部选择
	private Integer selectAll;
	//选择商品Id集合  下划线分割
	private String selectInput;
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public String getOriginalType() {
		return originalType;
	}
	public void setOriginalType(String originalType) {
		this.originalType = originalType;
	}
	public String getNewType() {
		return newType;
	}
	public void setNewType(String newType) {
		this.newType = newType;
	}
	public Integer getSelectAll() {
		return selectAll;
	}
	public void setSelectAll(Integer selectAll) {
		this.selectAll = selectAll;
	}
	public String getSelectInput() {
		return selectInput;
	}
	public void setSelectInput(String selectInput) {
		this.selectInput = selectInput;
	}
	
}
