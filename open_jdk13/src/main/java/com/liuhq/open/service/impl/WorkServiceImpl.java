package com.liuhq.open.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.liuhq.open.constant.OpenConstant;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.ExerciseDTO;
import com.liuhq.open.model.WorkDTO;
import com.liuhq.open.service.WorkService;
import com.liuhq.open.utils.WebClientFactory;
import com.liuhq.open.utils.WebRequestFactory;

/**
 * 作业service实现类
 * 
 * @author liuhaoqing
 *
 */
public class WorkServiceImpl implements WorkService {

	private WebClient client = WebClientFactory.getInstance();

	@Override
	public List<WorkDTO> listAll() {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.GET_ONLINE_JSON_ALL);
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			if (StringUtils.isNotBlank(result) && response.getStatusCode() == OpenConstant.HTTP_STATUS_CODE_OK) {
				var workJSONArray = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("listWork");
				if (Objects.nonNull(workJSONArray) && !workJSONArray.isEmpty()) {
					var list = new ArrayList<WorkDTO>();
					for (int i = 0; i < workJSONArray.size(); i++) {
						var workJSONObject = workJSONArray.getJSONObject(i);
						list.add(WorkDTO.builder().courseName(workJSONObject.getString("CourseName"))
								.exerciseList(JSONArray.parseArray(
										JSONArray.toJSONString(workJSONObject.getJSONArray("Data")), ExerciseDTO.class))
								.build());
					}
					return list;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new OpenException("获取作业失败");
	}

}
