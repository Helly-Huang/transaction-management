import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './TransactionApp.css'; // 引入 CSS 文件
import config from './config'; // 引入配置文件

const TransactionApp = () => {
  const [transactions, setTransactions] = useState([]);
  const [formData, setFormData] = useState({
    amount: '',
    currency: 'USD', // 默认货币类型
    type: 'DEPOSIT', // 默认交易类型
    category: '',
    description: '',
    status: 'PENDING', // 默认交易状态
  });
  const [editingTransaction, setEditingTransaction] = useState(null); // 当前正在编辑的交易记录
  const [pageNo, setPageNo] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [hasMore, setHasMore] = useState(true); // 是否还有更多数据

  // 获取所有交易记录
  const fetchTransactions = async () => {
    try {
      const response = await axios.get(`${config.apiBaseUrl}?pageNo=${pageNo}&pageSize=${pageSize}`);
      setTransactions(response.data.data.content);

      // 如果返回的数据少于 pageSize，说明没有更多数据了
      if (response.data.data.content.length < pageSize) {
        setHasMore(false);
      } else {
        // 检查下一页是否有数据
        const nextPageResponse = await axios.get(`${config.apiBaseUrl}?pageNo=${pageNo + 1}&pageSize=${pageSize}`);
        setHasMore(nextPageResponse.data.data.content.length > 0);
      }
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [pageNo, pageSize]);

  // 创建交易记录
  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await axios.post(config.apiBaseUrl, formData);
      setFormData({
        amount: '',
        currency: 'USD',
        type: 'DEPOSIT',
        category: '',
        description: '',
        status: 'PENDING',
      });
      fetchTransactions();
    } catch (error) {
      console.error('Error creating transaction:', error);
    }
  };

  // 更新交易记录
  const handleUpdate = async (id, updatedData) => {
    try {
      await axios.put(`${config.apiBaseUrl}/${id}`, updatedData);
      setEditingTransaction(null); // 清除编辑状态
      fetchTransactions();
    } catch (error) {
      console.error('Error updating transaction:', error);
    }
  };

  // 删除交易记录
  const handleDelete = async (id) => {
    try {
      await axios.delete(`${config.apiBaseUrl}/${id}`);
      // 删除后重新获取数据
      await fetchTransactions();
    } catch (error) {
      console.error('Error deleting transaction:', error);
    }
  };

  // 进入编辑模式
  const startEditing = (transaction) => {
    setEditingTransaction({ ...transaction }); // 复制交易记录到编辑状态
  };

  // 取消编辑模式
  const cancelEditing = () => {
    setEditingTransaction(null);
  };

  return (
    <div>
      <h1>Transaction Management</h1>

      {/* 创建表单 */}
      <form onSubmit={handleCreate}>
        <input
          type="number"
          placeholder="Amount"
          value={formData.amount}
          onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
          required
        />
        <select
          value={formData.currency}
          onChange={(e) => setFormData({ ...formData, currency: e.target.value })}
          required
        >
          <option value="USD">USD</option>
          <option value="EUR">EUR</option>
          <option value="CNY">CNY</option>
          {/* 添加更多货币类型 */}
        </select>
        <select
          value={formData.type}
          onChange={(e) => setFormData({ ...formData, type: e.target.value })}
          required
        >
          <option value="DEPOSIT">DEPOSIT</option>
          <option value="WITHDRAWAL">WITHDRAWAL</option>
          <option value="TRANSFER">TRANSFER</option>
          <option value="PAYMENT">PAYMENT</option>
          <option value="REFUND">REFUND</option>
        </select>
        <input
          type="text"
          placeholder="Category"
          value={formData.category}
          onChange={(e) => setFormData({ ...formData, category: e.target.value })}
        />
        <input
          type="text"
          placeholder="Description"
          value={formData.description}
          onChange={(e) => setFormData({ ...formData, description: e.target.value })}
        />
        <select
          value={formData.status}
          onChange={(e) => setFormData({ ...formData, status: e.target.value })}
          required
        >
          <option value="PENDING">Pending</option>
          <option value="COMPLETED">Completed</option>
          <option value="FAILED">Failed</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
        <button type="submit">创建</button>
      </form>

      <h2>Transaction List</h2>
      <table>
        <thead>
          <tr>
            <th>Amount</th>
            <th>Currency</th>
            <th>Type</th>
            <th>Category</th>
            <th>Description</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              {editingTransaction && editingTransaction.id === transaction.id ? (
                // 编辑模式
                <>
                  <td>
                    <input
                      type="number"
                      value={editingTransaction.amount}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          amount: e.target.value,
                        })
                      }
                      required
                    />
                  </td>
                  <td>
                    <select
                      value={editingTransaction.currency}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          currency: e.target.value,
                        })
                      }
                      required
                    >
                      <option value="USD">USD</option>
                      <option value="EUR">EUR</option>
                      <option value="CNY">CNY</option>
                      {/* 添加更多货币类型 */}
                    </select>
                  </td>
                  <td>
                    <select
                      value={editingTransaction.type}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          type: e.target.value,
                        })
                      }
                      required
                    >
                      <option value="DEPOSIT">DEPOSIT</option>
                      <option value="WITHDRAWAL">WITHDRAWAL</option>
                      <option value="TRANSFER">TRANSFER</option>
                      <option value="PAYMENT">PAYMENT</option>
                      <option value="REFUND">REFUND</option>
                    </select>
                  </td>
                  <td>
                    <input
                      type="text"
                      value={editingTransaction.category}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          category: e.target.value,
                        })
                      }
                    />
                  </td>
                  <td>
                    <input
                      type="text"
                      value={editingTransaction.description}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          description: e.target.value,
                        })
                      }
                    />
                  </td>
                  <td>
                    <select
                      value={editingTransaction.status}
                      onChange={(e) =>
                        setEditingTransaction({
                          ...editingTransaction,
                          status: e.target.value,
                        })
                      }
                      required
                    >
                      <option value="PENDING">Pending</option>
                      <option value="COMPLETED">Completed</option>
                      <option value="FAILED">Failed</option>
                      <option value="CANCELLED">Cancelled</option>
                    </select>
                  </td>
                  <td>
                    <button onClick={() => handleUpdate(transaction.id, editingTransaction)}>保存</button>
                    <button onClick={cancelEditing}>取消</button>
                  </td>
                </>
              ) : (
                // 显示模式
                <>
                  <td>{transaction.amount}</td>
                  <td>{transaction.currency}</td>
                  <td>{transaction.type}</td>
                  <td>{transaction.category}</td>
                  <td>{transaction.description}</td>
                  <td>{transaction.status}</td>
                  <td>
                    <button onClick={() => startEditing(transaction)}>Edit</button>
                    <button onClick={() => handleDelete(transaction.id)}>Delete</button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>

      <div>
        <button onClick={() => setPageNo(pageNo - 1)} disabled={pageNo === 0}>
          上一页
        </button>
        <span>页码 {pageNo + 1}</span>
        <button onClick={() => setPageNo(pageNo + 1)} disabled={!hasMore}>
          下一页
        </button>
      </div>
    </div>
  );
};

export default TransactionApp;