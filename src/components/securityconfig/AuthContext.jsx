import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authState, setAuthState] = useState({
        token: localStorage.getItem('token'), // get the token from local storage
        isAuthenticated: false,
        userRoles: null,
    });

    useEffect(() => {
        if (authState.token) {
            // Here you can decode the JWT and set user roles
            // For example, using a library to decode JWT and extract roles
            // const decoded = jwt_decode(authState.token);
            // setAuthState({ ...authState, isAuthenticated: true, userRoles: decoded.roles });

            setAuthState({ ...authState, isAuthenticated: true });
        }
    }, [authState.token]);

    return (
        <AuthContext.Provider value={{ authState, setAuthState }}>
            {children}
        </AuthContext.Provider>
    );
};
