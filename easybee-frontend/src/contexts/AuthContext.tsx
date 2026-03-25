import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import type { User } from '../types'; // Ton type User

// 1. On définit ce que contient notre "bulle" (le Contexte)
interface AuthContextType {
    user: User | null;
    login: (userData: User) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

// 2. On crée le composant "Fournisseur" qui va envelopper l'application
export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(() => {
        const savedUser = localStorage.getItem('easybee_user');
        return savedUser ? JSON.parse(savedUser) : null;
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem('easybee_user', JSON.stringify(user));
        } else {
            localStorage.removeItem('easybee_user');
        }
    }, [user]);

    const login = (userData: User) => setUser(userData);
    const logout = () => setUser(null);

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// 3. Le petit Hook magique pour utiliser le contexte facilement
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth doit être utilisé dans un AuthProvider");
    return context;
};