import {Button} from "@material-ui/core";
import React, {useState} from "react";
import SignInDialog from "./SignInDialog";

export default function SignInButton() {
    const [signInModalOpen, setSignInModalOpen] = useState(false);

    const openSignInModal = () => {
        setSignInModalOpen(true);
    };

    const handleSignInModalClose = () => {
        setSignInModalOpen(false);
    }

    return (
        <>
            <Button color="inherit" onClick={openSignInModal}>Sign In</Button>
            <SignInDialog open={signInModalOpen} onClose={handleSignInModalClose}/>
        </>
    );
}