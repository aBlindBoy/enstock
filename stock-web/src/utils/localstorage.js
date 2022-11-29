export function setLocalstorage (key, value) {
  var curTime = new Date().getTime()
  localStorage.setItem(key, JSON.stringify({ data: value, time: curTime }))
}

export function getLocalstorage (key, exp) {
  var data = localStorage.getItem(key)
  var dataObj = JSON.parse(data)
  if (new Date().getTime() - dataObj.time > exp) {
    console.log('信息已过期')
    // alert("信息已过期")
    return false
  } else {
    // console.log("data="+dataObj.data);
    // console.log(JSON.parse(dataObj.data));
    var dataObjDatatoJson = JSON.parse(dataObj.data)
    return dataObjDatatoJson
  }
}
