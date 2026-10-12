import React, { useEffect, useState, useContext } from 'react'
import axios from 'axios'
import { motion } from 'framer-motion'
import Sidebar from '../components/Sidebar'
import StatCard from '../components/StatCard'
import AnimatedBackground from '../components/AnimatedBackground'
import AuthContext from '../context/AuthContext'
import { Wallet, TrendingUp, Target, CreditCard } from 'lucide-react'
import { Line, Bar, Doughnut } from 'react-chartjs-2'
import Chart from 'chart.js/auto'

const Dashboard = () => {
  const { token } = useContext(AuthContext)
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('/api/dashboard/summary', {
          headers: { Authorization: `Bearer ${token}` },
        })
        setSummary(response.data)
      } catch (error) {
        console.error('Error fetching dashboard data:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [token])

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <AnimatedBackground />
        <div className="text-2xl glow-text">Loading...</div>
      </div>
    )
  }

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
    },
    scales: {
      y: {
        ticks: { color: '#9ca3af' },
        grid: { color: 'rgba(199, 125, 255, 0.1)' },
      },
      x: {
        ticks: { color: '#9ca3af' },
        grid: { color: 'rgba(199, 125, 255, 0.1)' },
      },
    },
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
          <h1 className="text-4xl font-bold glow-text mb-8">Financial Report</h1>

          <div className="grid grid-cols-4 gap-6 mb-8">
            <StatCard
              title="Total Income"
              value={summary?.totalIncome ? `$${summary.totalIncome.toFixed(2)}` : '$0.00'}
              icon={TrendingUp}
              color="green"
            />
            <StatCard
              title="Total Expenses"
              value={summary?.totalExpenses ? `$${summary.totalExpenses.toFixed(2)}` : '$0.00'}
              icon={CreditCard}
              color="pink"
            />
            <StatCard
              title="Net Cash Flow"
              value={summary?.netCashFlow ? `$${summary.netCashFlow.toFixed(2)}` : '$0.00'}
              icon={Wallet}
              color="cyan"
            />
            <StatCard
              title="Savings Rate"
              value={summary?.savingsRate ? `${summary.savingsRate.toFixed(1)}%` : '0%'}
              icon={Target}
              color="purple"
            />
          </div>

          <div className="grid grid-cols-2 gap-6">
            <motion.div
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.5, delay: 0.2 }}
              className="glass-effect rounded-2xl p-6"
            >
              <h3 className="text-xl font-bold mb-4">Monthly Trends</h3>
              <div className="h-64">
                <Line
                  data={{
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [
                      {
                        label: 'Income',
                        data: [5000, 6000, 5500, 7000, 8000, 7500],
                        borderColor: '#00ff88',
                        backgroundColor: 'rgba(0, 255, 136, 0.1)',
                        tension: 0.4,
                      },
                      {
                        label: 'Expenses',
                        data: [3000, 3500, 4000, 3800, 4200, 4500],
                        borderColor: '#ff006e',
                        backgroundColor: 'rgba(255, 0, 110, 0.1)',
                        tension: 0.4,
                      },
                    ],
                  }}
                  options={chartOptions}
                />
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.5, delay: 0.2 }}
              className="glass-effect rounded-2xl p-6"
            >
              <h3 className="text-xl font-bold mb-4">Spending by Category</h3>
              <div className="h-64">
                <Doughnut
                  data={{
                    labels: ['Food', 'Transport', 'Entertainment', 'Utilities', 'Shopping'],
                    datasets: [
                      {
                        data: [30, 20, 15, 20, 15],
                        backgroundColor: ['#c77dff', '#ff006e', '#00f5ff', '#00ff88', '#ffd60a'],
                        borderColor: '#0a0e27',
                        borderWidth: 2,
                      },
                    ],
                  }}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      legend: {
                        labels: { color: '#9ca3af' },
                      },
                    },
                  }}
                />
              </div>
            </motion.div>
          </div>
        </motion.div>
      </div>
    </div>
  )
}

export default Dashboard
