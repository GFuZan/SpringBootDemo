package org.gfuzan.demo;

import org.gfuzan.common.result.RawResponse;
import org.gfuzan.common.result.ServiceResult;
import org.gfuzan.common.utils.CommonUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author GFuZan
 *
 */
public class ResultTest {

	ObjectMapper om = CommonUtil.getObjectMapper();

	@Test
	public void testRaw() throws Exception {
		RawResponse<Integer> rr = new RawResponse<>();
		rr.setData(1);
		rr.setErrorCode(100);
		rr.setErrorMessage("em");
		Assert.assertEquals(rr.getOtherFieldValue("fieldName"), null);
		rr.addOtherField("fieldName", "value");
		Assert.assertEquals(rr.getOtherFieldValue("fieldName"), "value");
		rr.addOtherField("fieldName2", 100);
		
		System.out.println(om.writeValueAsString(rr));
	}
	
	
	@Test
	public void testSerRes() throws Exception {
		ServiceResult<Integer> rr = new ServiceResult<>();
		rr.setData(1);
		rr.setErrorMessage("em");
		Assert.assertEquals(rr.getOtherFieldValue("fieldName"), null);
		rr.addOtherField("fieldName", "value");
		Assert.assertEquals(rr.getOtherFieldValue("fieldName"), "value");
		rr.addOtherField("fieldName2", 100);
		
		System.out.println(om.writeValueAsString(rr));
	}
}
