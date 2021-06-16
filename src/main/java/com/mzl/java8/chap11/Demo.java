package com.mzl.java8.chap11;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Demo
 * @description: TODO
 * @date 2021/6/1 9:02
 */
public class Demo {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
    private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(executor);

    public static void main(String[] args) {
        /*Map<String, Object> search = new HashMap<>(2);
        List<List<String>> groupPersonList  = new ArrayList<>();
        //多线程处理生成账单
        List<ListenableFuture<Integer>> futures = Lists.newArrayList();
        Map<String,Object> queryMap = JSON.parseObject(JSON.toJSONString(search),Map.class);
        // step 1 根据room personid查询收费标准下要作废的账单
        for (List<String> perIds : groupPersonList) {
            futures.add(executorService.submit(()->processBillPaidInitTempDate(perIds,queryMap, taskId, organId)));
        }
        ListenableFuture<List<Integer>> listListenableFuture = Futures.successfulAsList(futures);

        try {
            List<Integer> integers = listListenableFuture.get();
            successCount = integers.stream().filter(x->x!=null).collect(Collectors.summingLong(Long::valueOf));
        } catch (Exception e) {
            logger.error("获取结果信息异常taskId={}",taskId,e);
        }*/
    }

    /**
     * 线程批量并发执行数据，分页分线程处理
     *  最大线程{@code DO_MAX_SIZE}
     * @author WangLu
     * @date 2019-08-20 18:21
     */
    /*
    private ExecutorService userThreadPool = new ThreadPoolExecutor(10, 50, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    private void executorMultiThreadPool (final Long pageTotal, final String querySql, final OasRollbackTask oasRollbackTask, final RollbackContentTypeEnum typeEnum) throws Exception {
        // 第一页已经处理过，不需要再处理，所以从第二页开始
        int startPage = 2, endPage = 0;
        final StringBuilder exception = new StringBuilder();
        for (int i = 2; i <= pageTotal; i++) {
            endPage++;
            if (i % DO_MAX_SIZE != 0 && i != pageTotal) {
                continue;
            }
            long timeMillis = System.currentTimeMillis();
            int cbNum = endPage - startPage + 1;
            // parties+1是因为给主线程添加了await;
            final CyclicBarrier cb = new CyclicBarrier(cbNum + 1);
            logger.info(">>>>>{}线程池批次执行-总页数[{}],起始页[{}],结束页[{}]", oasRollbackTask.getRollbackId(), pageTotal, startPage, endPage);
            for (int page = startPage; page <= endPage; page++) {
                final int pageNum = page;
                userThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PageUtils.startPage(pageNum, pageSize, false);
                            List<Map> mapList = dao.queryDataBySql(querySql);
                            logger.info(">>>>>{}线程池查询备份数据-总页数[{}],当前页[{}],条数[{}]", oasRollbackTask.getRollbackId(), pageTotal, pageNum, mapList.size());
                            // 分批次存储
                            saveRollBackContentBath(oasRollbackTask, typeEnum, mapList);
                            mapList.clear();
                        } catch (Exception e) {
                            logger.info(">>>>>{}处理异常[{}],当前页[{}]", oasRollbackTask.getRollbackId(), pageTotal, pageNum, e);
                            exception.append(e.getMessage());
                        } finally {
                            try {
                                cb.await();
                            } catch (Exception e) {
                                logger.info(">>>>>{}Finally处理异常[{}],当前页[{}]", oasRollbackTask.getRollbackId(), pageTotal, pageNum, e);
                            }
                        }
                    }
                });
                if (exception.length() > 0) {
                    throw new Exception(exception.toString());
                }
            }
            // 等待全部处理
            try {
                cb.await();
            } catch (Exception e) {
                logger.info(">>>>>{}await处理异常", oasRollbackTask.getRollbackId(), e);
                throw e;
            } finally {
                logger.info(">>>>>{}线程池批次执行-批次完成：起始页[{}],结束页[{}], 用时[{}]", oasRollbackTask.getRollbackId(), startPage, endPage, System.currentTimeMillis() - timeMillis);
            }
            // 本批次处理完后，将结束页赋给开始
            startPage = endPage + 1;
        }
        logger.info(">>>>>{}线程池处理所有批次-执行完成", oasRollbackTask.getRollbackId());
    }*/
}
