import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { makeStyles } from 'tss-react/mui';
import { Theme } from '@mui/material/styles';
import Container from '@mui/material/Container';
import {Dialog, DialogContent, DialogTitle, IconButton, useMediaQuery, useTheme} from '@mui/material';
import {Close} from '@mui/icons-material';
import {Controller, useForm} from 'react-hook-form';
import StateSelect from './StateSelect';
import {StateCode} from './State';
import {useAuth} from "../auth/Auth";

const useStyles = makeStyles()((theme: Theme) => ({
    paper: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(3),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    close: {
        padding: '0 10px',
        textAlign: 'end',
        lineHeight: '1rem',
    },
}));

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
    const { classes } = useStyles();
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const {refreshAuth} = useAuth();

    const {register, handleSubmit, control, formState: {errors}} = useForm<SignUpRequest>();
    const onSubmit = (data: SignUpRequest) => {
        fetch('/normal-users', {
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
            <DialogTitle className={classes.close}>
                <IconButton edge="end" color="inherit" onClick={onClose} aria-label="close">
                    <Close/>
                </IconButton>
            </DialogTitle>
            <DialogContent>
                <Container component="main" maxWidth="xs">
                    <div className={classes.paper}>
                        <Avatar className={classes.avatar}>
                            <LockOutlinedIcon/>
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            Sign up
                        </Typography>
                        <form className={classes.form} noValidate onSubmit={handleSubmit(onSubmit)}>
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
                                className={classes.submit}
                            >
                                Sign Up
                            </Button>
                        </form>
                    </div>
                </Container>
            </DialogContent>
        </Dialog>
    );
}