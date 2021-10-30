import {API_PERSONS, sendRequest} from "../modules/api";
import converter from "xml2js";
import {decomposePersonToNormalView} from "../modules/helpers/decompose";
import {constructPersonToNormalView, OBJtoXML} from "../modules/helpers/constructors";
import {useSelector} from "react-redux";

export const useRequest = () => {

    const filterData = useSelector(store => store.filter);

    const validResult = (res) => {
        return res
            && res.person_result
            && Array.isArray(res.person_result.persons)
            && res.person_result.persons[0].person
    }

    const getErrorMessage = async (response) => {
        return await converter.parseStringPromise(response).then(res => {
            return {
                error: true,
                message: res.serverResponse
                    ? res.serverResponse.message[0]
                    : 'Unknown error'
            }
        })
    }

    const createItem = async (value) => {
        value = OBJtoXML({person: constructPersonToNormalView(value)});
        return await sendRequest('POST', API_PERSONS, value).then(async response => {
            if (response.error)
                return await getErrorMessage(response.message);
            return response;
        })
    }

    const updateItem = async (value) => {
        const id = value.id;
        value = OBJtoXML({person: constructPersonToNormalView(value)});
        return await sendRequest('PUT', API_PERSONS + `/${id}`, value).then(async response => {
            if (response.error)
                return await getErrorMessage(response.message);
            return response;
        })
    }

    const getItem = async (value) => {
        return await sendRequest('GET', API_PERSONS + `/${value}`).then(async response => {
            if (!response.error) {
                return await converter.parseStringPromise(response).then(res => {
                    if (res && res.person) {
                        return decomposePersonToNormalView(res.person)
                    } else {
                        return {error: true}
                    }
                })
            } else {
                return await getErrorMessage(response.message);
            }
        })
    }

    const constructParamString = (filter) => {
        let str = '?pageIdx=1&pageSize=10&sortField=id';
        if (Object.keys(filter).length > 0) {
            str = '?';
            Object.keys(filter).forEach(key => {
                let moduledKey = key;
                if (key.includes('_'))
                    moduledKey = moduledKey.split('_')[0] + moduledKey.split('_')[1].toUpperCase();
                if (filter[key] !== '' && filter[key] !== undefined)
                    str += `${moduledKey}=${filter[key]}&`
            });
            str = str.slice(0, str.length - 1)
        }
        return str;
    }

    const getFilteredItems = async (value) => {
        let filter = filterData;
        if (value)
            filter = Object.assign(filter, value);
        return await sendRequest('GET', API_PERSONS + constructParamString(filter)).then(async response => {
            if (!response.error) {
                return await converter.parseStringPromise(response).then(res => {
                    let arr = [];
                    if (validResult(res) && Number(res.person_result.totalPersons[0]) !== 0) {
                        res.person_result.persons[0].person.forEach(item => {
                            arr.push(decomposePersonToNormalView(item));
                        })
                    }
                    return {
                        results: arr,
                        count: Number(res.person_result.totalPersons[0]),
                    };
                })
            } else {
                return await getErrorMessage(response.message);
            }
        })
    }

    const deleteItem = async (value) => {
        return await sendRequest('DELETE', API_PERSONS + `/${value}`).then(async response => {
            if (response.error)
                return await getErrorMessage(response.message);
            return response;
        });
    }

    return {
        createItem,
        updateItem,
        getItem,
        getFilteredItems,
        deleteItem,
    };
}