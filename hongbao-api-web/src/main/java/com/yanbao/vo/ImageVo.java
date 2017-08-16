package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年1月12日
 */
public class ImageVo implements Serializable {

	private static final long serialVersionUID = 2319684706069414795L;

	/** 图片Id */
	private String id;
	/** 图片路径 */
	private String path;
	/** 操作:0:新增，1：删除 */
	private Integer option;
	/** 是否设置封面:0:否，1：是 */
	private Integer isDefault;
	/** 图片关联Id */
	private String fileLinkId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getOption() {
		return option;
	}

	public void setOption(Integer option) {
		this.option = option;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getFileLinkId() {
		return fileLinkId;
	}

	public void setFileLinkId(String fileLinkId) {
		this.fileLinkId = fileLinkId;
	}

}
