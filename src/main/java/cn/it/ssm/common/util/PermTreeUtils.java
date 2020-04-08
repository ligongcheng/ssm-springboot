package cn.it.ssm.common.util;

import cn.it.ssm.common.util.tree.PermNode;
import cn.it.ssm.sys.domain.auto.SysPermission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PermTreeUtils {

    public static PermNode permTree(List<SysPermission> permissionList) {
        // root node
        PermNode topRoot = new PermNode();
        topRoot.setId(0);
        topRoot.setText("top");
        Integer parentId = 0;
        PermTreeUtils.childTree(topRoot, permissionList, parentId);
        return topRoot;
    }

    private static List<PermNode> childTree(PermNode topRoot, List<SysPermission> permissionList, Integer parentId) {
        List<PermNode> permNodes = new ArrayList<PermNode>();
        for (SysPermission p : permissionList) {
            // 加载本层节点
            if (p.getParentid().equals(parentId)) {
                PermNode node = new PermNode();
                node.setId(p.getId());
                node.setText(p.getName());
                node.setSort(p.getSort());
                node.setParentId(p.getParentid());
                // 设置children
                node.setChildren(childTree(node, permissionList, node.getId()));
                permNodes.add(node);

            }
        }
        permNodes.sort(Comparator.comparingInt(PermNode::getSort));
        topRoot.setChildren(permNodes);
        return permNodes;
    }
}
