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
                    my: 1,
                    display: 'flex',
                }}
            >
                <NavBar/>
            </Box>
        </Container>
    );
};

export default Home;
