# 用编译原理的方式打开计算器

## 结果如下：

![avatar](pic.jpg)

![avatar](1.gif)

![demo](demo.gif)

## 参考文章：

`https://zhuanlan.zhihu.com/p/24457041?refer=hinus`

## 主要文件

src/com/company处存放主要的代码

`AlgoFrame` : 是一个使用swing Graphics2D显示的框架，用于可视化。最新版保留该文件，用于测试多组样例

`Expression`: 是实现递归下降的主要代码。

`InputTokenStream`: 使用适配器思想，用于读入字节流输出Token流，即词法分析代码。

`MainForm` : 交互界面主要代码。

`Token` : 定义Token的类。

`TreeNode` : 通用的树，用于保存递归下降中间结果，实现了较强的可视化方法。

`Util` : 存放通用的工具方法。

## 提示

### 更改计算时间：

更改 Expression 类的 delay_time
同时根据需要设置 MainForm 类的 repaintInterval 刷新间隔小于 delay_time

### 更改显示大小：

更改 MainForm 类slider，设置其默认值，最大值，最小值。     
```
    JSlider slider = new JSlider(0, 30, 2);
```
