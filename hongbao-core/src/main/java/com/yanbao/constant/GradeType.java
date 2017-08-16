package com.yanbao.constant;

/**
 * 会员等级参考枚举类 <br/>
 *
 * @author jay.zheng
 * @date 2017年03月02日
 * 会员等级的顺序不能随意调整，若有调整请咨询作者
 */
public enum GradeType {
    grade1(0,"普通会员"),
    grade2(1,"vip会员"),
    grade3(2,"初级合伙人"),
    grade4(11,"一星合伙人"),
    grade5(12,"二星合伙人"),
    grade6(13,"三星合伙人"),
    grade7(14,"四星合伙人"),
    grade8(15,"五星合伙人"),
    grade9(16,"六星合伙人"),
    grade10(17,"七星合伙人"),
    grade11(21,"一星董事"),
    grade12(22,"二星董事"),
    grade13(23,"三星董事"),
    grade14(24,"四星董事"),
    grade15(25,"五星董事");
    private final Integer code;

    private final String msg;

    private GradeType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static GradeType fromCode(Integer code) {
        try {
            for(GradeType gradeType :GradeType.values()){
                if(gradeType.getCode().intValue() == code.intValue()){
                    return gradeType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static GradeType fromOrdinal(int ordinal) {
        try {
            for(GradeType gradeType :GradeType.values()){
                if(gradeType.ordinal() == ordinal){
                    return gradeType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String toCode() {
        return Integer.toString(this.ordinal());
    }
}
