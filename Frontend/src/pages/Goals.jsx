import React from 'react'
import { motion } from 'framer-motion'
import Sidebar from '../components/Sidebar'
import AnimatedBackground from '../components/AnimatedBackground'

const Goals = () => {
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
          <h1 className="text-4xl font-bold glow-text mb-8">Financial Goals</h1>
          <div className="glass-effect rounded-2xl p-8 text-center">
            <p className="text-gray-400 text-lg">Financial goals management coming soon...</p>
          </div>
        </motion.div>
      </div>
    </div>
  )
}

export default Goals
