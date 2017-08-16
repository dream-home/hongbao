package com.yanbao.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情类
 * @date 2016年12月7日
 */ 
public class GoodsDetailVo implements Serializable {

	private static final long serialVersionUID = -8041418455885782640L;
	/** id */
	private String id;
	/** 商品id */
	private String goodsId;
	/** 模块大标题 */
	private String model;
	/** 商品模块添加内容 */
	private String content;
	/** 排序号 */
	private Integer rank;
	/** 类型 */
	private Integer type;
	/** 状态 */
	private Integer status;
	/** 备注 */
	private String remark;
	/** 图片上传命名空间 */
	private String namespace;
	/** 图片历史集合*/
	private List<String> imglist;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<String> getImglist() {
		return imglist;
	}

	public void setImglist(List<String> imglist) {
		this.imglist = imglist;
	}
	
}
