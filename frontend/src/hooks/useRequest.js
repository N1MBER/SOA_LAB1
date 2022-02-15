import {API_EYE_COLOR, API_PERSONS, sendRequest} from "../modules/api";
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
        delete value.id;
        delete value.creationDate;
        value = OBJtoXML({person: constructPersonToNormalView(value)});
        return await sendRequest('PUT', API_PERSONS + `/${id}`, value).then(async response => {
            if (response.error)
                return await getErrorMessage(response.message);
            return response;
        })
    }

    const getEyeColor = async (color, nationality) => {
        try {
            console.log(color, nationality )
            return await sendRequest('GET', API_EYE_COLOR + `/${color ?? ''}` + (nationality ? `/nationality/${nationality ?? ''}/`: '')).then(async response => {
                console.log(234)
                console.log(response)
                if (!response.error) {
                    return await converter.parseStringPromise(response).then(res => {
                        if (res && res.person) {
                            return decomposePersonToNormalView(res.person)
                        } else if (res && res.serverResponse) {
                            return {message: res.serverResponse.message[0],}
                        } else {
                            return {error: true}
                        }
                    })
                } else {
                    console.log(response)
                    return await getErrorMessage(response.message);
                }
            })
        } catch (e) {
            console.log(e)

            return {
                error: true,
                message: "Неопознанная ошибка"
            }
        }
    }

    const getItem = async (value) => {
        try {
            return await sendRequest('GET', API_PERSONS + `/${value}`).then(async response => {
                if (!response.error) {
                    return await converter.parseStringPromise(response).then(res => {
                        if (res && res.person) {
                            return decomposePersonToNormalView(res.person)
                        } else if (res && res.serverResponse) {
                            return {message: res.serverResponse.message[0],}
                        } else {
                            return {error: true}
                        }
                    })
                } else {
                    return await getErrorMessage(response.message);
                }
            })
        } catch (e) {
            return {
                error: true,
                message: "Неопознанная ошибка"
            }
        }
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

    const getFilteredItems = async (value, withoutFilter = false, path) => {
        let filter = withoutFilter ? value : filterData;
        if (value && !withoutFilter)
            filter = Object.assign(filter, value);
        return await sendRequest('GET', API_PERSONS + `${path ? path: ''}` + constructParamString(filter)).then(async response => {
            console.log(response)
            try {
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
                            count: res.person_result ? Number(res.person_result.totalPersons[0]) : 0,
                        };
                    })
                } else {
                    return await getErrorMessage(response.message);
                }
            } catch(e) {
                console.log(e)
                return {
                    error: true,
                    message: "Неопознанная ошибка"
                }
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
        getEyeColor,
        deleteItem,
    };
}