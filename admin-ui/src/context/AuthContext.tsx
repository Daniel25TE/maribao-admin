import { createContext, useContext, useState } from 'react'
import { AUTH_KEY } from '../constants'

interface AuthContextType {
  isAuthenticated: boolean
  login: () => void
  logout: () => void
}

const AuthContext = createContext<AuthContextType | null>(null)

// wraps the whole app so any component can check login state
export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(
    localStorage.getItem(AUTH_KEY) === 'true'
  )

  function login() {
    localStorage.setItem(AUTH_KEY, 'true')
    setIsAuthenticated(true)
  }

  function logout() {
    localStorage.removeItem(AUTH_KEY)
    setIsAuthenticated(false)
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

// shortcut hook so components don't have to import useContext and AuthContext every time
export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used inside AuthProvider')
  return context
}
