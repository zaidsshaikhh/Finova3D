import React, { useEffect, useState, useContext } from 'react'
import axios from 'axios'
import { motion } from 'framer-motion'
import Sidebar from '../components/Sidebar'
import AnimatedBackground from '../components/AnimatedBackground'
import AuthContext from '../context/AuthContext'
import { Plus, Trash2, Edit2 } from 'lucide-react'

const Expenses = () => {
  const { token } = useContext(AuthContext)
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    title: '',
    amount: '',
    categoryId: '',
    transactionDate: new Date().toISOString().split('T')[0],
    paymentMethod: 'Cash',
    merchant: '',
    notes: '',
  })

  useEffect(() => {
    fetchExpenses()
  }, [token])

  const fetchExpenses = async () => {
    try {
      const response = await axios.get('/api/expenses', {
        headers: { Authorization: `Bearer ${token}` },
      })
      setExpenses(response.data)
    } catch (error) {
      console.error('Error fetching expenses:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await axios.post('/api/expenses', formData, {
        headers: { Authorization: `Bearer ${token}` },
      })
      fetchExpenses()
      setShowForm(false)
      setFormData({
        title: '',
        amount: '',
        categoryId: '',
        transactionDate: new Date().toISOString().split('T')[0],
        paymentMethod: 'Cash',
        merchant: '',
        notes: '',
      })
    } catch (error) {
      console.error('Error creating expense:', error)
    }
  }

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/expenses/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      fetchExpenses()
    } catch (error) {
      console.error('Error deleting expense:', error)
    }
  }

  return (
    <div className="min-h-screen bg-dark-900">
      <AnimatedBackground />
      <Sidebar />

      <div className="ml-64 p-8">
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-4xl font-bold glow-text">Expenses</h1>
            <button
              onClick={() => setShowForm(!showForm)}
              className="flex items-center space-x-2 bg-gradient-to-r from-neon-purple to-neon-pink px-6 py-3 rounded-lg hover:opacity-90 transition-opacity"
            >
              <Plus size={20} />
              <span>New Expense</span>
            </button>
          </div>

          {showForm && (
            <motion.div
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              className="glass-effect rounded-2xl p-6 mb-8"
            >
              <h2 className="text-2xl font-bold mb-6">Add New Expense</h2>
              <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
                <input
                  type="text"
                  placeholder="Title"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-neon-purple transition-colors col-span-2"
                  required
                />
                <input
                  type="number"
                  placeholder="Amount"
                  value={formData.amount}
                  onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-neon-purple transition-colors"
                  required
                />
                <input
                  type="date"
                  value={formData.transactionDate}
                  onChange={(e) => setFormData({ ...formData, transactionDate: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-neon-purple transition-colors"
                  required
                />
                <select
                  value={formData.paymentMethod}
                  onChange={(e) => setFormData({ ...formData, paymentMethod: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-purple transition-colors"
                >
                  <option value="Cash">Cash</option>
                  <option value="Card">Card</option>
                  <option value="Transfer">Transfer</option>
                </select>
                <input
                  type="text"
                  placeholder="Merchant"
                  value={formData.merchant}
                  onChange={(e) => setFormData({ ...formData, merchant: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-neon-purple transition-colors col-span-2"
                />
                <textarea
                  placeholder="Notes"
                  value={formData.notes}
                  onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                  className="bg-dark-700/50 border border-neon-purple/30 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-neon-purple transition-colors col-span-2 h-20"
                />
                <div className="col-span-2 flex space-x-4">
                  <button
                    type="submit"
                    className="flex-1 bg-gradient-to-r from-neon-purple to-neon-pink py-3 rounded-lg font-semibold hover:opacity-90 transition-opacity"
                  >
                    Add Expense
                  </button>
                  <button
                    type="button"
                    onClick={() => setShowForm(false)}
                    className="flex-1 bg-dark-700/50 border border-neon-purple/30 py-3 rounded-lg font-semibold hover:bg-dark-700 transition-colors"
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </motion.div>
          )}

          <div className="space-y-4">
            {loading ? (
              <div className="text-center py-8 text-gray-400">Loading expenses...</div>
            ) : expenses.length === 0 ? (
              <div className="text-center py-8 text-gray-400">No expenses yet</div>
            ) : (
              expenses.map((expense, index) => (
                <motion.div
                  key={expense.id}
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: index * 0.05 }}
                  className="glass-effect rounded-xl p-4 flex items-center justify-between card-hover"
                >
                  <div>
                    <p className="font-semibold text-lg">{expense.title}</p>
                    <p className="text-gray-400 text-sm">
                      {expense.categoryName} • {expense.transactionDate}
                    </p>
                  </div>
                  <div className="flex items-center space-x-6">
                    <p className="text-2xl font-bold text-neon-pink">${expense.amount.toFixed(2)}</p>
                    <button
                      onClick={() => handleDelete(expense.id)}
                      className="text-red-400 hover:text-red-300 transition-colors"
                    >
                      <Trash2 size={20} />
                    </button>
                  </div>
                </motion.div>
              ))
            )}
          </div>
        </motion.div>
      </div>
    </div>
  )
}

export default Expenses
