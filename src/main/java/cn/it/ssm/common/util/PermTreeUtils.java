package cn.it.ssm.common.util;

import cn.it.ssm.common.util.tree.PermNode;
import cn.it.ssm.sys.domain.auto.SysPermission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PermTreeUtils {

    public static PermNode permTree(List<SysPermission> permissionList) {
        // root node
        PermNode root = new PermNode();
        root.setId(0);
        root.setText("top");
        root.setChildren(PermTreeUtils.getChildren(root, permissionList));
        return root;
    }

    private static List<PermNode> getChildren(PermNode root, List<SysPermission> permissionList) {
        List<PermNode> permNodes = new ArrayList<PermNode>();
        for (SysPermission p : permissionList) {
            // 加载本层节点
            if (p.getParentid().equals(root.getId())) {
                PermNode node = new PermNode();
                node.setId(p.getId());
                node.setText(p.getName());
                node.setSort(p.getSort());
                node.setParentId(p.getParentid());
                // 设置children
                node.setChildren(getChildren(node, permissionList));
                permNodes.add(node);
            }
        }
        // 排序
        permNodes.sort(Comparator.comparingInt(PermNode::getSort));
        return permNodes;
    }
}
