import * as React from 'react';
import type {NextPage} from 'next';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import NavBar from "../src/nav/NavBar";

const Home: NextPage = () => {
    return (
        <Container maxWidth="lg">
            <Box
                sx={{
                    my: 4,
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'center',
                    alignItems: 'center',
                }}
            >
                <NavBar/>
            </Box>
        </Container>
    );
};

export default Home;
