package cn.it.ssm.common.util;

import cn.it.ssm.common.tree.PermNode;
import cn.it.ssm.domain.auto.SysPermission;

import java.util.ArrayList;
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

    private static void childTree(PermNode topRoot, List<SysPermission> permissionList, Integer parentId) {
        List<PermNode> permNodes = new ArrayList<PermNode>();
        for (SysPermission p : permissionList) {
            if (p.getParentid().equals(parentId)) {
                PermNode node = new PermNode();
                node.setId(p.getId());
                node.setText(p.getName());
                node.setParentId(p.getParentid());
                childTree(node, permissionList, node.getId());
                permNodes.add(node);
                topRoot.setChildren(permNodes);
            }
        }
    }
}
