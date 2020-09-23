package com.vick.test.util.tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vick
 * @date 2020/9/7
 */
public class TreeUtil {

    private String getIdMethod;
    private String getParentIdMethod;

    public TreeUtil(String getIdMethod, String getParentIdMethod) {
        this.getIdMethod = getIdMethod;
        this.getParentIdMethod = getParentIdMethod;
    }

    public <T> List<TreeEntity<T>> listToTreeList(List<T> dataList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TreeEntity<T>> forest = new ArrayList<>();
        if (dataList == null || dataList.size() == 0) {
            return forest;
        }
        Map<Long, TreeEntity<T>> nodeIdNodeMap = new HashMap<>();
        TreeEntity<T> node = null;
        for (T t : dataList) {
            node = new TreeEntity<T>();
            node.setModel(t);
            Long id = getId(t);
            node.setId(id);
            Long parentId = getParentId(t);
            node.setParentId(parentId);
            nodeIdNodeMap.put(id, node);
        }

        nodeIdNodeMap.values().forEach(e -> {
            if (e.getParentId() == null || !nodeIdNodeMap.containsKey(e.getParentId())) {
                forest.add(e);
            } else {
                TreeEntity<T> entity = nodeIdNodeMap.get(e.getParentId());
                if (null == entity.getChildren()) {
                    List<TreeEntity<T>> children = new ArrayList<>();
                    children.add(e);
                    entity.setChildren(children);
                } else {
                    entity.getChildren().add(e);
                }
            }
        });

        return forest;
    }

    /**
     * 获取节点ID
     *
     * @param item
     * @return
     * @throws Exception
     */
    private <T> Long getId(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = item.getClass().getMethod(getIdMethod, null);
        Object invoke = method.invoke(item, null);
        Long id = 0L;
        if (invoke != null) {
            id = Long.parseLong(invoke.toString());
        }
        return id;
    }

    /**
     * 获取父级节点ID
     *
     * @param item
     * @return
     * @throws Exception
     */
    private <T> Long getParentId(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = item.getClass().getMethod(getParentIdMethod, null);
        Object invoke = method.invoke(item, null);
        Long parentId = 0L;
        if (invoke != null) {
            parentId = Long.parseLong(invoke.toString());
        }
        return parentId;
    }

}
