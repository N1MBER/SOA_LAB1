
export const decomposePersonToNormalView = (object) => {
    let obj = {};
    if (object['id'])
        obj.id = Number(object['id'][0]);
    obj.name = object['name'][0];
    obj.coordinates = {
     x: Number(object['coordinates'][0]['x'][0]),
     y: Number(object['coordinates'][0]['y'][0])
    };
    if (object['creationDate'])
        obj.creationDate = object['creationDate'][0];
    obj.height = Number(object['height'][0]);
    obj.passportID = object['passportID'][0];
    obj.hairColor = object['hairColor'][0];
    obj.nationality = object['nationality'][0];
    obj.location = {
        x: Number(object['location'][0]['x'][0]),
        y: Number(object['location'][0]['y'][0]),
        z: Number(object['location'][0]['z'][0])
    };
    return obj;
}

export const decomposePersonToOneLayer = (object) => {
    let obj = object;
    Object.keys(object).forEach(key => {
        if (typeof object[key] === 'object') {
            Object.keys(object[key]).forEach(object_key => {
                obj[`${key}_${object_key}`] = object[key][object_key];
            })
            delete object[key];
        }
    })
    return obj;
};
