const isDev = true

export const backURL = isDev ? 'http://localhost:8080/persons' : '/persons';

export const API_PERSONS = '/persons';

export const start_page = '/persons/start';
export const person_page = '/persons/person';
export const location_page = '/persons/location';
export const coordinate_page = '/persons/coordinate';

function getFetchInit(method, headers, body){
    let init =  {
        method: method,
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: headers,
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        mode: 'cors',
    }
    if (method.trim().toLowerCase() !== 'get')
        init.body = body;
    return init;
}

export const sendRequest = async (method, end_point,data) => {
    let Headers = {};
    Headers["Content-Type"] =  "text/xml"
    return await fetch(backURL + end_point, getFetchInit(method, Headers, data)).then(async (response) => {
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
        return {error: true}
    });
}

