/* eslint-disable no-use-before-define */
import React from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import {makeStyles} from '@material-ui/core/styles';
import {FilterOptionsState} from "@material-ui/lab";

const useStyles = makeStyles({
    option: {
        fontSize: 15,
        '& > span': {
            marginRight: 10,
            fontSize: 18,
        },
    },
});

export default function StateSelect() {
    const classes = useStyles();

    return (
        <Autocomplete
            id="state-select"
            options={states}
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
                    label="US Address State"
                    helperText="Which state do you live now in US?"
                    variant="outlined"
                    inputProps={{
                        ...params.inputProps,
                        autoComplete: 'new-password', // disable autocomplete and autofill
                    }}
                />
            )}
        />
    );
}

interface State {
    code: string;
    label: string;
}

const states: State[] = [
    {label: "Alabama", code: "AL"},
    {label: "Alaska", code: "AK"},
    {label: "Arizona", code: "AZ"},
    {label: "Arkansas", code: "AR"},
    {label: "California", code: "CA"},
    {label: "Colorado", code: "CO"},
    {label: "Connecticut", code: "CT"},
    {label: "Delaware", code: "DE"},
    {label: "District of Columbia", code: "DC"},
    {label: "Florida", code: "FL"},
    {label: "Georgia", code: "GA"},
    {label: "Hawaii", code: "HI"},
    {label: "Idaho", code: "ID"},
    {label: "Illinois", code: "IL"},
    {label: "Indiana", code: "IN"},
    {label: "Iowa", code: "IA"},
    {label: "Kansas", code: "KS"},
    {label: "Kentucky", code: "KY"},
    {label: "Louisiana", code: "LA"},
    {label: "Maine", code: "ME"},
    {label: "Maryland", code: "MD"},
    {label: "Massachusetts", code: "MA"},
    {label: "Michigan", code: "MI"},
    {label: "Minnesota", code: "MN"},
    {label: "Mississippi", code: "MS"},
    {label: "Missouri", code: "MO"},
    {label: "Montana", code: "MT"},
    {label: "Nebraska", code: "NE"},
    {label: "Nevada", code: "NV"},
    {label: "New Hampshire", code: "NH"},
    {label: "New Jersey", code: "NJ"},
    {label: "New Mexico", code: "NM"},
    {label: "New York", code: "NY"},
    {label: "North Carolina", code: "NC"},
    {label: "North Dakota", code: "ND"},
    {label: "Ohio", code: "OH"},
    {label: "Oklahoma", code: "OK"},
    {label: "Oregon", code: "OR"},
    {label: "Pennsylvania", code: "PA"},
    {label: "Rhode Island", code: "RI"},
    {label: "South Carolina", code: "SC"},
    {label: "South Dakota", code: "SD"},
    {label: "Tennessee", code: "TN"},
    {label: "Texas", code: "TX"},
    {label: "Utah", code: "UT"},
    {label: "Vermont", code: "VT"},
    {label: "Virginia", code: "VA"},
    {label: "Washington", code: "WA"},
    {label: "West Virginia", code: "WV"},
    {label: "Wisconsin", code: "WI"},
    {label: "Wyoming", code: "WY"},
];
