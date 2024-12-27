import numpy as np
import matplotlib.pyplot as plt
import networkx as nx
import time

def find_index(path, point):
    """
    找到 `point` 在 `path` 中的索引，使用 `np.array_equal()` 比较数组。
    :param path: 由 NumPy 数组组成的路径
    :param point: 要查找的 NumPy 数组
    :return: 如果找到，返回索引。否则，返回 -1。
    """
    for i, p in enumerate(path):
        if np.array_equal(p, point):
            return i
    return -1  # 如果找不到返回 -1

def least_diversion_method(k, s, d, points):
    """
    论文中的LDM方法,寻找次优路径
    :param k: stop数量
    :param s: 起始点
    :param d: 终止点
    :param points: 候选点集
    :return: LDM搜索后的路径
    """
    path = [s, d]  # 初始化path，只包含两个点s和d
    
    for _ in range(k):  # 插入k个点
        # 计算当前路径中的所有线段
        line_segments = compute_line_segment(path)
        
        min_dist = float('inf')  # 初始化最小距离为正无穷
        closest_point = None     # 初始化最近的点为 None
        closest_lineseg = None   # 记录最近的线段
        
        # 遍历所有线段和候选点，计算点到线段的最小距离
        for line_segment in line_segments:
            for p in points:
                # 如果点已经在path中，跳过
                if any(np.array_equal(p, pt) for pt in path):
                    continue

                dist = p2l_dist(p, line_segment[0], line_segment[1])
                if dist < min_dist:
                    min_dist = dist
                    closest_point = p
                    closest_lineseg = line_segment
        
        # 找到最近线段，插入最近点
        if closest_point is not None and closest_lineseg is not None:
            # 使用自定义的 find_index 来查找线段起点的索引
            index = find_index(path, closest_lineseg[0])
            if index != -1:
                path.insert(index + 1, closest_point)  # 将最近点插入到路径中
            else:
                print(f"Error: 线段左端点不在路径中, 该线段: {closest_lineseg}, 当前路径: {path}")
    
    return path


def compute_line_segment(path):
    line_segments = [(path[i], path[i+1]) for i in range(len(path)-1)]
    #print(line_segments)
    return line_segments

def p2l_dist(P,A,B):#要计算距离的点，线段的两个端点
    AB = B - A  # 线段向量
    AP = P - A  # 点到 A 的向量
    # 计算投影系数 t
    AB_AB = np.dot(AB, AB)  # AB·AB
    if AB_AB == 0:  # A 和 B 是同一点
        return np.linalg.norm(P - A)  # 返回 P 到 A 的距离
    t = np.dot(AP, AB) / AB_AB
    
    # 判断 t 的范围
    if t < 0.0:
        # 投影点在 A 之外，最近点是 A
        closest_point = A
    elif t > 1.0:
        # 投影点在 B 之外，最近点是 B
        closest_point = B
    else:
        # 投影点在线段 AB 上，计算最近点
        closest_point = A + t * AB
    
    # 计算 P 到最近点的距离
    distance = np.linalg.norm(P - closest_point)
    return distance

def path_length(path):#计算路径的长度
    dist=0
    lsegs=compute_line_segment(path)
    for i in range(0,len(lsegs)):
        dist+= np.linalg.norm(lsegs[i][0]-lsegs[i][1])
    #print(dist)
    return dist

def prune(p,dist,s,d):#输入中间点集p,和路径长度，s,d进行剪枝
    pruned_p = []
    for node in p:
        # 计算 node 到 s 和 d 的距离和
        temp = np.linalg.norm(node - s) + np.linalg.norm(node - d)
        if temp <= dist:
            # 如果距离和小于等于给定的路径长度，保留该节点
            pruned_p.append(node)
    
    return np.array(pruned_p)

def generate_graph(points,s,d):#根据剪枝后的点生成完全连通图，边的权重就是欧氏空间的距离
    G=nx.Graph()
    G.add_node("s")
    G.add_node("d")
    G.add_nodes_from(range(len(points)))
    for i in range(len(points)):
        for j in range(i + 1, len(points)):  # 避免重复添加边
            # 计算欧几里得距离
            distance = np.linalg.norm(points[i] - points[j])
            G.add_edge(i, j, weight=distance,label=f"{distance:.4f}")
    for i in range(len(points)):
        s_dist= np.linalg.norm(points[i] - s)
        d_dist= np.linalg.norm(points[i] - d)
        G.add_edge(i, "s", weight=s_dist,label=f"{s_dist:.4f}")
        G.add_edge(i, "d", weight=d_dist,label=f"{d_dist:.4f}")
    nx.nx_pydot.write_dot(G, "graph.dot")
    return G

