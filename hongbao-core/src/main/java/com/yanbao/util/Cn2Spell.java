package com.yanbao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

import com.github.stuxuhai.jpinyin.PinyinFormat;

/**
 * 汉字转换位汉语拼音工具类<br/>
 * 英文字符不变<br/>
 * 
 * @author wenchao
 */
public class Cn2Spell {

	private static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	static {
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	// 20130923去掉英文括号()
	private final static String REGEX_FILTER = "[`~!@#$%^&*+=|{}:;,\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？()]";

	/**
	 * 生成拼音，且去掉空格和换行符
	 */
	public static String convert2PinyinNoBlank(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyword.length(); i++) {
			char tem = keyword.charAt(i);
			if (Cn2Spell.isChinese(tem)) {
				char data[] = { tem };
				sb.append(converterToSpell(new String(data)));
			} else {
				sb.append(tem);
			}
		}
		return getStringNoBlank(sb.toString());
	}

	/**
	 * 拼音+首字母:新浪微博 xinlangweibo xlwb 基于JPinyin 1.0，解决多音字问题
	 * */
	public static String convert2PinyinAndFirstSpell(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return null;
		}
		String pinyin = null;
		String first = null;
		try {
			pinyin = getStringNoBlank(com.github.stuxuhai.jpinyin.PinyinHelper.convertToPinyinString(keyword, "", PinyinFormat.WITHOUT_TONE));
			first = getStringNoBlank(com.github.stuxuhai.jpinyin.PinyinHelper.getShortPinyin(keyword));
		} catch (Exception e) {
			e.printStackTrace();
			return keyword;
		}
		if (!StringUtils.isEmpty(first)) {
			return pinyin + " " + first;
		}
		return pinyin;
	}

	/**
	 * 已弃用 基于pinyin4J转换拼音，存在多音字问题
	 */
	@Deprecated
	public static String convert2PinyinAndFirstSpell_old(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyword.length(); i++) {
			char tem = keyword.charAt(i);
			if (Cn2Spell.isChinese(tem)) {
				char data[] = { tem };
				sb.append(converterToSpell(new String(data)));
			} else {
				sb.append(tem);
			}
		}
		String pinyin = getStringNoBlank(sb.toString());
		String first = converterToFirstSpell(keyword);
		if (first != null) {
			return pinyin + " " + getStringNoBlank(first);
		}
		return pinyin;
	}

	/**
	 * 正则表达式替换空格和换行符
	 */
	public static String getStringNoBlank(String str) {
		if (str != null && !"".equals(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			String strNoBlank = m.replaceAll("");
			return strNoBlank;
		} else {
			return str;
		}
	}

	/**
	 * 生成拼音
	 */
	public static String convert2Pinyin(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyword.length(); i++) {
			char tem = keyword.charAt(i);
			if (Cn2Spell.isChinese(tem)) {
				char data[] = { tem };
				sb.append(Cn2Spell.converterToSpell(new String(data)));
			} else {
				sb.append(tem);
			}
		}
		return sb.toString();
	}

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					pinyinName += nameChar[i];
				} catch (NullPointerException e) { // 如果是日文可能抛出该异常
					pinyinName += nameChar[i];
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	/**
	 * 汉字转换位汉语拼音，英文字符不变，日语会抛出空指针异常<br/>
	 * 需要把u:转为v<br/>
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String pinyin = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
					pinyinName += pinyin.replace("u:", "v");
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					pinyinName += nameChar[i];
				} catch (Exception e) { // 如果是日文可能抛出该异常
					pinyinName += nameChar[i];
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	/**
	 * 输入的字符是否是汉字
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	/**
	 * 字符串是否包含中文
	 * **/
	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}

	/**
	 * 输入的字符是否是字母或数字
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static boolean isNumericAndLetter(String str) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 输入的字符是否同时包含字母和汉字
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static boolean containsChineseAndLetter(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		if (!containsChinese(s)) {
			return false;
		}
		s = s.toLowerCase();
		if (!Pattern.compile("(?i)[a-z]").matcher(s).find()) {
			return false;
		}
		return true;
	}

	/**
	 * 过滤特殊字符(不过滤中间空格)
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static String StringFilter(String str) throws PatternSyntaxException {
		if (str == null)
			return null;
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		Pattern p = Pattern.compile(REGEX_FILTER);
		Matcher m = p.matcher(str);
		return m.replaceAll(" ").trim(); // 空格代替特殊字符，解决yueyue*xingkong问题
	}

	/**
	 * 把中文后面的空格去掉(不然会影响solr搜索精度)
	 * **/
	public static String makeSearchWord(String word) {
		if (word == null) {
			return null;
		}
		word = word.trim();
		StringBuilder sb = new StringBuilder(word.length());
		char c;
		for (int i = 0; i < word.length(); i++) {
			c = word.charAt(i);
			if (' ' == c && sb.length() > 0 && isChinese(sb.charAt(sb.length() - 1))) {
				continue;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}