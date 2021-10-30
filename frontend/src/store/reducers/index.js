import {objectReducer} from "./objectReducer";
import {combineReducers} from "redux";
import {filterReducer} from "./filterReducer";

export const rootReducer = combineReducers({
    object: objectReducer,
    filter: filterReducer,
})
