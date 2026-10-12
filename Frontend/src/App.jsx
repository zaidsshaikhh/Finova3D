import React, { useState } from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Expenses from './pages/Expenses'
import Income from './pages/Income'
import Budgets from './pages/Budgets'
import Goals from './pages/Goals'
import Analytics from './pages/Analytics'
import AuthContext from './context/AuthContext'

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'))
  const [user, setUser] = useState(localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null)

  const login = (authToken, userData) => {
    setToken(authToken)
    setUser(userData)
    localStorage.setItem('token', authToken)
    localStorage.setItem('user', JSON.stringify(userData))
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      <Router>
        <Routes>
          <Route path="/login" element={token ? <Navigate to="/" /> : <Login />} />
          <Route path="/register" element={token ? <Navigate to="/" /> : <Register />} />
          <Route path="/" element={token ? <Dashboard /> : <Navigate to="/login" />} />
          <Route path="/expenses" element={token ? <Expenses /> : <Navigate to="/login" />} />
          <Route path="/income" element={token ? <Income /> : <Navigate to="/login" />} />
          <Route path="/budgets" element={token ? <Budgets /> : <Navigate to="/login" />} />
          <Route path="/goals" element={token ? <Goals /> : <Navigate to="/login" />} />
          <Route path="/analytics" element={token ? <Analytics /> : <Navigate to="/login" />} />
        </Routes>
      </Router>
    </AuthContext.Provider>
  )
}

export default App
