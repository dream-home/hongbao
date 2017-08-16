package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2016-12-20:17:14;
 */
public class GoodsSortVo extends BaseVo implements Serializable{


    private static final long serialVersionUID = -201235159423221358L;

    /** 名称 */
    private String name;
    /** 图片 */
    private String icon;
    /** 排序号 */
    private Integer rank;
    /** 商品数量 */
    private Integer count;
    
    private Integer status;
    

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
