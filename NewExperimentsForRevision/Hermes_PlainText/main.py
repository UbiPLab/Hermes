import numpy as np
import matplotlib.pyplot as plt
from shapely.geometry import LineString, Polygon, GeometryCollection
from shapely.ops import split
from math import acos, degrees
import time

def get_infinite_line(s, d, extension_factor=10):
    """
    根据给定的起点 s 和终点 d，生成一条穿过矩形的无限延长直线。
    extension_factor 用于确保线段延展得足够长
    """
    # 计算方向向量 (dx, dy)
    dx = d[0] - s[0]
    dy = d[1] - s[1]

    # 延长线段为"直线"的两端，确保其超出矩形边界
    extended_s = (s[0] - dx * extension_factor, s[1] - dy * extension_factor)
    extended_d = (d[0] + dx * extension_factor, d[1] + dy * extension_factor)
    return LineString([extended_s, extended_d])

def divide_map(s, d, edge_length):
    # 首先根据 SD 线段将地图划分为多个部分
    x1, y1 = 0, 0
    x2, y2 = edge_length, edge_length
    
    # 创建矩形
    rectangle = Polygon([(x1, y1), (x2, y1), (x2, y2), (x1, y2)])
    
    # 创建连接 s 和 d 的线段 SD
    line_sd = get_infinite_line(s, d, edge_length)
    
    # 计算垂直线 C 的斜率
    if d[0] != s[0]:  # 避免除以零
        slope_sd = (d[1] - s[1]) / (d[0] - s[0])
        slope_c = -1 / slope_sd
    else:
        slope_c = 0  # 如果 SD 是竖直的，那么 C 就是水平线
    
    # 定义垂直线 C，假设它跨越矩形并超出边界
    # 使用斜率公式 y = slope_c * (x - s[0]) + s[1]
    x_min, x_max = 0, edge_length
    y_min_c = slope_c * (x_min - s[0]) + s[1]
    y_max_c = slope_c * (x_max - s[0]) + s[1]
    line_c = LineString([(x_min, y_min_c), (x_max, y_max_c)])

    # 分割矩形，根据 SD 线段
    split_1 = split(rectangle, line_sd)
    #print(split_1)
    # 检查分割结果是否是 GeometryCollection，避免类型错误

    # 对每个分割后的部分进行垂直线 C 的再次分割
    split_2 = []
    for geom in split_1.geoms:
        split_result = split(geom, line_c)
        #print(split_result)
        # 再次检查 split 返回的几何是否是 GeometryCollection
        if isinstance(split_result, GeometryCollection):
            split_2.extend([g for g in split_result.geoms if not g.is_empty])
        else:
            split_2.append(split_result)

    x_min, x_max = 0, edge_length
    y_min_c = slope_c * (x_min - d[0]) + d[1]
    y_max_c = slope_c * (x_max - d[0]) + d[1]
    line_c2 = LineString([(x_min, y_min_c), (x_max, y_max_c)])

    regions=[]
    for geom in split_2:
        split_result = split(geom, line_c2)
        #print(split_result)
        # 再次检查 split 返回的几何是否是 GeometryCollection
        if isinstance(split_result, GeometryCollection):
            regions.extend([g for g in split_result.geoms if not g.is_empty])
        else:
            regions.append(split_result)
            
    # 绘制矩形和分割后的区域
    fig, ax = plt.subplots()
    for region in regions:
        if isinstance(region, Polygon):  # 确保是多边形才绘制
            x, y = region.exterior.xy
            ax.fill(x, y, alpha=0.5)

    # 绘制原始的线段 SD 和垂直线 C
    x_sd, y_sd = line_sd.xy
    x_c, y_c = line_c.xy
    x_c2,y_c2=line_c2.xy
    ax.plot(x_sd, y_sd, 'b-', label='Line SD')
    ax.plot(x_c, y_c, 'g--', label='Line C1')
    ax.plot(x_c2, y_c2, 'r--', label='Line C2')
    ax.set_xlim(0, edge_length)
    ax.set_ylim(0, edge_length)

    #绘制S D
    plt.scatter(s[0], s[1], c='green', marker='o', s=100, label='Start (S)', edgecolors='black',zorder=2)
    plt.scatter(d[0], d[1], c='red', marker='o', s=100, label='Destination (D)', edgecolors='black',zorder=2)

    # 设置图例和显示
    ax.legend()
    plt.savefig("map.png", dpi=600)
    plt.close()  # 关闭图像以避免内存泄漏

    return regions # 返回分割后的结果

def get_region(p,s,d):#判断属于SA^High, SA^Mid还是SA^Low
    #根据cos判断
    ds = s - d
    dp = p - d
    # 计算向量的点积
    dot_product = np.dot(ds, dp)
    # 计算向量的范数（长度）
    norm_ds = np.linalg.norm(ds)
    norm_dp = np.linalg.norm(dp)
    # 计算 cos(∠DSP)
    cos_dsp = dot_product / (norm_ds * norm_dp)
    sd = d - s
    sp = p - s
    # 计算向量的点积和范数（用于 cos(∠SDP)）
    dot_sd_sp = np.dot(sd, sp)
    norm_sd = np.linalg.norm(sd)
    norm_sp = np.linalg.norm(sp)

    # 计算 cos(∠SDP)
    cos_sdp = dot_sd_sp / (norm_sd * norm_sp)

    if cos_dsp<0 and cos_sdp>=0:
        return 0 #SA^Low
    elif cos_dsp>=0 and cos_sdp >=0:
        return 1 #SA^Mid
    else:
        return 2 #SA^High

