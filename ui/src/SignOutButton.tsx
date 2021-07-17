import {Button} from "@material-ui/core";
import React from "react";
import {useAuth} from "./auth/Auth";

export default function SignOutButton() {
    const {refreshAuth} = useAuth();

    const signOut = () => {
        fetch('/logout', {method: 'POST'})
            .then(refreshAuth);
    };

    return <Button color="inherit" onClick={signOut}>Sign Out</Button>;
}