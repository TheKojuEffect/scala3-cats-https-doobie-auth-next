import React from 'react';
import TextField from '@mui/material/TextField';
import {makeStyles} from 'tss-react/mui';
import {State, states} from "./State";
import {Autocomplete, FilterOptionsState} from "@mui/material";
import Box from "@mui/material/Box";

const useStyles = makeStyles()({
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
    const {classes} = useStyles();

    return (
        <Autocomplete
            options={states}
            classes={{option: classes.option}}
            autoHighlight
            getOptionLabel={(option: State) => `${option.name} (${option.code})`}
            renderOption={(props, state: State) => (
                <Box component="li" {...props}>
                    {state.name} ({state.code})
                </Box>
            )}
            onChange={(event, value) => onChange(value)}
            filterOptions={
                (options: State[], state: FilterOptionsState<State>) => {
                    const input = state.inputValue.toLocaleLowerCase();
                    return options.filter(
                        o => o.name.toLocaleLowerCase().includes(input)
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

