package com.liuhq.open.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.liuhq.open.model.VideoDTO;
import com.liuhq.open.model.VideoLearnProgressDTO;
import com.liuhq.open.model.XapiParmDTO;
import com.liuhq.open.service.AnswerService;
import com.liuhq.open.service.CourseService;
import com.liuhq.open.service.HomeworkService;
import com.liuhq.open.service.OpenService;
import com.liuhq.open.service.VideoService;
import com.liuhq.open.service.WorkService;

public class OpenServiceImpl implements OpenService {

	private WorkService workService = new WorkServiceImpl();

	private HomeworkService homeworkService = new HomeworkServiceImpl();

	private AnswerService answerService = new AnswerServiceImpl();

	private CourseService courseService = new CourseServiceImpl();

	private VideoService videoService = new VideoServiceImpl();

	@Override
	public void autoAnswer() {
		// 获取所有作业
		var workList = workService.listAll();
		// 筛选出需要做的作业,分数不等于100并且studentHomeworkId不等于1
		var exerciseList = workList.parallelStream().flatMap(work -> work.getExerciseList().stream())
				.filter(exercise -> BigDecimal.valueOf(100.0).compareTo(exercise.getMaxScore()) != 0
						&& StringUtils.isNotBlank(exercise.getStudentHomeworkId()))
				.collect(Collectors.toList());
		// 开启10个线程，加快答题速度
		var service = Executors.newFixedThreadPool(10);
		exerciseList.parallelStream().forEach(work -> {
			service.execute(() -> {
				System.out.println("[" + work.getExerciseName() + "]正在答题...");
				// 获取题目
				var homework = homeworkService.getByStudentHomeworkId(work.getStudentHomeworkId());
				var answerQuestionList = new ArrayList<Object>();
				var items = homework.getPaperInfo().getItems();

				items.forEach(item -> {
					// 获取答案
					var answerKey = answerService.getAnswerKey(homework.getPaperInfo().getModel().getP3(),
							item.getI1());
					answerQuestionList
							.add(Map.of("I1", item.getI1(), "I15", answerKey.getI7(), "Sub", new ArrayList<Object>()));
					// 暂存答案，这个可以不用调用
					answerService.stage(homework.getWorkAnswerId(), Map.of("Items", answerQuestionList));
				});
				// 提交
				var score = answerService.submit(homework.getWorkAnswerId(), answerQuestionList);
				System.out.println("[" + work.getExerciseName() + "]答题完成，分数：" + score);
			});
		});
		// 关闭线程池
		if (Objects.nonNull(service) && !service.isShutdown()) {
			service.shutdown();
		}
	}

