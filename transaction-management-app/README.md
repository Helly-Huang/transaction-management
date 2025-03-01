# 交易管理系统（前端）

这是一个基于 React 的前端项目，实现了交易记录的增删改查（CRUD）功能，并与后端 API 进行交互。

---

## 技术栈

- **前端框架**：React
- **HTTP客户端**：Axios
- **样式**：CSS
- **构建工具**：Create React App


---

## 环境配置

### 1. **Node.js 和 npm**

检查 Node.js 和 npm 版本：

```bash
node -v
npm -v
```

### 2. **安装依赖**

在项目根目录下运行以下命令，安装项目依赖：

```bash
npm install
```

### 3. **配置文件**

在 `src/config.js` 中配置后端 API 地址：

```javascript
const config = {
  apiBaseUrl: 'http://localhost:8081/api/transaction', // 后端 API 地址
};

export default config;
```

---

## 常用命令

### 1. **启动开发服务器**

在开发环境中运行项目：

```bash
npm start
```

项目将运行在 `http://localhost:3000`

### 2. **打包项目**

将项目打包为生产环境代码：

```bash
npm run build
```

打包后的文件将生成在 `build` 文件夹中

---

## 项目结构

以下是项目的主要文件结构：

```
my-frontend/
├── public/                  # 静态资源
├── src/                     # 源代码
│   ├── config.js            # 配置文件
│   ├── TransactionApp.js    # 主组件
│   ├── TransactionApp.css   # 样式文件
│   ├── App.js               # 根组件
│   ├── index.js             # 入口文件
│   └── index.css            # 全局样式
├── package.json             # 项目依赖和脚本
└── README.md                # 项目文档
```

---

## 功能说明

### 1. **交易记录列表**
- 显示所有交易记录，支持分页。
- 每页最多显示 10 条记录。

### 2. **创建交易记录**
- 提供表单用于创建新的交易记录。
- 支持字段：金额、货币类型、交易类型、类别、描述、状态。

### 3. **编辑交易记录**
- 点击“Edit”按钮进入编辑模式。
- 修改交易记录后，点击“保存”按钮更新数据。

### 4. **删除交易记录**
- 点击“Delete”按钮删除交易记录。

### 5. **分页功能**
- 支持上一页和下一页操作。
