# 屏幕自动滑动助手

## 项目简介
这是一个Android应用程序，提供屏幕自动滑动功能，主要用于浏览长页面内容。应用包含一个可拖动的悬浮窗，用户可以通过悬浮窗控制滑动的开始和停止。

## 主要功能
1. 悬浮窗控制
   - 可拖动的悬浮窗界面
   - 应用关闭后悬浮窗状态保持
   - 提供关闭悬浮窗的选项

2. 自动滑动
   - 支持向上和向下滑动
   - 滑动速度可调节
   - 开始滑动3分钟后自动停止

## 技术架构
1. 悬浮窗实现
   - 使用WindowManager实现悬浮窗
   - 通过SharedPreferences保存悬浮窗状态

2. 滑动控制
   - 使用AccessibilityService实现屏幕滑动
   - Handler实现定时器功能

3. 权限管理
   - 悬浮窗权限
   - 辅助功能权限

## 项目结构
```
app/
  ├── java/
  │   └── com.example.screensliding/
  │       ├── MainActivity.java          # 主活动
  │       ├── FloatingWindowService.java # 悬浮窗服务
  │       ├── AutoScrollService.java     # 自动滑动服务
  │       └── utils/
  │           ├── PreferencesUtil.java   # 配置管理
  │           └── PermissionUtil.java    # 权限管理
  └── res/
      ├── layout/
      │   ├── activity_main.xml         # 主界面布局
      │   └── float_window.xml          # 悬浮窗布局
      └── values/
          └── strings.xml               # 字符串资源
```

## 使用说明
1. 首次使用需要授予以下权限：
   - 悬浮窗权限
   - 辅助功能权限

2. 操作步骤：
   - 点击悬浮窗开始按钮启动自动滑动
   - 再次点击暂停滑动
   - 长按悬浮窗可以关闭

3. 注意事项：
   - 开始滑动后3分钟会自动停止
   - 应用关闭后悬浮窗会保持显示
   - 如需重新显示悬浮窗，请重新打开应用

## 后续优化
1. 添加滑动速度调节功能
2. 支持自定义滑动时间
3. 添加滑动方向切换按钮
4. 优化悬浮窗UI设计
5. 添加电池优化白名单