def bellman_ford_k_stops(G, source, target, k):
    # 初始化 dp 数组, dp[node][stops] 表示到达节点 node，经过恰好 stops 条边的最短路径长度
    # k个中间节点，加上s和d那么一共k+2个节点
    dp = {node: [float('inf')] * (k + 2) for node in G.nodes}
    
    dp[source][0] = 0  # 起点到自己，经过 0 条边的距离为 0
    
    # predecessor 数组，用于跟踪最短路径的前驱节点
    predecessor = {node: [-1] * (k + 2) for node in G.nodes}

    # 遍历最多 k+1 条边，k+2个节点的路径上有k+1条边
    for stops in range(k+1):
        # 为了避免边的顺序对结果的影响，创建一个临时的 dp 数组
        new_dp = {node: dp[node][:] for node in G.nodes}
        
        # 遍历图中的每一条边
        for u, v, data in G.edges(data=True):
            weight = data['weight']
            # 更新从 u 到 v 的距离
            if dp[u][stops] + weight < new_dp[v][stops + 1]:
                new_dp[v][stops + 1] = dp[u][stops] + weight
                predecessor[v][stops + 1] = u
            # 由于是无向图，反向也要更新
            if dp[v][stops] + weight < new_dp[u][stops + 1]:
                new_dp[u][stops + 1] = dp[v][stops] + weight
                predecessor[u][stops + 1] = v
        
        # 更新 dp 表
        dp = new_dp

    # 最后返回 dp[target][k+1]，即到达目标节点，经过恰好 k+1 条边的最短路径长度
    result = dp[target][k+1]  # 恰好经过 k+1 条边的最短路径
    
    if result == float('inf'):
        return None, None  # 表示没有经过恰好 k+1 条边的路径
    
    # 通过回溯 predecessor 数组构建路径
    path = []
    current_node = target
    current_stops = k+1
    
    while current_node != -1 and current_stops >= 0:
        path.append(current_node)
        current_node = predecessor[current_node][current_stops]
        current_stops -= 1
    
    path.reverse()  # 反转路径，因为我们是从目标节点回溯到起点
    
    return result, path


if __name__=="__main__":
    times=[]
    for i in range(100):
        start=time.perf_counter()
        Size= 1000*1000
        N = 17 #所有点的数量
        intermediate_points_count=N-2 #除去s和d后剩余点的数量
        k= 15

        # 生成二维欧几里得空间中的随机整数点，区间为 [0, 1000]
        intermediate_points = np.random.randint(0, Size+1, size=(intermediate_points_count, 2))  # 生成 intermediate_points_count 个二维点，坐标为整数

        # 第一个点作为起点 s，最后一个点作为终点 d
        s = np.random.randint(0, Size+1, size=(2,))
        d = np.random.randint(0, Size+1, size=(2,))
        # s = np.random.randint(0, 101, size=(2,))
        # d = np.random.randint(0, 1010000, size=(2,))
        step1_start=time.perf_counter()
        path=least_diversion_method(k,s,d,intermediate_points)
        step1_end=time.perf_counter()
        #print(f"Step 1 time elapsed:{(step1_end-step1_start):.4f}s")
        #print(path)
        length=path_length(path)
        step2_start=time.perf_counter()
        pruned=prune(intermediate_points,length,s,d)
        step2_end=time.perf_counter()
        #print(f"Step 2 time elapsed:{(step2_end-step2_start):.4f}s")
        #print(f"N1={len(pruned)}")
        unpruned_points = np.setdiff1d(intermediate_points.view([('', intermediate_points.dtype)] * 2), 
                                pruned.view([('', pruned.dtype)] * 2), 
                                assume_unique=True).view(intermediate_points.dtype).reshape(-1, 2)
        step3_start=time.perf_counter()
        G=generate_graph(pruned,s,d)
        result,opt_path=bellman_ford_k_stops(G,"s","d",k)
        step3_end=time.perf_counter()
        # print(f"Step 3 time elapsed:{(step3_end-step3_start):.4f}s")
        end=time.perf_counter()
        times.append(end-start)
    avg=sum(times)/len(times)
    print(f"Time elapsed: {(avg):.4f}s")


    # 绘制被选中的点
    # pruned_x=pruned[:, 0]
    # pruned_y=pruned[:, 1]
    # plt.scatter(pruned_x, pruned_y, c='blue', marker='o', label='Selected Points')

    # # 绘制被剪枝的点
    # unpruned_x=unpruned_points[:, 0]
    # unpruned_y=unpruned_points[:, 1]
    # plt.scatter(unpruned_x, unpruned_y, c='gray', marker='o', label='Selected Points')

    # #中间点的编号
    # # x = intermediate_points[:, 0]
    # # y = intermediate_points[:, 1]
    # # for i in range(0,intermediate_points_count):
    # #     plt.text(x[i], y[i], f'p{i+1}', fontsize=9, verticalalignment='bottom', horizontalalignment='right')

    # # 单独标记起点 s 和终点 d
    # plt.scatter(s[0], s[1], c='green', marker='o', s=100, label='Start (S)', edgecolors='black')
    # plt.scatter(d[0], d[1], c='red', marker='o', s=100, label='Destination (D)', edgecolors='black')

    # # 在图上添加文本注释
    # plt.text(s[0], s[1], '  S', fontsize=12, verticalalignment='bottom', color='green')
    # plt.text(d[0], d[1], '  D', fontsize=12, verticalalignment='bottom', color='red')


    # #绘制路径
    # path=np.array(path)
    # path_x = path[:, 0]
    # path_y = path[:, 1]

    # # 计算每个向量的 dx 和 dy
    # dx = np.diff(path_x)
    # dy = np.diff(path_y)

    # # 绘制带箭头的向量
    # plt.quiver(path_x[:-1], path_y[:-1], dx, dy, angles='xy', scale_units='xy', scale=1, color='black')

    # # 设置标题和标签
    # plt.title(f"Constrained-Shortest-Path-Computation in {N} Points")
    # plt.xlabel(f"X-axis [0, {Size}]")
    # plt.ylabel(f"Y-axis [0, {Size}]")
    # #plt.ylim(0,Size+1)
    # #plt.xlim(0,Size+1)

    # # 保存图像
    # plt.savefig("map.png", dpi=600)
