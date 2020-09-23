package com.vick.test.util.tree;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vick
 * @date 2020/9/7
 */
public class TreeEntity<T> implements Serializable {

    private static final long serialVersionUID = 2466860119869613039L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 父节点ID
     */
    private Long parentId;

    private T model;

    /**
     * 下级节点列表
     */
    private List<TreeEntity<T>> children;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public List<TreeEntity<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity<T>> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
