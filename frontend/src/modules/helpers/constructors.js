export const constructPerson = (value) => {
    return {
        id: value['id'] || 0,
        name: value['name'] || 'default',
        coordinates: value['coordinate']
            ? {
                x: value['coordinate_X'] || 0,
                y: value['coordinate_Y'] || 0,
            }
            : {
                x: 0,
                y: 0,
            },
        height: value['height'] || 0,
        passportID: value['passportID'] || 'default',
        hairColor: value['color'] || 'BROWN',
        nationality: value['country'] || 'JAPAN',
        location: value['location']
            ? {
                x: value['location_X'] || 0,
                y: value['location_X'] || 0,
                z: value['location_X'] || 0,
            }
            : {
                x: 0,
                y: 0,
                z: 0
            },
    }
}

export const constructLocation = (value) => {
    return {
        x: value['x'] || 0,
        y: value['y'] || 0,
        z: value['z'] || 0,
    }
}

export const constructCoordinate = (value) => {
    return {
        x: value['x'] || 0,
        y: value['y'] || 0,
    }
}

export const coord = {
    x: 20,
    y: 20
}

export const OBJtoXML = (obj) => {
    let xml = '';
    for (let prop in obj) {
        xml += obj[prop] instanceof Array ? '' : "<" + prop + ">";
        if (obj[prop] instanceof Array) {
            for (let array in obj[prop]) {
                xml += "<" + prop + ">";
                xml += OBJtoXML(new Object(obj[prop][array]));
                xml += "</" + prop + ">";
            }
        } else if (typeof obj[prop] == "object") {
            xml += OBJtoXML(new Object(obj[prop]));
        } else {
            xml += obj[prop];
        }
        xml += obj[prop] instanceof Array ? '' : "</" + prop + ">";
    }
    xml = xml.replace(/<\/?[0-9]{1,}>/g, '');
    return xml
}

export const constructPersonToNormalView = (object) => {
    let obj = object;
    Object.keys(object).forEach(key => {
        if (key.includes('_')) {
            let keyArr = key.split('_');
            if (!obj[keyArr[0]])
                obj[keyArr[0]] = {};
            obj[keyArr[0]][keyArr[1]] = object[key];
            delete obj[key];
        }
    })
    return obj;
}