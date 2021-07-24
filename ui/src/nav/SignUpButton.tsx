import {Button} from "@material-ui/core";
import React, {useState} from "react";
import SignUpDialog from "./SignUpDialog";

export default function SignUpButton() {
    const [signUpModalOpen, setSignUpModalOpen] = useState(false);

    const openSignUpModal = () => {
        setSignUpModalOpen(true);
    };

    const handleSignUpModalClose = () => {
        setSignUpModalOpen(false);
    }

    return (
        <>
            <Button color="inherit" onClick={openSignUpModal}>Sign Up</Button>
            <SignUpDialog open={signUpModalOpen} onClose={handleSignUpModalClose}/>
        </>
    )
}