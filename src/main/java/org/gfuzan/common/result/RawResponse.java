package org.gfuzan.common.result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Controller 序列化响应结果
 * 
 * @author GFuZan
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class RawResponse<T> {
	/**
	 * 数据
	 */
	private T data;
	/**
	 * 错误编码
	 */
	private int errorCode;
	/**
	 * 错误消息
	 */
	private String errorMessage;

	/**
	 * 其他字段
	 */
	@JsonIgnore
	private Map<String, Object> otherField;

	/**
	 * 获取数据
	 * 
	 * @return data
	 */
	public T getData() {
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 获取错误编码
	 * 
	 * @return errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置错误编码
	 * 
	 * @param errorCode
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 获取错误消息
	 * 
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 设置错误消息
	 * 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * 附加字段
	 * 
	 * @param fieldName 字段名
	 * @param value     值
	 */
	@JsonAnySetter
	@JsonIgnore
	public void addOtherField(String fieldName, Object value) {

		if (StringUtils.isEmpty(fieldName)) {
			throw new NullPointerException("fieldName 不能为空");
		}

		if (otherField == null) {
			synchronized (this) {
				if (otherField == null) {
					otherField = new ConcurrentHashMap<>();
				}
			}
		}

		otherField.put(fieldName, value);
	}

	/**
	 * 获取附加字段值
	 * 
	 * @param fieldName 字段名
	 * @return 附加字段的值 or null
	 */
	@JsonIgnore
	public Object getOtherFieldValue(String fieldName) {
		return otherField != null && StringUtils.hasLength(fieldName) ? otherField.get(fieldName) : null;
	}

	/**
	 * 获取附加字段值
	 * 
	 * @param fieldName 字段名
	 * @param resType   返回值类型
	 * @return 附加字段的值 or null
	 * @throws ClassCastException 类型转换异常
	 */
	@JsonIgnore
	@SuppressWarnings("unchecked")
	public <V> V getOtherFieldValue(String fieldName, Class<V> resType) {
		return (V) getOtherFieldValue(fieldName);
	}

	@JsonAnyGetter
	private Map<String, Object> getAllOtherField() {
		return otherField;
	}

}
