import React, { useContext } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { BarChart3, CreditCard, TrendingUp, Target, PieChart, Settings, LogOut } from 'lucide-react'
import AuthContext from '../context/AuthContext'

const Sidebar = () => {
  const location = useLocation()
  const { logout } = useContext(AuthContext)

  const menuItems = [
    { path: '/', label: 'Dashboard', icon: BarChart3 },
    { path: '/expenses', label: 'Expenses', icon: CreditCard },
    { path: '/income', label: 'Income', icon: TrendingUp },
    { path: '/budgets', label: 'Budgets', icon: PieChart },
    { path: '/goals', label: 'Goals', icon: Target },
    { path: '/analytics', label: 'Analytics', icon: BarChart3 },
  ]

  const isActive = (path) => location.pathname === path

  return (
    <div className="fixed left-0 top-0 h-screen w-64 glass-effect border-r border-neon-purple/20 p-6 flex flex-col">
      <h1 className="text-3xl font-bold glow-text mb-8">Finova 3D</h1>

      <nav className="flex-1 space-y-4">
        {menuItems.map(({ path, label, icon: Icon }) => (
          <Link
            key={path}
            to={path}
            className={`flex items-center space-x-3 px-4 py-3 rounded-lg transition-all ${
              isActive(path)
                ? 'bg-neon-purple/30 text-neon-purple border border-neon-purple/50'
                : 'text-gray-300 hover:text-neon-purple hover:bg-neon-purple/10'
            }`}
          >
            <Icon size={20} />
            <span>{label}</span>
          </Link>
        ))}
      </nav>

      <button
        onClick={logout}
        className="flex items-center space-x-3 px-4 py-3 rounded-lg text-red-400 hover:bg-red-500/10 transition-all w-full"
      >
        <LogOut size={20} />
        <span>Logout</span>
      </button>
    </div>
  )
}

export default Sidebar
