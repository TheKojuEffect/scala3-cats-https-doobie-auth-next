import React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import {Dialog, DialogContent, DialogTitle, IconButton, useMediaQuery, useTheme} from "@mui/material";
import {Close} from "@mui/icons-material";
import {useForm} from "react-hook-form";
import {useAuth} from "../auth/Auth";
import Box from "@mui/material/Box";

type SignInRequest = {
    email: string,
    password: string,
};

interface SignInDialogProps {
    open: boolean;
    onClose: () => void;
}

export default function SignInDialog({open, onClose}: SignInDialogProps) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const {refreshAuth} = useAuth();

    const {register, handleSubmit, formState: {errors}} = useForm<SignInRequest>();
    const onSubmit = (data: SignInRequest) => {
        fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data),
            }
        )
            .then(onClose)
            .then(refreshAuth)
    }

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullScreen={fullScreen}
        >
            <DialogTitle sx={{padding: '0 10px', textAlign: 'end', lineHeight: '1rem',}}>
                <IconButton edge="end" color="inherit" onClick={onClose} aria-label="close">
                    <Close/>
                </IconButton>
            </DialogTitle>
            <DialogContent>
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                        <LockOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Sign in
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate sx={{mt: 1}}>
                        <TextField
                            autoFocus
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            autoComplete="email"
                            {...register('email', {required: true})}
                            error={!!errors.email}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            {...register('password', {required: true})}
                            error={!!errors.password}
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2}}
                        >
                            Sign In
                        </Button>
                    </Box>
                </Box>
            </DialogContent>
        </Dialog>
    );
}