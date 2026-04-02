import { createContext, useContext, useState, type ReactNode } from 'react';
import type { AuthResponse, UserInfo } from '../types';

interface AuthContextType {
  currentUser: UserInfo | null;
  token: string | null;
  login: (authData: AuthResponse) => void;
  logout: () => void;
  isAuthenticated: boolean;
  isAdmin: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('token'));
  const [currentUser, setCurrentUser] = useState<UserInfo | null>(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      try {
        const parsed: AuthResponse = JSON.parse(stored);
        return parsed.user || null;
      } catch {
        return null;
      }
    }
    return null;
  });

  const login = (authData: AuthResponse) => {
    setToken(authData.token);
    setCurrentUser(authData.user);
    localStorage.setItem('token', authData.token);
    localStorage.setItem('user', JSON.stringify(authData));
  };

  const logout = () => {
    setToken(null);
    setCurrentUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };

  const isAuthenticated = !!token;
  const isAdmin = currentUser?.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{ currentUser, token, login, logout, isAuthenticated, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
