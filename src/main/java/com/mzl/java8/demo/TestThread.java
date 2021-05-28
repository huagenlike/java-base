package com.mzl.java8.demo;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author may
 * @version 1.0
 * @className: TestThread
 * @description: TODO
 * @date 2021/5/28 18:21
 */
public class TestThread {

    // 此方法为job项目同步分贝通项目的代码，跑的通，多线程后效率提高了百分之八十
    /*@ApiOperation(value = "同步项目", notes = "同步项目接口")
    @GetMapping(value = "/syncProject", produces = {"application/json;charset=UTF-8"})
    public Result syncDepartment() {
        Map<String, Object> map = new HashMap<>();
        Set<String> projectCodeSet = new HashSet<>();
        List<TravelAphProject> travelAphProjects = travelAphProjectMapper.getAphProjects(null);
        travelAphProjects = travelAphProjects.stream().limit(200).collect(toList());
        List<TravelEmployeeProjectRelation> travelEmployeeProjectRelations = travelStaffService.getEmployeeProjectRelations();

        long start = System.currentTimeMillis();

        List<CompletableFuture<Result>> futures = travelAphProjects.stream().map(travelAphProject -> CompletableFuture.supplyAsync(() -> {
            boolean isTrue = true;
            Result result = null;
            // 项目被禁用
            if ("N".equals(travelAphProject.getStatus())) {
                projectCodeSet.add(travelAphProject.getProjectCode());
                isTrue = false;
            }
            if (isTrue) {
                List<TravelManager> member = new ArrayList<>();
                Set<TravelEmployeeProjectRelation> travelEmployeeProjectRelationSet = travelEmployeeProjectRelations.parallelStream().filter(employeeProjectRelation -> employeeProjectRelation.getProjectcode().equals(travelAphProject.getProjectCode())).collect(Collectors.toSet());
//                for (TravelEmployeeProjectRelation travelEmployeeProjectRelation : travelEmployeeProjectRelationSet) {
//                    TravelManager travelManager = new TravelManager();
//                    travelManager.setMember_id(travelEmployeeProjectRelation.getStaCode());
//                    travelManager.set_manager(false);
//                    member.add(travelManager);
//                }
                TravelProject project = new TravelProject();
                project.setCompany_id(fenbeitongAppId);
                project.setUser_id(Constant.ADMIN_THIRD_CODE);
                project.setType(2);
                project.setThird_cost_id(travelAphProject.getProjectCode());
                project.setCode(travelAphProject.getProjectCode());
                project.setName(travelAphProject.getCostCenterName() + "-" + travelAphProject.getProjectName());
                project.setDescription(travelAphProject.getSegmentDesc());
                project.setBegin_date("1990-04-20 10:00:00");
                project.setEnd_date("2050-04-20 10:00:00");
                project.setExpired_state(1);
                project.setUsable_range(2);
                // 项目状态 1启用 0停用
                project.setState(0);
                project.setMember(member);

                result = travelProjectService.getProjectDetail(travelAphProject.getProjectCode());
                // 查到了，执行修改
                if (result.getStatus().intValue() == 200) {
                    result = travelProjectService.updateProject(project, travelEmployeeProjectRelations);
                } else {
                    result = travelProjectService.createProject(project, travelEmployeeProjectRelations);
                }
            }
            return result;
        })).collect(toList());

        if (CollectionUtil.isNotEmpty(projectCodeSet)) {
            String[] id_list = projectCodeSet.toArray(new String[projectCodeSet.size()]);
            travelProjectService.updateStateBatch(2, 0, id_list);
        }

        System.out.println("此处可以进行其他逻辑操作，待后面返回后，在根据返回结果进行处理");
//        List<Result> collect = futures.stream().parallel().map(f -> f.join()).collect(toList());
//
//        System.out.println(collect);
        futures.stream().parallel().map(f -> f.join()).forEach(result -> {
            Object data = result.getData();

        });

        return Result.ok((start-System.currentTimeMillis())/1000);
    }*/
}
