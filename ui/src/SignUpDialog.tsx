import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import {makeStyles, Theme} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import {Dialog, DialogContent, DialogTitle, IconButton, useMediaQuery, useTheme} from '@material-ui/core';
import {Close} from '@material-ui/icons';
import {Controller, useForm} from 'react-hook-form';
import StateSelect from './StateSelect';
import {StateCode} from './State';
import {useAuth} from "./auth/Auth";

const useStyles = makeStyles((theme: Theme) => ({
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
    const classes = useStyles();
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const {refreshAuth} = useAuth();

    const {register, handleSubmit, control, errors} = useForm<SignUpRequest>();
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
                                        name="firstName"
                                        variant="outlined"
                                        required
                                        fullWidth
                                        label="First Name"
                                        autoFocus
                                        inputRef={register({
                                            required: true
                                        })}
                                        error={!!errors.firstName}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        variant="outlined"
                                        required
                                        fullWidth
                                        label="Last Name"
                                        name="lastName"
                                        autoComplete="family-name"
                                        inputRef={register({
                                            required: true
                                        })}
                                        error={!!errors.lastName}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name="state"
                                        control={control}
                                        rules={{required: true}}
                                        defaultValue={null}
                                        render={
                                            ({onChange}) => (
                                                <StateSelect
                                                    error={!!errors.state}
                                                    onChange={(state) => {
                                                        onChange(state?.code)
                                                    }}/>)
                                        }
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        variant="outlined"
                                        required
                                        fullWidth
                                        label="Zip Code"
                                        name="zipCode"
                                        autoComplete="postal_code"
                                        inputRef={register({
                                            required: true
                                        })}
                                        error={!!errors.zipCode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        variant="outlined"
                                        required
                                        fullWidth
                                        label="Email Address"
                                        name="email"
                                        autoComplete="email"
                                        inputRef={register({
                                            required: true
                                        })}
                                        error={!!errors.email}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        variant="outlined"
                                        required
                                        fullWidth
                                        name="password"
                                        label="Password"
                                        type="password"
                                        autoComplete="new-password"
                                        inputRef={register({
                                            required: true
                                        })}
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
                            <Grid container justify="flex-end">
                                <Grid item>
                                    <Link href="#" variant="body2">
                                        Already have an account? Sign in
                                    </Link>
                                </Grid>
                            </Grid>
                        </form>
                    </div>
                </Container>
            </DialogContent>
        </Dialog>
    );
}