package org.gfuzan.common.result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

/**
 * Service 处理结果
 * 
 * @author GFuZan
 *
 */
public class ServiceResult<T> {
	/**
	 * 数据
	 */
	private T data;
	/**
	 * 处理成功
	 */
	private boolean success;
	/**
	 * 错误消息
	 */
	private String errorMessage;

	/**
	 * 其他字段
	 */
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
	 * 处理成功
	 * 
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置处理结果
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
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
	@SuppressWarnings("unchecked")
	public <V> V getOtherFieldValue(String fieldName, Class<V> resType) {
		return (V) getOtherFieldValue(fieldName);
	}
}
