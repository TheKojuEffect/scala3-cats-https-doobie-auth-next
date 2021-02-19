import React from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import {makeStyles} from '@material-ui/core/styles';
import {FilterOptionsState} from "@material-ui/lab";
import {State, States} from "./State";

const useStyles = makeStyles({
    option: {
        fontSize: 15,
        '& > span': {
            marginRight: 10,
            fontSize: 18,
        },
    },
});

interface StateSelectProps {
    onChange: (state: State | null) => void;
    error?: boolean;
}

export default function StateSelect({onChange, error}: StateSelectProps) {
    const classes = useStyles();

    return (
        <Autocomplete
            options={States}
            classes={{
                option: classes.option,
            }}
            autoHighlight
            getOptionLabel={option => option.label}
            renderOption={(option) => (
                <>
                    {option.label} ({option.code})
                </>
            )}
            onChange={(event, value) => onChange(value)}
            filterOptions={
                (options: State[], state: FilterOptionsState<State>) => {
                    const input = state.inputValue.toLocaleLowerCase();
                    return options.filter(
                        o => o.label.toLocaleLowerCase().includes(input)
                            || o.code.toLocaleLowerCase().includes(input)
                    );
                }
            }
            renderInput={(params) => (
                <TextField
                    {...params}
                    required
                    label="State"
                    variant="outlined"
                    inputProps={{
                        ...params.inputProps,
                        autoComplete: 'address-level1',
                    }}
                    error={error}
                />
            )}
        />
    );
}

