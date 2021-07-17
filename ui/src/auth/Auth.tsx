import React, {FC, useContext, useEffect, useState} from "react";

type Auth = {
    loading: boolean;
    authenticated: boolean;
    refreshAuth: () => void
}

const AuthContext = React.createContext<Auth | undefined>(undefined);

export const AuthProvider: FC = ({children}) => {
    const [loading, setLoading] = useState(false);
    const [authenticated, setAuthenticated] = useState(false);

    function refreshAuth() {
        setLoading(true);
        fetch('/current-user')
            .then(({status}) => {
                setAuthenticated(status === 200);
            })
            .finally(() => {
                setLoading(false);
            });
    }

    useEffect(refreshAuth, []);

    return (
        <AuthContext.Provider value={{loading, authenticated, refreshAuth}}>
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