import {SET_FILTER} from "../actions/filterAction";

const initialState = {
    pageIdx: 1,
    pageSize: 10,
    sortField: 'id',
}

export const filterReducer = (
    state = initialState,
    action) => {
    const {type, data} = action;
    switch (type){
        case SET_FILTER:
            return data;
        default:
            return state;
    }

}