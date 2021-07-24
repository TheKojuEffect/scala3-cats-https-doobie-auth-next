import React from 'react';
import {CssBaseline} from "@material-ui/core";
import NavBar from "./nav/NavBar";
import {AuthProvider} from "./auth/Auth";

function App() {
    return (
        <>
            <CssBaseline/>
            <AuthProvider>
                <NavBar/>
            </AuthProvider>
        </>
    );
}

export default App;