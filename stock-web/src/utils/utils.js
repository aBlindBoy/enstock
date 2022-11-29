// 单位用千位分隔符显示
export function thousandBitSeparator (str) {
  var num = parseFloat(str).toFixed(3) // 这里因为我需要两位小数所以做一个限制，你们看情况做小数位的限制
  var s = num.substring(0, (num.length - 1)) // 只取小数位2位
  return s && s.toString().replace(/(\d)(?=(\d{3})+\.)/g, function ($0, $1) {
    return $1 + ','
  })
}

// 判断是不是空 是--true
export function isNull (str) {
  if (str === '' || str === null) {
    return true
  } else {
    return false
  }
}

// 判断是不是邮箱 是--true
export function isEmail (str) {
  let myreg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/
  return myreg.test(str)
}

// 只能中英文
export function isName (str) {
  let myreg = /^[\u4E00-\u9FA5A-Za-z]+$/
  return myreg.test(str)
}

// 手机号码
export function isPhone (mobile) {
  // let myreg = /^[1][3,4,5,7,8][0-9]{9}$/  //手机号码验证
  let myreg = /^[0-9]{11}$/ // 手机号码验证
  return myreg.test(mobile)
}

// 数字隐藏显示 *
export function hideNumberTo (str) {
  let number = str.toFixed(2)
  let arr = number.split('.')
  console.log(arr)
  let string = ''
  for (var i = 0; i < arr[0].length; i++) {
    string += '*'
  }
  return string + '.**'
}

// 时间戳转换为时间格式
export function timeFormat (val) {
  let fmt = 'yyyy-MM-dd hh:mm:ss'
  // let date = new Date(time)
  if (!val) {
    return
  }
  let date = new Date(val)
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  let o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (let k in o) {
    if (new RegExp(`(${k})`).test(fmt)) {
      let str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero (str) {
  return ('00' + str).substr(str.length)
}


export function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  let o = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'h+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds()
  };
  for (let k in o) {
      if (new RegExp(`(${k})`).test(fmt)) {
      let str = o[k] + '';
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
      }
  }
  return fmt;
};