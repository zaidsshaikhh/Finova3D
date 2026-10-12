import React from 'react'

const AuthContext = React.createContext({
  token: null,
  user: null,
  login: () => {},
  logout: () => {}
})

export default AuthContext
