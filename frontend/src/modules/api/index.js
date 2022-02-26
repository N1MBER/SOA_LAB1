const isDev = false

export const backURL = isDev ? 'https://localhost:8081/back' : '/back';
export const back2URL = 'https://localhost:8182/back2'
export const API_PERSONS = '/api/persons';
export const API_EYE_COLOR = '/api/eye-color'

export const start_page = '/back/start';
export const person_page = '/back/person';
export const location_page = '/api/persons/location';
export const coordinate_page = '/api/persons/coordinate';

function getFetchInit(method, headers, body){
    let init =  {
        method: method,
        cache: 'no-cache',
        headers: headers,
        mode: 'cors',
    }
    if (method.trim().toLowerCase() !== 'get')
        init.body = body;
    return init;
}

export const sendRequest = async (method, end_point,data, back) => {
    let Headers = {};
    Headers["Content-Type"] =  "application/xml"
    const url = back ?? backURL;
    console.log(url + end_point, end_point, url)
    return await fetch(url + end_point, getFetchInit(method, Headers, data)).then(async (response) => {
        if (
            Math.trunc(response.status / 100) === 4
            || Math.trunc(response.status / 100) === 5
            || response.status === 429
        ) {
            let message = await response.text();
            return {error: true, message: message}
        }
        let res = await response.text()
        return res;
    }).catch(err => {
        console.log(err)
        return {error: true}
    });
}