def find_path(s, d, points):
    # 初始化路径和区域划分
    path = []
    low_region = []
    mid_region = []
    high_region = []
    original_s=s
    # 第一步：根据点的区域分类
    for p in points:
        region = get_region(p, s, d)
        if region == 0:
            low_region.append(p)
        elif region == 1:
            mid_region.append(p)
        else:
            high_region.append(p)
    # print("Low Region:", low_region)
    # print("Mid Region:", mid_region)
    # print("High Region:", high_region)
    # 第二步：在 Mid 区域找 B1 点，将 D 作为 B2 点
    B2 = d
    if mid_region:
        # (1) Mid 区域有点，取垂直距离最小的点作为 B1
        min_dist = float('inf')
        B1 = None
        for p in mid_region:
            dist = calculate_distance(p, s)
            if dist < min_dist:
                min_dist = dist
                B1 = p
        if B1 is not None:
            # 用列表推导式代替 remove，移除 B1
            mid_region = [p for p in mid_region if not np.array_equal(p, B1)]
    elif high_region:
        # (2) Mid 区域没有点，从 High 中随机选择一个点作为 B1
        B1 = high_region.pop(np.random.randint(len(high_region)))
    else:
        # (3) Mid 和 High 都没有点，取 B1 = D
        B1 = B2

    # 第三步：从 Low 区域开始找路径节点 P，使得 ∠(S, B1, P) 最小
    while low_region:
        best_p = None
        min_angle = float('inf')
        for p in low_region:
            angle = calculate_angle(s, B1, p)
            if angle < min_angle:
                min_angle = angle
                best_p = p
        if best_p is not None:
            path.append(best_p)
            # 用列表推导式代替 remove，移除 best_p
            low_region = [p for p in low_region if not np.array_equal(p, best_p)]
            s = best_p  # 更新 S 为 P

    # 第四步：将 B1 加入 Path
    path.append(B1)

    # 第五步：连接 B1, B2，在 Mid 区域找路径节点 P，使得 |P, B1B2| 最小
    while mid_region:
        best_p = None
        min_dist = float('inf')
        for p in mid_region:
            dist = calculate_distance(p, B1)
            if dist < min_dist:
                min_dist = dist
                best_p = p
        if best_p is not None:
            path.append(best_p)
            # 用列表推导式代替 remove，移除 best_p
            mid_region = [p for p in mid_region if not np.array_equal(p, best_p)]
            B1 = best_p  # 更新 B1 为 P

    # 第六步：在 High 区域找路径节点 P，使得 |P, P_last - B2| 最小
    P_last = path[-1]  # Path 中最后一个点
    while high_region:
        best_p = None
        min_dist = float('inf')
        for p in high_region:
            dist = calculate_distance(p, P_last)
            if dist < min_dist:
                min_dist = dist
                best_p = p
        if best_p is not None:
            path.append(best_p)
            # 用列表推导式代替 remove，移除 best_p
            high_region = [p for p in high_region if not np.array_equal(p, best_p)]
            P_last = best_p  # 更新 P_last 为 P

    # 最后将 B2 加入 Path
    path.append(B2)
    path.insert(0,original_s)
    return path


def calculate_angle(a, b, c):
    """
    计算夹角 (A, B, C) 的角度值。
    """
    ab = np.array(b) - np.array(a)
    bc = np.array(c) - np.array(b)
    cos_angle = np.dot(ab, bc) / (np.linalg.norm(ab) * np.linalg.norm(bc))
    return degrees(acos(cos_angle))

def calculate_distance(p1, p2):
    """
    计算两点之间的欧几里得距离。
    """
    return np.linalg.norm(np.array(p1) - np.array(p2))


if __name__=="__main__":
    Size= 1000*1000
    N = 2000 #所有点的数量
    k=150
    times=[]
    for i in range(10):
        start = time.perf_counter()
        # 生成二维欧几里得空间中的随机整数点，区间为 [0, Size]
        points_set = np.random.randint(0, Size+1, size=(N, 2))  # 生成 intermediate_points_count 个二维点，坐标为整数
        indices = np.random.choice(points_set.shape[0], size=k, replace=False)
        intermediate_points = points_set[indices]
        # intermediate_points = np.random.randint(0, Size+1, size=(k, 2))  # 生成 intermediate_points_count 个二维点，坐标为整数


        # 第一个点作为起点 s，最后一个点作为终点 d
        s = np.random.randint(0, Size+1, size=(2,))
        d = np.random.randint(0, Size+1, size=(2,))
        # divide_map(s,d,Size)

        path = find_path(s, d, intermediate_points) 
        end = time.perf_counter()
        times.append(end-start)
    # 打印最终路径
    # print("Final Path:", path)
    avg=sum(times)/len(times)
    print(f"Time elapsed: {(avg):.6f}s")
