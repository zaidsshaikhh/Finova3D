import React from 'react'
import { motion } from 'framer-motion'

const StatCard = ({ title, value, change, icon: Icon, color = 'purple' }) => {
  const colorMap = {
    purple: 'from-neon-purple to-pink-500',
    green: 'from-neon-green to-cyan-500',
    pink: 'from-neon-pink to-red-500',
    cyan: 'from-neon-cyan to-blue-500',
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="glass-effect rounded-2xl p-6 card-hover relative overflow-hidden group"
    >
      <div className="absolute inset-0 bg-gradient-to-r opacity-0 group-hover:opacity-10 transition-opacity" />

      <div className="flex items-start justify-between mb-4">
        <div>
          <p className="text-gray-400 text-sm mb-2">{title}</p>
          <p className="text-3xl font-bold text-white">{value}</p>
        </div>
        {Icon && <Icon size={28} className="text-neon-purple" />}
      </div>

      {change && (
        <div className={`text-sm ${change >= 0 ? 'text-neon-green' : 'text-red-400'}`}>
          {change >= 0 ? '↑' : '↓'} {Math.abs(change)}% from last month
        </div>
      )}
    </motion.div>
  )
}

export default StatCard
