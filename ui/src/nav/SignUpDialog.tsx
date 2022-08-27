import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import {Dialog, DialogContent, DialogTitle, IconButton, useMediaQuery, useTheme} from '@mui/material';
import {Close} from '@mui/icons-material';
import {Controller, useForm} from 'react-hook-form';
import StateSelect from './StateSelect';
import {StateCode} from './State';
import {useAuth} from "../auth/Auth";
import Box from "@mui/material/Box";

interface SignUpDialogProps {
    open: boolean;
    onClose: () => void;
}

type SignUpRequest = {
    firstName: string,
    lastName: string,
    state: StateCode,
    zipCode: string,
    email: string,
    password: string
};

export default function SignUpDialog({open, onClose}: SignUpDialogProps) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const {refreshAuth} = useAuth();

    const {register, handleSubmit, control, formState: {errors}} = useForm<SignUpRequest>();
    const onSubmit = (data: SignUpRequest) => {
        fetch('/api/normal-users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data),
            }
        )
            .then(onClose)
            .then(refreshAuth);
    }

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullScreen={fullScreen}
        >
            <DialogTitle sx={{padding: '0 10px', textAlign: 'end', lineHeight: '1rem'}}>
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
                        Sign up
                    </Typography>
                    <Box component="form" noValidate onSubmit={handleSubmit(onSubmit)} sx={{mt: 3}}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="given-name"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    label="First Name"
                                    autoFocus
                                    {...register('firstName', {required: true})}
                                    error={!!errors.firstName}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    label="Last Name"
                                    autoComplete="family-name"
                                    {...register('lastName', {required: true})}
                                    error={!!errors.lastName}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Controller
                                    name="state"
                                    control={control}
                                    rules={{required: true}}
                                    defaultValue={undefined}
                                    render={
                                        ({field: {onChange}}) =>
                                            <StateSelect
                                                error={!!errors.state}
                                                onChange={(state) => {
                                                    onChange(state?.code)
                                                }}/>
                                    }
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    label="Zip Code"
                                    autoComplete="postal_code"
                                    {...register('zipCode', {required: true})}
                                    error={!!errors.zipCode}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    label="Email Address"
                                    autoComplete="email"
                                    {...register('email', {required: true})}
                                    error={!!errors.email}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    label="Password"
                                    type="password"
                                    autoComplete="new-password"
                                    {...register('password', {required: true})}
                                    error={!!errors.password}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            sx={{mt: 3, mb: 2}}
                        >
                            Sign Up
                        </Button>
                    </Box>
                </Box>
            </DialogContent>
        </Dialog>
    );
}