	@Override
	public void antoWatchVideo() {
		// 获取所有科目
		var list = courseService.getAll();
		list.parallelStream().forEach(course -> {
			// 获取该课程已观看的视频，type=3就是已观看完成的
			List<VideoLearnProgressDTO> listVideoLearnProgress = courseService
					.listVideoLearnProgress(course.getCourseID()).stream().filter(progress -> progress.getType() == 3)
					.collect(Collectors.toList());
			courseService.listAllVideo(course.getCourseID()).parallelStream()
					.filter(video -> !listVideoLearnProgress.stream().map(VideoLearnProgressDTO::getResourceID)
							.collect(Collectors.toList()).contains(video.getId()))
					.forEach(video -> {
						try {
							var info = videoService.getParamInfo(video.getCourseId());
							// 需要调用四次接口-1
							videoService.dataCollection(JSON.toJSONString(getForParam1(info, video)), info.getAppkey(),
									info.getSignature(), info.getSignatureNonce(), info.getTimestamp());
							// 需要调用四次接口-2
							videoService.dataCollection(JSON.toJSONString(getForParam2(info, video)), info.getAppkey(),
									info.getSignature(), info.getSignatureNonce(), info.getTimestamp());
							// 需要调用四次接口-3
							videoService.dataCollection(JSON.toJSONString(getForParam3(info, video)), info.getAppkey(),
									info.getSignature(), info.getSignatureNonce(), info.getTimestamp());
							// 需要调用四次接口-4
							videoService.dataCollection(JSON.toJSONString(getForParam4(info, video)), info.getAppkey(),
									info.getSignature(), info.getSignatureNonce(), info.getTimestamp());
							System.out.println(video.getCourseName() + "[" + video.getCourseId() + "]" + " "
									+ video.getFileName() + "[" + video.getFileId() + "]观看完毕");
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
		});
	}

	@SuppressWarnings("serial")
	private Map<String, Object> getForParam4(XapiParmDTO info, VideoDTO video) {
		return new HashMap<String, Object>(Map.of("actor",
				new HashMap<String, Object>(Map.of("actorId", info.getUserId(), "actorName", info.getUserName(),
						"actorType", "1", "extensions",
						new HashMap<String, Object>(Map.of("studentStatusId", info.getStudentStatusId(),
								"organizationId", info.getOrganizationId())))),
				"objectInfo", new HashMap<String, Object>() {
					{
						put("objId", video.getId());
						put("objType", "video");
						put("objName", video.getFileName());
						put("extensions", new HashMap<String, Object>());
					}
				}, "verbInfo",
				new HashMap<String, Object>(
						Map.of("verbType", "completed", "extensions", new HashMap<String, Object>())),
				"context", new HashMap<String, Object>(Map.of("ip", info.getIp(), "terminalInfo",
						info.getTerminalinfo(), "terminalType", "1", "extensions", new HashMap<String, Object>() {
							{
								put("courseId", video.getCourseId().toString());
								put("resourcesId", video.getId());
								put("electiveCourseId", info.getElectiveCourseId());
								put("coursewareId", video.getCoursewareId());
								put("starting_point", video.getDuration() / 1000);
								put("ending_point", video.getDuration() / 1000);
								put("duration", 0);
								put("speed", "");
								put("volume", "");
								put("clarity", "");
							}
						})),
				"result", new HashMap<String, Object>(), "actionTimestamp",
				String.valueOf(System.currentTimeMillis() / 1000)));
	}

	@SuppressWarnings("serial")
	private Map<String, Object> getForParam3(XapiParmDTO info, VideoDTO video) {
		return new HashMap<String, Object>(Map.of("actor",
				new HashMap<String, Object>(Map.of("actorId", info.getUserId(), "actorName", info.getUserName(),
						"actorType", "1", "extensions",
						new HashMap<String, Object>(Map.of("studentStatusId", info.getStudentStatusId(),
								"organizationId", info.getOrganizationId())))),
				"objectInfo", new HashMap<String, Object>() {
					{
						put("objId", video.getId());
						put("objType", "video");
						put("objName", video.getFileName());
						put("extensions", new HashMap<String, Object>());
					}
				}, "verbInfo",
				new HashMap<String, Object>(
						Map.of("verbType", "completed", "extensions", new HashMap<String, Object>())),
				"context", new HashMap<String, Object>(Map.of("ip", info.getIp(), "terminalInfo",
						info.getTerminalinfo(), "terminalType", "1", "extensions", new HashMap<String, Object>() {
							{
								put("courseId", video.getCourseId().toString());
								put("resourcesId", video.getId());
								put("electiveCourseId", info.getElectiveCourseId());
								put("coursewareId", video.getCoursewareId());
							}
						})),
				"result", new HashMap<String, Object>(), "actionTimestamp",
				String.valueOf(System.currentTimeMillis() / 1000)));
	}

	@SuppressWarnings("serial")
	private Map<String, Object> getForParam2(XapiParmDTO info, VideoDTO video) {
		return new HashMap<String, Object>(Map.of("actor",
				new HashMap<String, Object>(Map.of("actorId", info.getUserId(), "actorName", info.getUserName(),
						"actorType", "1", "extensions",
						new HashMap<String, Object>(Map.of("studentStatusId", info.getStudentStatusId(),
								"organizationId", info.getOrganizationId())))),
				"objectInfo", new HashMap<String, Object>() {
					{
						put("objId", video.getId());
						put("objType", "video");
						put("objName", video.getFileName());
						put("extensions", new HashMap<String, Object>());
					}
				}, "verbInfo",
				new HashMap<String, Object>(Map.of("verbType", "watched", "extensions", new HashMap<String, Object>())),
				"context", new HashMap<String, Object>(Map.of("ip", info.getIp(), "terminalInfo",
						info.getTerminalinfo(), "terminalType", "1", "extensions", new HashMap<String, Object>() {
							{
								put("courseId", video.getCourseId().toString());
								put("resourcesId", video.getId());
								put("electiveCourseId", info.getElectiveCourseId());
								put("coursewareId", video.getCoursewareId());
								put("starting_point", video.getDuration() / 1000 - 7);
								put("ending_point", video.getDuration() / 1000);
								put("duration", 7);
								put("speed", "");
								put("volume", "");
								put("clarity", "");
							}
						})),
				"result", new HashMap<String, Object>(), "actionTimestamp",
				String.valueOf(System.currentTimeMillis() / 1000)));
	}

	@SuppressWarnings("serial")
	private Map<String, Object> getForParam1(XapiParmDTO info, VideoDTO video) {
		return new HashMap<String, Object>(Map.of("actor",
				new HashMap<String, Object>(Map.of("actorId", info.getUserId(), "actorName", info.getUserName(),
						"actorType", "1", "extensions",
						new HashMap<String, Object>(Map.of("studentStatusId", info.getStudentStatusId(),
								"organizationId", info.getOrganizationId())))),
				"objectInfo", new HashMap<String, Object>() {
					{
						put("objId", video.getId());
						put("objType", "video");
						put("objName", video.getFileName());
						put("extensions", new HashMap<String, Object>());
					}
				}, "verbInfo",
				new HashMap<String, Object>(Map.of("verbType", "set", "extensions",
						new HashMap<String, Object>(Map.of("verbCategory", "pause")))),
				"context", new HashMap<String, Object>(Map.of("ip", info.getIp(), "terminalInfo",
						info.getTerminalinfo(), "terminalType", "1", "extensions", new HashMap<String, Object>() {
							{
								put("courseId", video.getCourseId().toString());
								put("resourcesId", video.getId());
								put("electiveCourseId", info.getElectiveCourseId());
								put("coursewareId", video.getCoursewareId());
								put("time", String.valueOf(video.getDuration() / 1000));
							}
						})),
				"result", new HashMap<String, Object>(), "actionTimestamp",
				String.valueOf(System.currentTimeMillis() / 1000)));
	}

}
