import React from 'react';
import {CssBaseline} from "@mui/material";
import NavBar from "./nav/NavBar";
import {AuthProvider} from "./auth/Auth";

import {createTheme, ThemeProvider} from '@mui/material/styles';

const theme = createTheme();

function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <AuthProvider>
                <NavBar/>
            </AuthProvider>
        </ThemeProvider>
    );
}

export default App;