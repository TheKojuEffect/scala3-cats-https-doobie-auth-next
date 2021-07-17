import React, {FC, useContext, useEffect, useState} from "react";

export enum AuthStatus {
    UNAUTHENTICATED,
    LOADING,
    AUTHENTICATED,
}

type Auth = {
    status: AuthStatus;
    unauthenticated: boolean;
    loading: boolean;
    authenticated: boolean;
    refreshAuth: () => void;
}

const AuthContext = React.createContext<Auth | undefined>(undefined);

export const AuthProvider: FC = ({children}) => {
    let [status, setStatus] = useState(AuthStatus.UNAUTHENTICATED);

    function refreshAuth() {
        setStatus(AuthStatus.LOADING);
        fetch('/current-user')
            .then((response) => {
                setStatus(response.status === 200 ? AuthStatus.AUTHENTICATED : AuthStatus.UNAUTHENTICATED);
            });
    }

    useEffect(refreshAuth, []);

    const loading = status === AuthStatus.LOADING;
    const authenticated = status === AuthStatus.AUTHENTICATED;
    const unauthenticated = status === AuthStatus.UNAUTHENTICATED;

    return (
        <AuthContext.Provider value={{status, unauthenticated, loading, authenticated, refreshAuth}}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(): Auth {
    const auth = useContext(AuthContext);
    if (auth === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return auth;
}